package ui.note

import ui.Repository
import data.model.Note
import data.model.Result
import kotlinx.coroutines.launch
import ui.base.BaseViewModel
import java.util.*




class NoteViewModel(private val repository: Repository) : BaseViewModel<NoteViewState.NoteData>() {

    private val currentNote: Note?
        get() = getViewState().poll()?.note

    fun saveChanges(note: Note) {
        setData(NoteViewState.NoteData(note = note))
    }

    fun loadNote(noteId: String) {
        launch {
            try {
                repository.getNoteById(noteId).let {
                    setData(NoteViewState.NoteData(note = it))
                }
            } catch (e: Throwable) {
                setError(e)
            }
        }
    }


    fun deleteNote() {
        launch {
            try {
                currentNote?.let { repository.deleteNote(it.id) }
                setData(NoteViewState.NoteData(isDeleted = true))
            } catch (e: Throwable) {
                setError(e)
            }
        }
    }


    override fun onCleared() {
        launch {
            currentNote?.let { repository.saveNote(it) }
            super.onCleared()
        }
    }
}

