package hn.edu.ujcv.pdm_2021_ii_p3_proyecto3.MateriaPrima

import hn.edu.ujcv.pdm_2021_ii_p3_proyecto3.entities.MateriaPrimaDataCollectionItem
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*

interface MateriaPrimaService {
    @GET("materiaPrima")
    fun listMateria(): Call<List<MateriaPrimaDataCollectionItem>>
    @GET("materiaPrima/id/{id}")
    fun getMateriaById(@Path("id") id: Int): Call<MateriaPrimaDataCollectionItem>
    @GET("materiaPrima/nombreMateria/{nombreMateria}")
    fun getMateriaByNombreMateria(@Path("id") id: Int): Call<MateriaPrimaDataCollectionItem>
    @Headers("Content-Type: application/json")
    @POST("materiaPrima/addMateria")
    fun addMateria(@Body personData: MateriaPrimaDataCollectionItem): Call<MateriaPrimaDataCollectionItem>
    @Headers("Content-Type: application/json")
    @PUT("materia")
    fun updateMateria(@Body personData: MateriaPrimaDataCollectionItem): Call<MateriaPrimaDataCollectionItem>
    @DELETE("materia/delete/{id}")
    fun deleteMateria(@Path("id") id: Int): Call<ResponseBody>
}