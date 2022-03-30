package bas.lib.core


import bas.lib.core.lang.Coder
import bas.lib.core.lang.security.toRSADecryptString
import bas.lib.core.lang.security.toRSAEncryptString
import org.junit.Test

/**
 * Created by Lucio on 2022/2/24.
 */
class RSAUnitTest {

    @Test
    fun testRSAEncrpyt(){
        val data = "你好Hello World"
        Coder.setBase64Encoder(Coder.Base64EncoderJava(Coder.Base64EncoderJava.DEFAULT))
        val pubKey = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAvSwWdeY92hg9pDyE+9FtJxqjVlfEjjrJeKqA6rriDFeMee9d0Jo1zmH/zwf54paCM6HuOt+6hCA/Bg2H535dyjn8DfubjirxA9Oy+e5km8I5Ktdel8UfnkgyaFsjg/HZbIZFEtDEksCku/di3YZbrj/wRj/op368NJ/lvJoa5g2tFZck6qYK+KJqzoPAJ7yWl9OAgbDDX0r2h7bMmazx/hnX7slmb/s/oW/bJ8us4bVL19SMmxL0vWkeflyUHHEyaji0unRgbh+aeDWPhr2rfHFHc9GqxAC4EO3W4bB/2sAF3kfabeWH361pfsZz5ZS8HOqv1EXq3mrwIL71r4W87QIDAQAB"

        val privateKey = "MIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQC9LBZ15j3aGD2kPIT70W0nGqNWV8SOOsl4qoDquuIMV4x5713QmjXOYf/PB/niloIzoe4637qEID8GDYfnfl3KOfwN+5uOKvED07L57mSbwjkq116XxR+eSDJoWyOD8dlshkUS0MSSwKS792LdhluuP/BGP+infrw0n+W8mhrmDa0VlyTqpgr4omrOg8AnvJaX04CBsMNfSvaHtsyZrPH+GdfuyWZv+z+hb9sny6zhtUvX1IybEvS9aR5+XJQccTJqOLS6dGBuH5p4NY+Gvat8cUdz0arEALgQ7dbhsH/awAXeR9pt5YffrWl+xnPllLwc6q/URereavAgvvWvhbztAgMBAAECggEAAjSNXnp8pho6Pbvm4PikCRyqYstLI01UI+/46MOc9v/eR12e/luN3QYPcV5qb1XJ1shAerc4+WlW0r89olk6xqy+X3tbaODfRmglYfyBd79f4zv3glVt9O5qiATn+RXMrxMxovKEGCHeycIsiOCUGQftySPdlnZHw2VJzFUPkzb4d+NgAWmXMCbE9czcW5ooCidUJ7UuUrRoOlpY+oml323y6ljM3TJZbUAszWxkuyPYZKFg0MK2+5RiAqHzWpSjnSiNIGiVZv1ywgkT2HSkz4JquCDmsACmjGljJFUnvhEK8miml3NvN6bzkRNFJy5LUhtk/gXxdEBL7owACrZykQKBgQDfxtILqJcIg2J4WVlQRN86bOl52jI0+s/YC99QlOsu1PQ41ejRorQBBgURA85Msct5UVls0wkar4Fjk6xyRVWdAmZEnF1yo/QQBdbyl7XEB7XUFFccXTTNlaQ3RRcCvTGYl0KYRxO2mxeO8i0lmFByhU7C+awgIPL8GcR4tphSswKBgQDYaaDyIAQV+qATxlCVQoZcEFHMANkEYOKuUBa0A+BzuJ7N6Lii4E5oSnYqG4wRQ9MxhzpN+cmaSm6RsmhuD2c/Bex6hLiABt6OpAQug6RL7Z6OqqndLWKEQwQTpIq4WZt9i4LpqCa0Fkm3sS1Vhb7CFgdzwCazODDa8uWEGvEB3wKBgEWBd9RvNRpL1NYUyo0IF14KXiqe/2E9VjmA1ogs+S++rWzJ5FrY8pguynwSdo2T2+N+xQvsMLO9N686OyHzLzhpemJtPwlQq4oehjkrriWJT4zkFHqW6MPCaxNPvmn3YeRHd0PdL95UJekch7FZsgMgLOqaqkAAe/iZ+FCWRpHJAoGBAMMbcBP6k1RxcRKC77GxmUq1F9mxRfZxlsyKvf0rpZcO8nU3hAar7WSPBTtvPHdwS55SiVk1gvGMe2T1kLrxPicOoyXONDyQdOPqsS5yVpRxtUpwSNZ0DWaquKkw5jPP7aRhO/SGOeaQVYO7w+BfxRxcGfncWceoODudUxkn5PLHAoGBAIFZaIH5givfwSP/XOgO1AbTxwpV0TiPadBqNJbA+BGlqLuGeJ+/rZ/OdDh2IUGDQ7mGMUHIiDQSNKBBjqUEqFp6C7uGQ6qCdnJwBX6BDZx+F1pe44R1CxUWMktmfPKxF+HD7jsphexRIGjj5MSjA+T8gSkGbD/1JKiI9Gpi/Zwj"
        val encrypted = "你好Hello World".toRSAEncryptString(pubKey)
        println("RSA加密后结果：$encrypted")

        val decrypted = encrypted.toRSADecryptString(privateKey)
        println("RSA解密后结果：$decrypted")
    }
}