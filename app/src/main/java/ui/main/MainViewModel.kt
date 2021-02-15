package ui.main

import ui.Repository
import androidx.annotation.VisibleForTesting
import androidx.lifecycle.Observer
import data.model.Note
import data.model.Result
import data.model.Result.Error
import kotlinx.coroutines.launch
import ui.base.BaseViewModel


class MainViewModel(private val repository: Repository) : BaseViewModel<List<Note>?>() {

    private val notesChannel = repository.getNotes()

    init {
        launch {
            notesChannel.consumeEach {
                when (it) {
                    is Result.Success<*> -> setData(it.data as? List<Note>)
                    is Error -> setError(it.error)
                }
            }
        }
    }

    override fun onCleared() {
        notesChannel.cancel()
        super.onCleared()
    }
}

