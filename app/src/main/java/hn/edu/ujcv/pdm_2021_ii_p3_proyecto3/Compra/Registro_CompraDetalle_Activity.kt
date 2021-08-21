package hn.edu.ujcv.pdm_2021_ii_p3_proyecto3.Compra

import CompraDetalleService
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.ArrayAdapter
import android.widget.Toast
import com.google.gson.Gson
import hn.edu.ujcv.pdm_2021_ii_p3_proyecto3.MenuPrincipal.MenuActivity
import hn.edu.ujcv.pdm_2021_ii_p3_proyecto3.R
import hn.edu.ujcv.pdm_2021_ii_p3_proyecto3.RestEngine
import hn.edu.ujcv.pdm_2021_ii_p3_proyecto3.Toolbar.MyToolbar
import hn.edu.ujcv.pdm_2021_ii_p3_proyecto3.entities.CompraDetalleDataCollectionItem
import hn.edu.ujcv.pdm_2021_ii_p3_proyecto3.entities.CompraEncabezadoDataCollectionItem
import hn.edu.ujcv.pdm_2021_ii_p3_proyecto3.entities.RestApiError
import kotlinx.android.synthetic.main.activity_registro_compra_detalle.*
import kotlinx.android.synthetic.main.activity_registro_orden_detalle.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Registro_CompraDetalle_Activity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registro_compra_detalle)
        MyToolbar().show(this,"Registrar Compra Detalle", false)
        callServiceGetComprasEncabezado()
        btnRegistrarCompraDetalle.setOnClickListener{ callServicePostCompraDetalle() }

    }
    var comprasEncabezado = ArrayList<String>()
    //-----

    private fun callServicePostCompraDetalle() {
        val compraDetalleInfo = CompraDetalleDataCollectionItem(
            compraId = sCompraDetalle.selectedItem.toString().toLong(),
            producto = txtProductoCompra.text.toString(),
            cantidad = txtCantidad.text.toString().toInt(),
            precio   = txtPrecioCompra.text.toString().toDouble()
        )

        addCompraDetalle(compraDetalleInfo) {
            if (it?.compraId != null) {
                Toast.makeText(this@Registro_CompraDetalle_Activity,"Compra Detalle Registrada", Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(this@Registro_CompraDetalle_Activity,"Error", Toast.LENGTH_LONG).show()
            }
        }
    }

    fun addCompraDetalle(compraDetalleData: CompraDetalleDataCollectionItem, onResult: (CompraDetalleDataCollectionItem?) -> Unit){
        val retrofit = RestEngine.buildService().create(CompraDetalleService::class.java)
        var result: Call<CompraDetalleDataCollectionItem> = retrofit.addCompraDetalle(compraDetalleData)

        result.enqueue(object : Callback<CompraDetalleDataCollectionItem> {
            override fun onFailure(call: Call<CompraDetalleDataCollectionItem>, t: Throwable) {
                onResult(null)
            }

            override fun onResponse(call: Call<CompraDetalleDataCollectionItem>,
                                    response: Response<CompraDetalleDataCollectionItem>) {
                if (response.isSuccessful) {
                    val addedCompraDetalle = response.body()!!
                    onResult(addedCompraDetalle)
                }
                else if (response.code() == 401) {
                    Toast.makeText(this@Registro_CompraDetalle_Activity, "Sesion expirada", Toast.LENGTH_LONG).show()
                }
                else if (response.code() == 500){

                    val errorResponse = Gson().fromJson(response.errorBody()!!.string()!!, RestApiError::class.java)

                    Toast.makeText(this@Registro_CompraDetalle_Activity,errorResponse.errorDetails, Toast.LENGTH_LONG).show()
                }
                else{
                    Toast.makeText(this@Registro_CompraDetalle_Activity,"Fallo al traer el item", Toast.LENGTH_LONG).show()
                }
            }

        }
        )
    }

    private fun callServiceGetComprasEncabezado() {
        val compraEncabezadoService: CompraEncabezadoService = RestEngine.buildService().create(CompraEncabezadoService::class.java)
        var result: Call<List<CompraEncabezadoDataCollectionItem>> = compraEncabezadoService.listCompraEncabezado()

        result.enqueue(object : Callback<List<CompraEncabezadoDataCollectionItem>> {
            override fun onFailure(call: Call<List<CompraEncabezadoDataCollectionItem>>, t: Throwable) {
                Toast.makeText(this@Registro_CompraDetalle_Activity,"Error al encontrar productos",
                    Toast.LENGTH_LONG).show()
            }

            override fun onResponse(
                call: Call<List<CompraEncabezadoDataCollectionItem>>,
                response: Response<List<CompraEncabezadoDataCollectionItem>>
            ) {
                for (compraEncabezado in response.body()!!){
                    comprasEncabezado.add("${compraEncabezado.compraId}")
                }

                val adapterCompraEncabezado = ArrayAdapter(this@Registro_CompraDetalle_Activity, android.R.layout.simple_spinner_item, comprasEncabezado)
                sCompraDetalle.adapter = adapterCompraEncabezado

                Toast.makeText(this@Registro_CompraDetalle_Activity,"Productos encontrados", Toast.LENGTH_LONG).show()
            }
        })
    }

    //-----

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_contextual2, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        if (item.itemId == R.id.option_one)
            startActivity(Intent(this, Registro_CompraEncabezado_Activity::class.java))
        if (item.itemId == R.id.option_two)
            startActivity(Intent(this, Buscar_CompraEncabezado_Activity::class.java))
        if (item.itemId == R.id.option_three)
            startActivity(Intent(this, Registro_CompraDetalle_Activity::class.java))
        if (item.itemId == R.id.option_four)
            startActivity(Intent(this, Buscar_CompraDetalle_Activity::class.java))
        if (item.itemId == R.id.option_five)
            startActivity(Intent(this, MenuActivity::class.java))

        return super.onOptionsItemSelected(item)
    }
}