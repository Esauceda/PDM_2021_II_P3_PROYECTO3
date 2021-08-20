package hn.edu.ujcv.pdm_2021_ii_p3_proyecto3.Compra

import hn.edu.ujcv.pdm_2021_ii_p3_proyecto3.entities.CompraEncabezadoDataCollectionItem
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*

interface CompraEncabezadoService {

    @GET("compraEncabezado")
    fun listCompraEncabezado(): Call<List<CompraEncabezadoDataCollectionItem>>

    @GET("compraEncabezado/id/{id}")
    fun getCompraEncabezadoById(@Path("id")id: Long): Call<CompraEncabezadoDataCollectionItem>

    @Headers("Content-Type: application/json")
    @POST("compraEncabezado/addcompraEncabezado")
    fun addCompraEncabezado(@Body compraEncabezadoData: CompraEncabezadoDataCollectionItem): Call<CompraEncabezadoDataCollectionItem>

    @Headers("Content-Type: application/json")
    @PUT("compraEncabezado")
    fun updateCompraEncabezado(@Body compraEncabezadoData: CompraEncabezadoDataCollectionItem): Call<CompraEncabezadoDataCollectionItem>

    @DELETE("compraEncabezado/delete/{id}")
    fun deleteCompraEncabezado(@Path("id")id: Long): Call<ResponseBody>

}