package bas.lib.core.converter

import bas.lib.core.converter.fastjson.FastJsonConverter
import bas.lib.core.converter.gson.GsonConverter
import bas.lib.core.converter.jackson.JacksonConverter
import bas.lib.core.lang.CollectionUtils
import org.junit.Test

/**
 * Created by Lucio on 2021/7/22.
 */
class ConverterUnitTestKt {
    @Test
    fun testFastJsonConverter() {
        Converters.setJsonConverter(FastJsonConverter())
        testConverter("FastJson:")
    }

    @Test
    fun testJacksonConverter() {
        Converters.setJsonConverter(JacksonConverter())
        testConverter("Jackson:")
    }

    @Test
    fun testGsonConverter() {
        Converters.setJsonConverter(GsonConverter())
        testConverter("Gson:")
    }

    private fun testConverter(tag: String) {
        println(tag + Converters.getJsonConverter())
        val zhangsan = Person("Zhangsan", 18)
        val zhangsanJson = zhangsan.toJson()
        println(tag + zhangsanJson)
        println(tag + "zhangsan=" + zhangsanJson.toObject<Person>())
        val wang5 = Person("Wang5", 20)
        val persons: List<*> = CollectionUtils.newList(zhangsan, wang5)
        val personsJson = persons.toJson()
        println("$tag\npersons=$personsJson")
        println(
            """
            $tag
            persons=${
                personsJson.toObjectList<Person>()
            }
            """.trimIndent()
        )
    }
}