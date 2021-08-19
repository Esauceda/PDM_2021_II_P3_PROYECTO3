package hn.edu.ujcv.pdm_2021_ii_p3_proyecto3.Proveedores

import hn.edu.ujcv.pdm_2021_ii_p3_proyecto3.entities.ProveedorDataCollectionItem
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*

interface ProveedorService {
    @GET("proveedor")
    fun listProveedores(): Call<ProveedorDataCollectionItem>
    @GET("proveedor/id/{id}")
    fun getProveedorById(@Path("id")id: Long): Call<ProveedorDataCollectionItem>
    @Headers("Content-Type: application/json")
    @POST("proveedor/addProveedor")
    fun addProveedor(@Body proveedorData: ProveedorDataCollectionItem): Call<ProveedorDataCollectionItem>
    @Headers("Content-Type: application/json")
    @PUT("proveedor")
    fun updateProveedor(@Body poveedorData: ProveedorDataCollectionItem): Call<ProveedorDataCollectionItem>
    @DELETE("proveedor/delete/{id}")
    fun deleteProveedor(@Path("id")id: Long): Call<ResponseBody>

}