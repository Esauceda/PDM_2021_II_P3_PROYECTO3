package hn.edu.ujcv.pdm_2021_ii_p3_proyecto3.Producto

import hn.edu.ujcv.pdm_2021_ii_p3_proyecto3.entities.ProductoDataCollectionItem
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*

interface ProductoService {
    @GET("producto")
    fun listProducto(): Call<List<ProductoDataCollectionItem>>
    @GET("producto/id/{id}")
    fun getProductoById(@Path("id") id: Int): Call<ProductoDataCollectionItem>
    @GET("producto/nombreProducto/{nombreProducto}")
    fun getProductoByNombre(@Path("id") id: Int): Call<ProductoDataCollectionItem>
    @Headers("Content-Type: application/json")
    @POST("producto/addProducto")
    fun addProducto(@Body personData: ProductoDataCollectionItem): Call<ProductoDataCollectionItem>
    @Headers("Content-Type: application/json")
    @PUT("producto")
    fun updateProducto(@Body personData: ProductoDataCollectionItem): Call<ProductoDataCollectionItem>
    @DELETE("producto/delete/{id}")
    fun deleteProducto(@Path("id") id: Int): Call<ResponseBody>
}