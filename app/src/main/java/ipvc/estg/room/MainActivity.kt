package ipvc.estg.room

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle

import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import ipvc.estg.room.adapter.AlunoAdapter
import ipvc.estg.room.api.EndPoints
import ipvc.estg.room.api.ServiceBuilder
import ipvc.estg.room.api.User
import ipvc.estg.room.entities.Aluno
import ipvc.estg.room.viewModel.AddAluno
import ipvc.estg.room.viewModel.AlunoViewModel

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

import java.nio.file.Files.delete

import androidx.lifecycle.ViewModelProvider as ViewModelProvider1


class MainActivity : AppCompatActivity(), AlunoAdapter.ItemClicked {

    private lateinit var alunoViewModel: AlunoViewModel
    private val newWordActivityRequestCode = 1
    private val editAlunoRequestCode = 2


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // recycler view
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerview)
        val adapter = AlunoAdapter(this)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)


        // view model
        alunoViewModel = AlunoViewModel(this.application)

        // alunoViewModel = ViewModelProvider1(this).get(AlunoViewModel::class.java)
        alunoViewModel.allStudents.observe(this, Observer { alunos ->
            // Update the cached copy of the words in the adapter.
            alunos?.let { adapter.setAlunos(it) }
        })

        //Clique no botão adicionar aluno
        val fab = findViewById<FloatingActionButton>(R.id.fab)
        fab.setOnClickListener {
            val intent = Intent(this@MainActivity, AddAluno::class.java)
            this.startActivityForResult(intent, newWordActivityRequestCode)

        }

        //Swipe da recycler view para apagar o aluno DELETE
        val helper = ItemTouchHelper(
                object : ItemTouchHelper.SimpleCallback(0,
                        ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
                    override fun onMove(recyclerView: RecyclerView,
                                        viewHolder: RecyclerView.ViewHolder,
                                        target: RecyclerView.ViewHolder): Boolean {
                        return false
                    }

                    override fun onSwiped(viewHolder: RecyclerView.ViewHolder,
                                          direction: Int) {
                        val position = viewHolder.adapterPosition
                        val myAluno: Aluno = adapter.getWordAtPosition(position)
                        Toast.makeText(this@MainActivity, "Deleting " +
                                myAluno.aluno, Toast.LENGTH_LONG).show()

                        // Delete the word
                        alunoViewModel.delete(myAluno)
                    }
                })

        helper.attachToRecyclerView(recyclerView)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)


        val recyclerView = findViewById<RecyclerView>(R.id.recyclerview)
        val adapter = AlunoAdapter(this)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter.notifyDataSetChanged()

        // quando o botão de adicionar aluno é clicado
        if (requestCode == newWordActivityRequestCode && resultCode == Activity.RESULT_OK) {
            val paluno = data?.getStringExtra(AddAluno.EXTRA_REPLY_STUDENT)
            val pescola = data?.getStringExtra(AddAluno.EXTRA_REPLY_ESCOLA)

            if (paluno != null && pescola != null) {
                val aluno = Aluno(aluno = paluno, escola = pescola)
                alunoViewModel.insert(aluno)
                
            }
        // quando é clicado na recycler view para fazer o update
        }
        else if (requestCode == editAlunoRequestCode && resultCode == Activity.RESULT_OK) {
            val pid = data?.getIntExtra(AddAluno.EXTRA_REPLY_ID, -10)
            val paluno = data?.getStringExtra(AddAluno.EXTRA_REPLY_STUDENT)
            val pescola = data?.getStringExtra(AddAluno.EXTRA_REPLY_ESCOLA)


            if (pid != -10 && paluno != null && pescola != null) {
                val aluno = Aluno(id = pid, aluno = paluno, escola = pescola)
                alunoViewModel.updateAluno(aluno)
            }
        }
        else {
            Toast.makeText(
                    applicationContext,
                    "Empty aluno",
                    Toast.LENGTH_SHORT).show()

        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.menu, menu)
        return true
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle item selection
        return when (item.itemId) {

            R.id.apagartudo -> {
                alunoViewModel.deleteAll()
                true
            }

            R.id.alunosESTG -> {

                // recycler view
                val recyclerView = findViewById<RecyclerView>(R.id.recyclerview)
                val adapter = AlunoAdapter(this)
                recyclerView.adapter = adapter
                recyclerView.layoutManager = LinearLayoutManager(this)

                // view model
                alunoViewModel = ViewModelProvider1(this).get(AlunoViewModel::class.java)
                alunoViewModel.getAlunobyEscola("ESTG").observe(this, Observer { alunos ->
                    // Update the cached copy of the words in the adapter.
                    alunos?.let { adapter.setAlunos(it) }
                })

                true
            }

            R.id.todosAlunos -> {

                // recycler view
                val recyclerView = findViewById<RecyclerView>(R.id.recyclerview)
                val adapter = AlunoAdapter(this)
                recyclerView.adapter = adapter
                recyclerView.layoutManager = LinearLayoutManager(this)

                // view model
                alunoViewModel = ViewModelProvider1(this).get(AlunoViewModel::class.java)
                alunoViewModel.allStudents.observe(this, Observer { alunos ->
                    // Update the cached copy of the words in the adapter.

                    alunos?.let {
                        val alunose: List<Aluno> = it
                        adapter.setAlunos(alunose)

                    }
                })


                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }


    // quando for clicado a recycler view para mandar os valores para a view da recycler para update
    override fun onClickListener(aluno: Aluno) {
        Toast.makeText(applicationContext, "Editar Aluno", Toast.LENGTH_SHORT).show()

        val intent = Intent(this, AddAluno::class.java)
        intent.putExtra("Aluno", aluno.aluno)
        intent.putExtra("Escola", aluno.escola)
        intent.putExtra("id", aluno.id)
        this.startActivityForResult(intent, editAlunoRequestCode)

        // this.finish()
    }






}