package ui.main

import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import ru.geekbrains.kotlin.R


class LogoutDialog : DialogFragment() {
    companion object {
        val TAG = LogoutDialog::class.java.name + "TAG"
        fun createInstance() = LogoutDialog()
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog =
            AlertDialog.Builder(context!!)
                    .setTitle(R.string.logout_dialog_title)
                    .setMessage(R.string.logout_dialog_message)
                    .setPositiveButton(R.string.ok_bth_title) { _, _ ->  (activity as LogoutListener).onLogout() }
                    .setNegativeButton(R.string.logout_dialog_cancel) {_, _ -> dismiss() }
                    .create()

    interface LogoutListener {
        fun onLogout()
    }
}
