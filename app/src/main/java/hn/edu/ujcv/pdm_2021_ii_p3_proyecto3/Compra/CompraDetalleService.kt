import hn.edu.ujcv.pdm_2021_ii_p3_proyecto3.entities.CompraDetalleDataCollectionItem
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*

interface CompraDetalleService {
    @GET("compraDetalle")
    fun listCompraDetalle(): Call<List<CompraDetalleDataCollectionItem>>

    @GET("compraDetalle/compraId/{compraId}")
    fun listCompraDetalleByCompraId(@Path("compraId") compraId: Long): Call<List<CompraDetalleDataCollectionItem>>

    @GET("compraDetalle/id/{id}")
    fun getCompraDetalleById(@Path("id")id :Long): Call<CompraDetalleDataCollectionItem>
    @Headers("Content-Type: application/json")

    @POST("compraDetalle/addcompraDetalle")
    fun addCompraDetalle(@Body compraDetalleData: CompraDetalleDataCollectionItem): Call<CompraDetalleDataCollectionItem>
    @Headers("Content-Type: application/json")

    @PUT("compraDetalle")
    fun updateCompraDetalle(@Body compraDetalle: CompraDetalleDataCollectionItem): Call<CompraDetalleDataCollectionItem>

    @DELETE("compraDetalle/delete/{id}")
    fun deleteCompraDetalle(@Path("id") id: Long): Call<ResponseBody>
}