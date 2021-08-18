package hn.edu.ujcv.pdm_2021_ii_p3_proyecto3.entities

class ProductoDataCollection:ArrayList<ProductoDataCollectionItem>()

data class ProductoDataCollectionItem(
    val productoId: Int,
    val fabricaId: Int,
    val nombreProducto: String,
    val descripcion: String,
    val precio: Double,
    val unidadesEnAlmacen: Int,
    val unidadesMaximas: Int,
    val unidadesMinimas: Int
)