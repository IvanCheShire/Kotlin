package ui.note

import ui.base.BaseActivity
import ui.main.extentions.getColorInt
import ui.splash.SplashViewModel
import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.view.Menu
import android.view.MenuItem
import data.model.Color
import data.model.Note
import org.jetbrains.anko.alert
import org.jetbrains.anko.startActivity
import org.koin.android.ext.android.inject
import org.koin.android.viewmodel.ext.android.viewModel
import ru.geekbrains.kotlin.R
import java.util.*
private const val SAVE_DELAY = 2000L

class NoteActivity : BaseActivity<NoteViewState.Data, NoteViewState>() {
    override val viewModel: SplashViewModel by inject()
    override val model: SplashViewModel by viewModel()
    override val layoutRes: Int = R.layout.activity_note
    private var note: Note? = null


    companion object {
        private val EXTRA_NOTE = NoteActivity::class.java.name + "extra.NOTE"

        fun start(context: Context, noteId: String?) =
                context.startActivity<NoteActivity>(EXTRA_NOTE to noteId)
    }


    private var note: Note? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val noteId = intent.getStringExtra(EXTRA_NOTE)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        noteId?.let {
            viewModel.loadNote(it)
        }

        if (noteId == null ) supportActionBar?.title = getString(R.string.new_note_title)

        titleEt.addTextChangedListener(textChangeListener)
        bodyEt.addTextChangedListener(textChangeListener)
        colorPicker.onColorClickListener = {
            color = it
            setToolbarColor(it)
            triggerSaveNote()
        }
    }


    override fun renderData(data: NoteViewState.Data) {
        if (data.isDeleted) finish()

        this.note = data.note
        data.note?.let { color = it.color }
        initView()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean =
            menuInflater.inflate(R.menu.note_menu, menu).let { true }

    override fun onOptionsItemSelected(item: MenuItem): Boolean = when (item.itemId) {
        android.R.id.home -> super.onBackPressed().let { true }
        R.id.palette -> togglePalette().let { true }
        R.id.delete -> deleteNote().let { true }
        else -> super.onOptionsItemSelected(item)
    }


    private fun initView() {

        note?.run {
            supportActionBar?.title = lastChanged.format()
            toolbar.setBackgroundColor(color.getColorInt(this))

            removeEditListener()
            titleEt.setText(title)
            bodyEt.setText(body)
            setEditListener()
        }
    }


    private fun togglePalette() {
        if (colorPicker.isOpen) {
            colorPicker.close()
        } else {
            colorPicker.open()
        }
    }

    private fun deleteNote() {
        alert {
            messageResource = R.string.delete_dialog_message
            negativeButton(R.string.cancel_btn_title) { dialog ->  dialog.dismiss() }
            positiveButton(R.string.ok_bth_title) { viewModel.deleteNOte() }
        }.show()
    }

    private fun setEditListener() {
        titleEt.addTextChangedListener(textChangeListener)
        bodyEt.addTextChangedListener(textChangeListener)
    }

    private fun removeEditListener() {
        titleEt.removeTextChangedListener(textChangeListener)
        bodyEt.removeTextChangedListener(textChangeListener)
    }

    private fun triggerSaveNote() {
        if (titleEt.text.length < 3 && bodyEt.text.length < 3) return

        Handler().postDelayed({
            note = note?.copy(title = titleEt.text.toString(),
                    body = bodyEt.text.toString(),
                    lastChanged = Date(),
                    color = color)
                    ?: createNewNote()

            note?.let { viewModel.saveChanges(it) }
        }, SAVE_DELAY)
    }
    private fun setToolbarColor(color: Color) {
        toolbar.setBackgroundColor(color.getColorInt(this))
    }

    override fun onBackPressed() {
        if (colorPicker.isOpen) {
            colorPicker.close()
            return
        }
        super.onBackPressed()
    }




}

