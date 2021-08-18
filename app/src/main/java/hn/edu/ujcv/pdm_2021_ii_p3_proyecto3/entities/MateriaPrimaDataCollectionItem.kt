package hn.edu.ujcv.pdm_2021_ii_p3_proyecto3.entities

class MateriaPrimaDataCollection: ArrayList<MateriaPrimaDataCollectionItem>()

data class MateriaPrimaDataCollectionItem(
    val materiaprimaId: Int,
    val nombreMateria: String,
    val proveedorId: Int,
    val almacenId: Int,
    val descripcion: String,
    val cantidad: Int
)