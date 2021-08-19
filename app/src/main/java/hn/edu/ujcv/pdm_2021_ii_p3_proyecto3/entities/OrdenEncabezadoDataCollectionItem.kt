package hn.edu.ujcv.pdm_2021_ii_p3_proyecto3.entities

class OrndeEncabezadoDataColeection: ArrayList<OrdenEncabezadoDataCollectionItem>()

data class OrdenEncabezadoDataCollectionItem(
    val ordenId         : Int?,
    val empleadoId      : Int,
    val clienteId       : Int,
    val fechaOrden      : String,
    val fechaEnvio      : String,
    val direccionEnvio  : String,
    val estado          : String,
    val total           : Double
)
