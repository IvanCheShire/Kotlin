package ui.base

import ui.splash.SplashViewModel
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.firebase.ui.auth.AuthUI
import com.google.android.material.snackbar.Snackbar
import data.errors.NoAuthException
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.consumeEach
import org.koin.android.ext.android.inject
import org.koin.android.viewmodel.ext.android.viewModel
import ru.geekbrains.kotlin.R
import kotlin.coroutines.CoroutineContext


private const val RC_SIGN_IN = 458

abstract class BaseActivity<T> : AppCompatActivity(), CoroutineScope {

    override val coroutineContext: CoroutineContext by lazy { Dispatchers.Main + Job() }
    private lateinit var dataJob: Job
    private lateinit var errorJob: Job
    abstract val model: BaseViewModel<T>
    abstract val layoutRes: Int


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(layoutRes)
    }
    override fun onStart() {
        super.onStart()
        dataJob = launch {
            model.getViewState().consumeEach {
                renderData(it)
            }
        }

        errorJob = launch {
            model.getErrorChannel().consumeEach {
                renderError(it)
            }
        }
    }
    override fun onStop() {
        super.onStop()
        dataJob.cancel()
        errorJob.cancel()
    }

    override fun onDestroy() {
        super.onDestroy()
        coroutineContext.cancel()
    }



    protected fun renderError(error: Throwable) {
            error.message?.let { showError(it) }
        }

        abstract fun renderData(data: T)

        protected fun showError(error: String) {
            Snackbar.make(mainRecycler, error, Snackbar.LENGTH_INDEFINITE).apply {
                setAction(R.string.ok_bth_title) { dismiss() }
                show()
            }
        }
        protected open fun renderError(error: Throwable) {
            when(error) {
                is NoAuthException -> startLoginActivity()
                else -> error.message?.let { showError(it) }
            }
        }

        private fun startLoginActivity() {
            val providers = listOf(
                    AuthUI.IdpConfig.EmailBuilder().build(),
                    AuthUI.IdpConfig.GoogleBuilder().build())

            startActivityForResult(
                    AuthUI.getInstance()
                            .createSignInIntentBuilder()
                            .setLogo(R.drawable.android_robot)
                            .setTheme(R.style.LoginStyle)
                            .setAvailableProviders(providers)
                            .build(),
                    RC_SIGN_IN)
        }
        override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
            if (requestCode == RC_SIGN_IN && resultCode != Activity.RESULT_OK) {
                finish()
            }
        }

    }


