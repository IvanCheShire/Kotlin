package UI.main

import UI.Repository
import UI.base.BaseViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import data.model.Note
import data.model.NoteResult
import data.model.NoteResult.Error

class MainViewModel(val repository: Repository = Repository) : BaseViewModel<List<Note>?, MainViewState>() {

    private val notesObserver = object : Observer<NoteResult> {//Стандартный обсервер LiveData
    override fun onChanged(t: NoteResult?) {
        if (t == null) return

        when(t) {
            is NoteResult.Success<*> -> {

                viewStateLiveData.value = MainViewState(notes = t.data as? List<Note>)
            }
            is Error -> {

                viewStateLiveData.value = MainViewState(error = t.error)
            }
        }
    }
    }

    private val repositoryNotes = repository.getNotes()

    init {
        viewStateLiveData.value = MainViewState()
        repositoryNotes.observeForever(notesObserver)
    }

    override fun onCleared() {
        repositoryNotes.removeObserver(notesObserver)
    }
}
