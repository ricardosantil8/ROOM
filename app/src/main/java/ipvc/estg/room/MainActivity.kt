package ipvc.estg.room

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import ipvc.estg.room.adapter.AlunoAdapter
import ipvc.estg.room.entities.Aluno
import ipvc.estg.room.viewModel.AddAluno
import ipvc.estg.room.viewModel.AlunoViewModel
import androidx.lifecycle.ViewModelProvider as ViewModelProvider1

class MainActivity : AppCompatActivity() {

    private lateinit var alunoViewModel: AlunoViewModel
    private val newWordActivityRequestCode = 1

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

        //Fab
        val fab = findViewById<FloatingActionButton>(R.id.fab)
        fab.setOnClickListener {
            val intent = Intent(this@MainActivity, AddAluno::class.java)
            this.startActivityForResult(intent, newWordActivityRequestCode)
            
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == newWordActivityRequestCode && resultCode == Activity.RESULT_OK) {
            val paluno = data?.getStringExtra(AddAluno.EXTRA_REPLY_STUDENT)
            val pescola = data?.getStringExtra(AddAluno.EXTRA_REPLY_ESCOLA)

            if (paluno!= null && pescola != null) {
                val aluno = Aluno(aluno = paluno, escola = pescola)
                alunoViewModel.insert(aluno)
            }

        } else {
            Toast.makeText(
                applicationContext,
                R.string.empty_not_saved,
                Toast.LENGTH_LONG).show()
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
                    alunos?.let { adapter.setAlunos(it) }
                })


                true
            }
+
            R.id.getEscolafromRicardo -> {
                alunoViewModel = ViewModelProvider1(this).get(AlunoViewModel::class.java)
                alunoViewModel.getEscolabyAluno("Ricardo").observe(this, Observer { aluno ->
                    Toast.makeText(this, aluno.escola, Toast.LENGTH_SHORT).show()
                })
                true
            }

            R.id.apagarRicardo -> {
                alunoViewModel.deleteByAluno("Ricardo")
                true
            }

            R.id.alterar -> {
                val aluno = Aluno(id = 1, aluno = "xxx", escola = "xxx")
                alunoViewModel.updateAluno(aluno)
                true
            }

            R.id.alterarRicardo -> {
                alunoViewModel.updateEscolafromAluno("Ricardo", "ESE")
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

}