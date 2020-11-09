package ipvc.estg.room.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import ipvc.estg.room.entities.Aluno


@Dao
interface AlunoDao {

    @Query("SELECT * from aluno_table ORDER BY aluno ASC")
    fun getAllStudents(): LiveData<List<Aluno>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(aluno: Aluno)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateAluno(aluno: Aluno)

    @Query("DELETE FROM aluno_table")
    suspend fun deleteAll()

    @Query("SELECT * FROM aluno_table WHERE escola == :escola")
    fun getAlunobyEscola(escola: String): LiveData<List<Aluno>>

    @Query("SELECT * FROM aluno_table WHERE aluno == :aluno")
    fun getEscolabyAluno(aluno: String): LiveData<Aluno>



    @Query("UPDATE aluno_table SET escola=:escola WHERE aluno == :aluno")
    suspend fun updateEscolafromAluno(aluno: String, escola: String)

    @Delete
    suspend fun delete(aluno: Aluno)
}