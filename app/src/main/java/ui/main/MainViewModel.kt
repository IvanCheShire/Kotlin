package ui.main

import ui.Repository
import androidx.annotation.VisibleForTesting
import androidx.lifecycle.Observer
import data.model.Note
import data.model.Result
import data.model.Result.Error

class MainViewModel(repository: Repository){

    private val notesObserver = object : Observer<Result> {//Стандартный обсервер LiveData
    override fun onChanged(t: Result?) {
        if (t == null) return

        when(t) {
            is data.model.NoteResult.Result.Success<*> -> {

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

    @VisibleForTesting
    public override fun onCleared() {
        repositoryNotes.removeObserver(notesObserver)
    }
}
