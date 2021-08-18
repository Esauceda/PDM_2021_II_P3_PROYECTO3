package hn.edu.ujcv.pdm_2021_ii_p3_proyecto3.entities

class DeliveryDataCollection: ArrayList<DeliveryDataCollecionItem>()

data class DeliveryDataCollecionItem(
    val deliveryId: Int,
    val ordenId: Int,
    val nombreCompania: String,
    val numero: Int,
    val correo: String,
    val fechaEntrega: String
)