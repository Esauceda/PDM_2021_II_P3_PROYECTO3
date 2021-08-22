package hn.edu.ujcv.pdm_2021_ii_p3_proyecto3.entities

class OrdenDetalleDataCollection : ArrayList<OrdenDetalleDataCollectionItem>()

data class OrdenDetalleDataCollectionItem (
    val ordenDetalleId: Int?,
    val ordenId       : Int?,
    val almacenId     : Int,
    val productoId    : Int,
    val cantidad      : Int,
    val precio        : Double
    )