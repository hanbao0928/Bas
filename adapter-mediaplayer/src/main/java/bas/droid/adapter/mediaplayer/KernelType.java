package bas.droid.adapter.mediaplayer;

import androidx.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by Lucio on 2021/9/28.
 */

@IntDef({KernelType.LAZY, KernelType.SYS, KernelType.TX, KernelType.QN})
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

    /**
     * GSY 播放器
     */
    int GSY = 3;
}
