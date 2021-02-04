package data.provider

import androidx.lifecycle.LiveData
import data.model.Note
import data.model.Result
import data.model.User

interface RemoteDataProvider {

    fun subscribeToAllNotes(): LiveData<Result>
    fun getNoteById(id: String): LiveData<Result>
    fun saveNote(note: Note) : LiveData<Result>
    fun getCurrentUser(): LiveData<User?>
    fun deleteNote(noteId: String): LiveData<Result>


}
