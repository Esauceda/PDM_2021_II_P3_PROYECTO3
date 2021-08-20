package hn.edu.ujcv.pdm_2021_ii_p3_proyecto3.entities

class CompraEncabezadoDataCollection: ArrayList<CompraEncabezadoDataCollectionItem>()

data class CompraEncabezadoDataCollectionItem(
    val compraId: Long?,
    val proveedorId:Long,
    val empleadoId: Long,
    val fechaCompra: String,
    val total: Double,
    val estado: String,
    val fechaRecepcion: String
)
