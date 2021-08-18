package hn.edu.ujcv.pdm_2021_ii_p3_proyecto3.entities

class EmpleadoDataCollection: ArrayList<EmpleadoDataCollectionItem>()

data class EmpleadoDataCollectionItem(
    val empleadoId: Int,
    val nombre: String,
    val direccion: String,
    val telefono: Int,
    val salario: Double,
    val puesto: String,
    val fechaNacimiento: String,
    val fechaContratacion: String,
    val contrasena: String
)