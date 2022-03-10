package bas.droid.core.android

import androidx.test.ext.junit.runners.AndroidJUnit4
import bas.lib.core.BasConfigurator
import bas.droid.core.net.URLCoderDroid
import bas.droid.core.util.Base64CoderDroid
import bas.lib.core.lang.*
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Created by Lucio on 2022/2/14.
 */
@RunWith(AndroidJUnit4::class)
class CoderTest {

    @Test
    fun testURLCoder() {
        val value = "n s"
        val javaEncode = Coder.URLCoderJava.encode(value)
        val droidEncode = URLCoderDroid.encode(value)
        println(javaEncode)
        println(droidEncode)
        Assert.assertEquals(javaEncode, "n+s")
        Assert.assertEquals(droidEncode, "n%20s")

        Assert.assertEquals(value.toUrlEncode(), "n+s")
        Assert.assertEquals(value, "n+s".toUrlDecode())
        BasConfigurator.setURLCoder(URLCoderDroid)
        Assert.assertEquals(value.toUrlEncode(), "n%20s")
        Assert.assertEquals(value, "n%20s".toUrlDecode())
    }

    @Test
    fun testBase64CoderImpl() {
        val defaultCoder = Base64CoderDroid(android.util.Base64.DEFAULT)
        val nowrapCoder = Base64CoderDroid(android.util.Base64.NO_WRAP)
        Coder.setBase64Encoder(nowrapCoder)
        Coder.setBase64Decoder(nowrapCoder)
        val sourceValue = "euc-kr代表说明网站是采用的编码是韩文; ...等等有很多编码euc-kr代表说明网站是采用的编码是韩文; ...等等有很多编码"

        //期望的base64结果：不带换行
        val expect =
            "ZXVjLWty5Luj6KGo6K+05piO572R56uZ5piv6YeH55So55qE57yW56CB5piv6Z+p5paHOyAuLi7nrYnnrYnmnInlvojlpJrnvJbnoIFldWMta3Lku6Pooajor7TmmI7nvZHnq5nmmK/ph4fnlKjnmoTnvJbnoIHmmK/pn6nmloc7IC4uLuetieetieacieW+iOWkmue8lueggQ=="
        //期望的base64结果：带换行
        val expectWraped =
            "ZXVjLWty5Luj6KGo6K+05piO572R56uZ5piv6YeH55So55qE57yW56CB5piv6Z+p5paHOyAuLi7n\r\n" +
                    "rYnnrYnmnInlvojlpJrnvJbnoIFldWMta3Lku6Pooajor7TmmI7nvZHnq5nmmK/ph4fnlKjnmoTn\r\n" +
                    "vJbnoIHmmK/pn6nmloc7IC4uLuetieetieacieW+iOWkmue8lueggQ=="

        println("base64 decoded wrapper ${expectWraped.toBase64DecodeString()}")
        println("base64 decoded nowrapper ${expect.toBase64DecodeString()}")

        //换行和不换行的字符串解码结果相同
        Assert.assertEquals(expect.toBase64DecodeString(), expectWraped.toBase64DecodeString())
        Assert.assertEquals(sourceValue, expect.toBase64DecodeString())

        val encodedString = sourceValue.toBase64EncodeString()
        println("base64 encoded = $encodedString")
        //编码结果相同
        Assert.assertEquals(expect, encodedString)
        Assert.assertNotEquals(expectWraped, encodedString)

        val decodedString = encodedString.toBase64DecodeString()
        println("base64 decoded = $decodedString")
        Assert.assertEquals(sourceValue, decodedString)

        val ss = defaultCoder.decodeToString(expect.toByteArray())
        val ss1 = defaultCoder.decodeToString(expectWraped.toByteArray())

        val ss2 = nowrapCoder.decodeToString(expect.toByteArray())
        val ss21 = nowrapCoder.decodeToString(expectWraped.toByteArray())
        Assert.assertEquals(ss, ss2)
        Assert.assertEquals(ss, ss21)
        Assert.assertEquals(ss1, ss21)

    }
}