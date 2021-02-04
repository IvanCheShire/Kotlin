package UI.base

import UI.splash.SplashViewModel
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.firebase.ui.auth.AuthUI
import com.google.android.material.snackbar.Snackbar
import data.errors.NoAuthException
import org.koin.android.ext.android.inject
import org.koin.android.viewmodel.ext.android.viewModel
import ru.geekbrains.kotlin.R


private const val RC_SIGN_IN = 458

    abstract class BaseActivity<T, S : BaseViewState<T>> : AppCompatActivity() {

        override val viewModel: SplashViewModel by inject()
        override val model: SplashViewModel by viewModel()
        abstract val layoutRes: Int

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            setContentView(layoutRes)
            viewModel.getViewState().observe(this, Observer<S> { t ->
                t?.apply {
                    data?.let { renderData(it) }
                    error?.let { renderError(it) }
                }
            })
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


