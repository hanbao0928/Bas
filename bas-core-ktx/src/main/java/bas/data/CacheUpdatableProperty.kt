package bas.data

import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

/**
 * Created by Lucio on 2019/10/23.
 * 会刷新的缓存属性 ：在设置的[expires]时间之后获取数据，会重新调用[acquirer]请求数据。
 * @param expires 数据定时时间，单位ms
 * @param acquirer 数据提供器，超时之后的数据来源
 */
class CacheUpdatableProperty<T> constructor(
    private val expires: Long,
    private var data: T?,
    private val acquirer: () -> T?
) : ReadOnlyProperty<Any?, T?> {

    //计时时间，单位ms
    private var timing: Long = 0

    init {
        if(data != null){
            //初始值不为空，则开始计时
            timing = System.currentTimeMillis()
        }
    }

    override fun getValue(thisRef: Any?, property: KProperty<*>): T? {
        if (System.currentTimeMillis() - timing >= expires) {
            //如果超过了timingTime，则重新获取数据
            data = acquirer.invoke()
            timing = System.currentTimeMillis()
        }
        return data
    }

}