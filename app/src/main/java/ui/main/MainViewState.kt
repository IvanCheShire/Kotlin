package ui.main

import ui.base.BaseViewState
import data.model.Note



class MainViewState(notes: List<Note>? = null, error: Throwable? = null)
    : BaseViewState<List<Note>?>(notes, error)

