package bas.droid.adapter.mediaplayer;

import androidx.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import static bas.droid.adapter.mediaplayer.KernelType.QN;
import static bas.droid.adapter.mediaplayer.KernelType.SYS;
import static bas.droid.adapter.mediaplayer.KernelType.TX;

/**
 * Created by Lucio on 2021/9/28.
 */

@IntDef({SYS, TX, QN})
@Retention(RetentionPolicy.SOURCE)
public @interface KernelType {

    /**
     * 懒加载，即[ConfigurableVideoView]启动时不加载播放器内核，由调用方指定
     */
    int LAZY = -1;

    /**
     * 系统播放器
     */
    int SYS = 0;

    /**
     * 腾讯播放器
     */
    int TX = 1;

    /**
     * 七牛播放器
     */
    int QN = 2;

}
