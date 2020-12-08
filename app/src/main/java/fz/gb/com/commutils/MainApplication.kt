package fz.gb.com.commutils

import androidx.multidex.MultiDexApplication
import com.fz.common.text.isNonEmpty

class MainApplication : MultiDexApplication() {
    override fun onCreate() {
        super.onCreate()
        null.isNonEmpty("ssss")
    }

}