package UI.base

import UI.Repository
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel


open class BaseViewModel(repository: Repository){

    open val viewStateLiveData = MutableLiveData<S>()

    open fun getViewState(): LiveData<S> = viewStateLiveData
}
