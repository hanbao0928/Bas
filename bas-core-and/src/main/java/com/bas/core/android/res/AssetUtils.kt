package com.bas.core.android.res

import android.content.Context
import com.bas.core.lang.annotation.Suggestion
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader

/**
 * Created by Lucio on 2021/7/27.
 */
object AssetUtils {

    /**
     * 读取Asset目录下的文件内容
     */
    @JvmStatic
    @Suggestion(message = "建议放在IO线程调用")
    fun readAsset(ctx: Context, fileName: String): String {
        val sb = StringBuilder()
        try {
            val bf = BufferedReader(InputStreamReader(ctx.assets.open(fileName)))
            var line: String?
            while (bf.readLine().also { line = it } != null) {
                sb.append(line)
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return sb.toString()
    }
}