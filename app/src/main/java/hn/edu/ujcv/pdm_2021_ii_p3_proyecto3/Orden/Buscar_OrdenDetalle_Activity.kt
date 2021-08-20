package hn.edu.ujcv.pdm_2021_ii_p3_proyecto3.Orden

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import hn.edu.ujcv.pdm_2021_ii_p3_proyecto3.MenuPrincipal.MenuActivity
import hn.edu.ujcv.pdm_2021_ii_p3_proyecto3.R
import hn.edu.ujcv.pdm_2021_ii_p3_proyecto3.RestEngine
import hn.edu.ujcv.pdm_2021_ii_p3_proyecto3.Toolbar.MyToolbar
import hn.edu.ujcv.pdm_2021_ii_p3_proyecto3.entities.OrdenDetalleDataCollectionItem
import kotlinx.android.synthetic.main.activity_buscar_orden_detalle.*
import kotlinx.android.synthetic.main.activity_registro_orden_detalle.*
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Buscar_OrdenDetalle_Activity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_buscar_orden_detalle)

        MyToolbar().show(this,"Buscar Orden Detalle", false)
        callServiceGetOrdenDetalle()
        btnEliminarOrdenDetalle.setOnClickListener { callServiceDeleteOrdenDetalle() }
    }

    //GET

    private fun callServiceGetOrdenDetalle() {

        txvMostrarOrdenDetalleID.setText(intent.getSerializableExtra("ordenId").toString())

        val ordenDetalleService:OrdenDetalleService = RestEngine.buildService().create(OrdenDetalleService::class.java)
        var result: Call<OrdenDetalleDataCollectionItem> = ordenDetalleService.getOrdenDetalleById(txvMostrarOrdenDetalleID.text.toString().toLong())

        result.enqueue(object : Callback<OrdenDetalleDataCollectionItem> {
            override fun onFailure(call: Call<OrdenDetalleDataCollectionItem>, t: Throwable) {
                Toast.makeText(this@Buscar_OrdenDetalle_Activity,"Error al encontrar el orden detalle",
                    Toast.LENGTH_LONG).show()
            }

            override fun onResponse(
                call: Call<OrdenDetalleDataCollectionItem>,
                response: Response<OrdenDetalleDataCollectionItem>
            ) {
                if (response.code() == 404){
                    Toast.makeText(this@Buscar_OrdenDetalle_Activity, "Ese orden detalle no existe", Toast.LENGTH_SHORT).show()
                }else {
                    txvMostrarOrdenDetalleID.setText(response.body()!!.ordenId.toString())
                    txvMostrarOrdenDetalleAlmacenID.setText(response.body()!!.almacenId.toString())
                    txvMostrarOrdenDetalleProductoID.setText(response.body()!!.productoId.toString())
                    txvMostrarOrdenDetalleCantidad.setText(response.body()!!.cantidad.toString())
                    txvMostrarOrdenDetallePrecio.setText(response.body()!!.precio.toString())

                    Toast.makeText(this@Buscar_OrdenDetalle_Activity,
                        "Orden detalle encontrado",
                        Toast.LENGTH_LONG).show()
                }
            }
        })
    }

//DELETE

    private fun callServiceDeleteOrdenDetalle() {
        val ordenDetalleService:OrdenDetalleService = RestEngine.buildService().create(OrdenDetalleService::class.java)
        var result: Call<ResponseBody> = ordenDetalleService.deleteOrdenDetalle(txvMostrarOrdenDetalleID.text.toString().toLong())

        result.enqueue(object :  Callback<ResponseBody> {
            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Toast.makeText(this@Buscar_OrdenDetalle_Activity,"Error al eliminar el orden detalle",Toast.LENGTH_LONG).show()
            }

            override fun onResponse(
                call: Call<ResponseBody>,
                response: Response<ResponseBody>
            ) {
                if (response.isSuccessful) {
                    Toast.makeText(this@Buscar_OrdenDetalle_Activity,"Orden detalle eliminado",Toast.LENGTH_LONG).show()
                }
                else if (response.code() == 401){
                    Toast.makeText(this@Buscar_OrdenDetalle_Activity,"Sesion expirada",Toast.LENGTH_LONG).show()
                }
                else{
                    Toast.makeText(this@Buscar_OrdenDetalle_Activity,"Fallo al traer el item",Toast.LENGTH_LONG).show()
                }
            }
        })
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_contextual2, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        if (item.itemId == R.id.option_one)
            startActivity(Intent(this, Registro_OrdenEncabezado_Activity::class.java))
        if (item.itemId == R.id.option_two)
            startActivity(Intent(this, Buscar_OrdenEncabezado_Activity::class.java))
        if (item.itemId == R.id.option_three)
            startActivity(Intent(this, Registro_OrdenDetalle_Activity::class.java))
        if (item.itemId == R.id.option_four)
            startActivity(Intent(this, Buscar_OrdenDetalle_Activity::class.java))
        if (item.itemId == R.id.option_five)
            startActivity(Intent(this, MenuActivity::class.java))

        return super.onOptionsItemSelected(item)
    }
}