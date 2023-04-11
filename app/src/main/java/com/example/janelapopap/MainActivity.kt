package com.example.janelapopap

import android.content.DialogInterface
import android.content.DialogInterface.OnClickListener
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.util.Log
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.example.janelapopap.OnItemLongClickRecyclerView as OnItemLongClickRecyclerView1

class MainActivity : AppCompatActivity() {
    private lateinit var rvNomes: RecyclerView
    private lateinit var fbAdd: FloatingActionButton
    private var lista = mutableListOf<String>()
    private lateinit var etNome: EditText
    private lateinit var ttsTexto: TextToSpeech
    private var posicao: Int = 0

    init {
        this.lista.add("Primeiro")
        this.lista.add("Segundo")
        this.lista.add("Terceiro")
//        this.lista.add("Quarto")
//        this.lista.add("Quinto")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        this.rvNomes = findViewById(R.id.rvNomes)
        this.fbAdd = findViewById(R.id.fbAdd)

        // Chamar a Janela Popup salvar objeto
        this.fbAdd.setOnClickListener {addTexto()}
        this.rvNomes.adapter = MyAdapter(this.lista)

       (this.rvNomes.adapter as MyAdapter).onItemClickRecyclerView = OnItemClick()
        this.ttsTexto = TextToSpeech(this, null)

        // Chamar a Janela Popupo com o click Longo
        (this.rvNomes.adapter as MyAdapter).onItemLongClickRecyclerView = LongClickList()

        // Config movimentação objeto na Tela
        ItemTouchHelper(OnSwipe()).attachToRecyclerView(this.rvNomes)
    }

    // Click do botão flutuante da Tela chama o addTexto()
    fun addTexto(){
        this.etNome = EditText(this)
        val builder = AlertDialog.Builder(this).apply {
            setTitle("Novo Nome!")
            setMessage("Digite aqui o nome")
            setView(this@MainActivity.etNome)
            setPositiveButton("Salvar", OnClick())
            setNegativeButton("Cancelar", null)
        }
        builder.create().show()
    }

    // Click do botão Salvar da Janela Popup
    inner class OnClick: OnClickListener{
        override fun onClick(dialog: DialogInterface?, which: Int) {
            val nome = this@MainActivity.etNome.text.toString()
            // Acessar a fun ad() no MyAdapter
            (this@MainActivity.rvNomes.adapter as MyAdapter).add(nome)
            Log.d("APP_LOG", "Lista Atualizada: " + this@MainActivity.lista)
        }
    }

    // Click curto no objeto da Tela
    inner class OnItemClick: OnItemClickRecyclerView {
        override fun onItemClick(position: Int){
            val nome = this@MainActivity.lista.get(position)
            //Toast.makeText(this@MainActivity, nome, Toast.LENGTH_SHORT).show()
            Log.d("APP_LOG", "Click Curto: "+nome)
            this@MainActivity.ttsTexto.speak(nome, TextToSpeech.QUEUE_FLUSH, null, null)
        }
    }

    // Click longo no objeto da Tela chama a Janela Popup Delete
    inner class LongClickList: OnItemLongClickRecyclerView1 {
        override fun onItemLongClick(position: Int): Boolean {
            //Log.d("APP_LOG", "Click Longo Delete")
            posicao = position
            deleteTexto(this@MainActivity.lista.get(position))
            Log.d("APP_LOG", "Lista Atualizada: " + this@MainActivity.lista)
            //Toast.makeText(this@MainActivity, st, Toast.LENGTH_SHORT).show()
            return true
        }
    }

    // Click longo na imagem do objeto chama a Janela Popup
    fun deleteTexto(st: String): Boolean {
        Log.d("APP_LOG", "Click Longo Delete: "+st)
        val builder = AlertDialog.Builder(this).apply {
            setTitle("                      ATENÇÃO")
            setMessage("             Deseja deletar "+st+" !!")
            setPositiveButton("Deletar", OnClickDelete())
            setNegativeButton("Cancelar", null)
        }
        builder.create().show()
        (this.rvNomes.adapter as MyAdapter).notifyDataSetChanged()
        return true
    }

    // Click no Delete do objeto da Tela
    inner class OnClickDelete: OnClickListener{
        override fun onClick(dialog: DialogInterface?, which: Int) {
            (this@MainActivity.rvNomes.adapter as MyAdapter).delete(posicao)
            Log.d("APP_LOG", "Lista Atualizada: " + this@MainActivity.lista)
            (this@MainActivity.rvNomes.adapter as MyAdapter).notifyDataSetChanged()
        }
    }

    // Click para mover objeto na Tela
    inner class OnSwipe: ItemTouchHelper.SimpleCallback(
        ItemTouchHelper.DOWN or ItemTouchHelper.UP,
        ItemTouchHelper.START or ItemTouchHelper.END) {

        // Mover objeto na tela em todas as direções
        override fun onMove(
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder,
            target: RecyclerView.ViewHolder): Boolean {
            Log.d("APP_LOG", "Mover Objeto na Tela onMove")
            //Acessar fun move() do MyAdapter
            (this@MainActivity.rvNomes.adapter as MyAdapter).move(
                viewHolder.adapterPosition, target.adapterPosition)
            Log.d("APP_LOG", "Lista Atualizada: " + this@MainActivity.lista)
            return true
        }

        //Deletar objeto da Tela ao deslizar para a direita ou esquerda
        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
            deleteTexto(this@MainActivity.lista.get(posicao))
            Log.d("APP_LOG", "Lista Atualizada: " + this@MainActivity.lista)
        }
    }
}