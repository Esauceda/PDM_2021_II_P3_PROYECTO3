package hn.edu.ujcv.pdm_2021_ii_p3_proyecto3.entities

class ClienteDataCollection: ArrayList<ClienteDataCollectionItem>()

data class ClienteDataCollectionItem(
    val clienteId: Int?,
    val nombreCompania: String,
    val nombre: String,
    val telefono: Int,
    val correo: String,
    val pais: String,
    val direccion: String,
    val categoria: String
)
