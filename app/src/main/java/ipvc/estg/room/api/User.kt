package ipvc.estg.room.api

data class User(
        val nomeutilizador: String,
        val passutilizador: String,
        val address: Address
)

data class Address(
        val street: String,
        val city: String,
        val geo: Geo
)

data class Geo(
        val lat: String,
        val lng: String
)

