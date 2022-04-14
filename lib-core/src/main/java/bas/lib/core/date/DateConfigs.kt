@file:JvmName("BasConfigsKt")
@file:JvmMultifileClass

//不要更改包名
package bas.lib.core

import bas.lib.core.date.DateFormatUseCase
import bas.lib.core.date.FriendlyDateFormatUseCase
import java.util.*

/**
 * 友好时间格式化器
 */
var friendlyDateFormat: DateFormatUseCase = FriendlyDateFormatUseCase()


var globalLocale: Locale = Locale.getDefault()
    set(value) {
        field = value
    }