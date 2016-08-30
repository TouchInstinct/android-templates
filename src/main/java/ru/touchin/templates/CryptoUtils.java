package ru.touchin.templates;

import android.support.annotation.NonNull;

import com.tozny.crypto.android.AesCbcWithIntegrity;

import java.nio.charset.Charset;
import java.security.GeneralSecurityException;

/**
 * Created by Gavriil Sitnikov on 30/08/2016.
 * TODO: fill description
 */
public final class CryptoUtils {

    /**
     * Dependency needed: compile 'com.scottyab:aes-crypto:+'.
     *
     * @param bytes
     * @param keyString
     * @return
     */
    @NonNull
    public static byte[] simpleEncryptBytes(@NonNull final byte[] bytes, @NonNull final String keyString) {
        try {
            final AesCbcWithIntegrity.SecretKeys key = AesCbcWithIntegrity.keys(keyString);
            final AesCbcWithIntegrity.CipherTextIvMac cipherTextIvMac = AesCbcWithIntegrity.encrypt(bytes, key);
            return cipherTextIvMac.toString().getBytes(Charset.forName("UTF-8"));
        } catch (final GeneralSecurityException exception) {
            throw new RuntimeException(exception);
        }
    }

    /**
     * Dependency needed: compile 'com.scottyab:aes-crypto:+'.
     *
     * @param encrypted
     * @param keyString
     * @return
     */
    @NonNull
    public static byte[] simpleDecryptBytes(@NonNull final byte[] encrypted, @NonNull final String keyString) {
        try {
            final AesCbcWithIntegrity.SecretKeys key = AesCbcWithIntegrity.keys(keyString);
            final AesCbcWithIntegrity.CipherTextIvMac cipherTextIvMac
                    = new AesCbcWithIntegrity.CipherTextIvMac(new String(encrypted, Charset.forName("UTF-8")));
            return AesCbcWithIntegrity.decrypt(cipherTextIvMac, key);
        } catch (final GeneralSecurityException exception) {
            throw new RuntimeException(exception);
        }
    }

    private CryptoUtils() {
    }

}
