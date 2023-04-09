package com.example.janelapopup

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.util.*

class MyAdapter(val lista: MutableList<String>): RecyclerView.Adapter<MyAdapter.MyHolder>() {
    var onItemClickRecyclerView: OnItemClickRecyclerView? = null
    var onItemLongClickRecyclerView: OnItemLongClickRecyclerView? = null
    var onItemClick: OnItemClickRecyclerView? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyAdapter.MyHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_list, parent, false)
        return MyHolder(view)
    }

    override fun onBindViewHolder(holder: MyAdapter.MyHolder, position: Int) {
        val nome = this.lista.get(position)
        holder.tvNome.text = nome

    }

    override fun getItemCount(): Int {
        return this.lista.size
    }

    fun add(nome: String){
        this.lista.add(nome)
        this.notifyItemInserted(this.lista.size)
    }

    fun delete(position: Int) {
        Log.d("APP_LOG", "Deletar Objeto na Tela MyAdapter")
        this.lista.removeAt(position)
        notifyInteRemoved(position)
        notifyItemRangeChanged(position, this.lista.size)
    }

    fun move(from: Int, to: Int) {
        Log.d("APP_LOG", "Mover Objeto na Tela MyAdapter")
        Collections.swap(this.lista, from, to)
        notifyItemMoved(from, to)
    }

    private fun notifyInteRemoved(position: Int) {
        // Not yet codified
    }

    inner class MyHolder (itemView: View): RecyclerView.ViewHolder(itemView){
        var tvNome: TextView

        init {
            this.tvNome = itemView.findViewById(R.id.tvItemNome)

            itemView.setOnClickListener {
                this@MyAdapter.onItemClickRecyclerView?.onItemClick(this.adapterPosition)
                this@MyAdapter.onItemClick?.onItemClick(this.adapterPosition)
            }
            itemView.setOnLongClickListener {
                this@MyAdapter.onItemLongClickRecyclerView?.onItemLongClick(this.adapterPosition)
                true
            }

        }
    }
}