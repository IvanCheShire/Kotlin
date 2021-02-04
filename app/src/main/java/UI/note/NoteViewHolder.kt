package UI.note

import UI.main.extentions.getColorInt
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import data.model.Note


inner class NoteViewHolder(override val containerView: View) :
        RecyclerView.ViewHolder(containerView), LayoutContainer {

    fun bind(note: Note) {
        title.text = note.title
        body.text = note.body

        itemView.setBackgroundColor(note.color.getColorInt(itemView.context))
        itemView.setOnClickListener { onItemClickListener(note) }

    }
}
