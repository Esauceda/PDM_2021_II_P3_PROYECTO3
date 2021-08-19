package hn.edu.ujcv.pdm_2021_ii_p3_proyecto3.Fabrica

import hn.edu.ujcv.pdm_2021_ii_p3_proyecto3.entities.FabricaDataCollectionItem
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*

interface FabricaService {
        @GET("fabrica")
        fun listFabricas(): Call<List<FabricaDataCollectionItem>>
        @GET("fabrica/id/{id}")
        fun getFabricaById(@Path("id")id: Long): Call<FabricaDataCollectionItem>
        @Headers("Content-Type: application/json")
        @POST("fabrica/addFabrica")
        fun addFabrica(@Body fabricaData: FabricaDataCollectionItem): Call<FabricaDataCollectionItem>
        @Headers("Content-Type: application/json")
        @PUT("fabrica")
        fun updateFabrica(@Body fabricaData: FabricaDataCollectionItem): Call<FabricaDataCollectionItem>
        @DELETE("fabrica/delete/{id}")
        fun deleteFabrica(@Path("id") id: Long): Call<ResponseBody>
}