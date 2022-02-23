package quevedo.servidorSecretos.utils;

import io.vavr.control.Either;
import lombok.extern.log4j.Log4j2;
import org.bouncycastle.jce.X509Principal;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.x509.X509V3CertificateGenerator;
import quevedo.Common.encriptacion.ConstantesEncriptacion;

import javax.crypto.Cipher;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;
import java.security.spec.X509EncodedKeySpec;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
import java.util.Base64;
import java.util.Date;

@Log4j2
public class EncriptacionAsimetrica {
    public Either<String, KeyPair> generateKeys() {
        Either<String,KeyPair> result;
        try {

            //Generar claves
            Security.addProvider(new BouncyCastleProvider());
            KeyPairGenerator generadorRSA = KeyPairGenerator.getInstance(ConstantesEncriptacion.RSA);
            generadorRSA.initialize(ConstantesEncriptacion.KEYSIZE);
            KeyPair clavesRSA = generadorRSA.generateKeyPair();
            result = Either.right(clavesRSA);

        } catch (Exception e) {
            log.error(e.getMessage(), e);
            result = Either.left(ConstantesEncriptacion.ERROR_CREAR_CLAVES);
        }

        return result;

    }

    public boolean crearKeyStore(String passUser, PrivateKey clavePrivada, X509Certificate cert, String ruta)  {
        boolean confirmar = false;
        try {
            //Creamos el keyStore
            KeyStore ks = KeyStore.getInstance(ConstantesEncriptacion.PKCS_12);
            char[] password = passUser.toCharArray();
            ks.load(null, null);
            ks.setCertificateEntry(ConstantesEncriptacion.PUBLICA, cert);
            ks.setKeyEntry(ConstantesEncriptacion.PRIVADA, clavePrivada, passUser.toCharArray(), new Certificate[]{cert});
            FileOutputStream fos = new FileOutputStream(ruta);
            ks.store(fos, password);
            fos.close();
            confirmar = true;
        }catch (Exception e) {
            log.error(e.getMessage(), e);
        }

        return confirmar;
    }

    public Either<String,String> crearCerfiticado(String userName, PublicKey publicKeyUsuario,PrivateKey privateKeyServer) {
        Either<String,String> result;
        try{
            //Generar certificado
            Security.addProvider(new BouncyCastleProvider());
            X509V3CertificateGenerator certificado = new X509V3CertificateGenerator();
            certificado.setSerialNumber(BigInteger.valueOf(ConstantesEncriptacion.UNO));
            certificado.setSubjectDN(new X509Principal(ConstantesEncriptacion.CN + userName));
            certificado.setIssuerDN(new X509Principal(ConstantesEncriptacion.CN + ConstantesEncriptacion.SERVIDOR_JOSEPH));
            //Insertamos la clave publica en el certificado
            certificado.setPublicKey(publicKeyUsuario);
            certificado.setNotBefore(
                    Date.from(LocalDate.now().plus(ConstantesEncriptacion.DAYS_CERT, ChronoUnit.DAYS).atStartOfDay().toInstant(ZoneOffset.UTC)));
            certificado.setNotAfter(new Date());
            certificado.setSignatureAlgorithm(ConstantesEncriptacion.SHA1);

            X509Certificate cert = certificado.generateX509Certificate(privateKeyServer);

            result = Either.right(Base64.getUrlEncoder().encodeToString(cert.getEncoded()));
        }catch (Exception e){
            log.error(e.getMessage(), e);
            result = Either.left(ConstantesEncriptacion.ERROR_CREAR_CERTIFICADO);
        }

        return result;
    }


    public Either<String, PrivateKey> getPrivateKey(String passUser) {
        Either<String, PrivateKey> result;
        try {
            //Cargamos el keystore
            KeyStore ksLoad = KeyStore.getInstance(ConstantesEncriptacion.PKCS_12);
            ksLoad.load(this.getClass().getClassLoader().getResourceAsStream(Constantes.PATH_CLAVE_SERVIDOR), passUser.toCharArray());
            //Con la contraseña del usuario obtenermos la clave privada que anteriormente guardamos con esa misma contraseña
            KeyStore.PasswordProtection passwordProtection = new KeyStore.PasswordProtection(passUser.toCharArray());
            KeyStore.PrivateKeyEntry privateKeyEntry = (KeyStore.PrivateKeyEntry) ksLoad.getEntry(ConstantesEncriptacion.PRIVADA, passwordProtection);
            PrivateKey clavePrivada = privateKeyEntry.getPrivateKey();

            result = Either.right(clavePrivada);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            result = Either.left(ConstantesEncriptacion.ERROR_PRIVADA);
        }

        return result;
    }




