package bas.data

import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

/**
 * Created by Lucio on 2019/10/23.
 * 会超时的数据：指定的[expires]时间之后，获取的数据则为null. 设置数据之后，重新开始计时
 * @param expires 单位毫秒，有效期时间：设置的数据在超过[expires]指定的时间后数据无效
 */
class CacheProperty<T> constructor(
    private val expires: Long,
    private var data: T? = null
) : ReadWriteProperty<Any, T?> {

    //计时时间：ms
    private var timing: Long = 0

    override fun getValue(thisRef: Any, property: KProperty<*>): T? {
        if (System.currentTimeMillis() - timing >= expires) {
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
        timing = System.currentTimeMillis()
    }

}