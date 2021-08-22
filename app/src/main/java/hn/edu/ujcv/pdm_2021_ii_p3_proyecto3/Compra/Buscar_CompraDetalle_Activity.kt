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
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Buscar_CompraDetalle_Activity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_buscar_compra_detalle)

        MyToolbar().show(this,"Buscar Compra Detalle", false)
        verCompraDetalles()
        btnEliminarComprasDetalle.setOnClickListener{ eliminar() }
        /*callServiceGetCompraDetalle()
        btnEliminarCompraDetalle.setOnClickListener { callServiceDeleteCompraDetalle() }*/
    }

    private fun eliminar() {
        intent = Intent(this@Buscar_CompraDetalle_Activity, Eliminar_Compra_Detalle_Activity::class.java)
        startActivity(intent)
    }

    private fun verCompraDetalles() {
        val compraDetalleService:CompraDetalleService = RestEngine.buildService().create(CompraDetalleService::class.java)
        val compraId = intent.getSerializableExtra("compraId")
        var result: Call<List<CompraDetalleDataCollectionItem>> = compraDetalleService.listCompraDetalleByCompraId(compraId.toString().toLong())
        var comprasDetalleByCompraId = ArrayList<String>()

        result.enqueue(object :  Callback<List<CompraDetalleDataCollectionItem>> {
            override fun onFailure(call: Call<List<CompraDetalleDataCollectionItem>>, t: Throwable) {
                Toast.makeText(this@Buscar_CompraDetalle_Activity,"Error al encontrar compras detalle",Toast.LENGTH_SHORT).show()
            }

            override fun onResponse(
                call: Call<List<CompraDetalleDataCollectionItem>>,
                response: Response<List<CompraDetalleDataCollectionItem>>
            ) {
                comprasDetalleByCompraId.add("ID | Producto | Cantidad | Precio")
                for (compraDetalle in response.body()!!){
                    comprasDetalleByCompraId.add("${compraDetalle.compraDetalleId} | ${compraDetalle.producto} | ${compraDetalle.cantidad} | ${compraDetalle.precio}")
                }

                val adapterComprasDetalle = ArrayAdapter(this@Buscar_CompraDetalle_Activity, android.R.layout.simple_list_item_1, comprasDetalleByCompraId)
                lstvComprasDetalla.adapter = adapterComprasDetalle

                Toast.makeText(this@Buscar_CompraDetalle_Activity,"Compras detalle encontradas",Toast.LENGTH_SHORT).show()
            }
        })
    }

    //-----

    /*private fun callServiceGetCompraDetalle(){
        txtMostrarCompraDetalleID.setText(intent.getSerializableExtra("compraId").toString())

        val compraDetalleService: CompraDetalleService = RestEngine.buildService().create(CompraDetalleService::class.java)
        var result: Call<CompraDetalleDataCollectionItem> = compraDetalleService.getCompraDetalleById(txtMostrarCompraDetalleID.text.toString().toLong())

        result.enqueue(object : Callback<CompraDetalleDataCollectionItem> {
            override fun onFailure(call: Call<CompraDetalleDataCollectionItem>, t: Throwable) {
                Toast.makeText(this@Buscar_CompraDetalle_Activity,"Error al encontrar la compra detalle", Toast.LENGTH_SHORT).show()
            }

            override fun onResponse(
                call: Call<CompraDetalleDataCollectionItem>,
                response: Response<CompraDetalleDataCollectionItem>
            ) {
                if (response.code() == 404){
                    Toast.makeText(this@Buscar_CompraDetalle_Activity, "Esa compra detalle no existe", Toast.LENGTH_SHORT).show()
                }else {
                    txtMostrarCompraDetalleID.setText(response.body()!!.compraId.toString())
                    txvMostarProductoID.setText(response.body()!!.producto)
                    txvMostrarCantidad.setText(response.body()!!.cantidad.toString())
                    txvMostrarPrecioCompra.setText(response.body()!!.precio.toString())
                    Toast.makeText(this@Buscar_CompraDetalle_Activity, "Compra detalle encontrado", Toast.LENGTH_SHORT).show()
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
                Toast.makeText(this@Buscar_CompraDetalle_Activity,"Error al eliminar la compra detalle",Toast.LENGTH_SHORT).show()
            }

            override fun onResponse(
                call: Call<ResponseBody>,
                response: Response<ResponseBody>
            ) {
                if (response.isSuccessful) {
                    Toast.makeText(this@Buscar_CompraDetalle_Activity,"Compra Detalle Eliminada",Toast.LENGTH_SHORT).show()
                }
                else if (response.code() == 401){
                    Toast.makeText(this@Buscar_CompraDetalle_Activity,"Sesion expirada",Toast.LENGTH_SHORT).show()
                }
                else{
                    Toast.makeText(this@Buscar_CompraDetalle_Activity,"Fallo al traer el item",Toast.LENGTH_SHORT).show()
                }
            }
        })
    }*/

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