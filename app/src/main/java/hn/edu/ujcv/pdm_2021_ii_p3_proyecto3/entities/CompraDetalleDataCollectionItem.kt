package hn.edu.ujcv.pdm_2021_ii_p3_proyecto3.entities


class CompraDetalleDataCollection: ArrayList<CompraDetalleDataCollectionItem>()

data class CompraDetalleDataCollectionItem(
    val compraId: Long?,
    val producto: String,
    val cantidad: Int,
    val precio: Double
)