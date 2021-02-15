package data.provider

import androidx.lifecycle.LiveData
import data.model.Note
import data.model.Result
import data.model.User

interface RemoteDataProvider {

    fun subscribeToAllNotes(): Note
    fun getNoteById(id: String): Note
    fun saveNote(note: Note) : Note
    fun getCurrentUser(): User
    fun deleteNote(noteId: String): Note


}
