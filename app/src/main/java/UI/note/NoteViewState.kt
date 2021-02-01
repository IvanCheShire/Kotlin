package UI.note

import UI.base.BaseViewState
import data.model.Note

class NoteViewState(note: Note? = null, error: Throwable? = null) : BaseViewState<Note?>(note, error)