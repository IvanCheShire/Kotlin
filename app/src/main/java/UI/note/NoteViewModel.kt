package UI.note

import UI.Repository
import UI.base.BaseViewModel
import data.model.Note
import data.model.Result
import java.util.*



class NoteViewModel(repository: Repository)
 {

    private var pendingNote: Note? = null


     fun saveChanges(note: Note) {
         viewStateLiveData.value = NoteViewState(NoteViewState.Data(note = note))
     }

     override fun onCleared() {
         currentNote?.let { repository.saveNote(it) }
     }



     fun loadNote(noteId: String) {
         repository.getNoteById(noteId).observeForever { t ->
             t?.let {
                 viewStateLiveData.value = when (t) {
                     is Result.Success<*> -> NoteViewState(NoteViewState.Data(note = t.data as? Note))
                     is Result.Error -> NoteViewState(error = t.error)
                 }
             }
         }
     }
     private val currentNote: Note?
         get() = viewStateLiveData.value?.data?.note

     fun deleteNote() {
         currentNote?.let {
             repository.deleteNote(it.id).observeForever { t ->
                 t?.let {
                     viewStateLiveData.value = when (it) {
                         is Result.Success<*> -> NoteViewState(NoteViewState.Data(isDeleted = true))
                         is Result.Error -> NoteViewState(error = it.error)
                     }
                 }
             }
         }
     }


 }
