package hn.edu.ujcv.pdm_2021_ii_p3_proyecto3.Orden

import hn.edu.ujcv.pdm_2021_ii_p3_proyecto3.entities.OrdenEncabezadoDataCollectionItem
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*

interface OrdenEncabezadoService {
    @GET("OrdenEncabezado")
    fun listOrdenesEncabezado(): Call<List<OrdenEncabezadoDataCollectionItem>>

    @GET("OrdenEncabezado/id/{id}")
    fun getOrdenEncabezadoById(@Path("id") id: Long): Call<OrdenEncabezadoDataCollectionItem>
    @Headers("Content-Type: application/json")

    @POST("OrdenEncabezado/addOrdenEncabezado")
    fun addOrdenEncabezado(@Body ordenEncabezadoData: OrdenEncabezadoDataCollectionItem): Call<OrdenEncabezadoDataCollectionItem>
    @Headers("Content-Type: application/json")

    @PUT("OrdenEncabezado")
    fun updateOrdenEncabezado(@Body ordenEncabezadoData: OrdenEncabezadoDataCollectionItem): Call<OrdenEncabezadoDataCollectionItem>

    @DELETE("OrdenEncabezado/delete/{id}")
    fun deleteOrdenEncabezado(@Path("id") id: Long): Call<ResponseBody>
}