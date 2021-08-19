package hn.edu.ujcv.pdm_2021_ii_p3_proyecto3.entities

class FacturaDataCollection: ArrayList<FacturaDataCollectionItem>()

data class FacturaDataCollectionItem(
    val facturaId   : Int?,
    val ordenId     : Int,
    val fechaFactura: String,
    val total       : Double

)
