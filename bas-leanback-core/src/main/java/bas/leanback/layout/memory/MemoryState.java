package bas.leanback.layout.memory;

import androidx.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by Lucio on 2021/11/26.
 */
@IntDef({MemoryState.NONE,MemoryState.SELECTED,MemoryState.ACTIVATED})
@Retention(RetentionPolicy.SOURCE)
public @interface MemoryState {

    /**
     * 不处理任何焦点记忆View的状态
     */
    int NONE = 0;

    /**
     * 焦点记忆的view状态类型
     * 即保持当前焦点记忆的[View]设置[View.setSelected]为true，而其他view则设置为false
     */
    int SELECTED = 1;

    /**
     * 焦点记忆的view状态类型
     * 即保持当前焦点记忆的[View]设置[View.setActivated]为true，而其他view则设置为false
     */
    int ACTIVATED = 2;
    
}
