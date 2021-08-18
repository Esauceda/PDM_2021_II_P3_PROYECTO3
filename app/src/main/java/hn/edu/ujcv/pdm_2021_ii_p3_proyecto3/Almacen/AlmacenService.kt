package hn.edu.ujcv.pdm_2021_ii_p3_proyecto3.Almacen

import hn.edu.ujcv.pdm_2021_ii_p3_proyecto3.entities.AlmacenDataCollectionItem
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*

interface AlmacenService {
    @GET("almacen")
    fun listAlmacen(): Call<List<AlmacenDataCollectionItem>>
    @GET("almacen/id/{id}")
    fun getAlmacenById(@Path("id") id: Int): Call<AlmacenDataCollectionItem>
    @GET("almacen/encargado/{encargado}")
    fun getAlmacenByEncargado(@Path("id") id: Int): Call<AlmacenDataCollectionItem>
    @Headers("Content-Type: application/json")
    @POST("almacen/addAlmacen")
    fun addAlmacen(@Body personData: AlmacenDataCollectionItem): Call<AlmacenDataCollectionItem>
    @Headers("Content-Type: application/json")
    @PUT("almacen")
    fun updateAlmacen(@Body personData: AlmacenDataCollectionItem): Call<AlmacenDataCollectionItem>
    @DELETE("almacen/delete/{id}")
    fun deleteAlmacen(@Path("id") id: Int): Call<ResponseBody>
}