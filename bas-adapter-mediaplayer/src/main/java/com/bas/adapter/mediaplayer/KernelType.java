package com.bas.adapter.mediaplayer;

import androidx.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import static com.bas.adapter.mediaplayer.KernelType.QN;
import static com.bas.adapter.mediaplayer.KernelType.SYS;
import static com.bas.adapter.mediaplayer.KernelType.TX;

/**
 * Created by Lucio on 2021/9/28.
 */

@IntDef({SYS, TX, QN})
@Retention(RetentionPolicy.SOURCE)
public @interface KernelType {

    int LAZY = -1;

    int SYS = 0;

    int TX = 1;

    int QN = 2;

}
