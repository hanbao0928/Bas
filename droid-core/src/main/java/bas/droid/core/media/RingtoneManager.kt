package bas.droid.core.media

import android.content.Context
import android.media.Ringtone
import android.media.RingtoneManager

/**
 * Created by Lucio on 2021/2/19.
 * 铃声管理
 */
object RingtoneManager {

    /**
     * 播放通知声音
     */
    @JvmStatic
    fun playNotification(ctx: Context): Ringtone? {
        val notificationTone =
            RingtoneManager.getActualDefaultRingtoneUri(ctx, RingtoneManager.TYPE_NOTIFICATION)
                ?: RingtoneManager.getValidRingtoneUri(ctx)
        val r = RingtoneManager.getRingtone(ctx, notificationTone)
        r?.play()
        return r
    }

    /**
     * 播放系统默认通知声音
     */
    @JvmStatic
    fun playDefaultNotification(ctx: Context): Ringtone? {
        val notificationTone = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
            ?: RingtoneManager.getValidRingtoneUri(ctx)
        val r = RingtoneManager.getRingtone(ctx, notificationTone)
        r?.play()
        return r
    }
}