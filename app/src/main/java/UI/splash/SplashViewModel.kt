package UI.splash

import UI.Repository
import UI.base.BaseViewModel
import data.errors.NoAuthException


class SplashViewModel(repository: Repository) {

    fun requestUser() {
        repository.getCurrentUser().observeForever {
            viewStateLiveData.value = if (it != null) {
                SplashViewState(isAuth = true)
            } else {
                SplashViewState(error = NoAuthException())
            }
        }
    }
}
