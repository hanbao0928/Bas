package bas.lib.core

import bas.lib.core.converter.Converters
import bas.lib.core.converter.JsonConverter
import bas.lib.core.date.DateFormatUseCase
import bas.lib.core.date.friendlyDateFormat
import bas.lib.core.exception.ExceptionHandler
import bas.lib.core.exception.ExceptionMessageTransformer
import bas.lib.core.exception.exceptionHandler
import bas.lib.core.exception.exceptionMessageTransformer
import bas.lib.core.lang.Coder

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

    /**
     * 设置友好日期格式化用例
     */
    @JvmStatic
    fun setFriendlyDateFormat(format:DateFormatUseCase){
        friendlyDateFormat = format
    }

}