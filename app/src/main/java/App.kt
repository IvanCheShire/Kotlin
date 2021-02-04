import DI.appModule
import DI.mainModule
import DI.noteModule
import DI.splasModule
import androidx.multidex.MultiDexApplication
import org.koin.android.ext.android.startKoin

class App : MultiDexApplication() {

    override fun onCreate() {
        super.onCreate()

        startKoin(this, listOf(appModule, splasModule, mainModule, noteModule))
    }
}