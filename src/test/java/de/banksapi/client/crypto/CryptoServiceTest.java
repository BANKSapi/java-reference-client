package de.banksapi.client.crypto;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.junit.Before;
import org.junit.Test;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.IOException;
import java.net.URL;
import java.security.*;
import java.security.cert.CertificateException;

public class CryptoServiceTest {

    @Before
    public void setup() {
        Security.addProvider(new BouncyCastleProvider());
    }

    @Test
    public void testCryptoServiceFromPEM() throws CertificateException, IOException, NoSuchPaddingException,
            NoSuchAlgorithmException, IllegalBlockSizeException, BadPaddingException, NoSuchProviderException,
            InvalidKeyException {
        URL pem = CryptoServiceTest.class.getResource("/pubkey.pem");
        CryptoService cryptoService = CryptoService.fromX509PEMs(pem.getPath());
        String ciphertextBase64 = cryptoService.encryptToBase64String("my plaintext");
        //System.out.println(ciphertextBase64);
        assert ciphertextBase64.length() > 50;
    }

    @Test
    public void testCryptoServiceFromKeystore() throws CertificateException, NoSuchAlgorithmException,
            KeyStoreException, IOException, IllegalBlockSizeException, NoSuchPaddingException, BadPaddingException,
            NoSuchProviderException, InvalidKeyException {
        URL keystore = CryptoServiceTest.class.getResource("/keystore.jks");
        CryptoService cryptoService = CryptoService.fromKeystore(keystore.getPath(), "demo");
        String ciphertextBase64 = cryptoService.encryptToBase64String("my plaintext");
        //System.out.println(ciphertextBase64);
        assert ciphertextBase64.length() > 50;
    }
}
