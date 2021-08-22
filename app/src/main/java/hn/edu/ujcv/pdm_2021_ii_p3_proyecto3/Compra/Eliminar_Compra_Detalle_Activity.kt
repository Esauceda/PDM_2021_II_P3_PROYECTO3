package hn.edu.ujcv.pdm_2021_ii_p3_proyecto3.Compra

import CompraDetalleService
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.ArrayAdapter
import android.widget.Toast
import hn.edu.ujcv.pdm_2021_ii_p3_proyecto3.MenuPrincipal.MenuActivity
import hn.edu.ujcv.pdm_2021_ii_p3_proyecto3.R
import hn.edu.ujcv.pdm_2021_ii_p3_proyecto3.RestEngine
import hn.edu.ujcv.pdm_2021_ii_p3_proyecto3.Toolbar.MyToolbar
import hn.edu.ujcv.pdm_2021_ii_p3_proyecto3.entities.CompraDetalleDataCollectionItem
import kotlinx.android.synthetic.main.activity_buscar_compra_detalle.*
import kotlinx.android.synthetic.main.activity_buscar_orden_detalle.*
import kotlinx.android.synthetic.main.activity_eliminar_compra_detalle.*
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Eliminar_Compra_Detalle_Activity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_eliminar_compra_detalle)
        btnBuscarComprasDetalle.setOnClickListener{ callServiceGetCompraDetalle() }
        btnEliminarCompraDetalle.setOnClickListener { callServiceDeleteCompraDetalle() }

//        MyToolbar().show(this,"Eliminar Compra Detalle", false)
    }

    private fun callServiceGetCompraDetalle(){
        val compraDetalleService: CompraDetalleService = RestEngine.buildService().create(CompraDetalleService::class.java)
        var result: Call<CompraDetalleDataCollectionItem> = compraDetalleService.getCompraDetalleById(txtMostrarCompraDetalleID.text.toString().toLong())

        result.enqueue(object : Callback<CompraDetalleDataCollectionItem> {
            override fun onFailure(call: Call<CompraDetalleDataCollectionItem>, t: Throwable) {
                Toast.makeText(this@Eliminar_Compra_Detalle_Activity,"Error al encontrar la compra detalle", Toast.LENGTH_SHORT).show()
            }

            override fun onResponse(
                call: Call<CompraDetalleDataCollectionItem>,
                response: Response<CompraDetalleDataCollectionItem>
            ) {
                if (response.code() == 404){
                    Toast.makeText(this@Eliminar_Compra_Detalle_Activity, "Esa compra detalle no existe", Toast.LENGTH_SHORT).show()
                }else {
                    txtMostrarCompraDetalleID.setText(response.body()!!.compraDetalleId.toString())
                    txvMostarProductoID.setText(response.body()!!.producto)
                    txvMostrarCantidad.setText(response.body()!!.cantidad.toString())
                    txvMostrarPrecioCompra.setText(response.body()!!.precio.toString())
                    Toast.makeText(this@Eliminar_Compra_Detalle_Activity, "Compra detalle encontrado", Toast.LENGTH_SHORT).show()
                }
            }
        })
    }

    //DELETE

    private fun callServiceDeleteCompraDetalle() {
        val personService:CompraDetalleService = RestEngine.buildService().create(CompraDetalleService::class.java)
        var result: Call<ResponseBody> = personService.deleteCompraDetalle(txtMostrarCompraDetalleID.text.toString().toLong())

        result.enqueue(object :  Callback<ResponseBody> {
            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Toast.makeText(this@Eliminar_Compra_Detalle_Activity,"Error al eliminar la compra detalle",Toast.LENGTH_SHORT).show()
            }

            override fun onResponse(
                call: Call<ResponseBody>,
                response: Response<ResponseBody>
            ) {
                if (response.isSuccessful) {
                    Toast.makeText(this@Eliminar_Compra_Detalle_Activity,"Compra Detalle Eliminada",Toast.LENGTH_SHORT).show()
                }
                else if (response.code() == 401){
                    Toast.makeText(this@Eliminar_Compra_Detalle_Activity,"Sesion expirada",Toast.LENGTH_SHORT).show()
                }
                else{
                    Toast.makeText(this@Eliminar_Compra_Detalle_Activity,"Fallo al traer el item",Toast.LENGTH_SHORT).show()
                }
            }
        })
    }

}