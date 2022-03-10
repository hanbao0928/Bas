package bas.droid.core.io

import bas.droid.core.util.Logger

/**
 * Created by Lucio on 2019/6/30.
 *
 * 默认存储实现
 */

internal object DefaultStorage : AbstractStorage(){
    init {
        Logger.d("Storage","init success.")
    }
}
