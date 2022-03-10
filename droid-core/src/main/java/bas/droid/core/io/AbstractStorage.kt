package bas.droid.core.io

import android.content.Context
import android.os.Build
import android.os.Environment
import bas.droid.core.ctxBas
import java.io.File

/**
 * Created by Lucio on 2022/3/7.
 */
abstract class AbstractStorage : Storage {

    private val ctx: Context get() = ctxBas

    protected open fun isStorageStateAvailable(file: File): Boolean {
        try {
            return (Build.VERSION.SDK_INT >= 21
                    && Environment.getExternalStorageState(file) == Environment.MEDIA_MOUNTED)
                    || Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED
        } catch (e: Throwable) {
            //在某些盒子里面，提示出现该异常：Could not find method android.os.Environment.getExternalStorageState, referenced from method andme.core.support.io.AMStorageImpl.isStorageStateAvailable
            return false
        }
    }

    /**
     * 外置存储是否可用
     */
    override val isExternalStorageAvailable: Boolean
        get() = Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED

    /**
     * 获取内部缓存目录
     * app内部缓存目录路径[android.content.Context.getCacheDir]，eg./data/user/0/{packagename}/cache 用户无法查看目录（root可看）
     */
    override fun getInnerCacheDirectory(): File {
        return ctx.cacheDir
    }

    /**
     * 获取缓存目录
     * 如果外部存储可用，则为[android.content.Context.getExternalCacheDir]，eg：/storage/emulated/0/Android/data/{packagename}/cache
     * 否则为app内部缓存目录路径[getInnerCacheDirectory]，用户无法查看
     */
    override fun getCacheDirectory(): File {
        val extCacheDir = ctx.externalCacheDir

        if (extCacheDir != null && isStorageStateAvailable(extCacheDir)) {
            return extCacheDir
        }
        return getInnerCacheDirectory()
    }

    /**
     * 获取缓存目录，路径值为[getCacheDirectory]/[child]
     * @param child 子目录路径
     */
    override fun getCacheDirectory(child: String): File {
        require(child.isNotEmpty()) { "child path is empty." }
        val parent = getCacheDirectory()
        val dir = File(parent, child)
        ensureDirectoryAvailable(dir)
        return dir
    }

    /**
     * 获取内部文件目录
     * app内部文件目录路径[android.content.Context.getFilesDir]，用户无法查看 ,eg. /data/user/0/{packagename}/files
     */
    override fun getInnerFilesDirectory(): File {
        return ctx.filesDir
    }

    /**
     * 获取文件目录
     * 如果外部存储可用，则为[android.content.Context.getExternalFilesDir]，eg. /storage/emulated/0/Android/data/{packagename}/files
     * 否则为app内部缓存目录路径[getInnerFilesDirectory]
     */
    override fun getFilesDirectory(): File {
        val extFileDir = ctx.getExternalFilesDir(null)
        if (extFileDir != null && isStorageStateAvailable(extFileDir)) {
            return extFileDir
        }
        return getInnerFilesDirectory()
    }

    /**
     * 获取文件目录，路径值为[getFilesDirectory]/[child]
     * @param child 子目录路径
     */
    override fun getFilesDirectory(child: String): File {
        require(child.isNotEmpty()) { "child path is empty." }
        val parent = getFilesDirectory()
        val dir = File(parent, child)
        ensureDirectoryAvailable(dir)
        return dir
    }

    /**
     * 获取内部Download目录
     * app内部文件目录路径[android.content.Context.getFilesDir].parent，用户无法查看 ,eg. /data/user/0/{packagename}/files/Download
     */
    override fun getInnerDownloadDirectory(): File {
        val dir = File(getInnerFilesDirectory(), DIRECTORY_DOWNLOADS)
        ensureDirectoryAvailable(dir)
        return dir
    }

    /**
     * 获取Download目录
     * 如果外部存储可用，则为[android.content.Context.getExternalFilesDir].parent，eg.  /storage/emulated/0/Android/data/{packagename}/files/Download
     * 否则为app内部缓存目录路径[getInnerDownloadDirectory]
     */
    override fun getDownloadDirectory(): File {
        return getFilesDirectory(DIRECTORY_DOWNLOADS)
    }

    companion object {

        const val DIRECTORY_DOWNLOADS = "Download"
    }
}