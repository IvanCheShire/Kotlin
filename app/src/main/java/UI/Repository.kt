package UI

import android.provider.ContactsContract
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import data.model.Color
import data.model.Note
import data.provider.FireStoreProvider
import data.provider.RemoteDataProvider
import java.util.*




class Repository(private val remoteProvider: RemoteDataProvider)
{
    private val remoteProvider: RemoteDataProvider = FireStoreProvider()

    fun getNotes() = remoteProvider.subscribeToAllNotes()
    fun saveNote(note: Note) = remoteProvider.saveNote(note)
    fun getNoteById(id: String) = remoteProvider.getNoteById(id)
    fun getCurrentUser() = remoteProvider.getCurrentUser()
    fun deleteNote(noteId: String) = remoteProvider.deleteNote(noteId)

}

