package bas.core

import bas.lang.toBase64DecodeString
import bas.lang.toBase64EncodeString
import bas.lang.toUrlDecode
import bas.lang.toUrlEncode
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Test

/**
 * Created by Lucio on 2022/2/23.
 */
class CoderUnitTest {

    @Test
    fun testUrlCoder() = runBlocking {
        val value = "n s"
        val javaEncode = value.toUrlEncode()
        println(javaEncode)
        Assert.assertEquals(javaEncode, "n+s")
        Assert.assertEquals(value, "n+s".toUrlDecode())
        Assert.assertEquals(value.toUrlEncode(), "n+s")
    }

    @Test
    fun testBase64Coder() = runBlocking {
        val sourceValue = "euc-kr代表说明网站是采用的编码是韩文; ...等等有很多编码euc-kr代表说明网站是采用的编码是韩文; ...等等有很多编码"

        //期望的base64结果：不带换行
        val expect =
            "ZXVjLWty5Luj6KGo6K+05piO572R56uZ5piv6YeH55So55qE57yW56CB5piv6Z+p5paHOyAuLi7nrYnnrYnmnInlvojlpJrnvJbnoIFldWMta3Lku6Pooajor7TmmI7nvZHnq5nmmK/ph4fnlKjnmoTnvJbnoIHmmK/pn6nmloc7IC4uLuetieetieacieW+iOWkmue8lueggQ=="
        //期望的base64结果：带换行
        val expectWraped =
            "ZXVjLWty5Luj6KGo6K+05piO572R56uZ5piv6YeH55So55qE57yW56CB5piv6Z+p5paHOyAuLi7n\n" +
                    "rYnnrYnmnInlvojlpJrnvJbnoIFldWMta3Lku6Pooajor7TmmI7nvZHnq5nmmK/ph4fnlKjnmoTn\n" +
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
    }
}