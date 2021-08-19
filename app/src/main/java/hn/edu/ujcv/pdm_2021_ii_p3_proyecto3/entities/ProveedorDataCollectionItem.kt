package hn.edu.ujcv.pdm_2021_ii_p3_proyecto3.entities

class ProveedorDataCollection: ArrayList<ProveedorDataCollectionItem>()


data class ProveedorDataCollectionItem(
    val proveedorId: Long,
    val nombreCompania: String,
    val nombreContacto: String,
    val numero: Int,
    val correo: String,
    val pais:String,
    val direccion: String
)