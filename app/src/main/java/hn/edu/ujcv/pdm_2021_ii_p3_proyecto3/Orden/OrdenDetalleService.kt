package hn.edu.ujcv.pdm_2021_ii_p3_proyecto3.Orden

import hn.edu.ujcv.pdm_2021_ii_p3_proyecto3.entities.OrdenDetalleDataCollectionItem
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*

interface OrdenDetalleService {
    @GET("OrdenDetalle")
    fun listOrdenesDetalle(): Call<List<OrdenDetalleDataCollectionItem>>

    @GET("OrdenDetalle/id/{id}")
    fun getOrdenDetalleById(@Path("id") id: Long): Call<OrdenDetalleDataCollectionItem>
    @Headers("Content-Type: application/json")

    @POST("OrdenDetalle/addOrdenDetalle")
    fun addOrdenDetalle(@Body ordenDetalleData: OrdenDetalleDataCollectionItem): Call<OrdenDetalleDataCollectionItem>
    @Headers("Content-Type: application/json")

    @PUT("OrdenDetalle")
    fun updateOrdenDetalle(@Body ordenDetalleData: OrdenDetalleDataCollectionItem): Call<OrdenDetalleDataCollectionItem>

    @DELETE("OrdenDetalle/delete/{id}")
    fun deleteOrdenDetalle(@Path("id") id: Long): Call<ResponseBody>
}