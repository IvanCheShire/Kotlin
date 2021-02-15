package ui.splash

import ui.Repository
import data.errors.NoAuthException
import kotlinx.coroutines.launch
import ui.base.BaseViewModel


class SplashViewModel(private val repository: Repository) : BaseViewModel<Boolean>() {

    fun requestUser() {
        launch {
            repository.getCurrentUser()?.let {
                setData(true)
            } ?: setError(NoAuthException())
        }
    }
}
