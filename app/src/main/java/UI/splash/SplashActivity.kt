package UI.splash

import UI.base.BaseActivity
import UI.main.MainActivity
import UI.note.NoteActivity.Companion.getStartIntent
import android.os.Handler
import androidx.lifecycle.ViewModelProviders
import org.koin.android.ext.android.inject
import org.koin.android.viewmodel.ext.android.viewModel
import ru.geekbrains.kotlin.R


private const val START_DELAY = 1000L

class SplashActivity : BaseActivity<Boolean?, SplashViewState>() {

    override val viewModel: SplashViewModel by inject()
    override val model: SplashViewModel by viewModel()
    override val layoutRes: Int = R.layout.activity_splash

    override fun onResume() {
        super.onResume()
        Handler().postDelayed({ viewModel.requestUser() }, START_DELAY)

    }

    override fun renderData(data: Boolean?) {
        data?.takeIf{ it }?.let {
            startMainActivity()
        }
    }


    private fun startMainActivity() {
        startActivity(MainActivity.getStartIntent(this))
        finish()
    }
}
