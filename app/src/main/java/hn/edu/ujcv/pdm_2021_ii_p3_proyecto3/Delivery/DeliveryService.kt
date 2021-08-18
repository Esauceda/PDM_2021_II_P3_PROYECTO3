package hn.edu.ujcv.pdm_2021_ii_p3_proyecto3.Delivery


import hn.edu.ujcv.pdm_2021_ii_p3_proyecto3.entities.DeliveryDataCollecionItem
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*

interface DeliveryService {
    @GET("delivery")
    fun listDelivery(): Call<List<DeliveryDataCollecionItem>>
    @GET("delivery/id/{id}")
    fun getDeliveryById(@Path("id") id: Int): Call<DeliveryDataCollecionItem>
    @GET("delivery/nombreCompania/{nombreCompania}")
    fun getDeliveryByNombre(@Path("id") id: Int): Call<DeliveryDataCollecionItem>
    @Headers("Content-Type: application/json")
    @POST("delivery/addDelivery")
    fun addDelivery(@Body personData: DeliveryDataCollecionItem): Call<DeliveryDataCollecionItem>
    @Headers("Content-Type: application/json")
    @PUT("delivery")
    fun updateDelivery(@Body personData: DeliveryDataCollecionItem): Call<DeliveryDataCollecionItem>
    @DELETE("delivery/delete/{id}")
    fun deleteDelivery(@Path("id") id: Int): Call<ResponseBody>
}