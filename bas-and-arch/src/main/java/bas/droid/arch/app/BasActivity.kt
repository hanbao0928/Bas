package bas.droid.arch.app

import androidx.appcompat.app.AppCompatActivity

/**
 * Created by Lucio on 2021/12/6.
 */
open class BasActivity : AppCompatActivity {
    constructor() : super()
    constructor(contentLayoutId: Int) : super(contentLayoutId)
}