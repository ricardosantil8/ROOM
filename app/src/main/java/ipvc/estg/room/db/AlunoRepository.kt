package ipvc.estg.room.db

import androidx.lifecycle.LiveData
import ipvc.estg.room.dao.AlunoDao
import ipvc.estg.room.entities.Aluno



// Declares the DAO as a private property in the constructor. Pass in the DAO
// instead of the whole database, because you only need access to the DAO
class AlunoRepository(private val alunoDao: AlunoDao) {

    // Room executes all queries on a separate thread.
    // Observed LiveData will notify the observer when the data has changed.
    val allStudents: LiveData<List<Aluno>> = alunoDao.getAllStudents()

    suspend fun insert(aluno: Aluno) {
        alunoDao.insert(aluno)
    }

    suspend fun deleteAll(){
        alunoDao.deleteAll()
    }

    fun getAlunobyEscola(escola: String): LiveData<List<Aluno>> {
        return alunoDao.getAlunobyEscola(escola)
    }

    fun getEscolabyAluno(aluno: String): LiveData<Aluno> {
        return alunoDao.getEscolabyAluno(aluno)
    }

    suspend fun deleteByAluno(aluno: String){
        alunoDao.deleteByAluno(aluno)
    }

    suspend fun updateAluno(aluno: Aluno) {
        alunoDao.updateAluno(aluno)
    }

    suspend fun updateEscolafromAluno(aluno: String, escola: String){
        alunoDao.updateEscolafromAluno(aluno, escola)
    }


}