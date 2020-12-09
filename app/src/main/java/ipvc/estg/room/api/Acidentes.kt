package ipvc.estg.room.api

data class Acidentes(
    val acidente_id: String,
    val nomeutilizador: String,
    val utilizador_id: Int,
    val lat: String,
    val long: String,
    val descricao: String,
    val error: Boolean,
    val tipo_id: Int
)

