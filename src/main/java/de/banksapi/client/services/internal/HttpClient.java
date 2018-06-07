package de.banksapi.client.services.internal;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.net.ssl.HttpsURLConnection;
import java.io.*;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

import static de.banksapi.client.services.internal.StringUtil.isBlank;

/**
 * This class provides basic HTTP communication functions via {@link HttpsURLConnection} and acts
 * as a facade to ease the integration of a different HTTP client such as OkHttp, Apache CXF, Apache
 * HttpComponents and so on.
 * <p>This client is stateful and you should use one {@link HttpClient} instance per request.</p>
 */
public class HttpClient {

    private URL url;

    private HttpsURLConnection httpsURLConnection;

    private ObjectMapper objectMapper;

    public HttpClient(URL url, PropertyNamingStrategy propertyNamingStrategy) {
        try {
            this.url = url;
            httpsURLConnection = (HttpsURLConnection) this.url.openConnection();
            objectMapper = new ObjectMapper().setPropertyNamingStrategy(
                    propertyNamingStrategy.toJacksonStrategy());
        } catch (MalformedURLException e) {
            throw new IllegalStateException("Invalid URL '" + url + "'", e);
        } catch (IOException e) {
            throw new IllegalStateException("Unable to setup HTTP connection to '" + url + "'", e);
        }
    }

    public HttpClient setHeader(String key, String value) {
        httpsURLConnection.setRequestProperty(key, value);
        return this;
    }

    public <T> Response<T> post(String postData, Class<T> responseClass) {
        try {
            httpsURLConnection.setRequestMethod("POST");
            httpsURLConnection.setDoOutput(true);
            try (DataOutputStream out = new DataOutputStream(httpsURLConnection.getOutputStream())) {
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

    public <T> Response<T> post(Class<T> responseClass) {
        try {
            httpsURLConnection.setRequestMethod("POST");
            httpsURLConnection.setDoOutput(true);
        } catch (IOException e) {
            throw new IllegalStateException("Unable to setup POST request to '" + url + "'", e);
        }

        return performRequest(responseClass);
    }

    public <T> Response<T> put(String putData, Class<T> responseClass) {
        try {
            httpsURLConnection.setRequestMethod("PUT");
            httpsURLConnection.setDoOutput(true);
            try (DataOutputStream out = new DataOutputStream(httpsURLConnection.getOutputStream())) {
                out.write(putData.getBytes());
            }
        } catch (IOException e) {
            throw new IllegalStateException("Unable to setup POST request to '" + url + "'", e);
        }

        return performRequest(responseClass);
    }

    public <U, T> Response<T> put(U putData, Class<T> responseClass) {
        try {
            return put(objectMapper.writeValueAsString(putData), responseClass);
        } catch (JsonProcessingException e) {
            throw new JsonSerializationException("Unable to serialize object to JSON", e);
        }
    }

    public <T> Response<T> put(Class<T> responseClass) {
        try {
            httpsURLConnection.setRequestMethod("PUT");
            httpsURLConnection.setDoOutput(true);
        } catch (IOException e) {
            throw new IllegalStateException("Unable to setup PUT request to '" + url + "'", e);
        }

        return performRequest(responseClass);
    }

    public Response<String> delete() {
        try {
            httpsURLConnection.setRequestMethod("DELETE");
            httpsURLConnection.setDoOutput(true);
        } catch (ProtocolException e) {
            throw new IllegalStateException("Unable to setup DELETE request to '" + url + "'", e);
        }

        return performRequest(String.class);
    }

    public <T> Response<T> get(Class<T> responseClass) {
        try {
            httpsURLConnection.setRequestMethod("GET");
            httpsURLConnection.setDoOutput(true);
        } catch (ProtocolException e) {
            throw new IllegalStateException("Unable to setup GET request to '" + url + "'", e);
        }

        return performRequest(responseClass);
    }

    private <T> Response<T> performRequest(Class<T> responseClass) {
        Response<T> response = new Response<>();

        try {
            response.httpCode = httpsURLConnection.getResponseCode();

            InputStream inputStream = httpsURLConnection.getInputStream();
            T object = null;
            if (responseClass != null && inputStream.available() > 0) {
                String input = readStream(inputStream);
                object = objectMapper.readValue(input, responseClass);
            }
            response.data = object;

            String location = httpsURLConnection.getHeaderField("location");
            if (!isBlank(location)) {
                response.location = location;
            }
        } catch (JsonMappingException ex) {
            response.error = ex.getMessage();
        } catch (IOException ex) {
            InputStream errorStream = httpsURLConnection.getErrorStream();
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
        }

        return builder.toString();
    }

    public static class Response<T> {

        private int httpCode;
        private T data;
        private String error;
        private String location;

        public int getHttpCode() {
            return httpCode;
        }

        public T getData() {
            return data;
        }

        public String getError() {
            return error;
        }

        public String getLocation() {
            return location;
        }

        public URL getLocationAsUrl() {
            try {
                return new URL(location);
            } catch (MalformedURLException e) {
                throw new IllegalStateException("Location '" + location + "' holds an invalid URL", e);
            }
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
