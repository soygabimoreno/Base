package soy.gabimoreno.remoteconfig

import android.util.Base64
import java.security.SecureRandom
import javax.crypto.Cipher
import javax.crypto.SecretKeyFactory
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.PBEKeySpec
import javax.crypto.spec.SecretKeySpec
import javax.inject.Singleton

class SecureEncryptor(
    private val masterKey: String
) {

    fun encrypt(plainText: String): String {
        val salt = generateRandomBytes(SALT_LENGTH)
        val iv = generateRandomBytes(IV_LENGTH)
        val key = deriveKey(masterKey, salt)
        val cipher = Cipher.getInstance(AES_MODE)
        cipher.init(Cipher.ENCRYPT_MODE, key, IvParameterSpec(iv))
        val encrypted = cipher.doFinal(plainText.toByteArray())

        val combined = salt + iv + encrypted
        return Base64.encodeToString(combined, Base64.NO_WRAP)
    }

    fun decrypt(encryptedBase64: String): String {
        val combined = Base64.decode(encryptedBase64, Base64.NO_WRAP)
        val salt = combined.copyOfRange(0, SALT_LENGTH)
        val iv = combined.copyOfRange(SALT_LENGTH, SALT_LENGTH + IV_LENGTH)
        val encrypted = combined.copyOfRange(SALT_LENGTH + IV_LENGTH, combined.size)

        val key = deriveKey(masterKey, salt)
        val cipher = Cipher.getInstance(AES_MODE)
        cipher.init(Cipher.DECRYPT_MODE, key, IvParameterSpec(iv))
        val decrypted = cipher.doFinal(encrypted)
        return String(decrypted)
    }

    private fun deriveKey(password: String, salt: ByteArray): SecretKeySpec {
        val factory = SecretKeyFactory.getInstance(KEY_ALGORITHM)
        val spec = PBEKeySpec(password.toCharArray(), salt, ITERATION_COUNT, KEY_LENGTH)
        val keyBytes = factory.generateSecret(spec).encoded
        return SecretKeySpec(keyBytes, "AES")
    }

    private fun generateRandomBytes(length: Int): ByteArray =
        ByteArray(length).apply { SecureRandom().nextBytes(this) }
}

private const val AES_MODE = "AES/CBC/PKCS5Padding"
private const val KEY_ALGORITHM = "PBKDF2WithHmacSHA256"
private const val ITERATION_COUNT = 100_000
private const val KEY_LENGTH = 256
private const val IV_LENGTH = 16
private const val SALT_LENGTH = 16
