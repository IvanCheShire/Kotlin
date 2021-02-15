package ui.splash

import ui.base.BaseViewState

class SplashViewState(isAuth: Boolean? = null, error: Throwable? = null) :
        BaseViewState<Boolean?>(isAuth, error)