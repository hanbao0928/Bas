package bas.droid.net.retrofit

import retrofit2.Retrofit
import kotlin.reflect.KClass

interface ApiServiceFactory {
    val retrofit: Retrofit

    fun <T : Any> createService(clz: KClass<T>): T = retrofit.create(clz.java)
}