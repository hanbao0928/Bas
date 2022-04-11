package bas.droid.core.content

import android.content.Context
import android.net.Uri
import android.os.Build
import androidx.core.content.FileProvider
import java.io.File

class FileProviderCompat : FileProvider() {

    companion object {

        @JvmStatic
        fun getAuthorities(context: Context): String {
            return "${context.packageName}.FileProviderCompat"
        }

        /**
         * 根据文件对应的URI，兼容7.0及以上
         */
        @JvmStatic
        fun getUriForFile(context: Context, file: File): Uri {
            return if (Build.VERSION.SDK_INT >= 24) {
                //7.0 以上使用FileProvider
                getUriForFile(context, getAuthorities(context), file)
            } else {
                Uri.fromFile(file)
            }
        }
    }
}