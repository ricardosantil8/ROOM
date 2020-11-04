package ipvc.estg.room.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "aluno_table")

class Aluno(

    @PrimaryKey(autoGenerate = true) val id: Int? = null,
    @ColumnInfo(name = "aluno") val aluno: String,
    @ColumnInfo(name = "escola") val escola: String
)

