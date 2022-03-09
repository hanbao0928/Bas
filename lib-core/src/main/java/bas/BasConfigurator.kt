package bas

import bas.converter.Converters
import bas.converter.JsonConverter
import bas.lang.Coder

/**
 * Created by Lucio on 2022/2/13.
 */
object BasConfigurator {

    /**
     * 设置序列化和反序列化工具
     */
    @JvmStatic
    fun setJsonConverter(converter: JsonConverter) {
        Converters.setJsonConverter(converter)
    }

    /**
     * 设置URL编解码工具
     */
    @JvmStatic
    fun setURLCoder(coder: Coder.URLCoder){
        Coder.setURLCoder(coder)
    }

    @JvmStatic
    fun setBase64Encoder(encoder: Coder.Base64Encoder) {
        Coder.setBase64Encoder(encoder)
    }

    @JvmStatic
    fun setBase64Decoder(decoder: Coder.Base64Decoder) {
        Coder.setBase64Decoder(decoder)
    }

}