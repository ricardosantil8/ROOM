package ipvc.estg.room.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import ipvc.estg.room.db.AlunoDB
import ipvc.estg.room.db.AlunoRepository
import ipvc.estg.room.entities.Aluno
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AlunoViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: AlunoRepository
    // Using LiveData and caching what getAlphabetizedWords returns has several benefits:
    // - We can put an observer on the data (instead of polling for changes) and only update the
    //   the UI when the data actually changes.
    // - Repository is completely separated from the UI through the ViewModel.
    val allStudents: LiveData<List<Aluno>>

    init {
        val alunoDao = AlunoDB.getDatabase(application, viewModelScope).AlunoDao()
        repository = AlunoRepository(alunoDao)
        allStudents = repository.allStudents
    }

    /**
     * Launching a new coroutine to insert the data in a non-blocking way
     **/

    fun insert(aluno: Aluno) = viewModelScope.launch(Dispatchers.IO) {
        repository.insert(aluno)
    }

    fun deleteAll() = viewModelScope.launch(Dispatchers.IO) {
        repository.deleteAll()
    }

    fun deleteByAluno(aluno: String) = viewModelScope.launch(Dispatchers.IO) {
        repository.deleteByAluno(aluno)
    }

    fun getAlunobyEscola(escola: String): LiveData<List<Aluno>> {
        return repository.getAlunobyEscola(escola)
    }

    fun getEscolabyAluno(aluno: String): LiveData<Aluno> {
        return repository.getEscolabyAluno(aluno)
    }

    fun updateAluno(aluno: Aluno) = viewModelScope.launch {
        repository.updateAluno(aluno)
    }

    fun updateEscolafromAluno(aluno: String, escola: String) = viewModelScope.launch {
        repository.updateEscolafromAluno(aluno, escola)
    }
}

