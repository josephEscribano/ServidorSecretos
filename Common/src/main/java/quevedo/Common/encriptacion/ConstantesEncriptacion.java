package quevedo.Common.encriptacion;

public class ConstantesEncriptacion {
    public static final String PBKDF2_ALGORITHM = "PBKDF2WithHmacSHA512";

    public static final int SALT_BYTE_SIZE = 48;
    public static final int HASH_BYTE_SIZE = 48;
    public static final int PBKDF2_ITERATIONS = 1000;

    public static final int ITERATION_INDEX = 0;
    public static final int SALT_INDEX = 1;
    public static final int PBKDF2_INDEX = 2;
    public static final String RSA = "RSA";
    public static final String ERROR_PRIVADA = "Ha ocurrido un error al obtener la clave privada";
    public static final String ERROR_PUBLICA = "Error al obtener la clave publica";
    public static final String RUTA_CLAVES = "claves\\";
    public static final int KEYSIZE = 2048;
    public static final String ERROR_AL_CIFRAR = "Error al cifrar";
    public static final String ERROR_AL_DESCIFRAR = "Error al descifrar";
    public static final String ERROR_AL_DESENCRIPTAR = "Error al desencriptar";
    public static final String ERROR_AL_ENCRYPTAR = "Error al encryptar";
    public static final String CN = "CN=";
    public static final String PUBLICA = "publica";
    public static final String PRIVADA = "privada";
    public static final String KEYSTORE_PFX = "keystore.pfx";
    public static final String SHA1 = "SHA1WithRSAEncryption";
    public static final String PKCS_12 = "PKCS12";
    public static final int DAYS_CERT = 365;
    public static final int UNO = 1;
    public static final int TAMAÑO_CLAVE_ALEATORIA = 10;
    public static final String toHexValue = "%0";
    public static final String TOHEXVALUED = "d";
    public static final String REGEX_DOS_PUNTOS = ":";


    public static final String PBKDFSHA256 = "PBKDF2WithHmacSHA256";
    public static final String AES_GCM_NO_PADDING = "AES/GCM/noPadding";
    public static final String AES = "AES";
    public static final int IV = 12;
    public static final int SALT = 16;
    public static final int LEN_SPEC = 128;
    public static final int ITERATION_COUNT = 65536;
    public static final int KEY_LENGTH = 256;
    public static final int VEINTIOCHO = 28;
    public static final String ERROR_CREAR_CLAVES = "Error al crear la clave publica y privada";
    public static final String ERROR_CREAR_CERTIFICADO = "Error al crear el certificado";
    public static final String SERVIDOR_JOSEPH = "ServidorJoseph";
    public static final String ERROR_RECONSTRUIR = "Error al reconstruir la clave publica";
}
