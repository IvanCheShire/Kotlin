package ui.base

import ui.Repository
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData


open class BaseViewModel(repository: Repository){

    open val viewStateLiveData = MutableLiveData<S>()

    open fun getViewState(): LiveData<S> = viewStateLiveData
}
