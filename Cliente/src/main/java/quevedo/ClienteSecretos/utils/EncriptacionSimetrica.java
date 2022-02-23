package quevedo.ClienteSecretos.utils;


import com.google.common.primitives.Bytes;
import io.vavr.control.Either;
import lombok.extern.log4j.Log4j2;
import quevedo.Common.encriptacion.ConstantesEncriptacion;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Arrays;
import java.util.Base64;

@Log4j2
public class EncriptacionSimetrica {

    public String generarClaveAleatoria() {
        SecureRandom secureRandom = new SecureRandom();
        byte[] passBytes = new byte[ConstantesEncriptacion.TAMAÑO_CLAVE_ALEATORIA];
        secureRandom.nextBytes(passBytes);

        return new String(passBytes, StandardCharsets.UTF_8);

    }

    public Either<String, String> encrypt(String secreto, String pass) {
        Either<String, String> result;
        try {

            byte[] iv = new byte[ConstantesEncriptacion.IV];
            byte[] salt = new byte[ConstantesEncriptacion.SALT];
            SecureRandom sr = new SecureRandom();
            sr.nextBytes(iv);
            sr.nextBytes(salt);
            Cipher cipher = getCipher(Cipher.ENCRYPT_MODE, pass, iv, salt);
            result = Either.right(Base64.getUrlEncoder().encodeToString(Bytes.concat(iv, salt,
                    cipher.doFinal(secreto.getBytes(StandardCharsets.UTF_8)))));
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            result = Either.left(ConstantesEncriptacion.ERROR_AL_ENCRYPTAR);
        }
        return result;
    }

    public Either<String, String> decrypt(String secreto, String pass) {
        Either<String, String> result;
        try {
            byte[] decoded = Base64.getUrlDecoder().decode(secreto);

            byte[] iv = Arrays.copyOf(decoded, ConstantesEncriptacion.IV);
            byte[] salt = Arrays.copyOfRange(decoded, ConstantesEncriptacion.IV, ConstantesEncriptacion.VEINTIOCHO);

            Cipher cipher = getCipher(Cipher.DECRYPT_MODE, pass, iv, salt);
            result = Either.right(new String(cipher.doFinal(Arrays.copyOfRange(decoded, ConstantesEncriptacion.VEINTIOCHO, decoded.length)), StandardCharsets.UTF_8));
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            result = Either.left(ConstantesEncriptacion.ERROR_AL_DESENCRIPTAR);
        }
        return result;
    }

    private Cipher getCipher(int typeCipher, String pass, byte[] iv,
                             byte[] salt) throws NoSuchAlgorithmException
            , InvalidKeySpecException, NoSuchPaddingException, InvalidKeyException, InvalidAlgorithmParameterException {
        GCMParameterSpec parameterSpec = new GCMParameterSpec(ConstantesEncriptacion.LEN_SPEC, iv);

        SecretKeyFactory factory = SecretKeyFactory.getInstance(ConstantesEncriptacion.PBKDFSHA256);
        KeySpec spec = new PBEKeySpec(pass.toCharArray(), salt, ConstantesEncriptacion.ITERATION_COUNT, ConstantesEncriptacion.KEY_LENGTH);
        SecretKey tmp = factory.generateSecret(spec);
        SecretKeySpec secretKey = new SecretKeySpec(tmp.getEncoded(), ConstantesEncriptacion.AES);

        Cipher cipher = Cipher.getInstance(ConstantesEncriptacion.AES_GCM_NO_PADDING);
        cipher.init(typeCipher, secretKey, parameterSpec);
        return cipher;
    }
}
