package bas.droid.net.retrofit

import kotlin.reflect.KClass

/**
 * Created by Lucio on 2018/7/12.
 */

class ApiServiceLazy<Service : Any>(
    private val clz: KClass<Service>,
    private val factory: ApiServiceFactory
) : Lazy<Service> {

    @Volatile
    private var _service: Service? = null

    private var _url: String? = null


    override val value: Service
        get() {
            val retrofit = factory.retrofit
            val currentUrl = retrofit.baseUrl().toString()
            if (currentUrl != _url) {//不可用
                //检查service是否可用：如果当前service的url与client的url不一致，则不可用
                _service = null
            }
            val service = _service
            if (service == null) {
                synchronized(ApiServiceLazy::class.java) {
                    if (_service == null) {
                        _url = currentUrl
                        _service = retrofit.create(clz.java)
                    }
                }
            }
            return _service!!
        }

    override fun isInitialized(): Boolean = _service != null
}

inline fun <reified Service : Any> apiService(factory: ApiServiceFactory): Lazy<Service> {
    return ApiServiceLazy(Service::class, factory)
}