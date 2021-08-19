package hn.edu.ujcv.pdm_2021_ii_p3_proyecto3.entities

class MaquinariaDataCollection:ArrayList<MaquinariaDataCollectionItem>()

data class MaquinariaDataCollectionItem(
    val maquinaId   : Int?,
    val fabricaId   : Int,
    val marca       : String,
    val horasUso    : Double,
    val tipoMaquina : String
)
