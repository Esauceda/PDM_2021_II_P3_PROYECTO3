package hn.edu.ujcv.pdm_2021_ii_p3_proyecto3.Maquinaria

import hn.edu.ujcv.pdm_2021_ii_p3_proyecto3.entities.MaquinariaDataCollectionItem
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*

interface MaquinariaService {
    @GET("Maquinaria")
    fun listMaquinarias(): Call<List<MaquinariaDataCollectionItem>>

    @GET("Maquinaria/id/{id}")
    fun getMaquinariaById(@Path("id") id: Long): Call<MaquinariaDataCollectionItem>
    @Headers("Content-Type: application/json")

    @POST("Maquinaria/addMaquinaria")
    fun addMaquinaria(@Body maquinariaData: MaquinariaDataCollectionItem): Call<MaquinariaDataCollectionItem>
    @Headers("Content-Type: application/json")

    @PUT("Maquinaria")
    fun updateMaquinaria(@Body maquinariaData: MaquinariaDataCollectionItem): Call<MaquinariaDataCollectionItem>

    @DELETE("Maquinaria/delete/{id}")
    fun deleteMaquinaria(@Path("id") id: Long): Call<ResponseBody>
}