    public Either<String, PublicKey> getPublicKey(String passUser,String ruta) {

        Either<String, PublicKey> result;
        try {
            KeyStore ksLoad = KeyStore.getInstance(ConstantesEncriptacion.PKCS_12);
            ksLoad.load(new FileInputStream(ruta), passUser.toCharArray());
            X509Certificate certificate = (X509Certificate) ksLoad.getCertificate(ConstantesEncriptacion.PUBLICA);
            PublicKey clavePublica = certificate.getPublicKey();
            result = Either.right(clavePublica);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            result = Either.left(ConstantesEncriptacion.ERROR_PUBLICA);
        }

        return result;
    }

    public Either<String,PublicKey> reconstruirPublica(String publicaEncode)  {
        Either<String,PublicKey> result;
        try {
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            byte[] clavePublicBuffer = Base64.getUrlDecoder().decode(publicaEncode);
            X509EncodedKeySpec clavePublicaSpec = new X509EncodedKeySpec(clavePublicBuffer);
            PublicKey publicKey = keyFactory.generatePublic(clavePublicaSpec);
            result = Either.right(publicKey);
        }catch (Exception e){
            log.error(e.getMessage(), e);
            result = Either.left(ConstantesEncriptacion.ERROR_RECONSTRUIR);
        }

        return result;

    }

    public Either<String, String> cifrar(String passUser, String passEncriptar,String ruta) {
        Either<String, String> cifradoResult;
        String clave;
        try {
            Cipher cifrador = Cipher.getInstance(ConstantesEncriptacion.RSA);
            Either<String, PublicKey> result = getPublicKey(passUser,ruta);
            if (result.isRight()) {
                //pasamos lo que queremos encriptar a bytes
                byte[] buffer = passEncriptar.getBytes(StandardCharsets.UTF_8);
                //Ponemos el cifrador en modo cifrado
                cifrador.init(Cipher.ENCRYPT_MODE, result.get());
                byte[] bufferFinal = cifrador.doFinal(buffer);
                //Lo que ciframos lo encodeamos en base64 y lo metemos en un byte[], por ultimo lo metemos en un new String
                byte[] encript = Base64.getUrlEncoder().encode(bufferFinal);
                clave = new String(encript);
                cifradoResult = Either.right(clave);
            } else {
                cifradoResult = Either.left(result.getLeft());
            }

        } catch (Exception e) {
            log.error(e.getMessage(), e);
            cifradoResult = Either.left(ConstantesEncriptacion.ERROR_AL_CIFRAR);
        }

        return cifradoResult;
    }

    public Either<String, String> descifrar(String passUser, String passDesencriptar,InputStream ruta) {
        Either<String, String> cifradoResult;
        String clave;
        try {
            Cipher cifrador = Cipher.getInstance(ConstantesEncriptacion.RSA);
            Either<String, PrivateKey> result = getPrivateKey(passUser);
            if (result.isRight()) {
                //Lo ponemos en modo desencriptar y le decimos con que queremos desencriptar
                cifrador.init(Cipher.DECRYPT_MODE, result.get());
                //la clave a descifrar los decodeamos en base64
                byte[] desencriptado = Base64.getUrlDecoder().decode(passDesencriptar);
                //Lo desciframos
                byte[] cipher = cifrador.doFinal(desencriptado);
                clave = new String(cipher, StandardCharsets.UTF_8);
                cifradoResult = Either.right(clave);
            } else {
                cifradoResult = Either.left(result.getLeft());
            }

        } catch (Exception e) {
            log.error(e.getMessage(), e);
            cifradoResult = Either.left(ConstantesEncriptacion.ERROR_AL_DESCIFRAR);
        }

        return cifradoResult;
    }
}
