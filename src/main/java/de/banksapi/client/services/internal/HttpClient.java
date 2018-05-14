package de.banksapi.client.services.internal;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

/**
 * This class provides basic HTTP communication functions via {@link HttpURLConnection} and acts
 * as a facade to ease the integration of a different HTTP client such as OkHttp, Apache CXF, Apache
 * HttpComponents and so on.
 * <p>This client is stateful and you should use one {@link HttpClient} instance per request.</p>
 */
public class HttpClient {

    private URL url;

    private HttpURLConnection httpURLConnection;

    private ObjectMapper objectMapper;

    public HttpClient(URL url, PropertyNamingStrategy propertyNamingStrategy) {
        try {
            this.url = url;
            httpURLConnection = (HttpURLConnection) this.url.openConnection();
            objectMapper = new ObjectMapper().setPropertyNamingStrategy(
                    propertyNamingStrategy.toJacksonStrategy());
        } catch (MalformedURLException e) {
            throw new IllegalStateException("Invalid URL '" + url + "'", e);
        } catch (IOException e) {
            throw new IllegalStateException("Unable to setup HTTP connection to '" + url + "'", e);
        }
    }

    public HttpClient setHeader(String key, String value) {
        httpURLConnection.setRequestProperty(key, value);
        return this;
    }

    public <T> Response<T> post(String postData, Class<T> responseClass) {
        try {
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setDoOutput(true);
            try (DataOutputStream out = new DataOutputStream(httpURLConnection.getOutputStream())) {
                out.write(postData.getBytes());
            }
        } catch (IOException e) {
            throw new IllegalStateException("Unable to setup POST request to '" + url + "'", e);
        }

        return performRequest(responseClass);
    }

    public <U, T> Response<T> post(U postData, Class<T> responseClass) {
        try {
            return post(objectMapper.writeValueAsString(postData), responseClass);
        } catch (JsonProcessingException e) {
            throw new JsonSerializationException("Unable to serialize object to JSON", e);
        }
    }

    public Response<String> delete() {
        try {
            httpURLConnection.setRequestMethod("DELETE");
            httpURLConnection.setDoOutput(true);
        } catch (ProtocolException e) {
            throw new IllegalStateException("Unable to setup DELETE request to '" + url + "'", e);
        }

        return performRequest(String.class);
    }

    public <T> Response<T> get(Class<T> responseClass) {
        try {
            httpURLConnection.setRequestMethod("GET");
            httpURLConnection.setDoOutput(true);
        } catch (ProtocolException e) {
            throw new IllegalStateException("Unable to setup GET request to '" + url + "'", e);
        }

        return performRequest(responseClass);
    }

    private <T> Response<T> performRequest(Class<T> responseClass) {
        Response<T> response = new Response<>();

        try {
            response.httpCode = httpURLConnection.getResponseCode();
            InputStream inputStream = httpURLConnection.getInputStream();
            T object = null;
            if (inputStream.available() > 0) {
                String input = readStream(inputStream);
                object = objectMapper.readValue(input, responseClass);
            }
            response.data = object;
        } catch (JsonMappingException ex) {
            response.error = ex.getMessage();
        } catch (IOException ex) {
            InputStream errorStream = httpURLConnection.getErrorStream();
            try {
                response.error = readStream(errorStream);
            } catch (IOException exError) {
                throw new IllegalStateException("Unable to read error stream", exError);
            }
        }

        return response;
    }

    private static String readStream(InputStream stream) throws IOException {
        if (stream == null) {
            return null;
        }

        StringBuilder builder = new StringBuilder();

        try (BufferedReader in = new BufferedReader(new InputStreamReader(stream))) {
            String line;
            while ((line = in.readLine()) != null) {
                builder.append(line);
            }
            in.close();
        }

        return builder.toString();
    }

    public static class Response<T> {

        private int httpCode;
        private T data;
        private String error;

        public int getHttpCode() {
            return httpCode;
        }

        public T getData() {
            return data;
        }

        public String getError() {
            return error;
        }

    }

    public enum PropertyNamingStrategy {
        SNAKE_CASE,
        LOWER_CAMEL_CASE;

        private com.fasterxml.jackson.databind.PropertyNamingStrategy toJacksonStrategy() {
            switch (this) {
                case SNAKE_CASE:
                    return com.fasterxml.jackson.databind.PropertyNamingStrategy.SNAKE_CASE;
                case LOWER_CAMEL_CASE:
                    return com.fasterxml.jackson.databind.PropertyNamingStrategy.LOWER_CAMEL_CASE;
                default:
                    return null;
            }
        }
    }
}
