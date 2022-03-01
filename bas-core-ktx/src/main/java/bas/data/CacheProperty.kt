package bas.data

import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

/**
 * Created by Lucio on 2019/10/23.
 * 会超时的数据：指定的[expireMillis]时间之后，获取的数据则为null. 设置数据之后，重新开始计时
 * @param expireMillis 单位毫秒，有效期时间：设置的数据在超过[expireMillis]指定的时间后数据无效
 */
class CacheProperty<T> constructor(
    private val expireMillis: Long,
    private var data: T? = null
) : ReadWriteProperty<Any, T?> {

    //计时时间：ms
    private var timingMillis: Long = 0

    override fun getValue(thisRef: Any, property: KProperty<*>): T? {
        if (System.currentTimeMillis() - timingMillis >= expireMillis) {
            //如果超过了timingTime，则重新获取数据
            data = null
        }
        return data
    }

    override fun setValue(thisRef: Any, property: KProperty<*>, value: T?) {
        bindData(value)
    }

    private fun bindData(data: T?) {
        this.data = data
        timingMillis = System.currentTimeMillis()
    }

}