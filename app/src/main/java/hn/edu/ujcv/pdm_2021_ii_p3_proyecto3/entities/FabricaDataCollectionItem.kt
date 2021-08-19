package hn.edu.ujcv.pdm_2021_ii_p3_proyecto3.entities

class FabricaDataCollection:ArrayList<FabricaDataCollectionItem>()
data class FabricaDataCollectionItem(
    val fabricaId: Long?,
    val nombreFabrica: String,
    val telefono: Long,
    val tipoProduccion: String,
    val direccion: String,
    val encargado: String
)