/**
 * Created by Lucio on 2022/2/12.
 */
package bas.lang.security

import bas.lang.Coder
import java.security.KeyFactory
import java.security.PrivateKey
import java.security.PublicKey
import java.security.spec.PKCS8EncodedKeySpec
import java.security.spec.X509EncodedKeySpec
import javax.crypto.Cipher

object RSAUtils {

    /**
     * Bouncy或Android一般支持该方式
     */
    const val RSA_TRANSFORMATION_NONE = "RSA/ECB/PKCS1Padding"//"RSA/None/PKCS1Padding"

    /**
     * Java SE程序支持该方式
     */
    const val RSA_TRANSFORMATION_ECB = "RSA/ECB/PKCS1Padding"

    /**
     * RSA 根据公钥加密
     * @param data  数据（字符串数据对应的ByteArray）
     * @param pubKey 公钥(对公钥字符串进行Base64解码后字符串对应的ByteArray)
     * @param transformation 加密方式，见[Cipher.getInstance]
     */
    @JvmStatic
    @Throws(Exception::class)
    private fun encrypt(
        data: ByteArray,
        pubKey: ByteArray,
        transformation: String = RSA_TRANSFORMATION_NONE
    ): ByteArray {
        // 得到公钥对象
        val keySpec = X509EncodedKeySpec(pubKey)
        val kf: KeyFactory = KeyFactory.getInstance("RSA")
        val key: PublicKey = kf.generatePublic(keySpec)
        // 加密数据
        val cp: Cipher = Cipher.getInstance(transformation)
        cp.init(Cipher.ENCRYPT_MODE, key)
        return cp.doFinal(data)
    }

    /**
     * RSA加密
     * @param data 数据字符串
     * @param pubKey 公钥
     * @param transformation 加密方式，见[Cipher.getInstance];常用Android-[RSA_TRANSFORMATION_NONE]、JavaSE-[RSA_TRANSFORMATION_ECB]
     */
    @JvmStatic
    fun encrypt(
        data: String,
        pubKey: String,
        transformation: String = RSA_TRANSFORMATION_NONE
    ): ByteArray {
        return encrypt(data.toByteArray(), Coder.base64Decode(pubKey), transformation)
    }

    /**
     * RSA加密
     * @see encrypt
     * @return 加密结果对应ByteArray进行Base64编码后的字符串
     */
    @JvmStatic
    fun encryptToString(
        data: String,
        pubKey: String,
        transformation: String = RSA_TRANSFORMATION_NONE
    ): String {
        return Coder.base64EncodeToString(encrypt(data, pubKey, transformation))
    }


    /**
     * RSA 根据私钥解密
     * @param encrypted 待解密数据（将加密数据进行Base64解码，之后得到的字符串对应的ByteArray）
     * @param privateKey 私钥数据（将私钥字符串进行Base64解码之后得到的字符串对应的ByteArray）
     */
    @Throws(Exception::class)
    private fun decrypt(
        encrypted: ByteArray,
        privateKey: ByteArray,
        transformation: String = RSA_TRANSFORMATION_NONE
    ): ByteArray {
        // 得到私钥对象
        val keySpec = PKCS8EncodedKeySpec(privateKey)
        val kf = KeyFactory.getInstance("RSA")
        val key: PrivateKey = kf.generatePrivate(keySpec)
        // 解密数据
        val cp = Cipher.getInstance(transformation)
        cp.init(Cipher.DECRYPT_MODE, key)
        return cp.doFinal(encrypted)
    }

    /**
     * RSA解码
     * @see decrypt
     * @return 解码之后的字节数据
     */
    @JvmStatic
    fun decrypt(
        data: String,
        privateKey: String,
        transformation: String = RSA_TRANSFORMATION_NONE
    ): ByteArray {
        return decrypt(Coder.base64Decode(data), Coder.base64Decode(privateKey), transformation)
    }

    /**
     * RSA解码
     * @see decrypt
     * @return 解码之后的字符串
     */
    @JvmStatic
    fun decryptToString(
        data: String,
        privateKey: String,
        transformation: String = RSA_TRANSFORMATION_NONE
    ): String {
        return String(decrypt(data, privateKey, transformation))
    }

}