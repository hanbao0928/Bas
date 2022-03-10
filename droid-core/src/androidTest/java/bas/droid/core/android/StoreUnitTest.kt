package bas.droid.core.android


import androidx.test.platform.app.InstrumentationRegistry
import bas.droid.core.ctxBas
import bas.droid.core.storage
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import java.io.File

/**
 * Created by Lucio on 2020-02-28.
 */
@RunWith(JUnit4::class)
class AMStoreTest {

    @Test
    fun getPaths() = runBlocking {
        ctxBas = InstrumentationRegistry.getInstrumentation().targetContext
        println("isExternalStorageAvailable=${storage.isExternalStorageAvailable}")
        println("CacheDirectory=${storage.getCacheDirectory().absolutePath}")
        println("InnerCacheDirectory=${storage.getInnerCacheDirectory().absolutePath}")
        println("ImageCacheDirectory=${storage.getCacheDirectory("Image").absolutePath}")
        println("CustomCacheDirectory=${storage.getCacheDirectory("Custom")}")
        println("CustomCachePath=${File( storage.getCacheDirectory("Custom"),"cache.txt")}")

        println("FilesDirectory=${storage.getFilesDirectory().absolutePath}")
        println("InnerFilesDirectory=${storage.getInnerFilesDirectory().absolutePath}")
        println("CustomFileDirectory=${storage.getFilesDirectory("Custom")}")
        println("CustomFilePath=${File( storage.getFilesDirectory("Custom"),"child.txt")}")

        println("DownloadDirectory=${storage.getDownloadDirectory().absolutePath}")
        println("InnerDownloadDirectory=${storage.getInnerDownloadDirectory().absolutePath}")
    }


}