package data.provider

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.*
import data.errors.NoAuthException
import data.model.Note
import data.model.Result
import data.model.User
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.ReceiveChannel
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine


private const val NOTES_COLLECTION = "notes"
private const val USERS_COLLECTION = "users"


class FireStoreProvider(private val firebaseAuth: FirebaseAuth,
                        private val db: FirebaseFirestore) : RemoteDataProvider
{

    private val TAG = "${FireStoreProvider::class.java.simpleName} :"

    private val db = FirebaseFirestore.getInstance()
    private val notesReference = db.collection(NOTES_COLLECTION)
    private val currentUser
        get() = FirebaseAuth.getInstance().currentUser



    override fun subscribeToAllNotes(): ReceiveChannel<Result> =
            Channel<Result>(Channel.CONFLATED).apply {
                var registration: ListenerRegistration? = null

                try {
                    registration =
                            getUserNotesCollection().addSnapshotListener { snapshot, e ->
                                val value = e?.let {
                                    Result.Error(it)
                                } ?: snapshot?.let {
                                    val notes = it.documents.map {
                                        it.toObject(Note::class.java)
                                    }
                                    Result.Success(notes)
                                }

                                value?.let { offer(it) }
                            }
                } catch (e: Throwable) {
                    offer(Result.Error(e))
                }

                invokeOnClose { registration?.remove() }
            }


    override suspend fun saveNote(note: Note): Note =
            suspendCoroutine { continuation ->
                try {
                    getUserNotesCollection().document(note.id)
                            .set(note).addOnSuccessListener {
                                Log.d(TAG, "Note $note is saved")
                                continuation.resume(it.toObject(Note::class.java)!!)
                            }.addOnFailureListener {
                                Log.d(TAG, "Error saving note $note, message: ${it.message}")
                                continuation.resumeWithException(it)
                            }
                } catch (e: Throwable) {
                    continuation.resumeWithException(e)
                }
            }

    override suspend fun getNoteById(id: String): Note =
            suspendCoroutine { continuation ->
                try {
                    getUserNotesCollection().document(id).get()
                            .addOnSuccessListener {
                                continuation.resume(it.toObject(Note::class.java)!!)
                            }.addOnFailureListener {
                                continuation.resumeWithException(it)
                            }
                } catch (e: Throwable) {
                    continuation.resumeWithException(e)
                }
            }


    private fun getUserNotesCollection() = currentUser?.let {
        db.collection(USERS_COLLECTION).document(it.uid).collection(NOTES_COLLECTION)
    } ?: throw NoAuthException()

    override suspend fun getCurrentUser(): User =
            suspendCoroutine { continuation ->
                try {
                    db.collection(USERS_COLLECTION).document(name, email).get()
                            .addOnSuccessListener {
                                continuation.resume(it.toObject(User::class.java)!!)
                            }.addOnFailureListener {
                                continuation.resumeWithException(it)
                            }
                } catch (e: Throwable) {
                    continuation.resumeWithException(e)
                }
            }

    override suspend fun deleteNote(noteId: String): Note =
            suspendCoroutine { continuation ->
                try {
                    getUserNotesCollection().document(noteId).delete()
                            .addOnSuccessListener {
                                continuation.resume(it.toObject(Note::class.java)!!)
                            }.addOnFailureListener {
                                continuation.resumeWithException(it)
                            }
                } catch (e: Throwable) {
                    continuation.resumeWithException(e)
                }
            }


}




