package bas.droid.arch

import androidx.appcompat.app.AppCompatActivity

abstract class AppCompatActivityArch : AppCompatActivity {
    constructor() : super()
    constructor(contentLayoutId: Int) : super(contentLayoutId)
}