package hn.edu.ujcv.pdm_2021_ii_p3_proyecto3.Factura

import hn.edu.ujcv.pdm_2021_ii_p3_proyecto3.entities.FacturaDataCollectionItem
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*

interface FacturaService {
    @GET("Factura")
    fun listFactura(): Call<List<FacturaDataCollectionItem>>

    @GET("Factura/id/{id}")
    fun getFacturaById(@Path("id") id: Long): Call<FacturaDataCollectionItem>
    @Headers("Content-Type: application/json")

    @POST("Factura/addFactura")
    fun addFactura(@Body facturaData: FacturaDataCollectionItem): Call<FacturaDataCollectionItem>
    @Headers("Content-Type: application/json")

    @PUT("Factura")
    fun updateFactura(@Body facturaData: FacturaDataCollectionItem): Call<FacturaDataCollectionItem>

    @DELETE("Factura/delete/{id}")
    fun deleteFactura(@Path("id") id: Long): Call<ResponseBody>
}