package hn.edu.ujcv.pdm_2021_ii_p3_proyecto3.entities

class AlmacenDataCollection: ArrayList<AlmacenDataCollectionItem>()

data class AlmacenDataCollectionItem(
    val almacenId: Int?,
    val telefono:  Int ,
    val direccion: String,
    val encargado: String
)