package hn.edu.ujcv.pdm_2021_ii_p3_proyecto3.Empleado

import hn.edu.ujcv.pdm_2021_ii_p3_proyecto3.entities.EmpleadoDataCollectionItem
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*

interface EmpleadoService {
    @GET("empleado")
    fun listEmpleado(): Call<List<EmpleadoDataCollectionItem>>
    @GET("empleado/id/{id}")
    fun getEmpleadoById(@Path("id") id: Int): Call<EmpleadoDataCollectionItem>
    @GET("empleado/nombre/{nombre}")
    fun getEmpleadoByNombre(@Path("id") id: Int): Call<EmpleadoDataCollectionItem>
    @Headers("Content-Type: application/json")
    @POST("empleado/addEmpleado")
    fun addEmpleado(@Body personData: EmpleadoDataCollectionItem): Call<EmpleadoDataCollectionItem>
    @Headers("Content-Type: application/json")
    @PUT("empleado")
    fun updateEmpleado(@Body personData: EmpleadoDataCollectionItem): Call<EmpleadoDataCollectionItem>
    @DELETE("empleado/delete/{id}")
    fun deleteEmpleado(@Path("id") id: Int): Call<ResponseBody>
}