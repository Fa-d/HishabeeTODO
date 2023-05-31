package com.fahad.hishabeetodo.ui

import android.graphics.Paint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.widget.TextViewCompat
import androidx.recyclerview.widget.RecyclerView
import com.fahad.hishabeetodo.R
import com.fahad.hishabeetodo.databinding.ItemViewNoteBinding
import com.fahad.hishabeetodo.db.TodoEntity

class NoteItemAdapter : RecyclerView.Adapter<NoteItemAdapter.ViewHolder>() {

    private var noteList: MutableList<TodoEntity> = mutableListOf()
    var mClickListener: ((todoEntity: TodoEntity) -> Unit)? = null


    inner class ViewHolder(var binding: ItemViewNoteBinding) :
        RecyclerView.ViewHolder(binding.root) {
        init {
            binding.root.setOnClickListener {
                mClickListener?.invoke(noteList[adapterPosition])
            }
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemViewNoteBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.theChekBox.isChecked = noteList[position].is_checked
        if (noteList[position].is_checked) {
            holder.binding.theText.text = noteList[position].note_txt
            holder.binding.theText.paintFlags =
                holder.binding.theText.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
            TextViewCompat.setTextAppearance(holder.binding.theText, R.style.dimTextStyle)
        } else {
            holder.binding.theText.text = noteList[position].note_txt
            holder.binding.theText.paintFlags =
                holder.binding.theText.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
            TextViewCompat.setTextAppearance(holder.binding.theText, R.style.regularTextStyle)
        }

    }

    override fun getItemCount(): Int {
        return noteList.size
    }

    fun initData(passedData: List<TodoEntity>) {
        noteList.clear()
        noteList.addAll(passedData)
        notifyDataSetChanged()
    }

    fun updateData(pos: Int) {
        noteList.removeAt(pos)
        notifyDataSetChanged()
    }

}