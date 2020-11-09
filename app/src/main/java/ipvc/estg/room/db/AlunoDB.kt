package ipvc.estg.room.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import ipvc.estg.room.dao.AlunoDao
import ipvc.estg.room.entities.Aluno
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

// Annotates class to be a Room Database with a table (entity) of the City class

// Note: When you modify the database schema, you'll need to update the version number and define a migration strategy
//For a sample, a destroy and re-create strategy can be sufficient. But, for a real app, you must implement a migration strategy.

@Database(entities = arrayOf(Aluno::class), version = 9, exportSchema = false)
public abstract class AlunoDB : RoomDatabase() {

    abstract fun AlunoDao(): AlunoDao

    private class WordDatabaseCallback(
        private val scope: CoroutineScope
    ) : RoomDatabase.Callback() {

        override fun onOpen(db: SupportSQLiteDatabase) {
            super.onOpen(db)
            INSTANCE?.let { database ->
                scope.launch {
                    var AlunoDao = database.AlunoDao()

                    // Delete all content here.
                    //AlunoDao.deleteAll()
/*
                    var aluno = Aluno(1, "João", "ESCE")
                    AlunoDao.insert(aluno)
                    aluno = Aluno(2, "Ricardo", "ESTG")
                    AlunoDao.insert(aluno)
                    aluno = Aluno(3, "Ana", "ESE")
                    AlunoDao.insert(aluno)
*/
                }
            }
        }
    }

    companion object {
        // Singleton prevents multiple instances of database opening at the
        // same time.
        @Volatile
        private var INSTANCE: AlunoDB? = null

        fun getDatabase(context: Context, scope: CoroutineScope): AlunoDB {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AlunoDB::class.java,
                    "aluno_database",
                )
                    //estratégia de destruição
                    //.fallbackToDestructiveMigration()
                    .addCallback(WordDatabaseCallback(scope))
                    .build()

                INSTANCE = instance
                return instance
            }
        }
    }
}