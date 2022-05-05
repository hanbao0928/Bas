package bas.droid.compat.multidex

import android.content.Context
import android.util.Log
import androidx.multidex.MultiDex
import com.bytedance.boost_multidex.BoostMultiDex

/**
 * Created by Lucio on 2021/11/17.
 * 使用字节跳动的安装方式：在机顶盒上会闪退，暂时不能启用该方式
 * @param useJitPackInstallerOnFailure 使用字节方案出错时是否使用官方方案，默认使用
 */
class BoostMultiDexInstaller(private val useJitPackInstallerOnFailure: Boolean = true) :
    Installer() {

    override fun install(context: Context) {
        val time = measureTimeMillis {
            val result: com.bytedance.boost_multidex.Result? = BoostMultiDex.install(context)
            if (result?.fatalThrowable != null) {
                Log.e(
                    "BoostMultiDex",
                    "exception occored on BoostMultiDexInstaller.doInstall ",
                    result.fatalThrowable
                )

                if (useJitPackInstallerOnFailure) {
                    Log.i(
                        "BoostMultiDex",
                        "use MultiDex.install on BoostMultiDexInstaller failure."
                    )
                    MultiDex.install(context)
                }
            }
        }

        Log.i("BoostMultiDex", "install done. it takes time $time")

    }
}