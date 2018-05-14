package de.banksapi.client.crypto;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.*;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.Base64;

/**
 * This service provides a method to encrypt plaintexts.
 * <p>The used crypto system is defined by the following attributes:</p>
 * <dl>
 * <dt>Encryption Algorithm</dt>
 * <dd>RSA</dd>
 * <dt>Block cipher mode of operation</dt>
 * <dd>none</dd>
 * <dt>Padding</dt>
 * <dd>OAEP (Optimal Asymmetric Encryption Padding) with SHA-1 (hash function) and MGF1 (mask
 * generation function)</dd>
 * </dl>
 *
 * @see <a href="https://docs.banksapi.de/customer.html#verschlusselung">BANKSapi Verschlüsselung</a>
 * @see <a href="https://docs.oracle.com/javase/8/docs/technotes/guides/security/StandardNames.html">
 * Java Cryptography Architecture Standard Algorithm Name Documentation for JDK 8</a>
 * @see <a href="https://tools.ietf.org/html/rfc8017">RFC 8017</a>
 */
public class CryptoService {

    private static final String TRANSFORMATION = "RSA/NONE/OAEPWithSHA1AndMGF1padding";
    private static final String CRYPTO_PROVIDER = "BC";

    private PublicKey publicKey;

    private Base64.Encoder b64encoder = Base64.getEncoder();

    private CryptoService() {
    }

    private Cipher getEncryptionCipher() throws NoSuchPaddingException, NoSuchAlgorithmException,
            NoSuchProviderException, InvalidKeyException {
        Cipher cipher = Cipher.getInstance(TRANSFORMATION, CRYPTO_PROVIDER);
        cipher.init(Cipher.ENCRYPT_MODE, publicKey, new SecureRandom());
        return cipher;
    }

    /**
     * Creates a new crypto service from a PEM encoded X.509 certificate.
     *
     * @param pemPublicKey file with PEM encoded X.509 certificate
     * @return a new crypto service instance
     * @throws CertificateException if the certificate could not be extracted
     * @throws IOException if the certificate file could not be read
     */
    public static CryptoService fromX509PEMs(String pemPublicKey)
            throws CertificateException, IOException {
        CryptoService cryptoService = new CryptoService();

        try (FileInputStream fis = new FileInputStream(pemPublicKey)) {
            CertificateFactory fact = CertificateFactory.getInstance("X.509");
            X509Certificate cer = (X509Certificate) fact.generateCertificate(fis);
            cryptoService.publicKey = cer.getPublicKey();
        }

        return cryptoService;
    }

    /**
     * Creates a new crypto service from a jks keystore (Java default).
     *
     * @param keystoreFilename keystore file
     * @param keyAlias key alias to use
     * @return a new crypto service instance
     * @throws KeyStoreException if the keystore could not be created
     * @throws IOException if the keystore file could not be read
     * @throws CertificateException if the certificate could not be extracted
     * @throws NoSuchAlgorithmException if the algorithm used to check
     * the integrity of the keystore cannot be found
     */
    public static CryptoService fromKeystore(String keystoreFilename, String keyAlias)
            throws KeyStoreException, IOException, CertificateException, NoSuchAlgorithmException {
        CryptoService cryptoService = new CryptoService();

        KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
        try (FileInputStream fis = new FileInputStream(keystoreFilename)) {
            keyStore.load(fis, null);
            Certificate cert = keyStore.getCertificate(keyAlias);
            cryptoService.publicKey = cert.getPublicKey();
        }

        return cryptoService;
    }

    /**
     * Encrypts the given plaintext and encodes the cipher to Base64.
     *
     * @param plaintext plaintext to encrypt and encode
     * @return Base64 encoded cipher
     */
    public String encryptToBase64String(String plaintext) {
        byte[] plaintextBytes = plaintext.getBytes();
        byte[] ciphertextBytes;
        try {
            ciphertextBytes = getEncryptionCipher().doFinal(plaintextBytes);
        } catch (IllegalBlockSizeException | BadPaddingException | NoSuchPaddingException |
                NoSuchAlgorithmException | NoSuchProviderException | InvalidKeyException e) {
            throw new IllegalStateException("Unable to encrypt", e);
        }
        return b64encoder.encodeToString(ciphertextBytes);
    }
}
