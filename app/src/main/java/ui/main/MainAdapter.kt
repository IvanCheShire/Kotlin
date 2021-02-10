package ui.main

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import data.model.Color
import data.model.Note
import ru.geekbrains.kotlin.R
import ru.geekbrains.kotlin.R.*

class MainAdapter(private val onItemClickListener: OnItemClickListener) {

    var notes: List<Note> = listOf()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(layout.item_note, parent, false)
        return NoteViewHolder(view)
    }

    override fun getItemCount() = notes.size

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int): Unit {
        holder.bind(notes[position])
    }

inner class NoteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val title = itemView.findViewById<TextView>(id.title)
    private val body = itemView.findViewById<TextView>(id.body)

    fun bind(note: Note) {
        title.text = note.title
        body.text = note.note

        val color = when (note.color) {
            Color.WHITE -> R.color.color_white
            Color.VIOLET -> R.color.color_violet
            Color.YELLOW -> R.color.color_yello
            Color.RED -> R.color.color_red
            Color.PINK -> R.color.color_pink
            Color.GREEN -> R.color.color_green
            Color.BLUE -> R.color.color_blue
        }

        itemView.setBackgroundColor(itemView.context.resources.getColor(color))
        itemView.setOnClickListener { onItemClickListener.onItemClick(note) }


    }
}
}
interface OnItemClickListener {
    fun onItemClick(note: Note)
}
