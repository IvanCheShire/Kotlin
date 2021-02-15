package ui.note

import ui.base.BaseViewState
import data.model.Note


class NoteViewState(data: NoteData = NoteData(),
                    error: Throwable? = null) : BaseViewState<NoteViewState.NoteData>(data, error) {

    data class NoteData(val isDeleted: Boolean = false, val note: Note? = null)
}
