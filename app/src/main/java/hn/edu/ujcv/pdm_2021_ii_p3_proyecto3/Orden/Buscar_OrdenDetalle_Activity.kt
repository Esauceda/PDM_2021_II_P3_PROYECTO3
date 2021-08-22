package hn.edu.ujcv.pdm_2021_ii_p3_proyecto3.Orden

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
import hn.edu.ujcv.pdm_2021_ii_p3_proyecto3.entities.OrdenDetalleDataCollectionItem
import kotlinx.android.synthetic.main.activity_buscar_orden_detalle.*
import kotlinx.android.synthetic.main.activity_registro_orden_detalle.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Buscar_OrdenDetalle_Activity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_buscar_orden_detalle)

        MyToolbar().show(this,"Buscar Orden Detalle", false)
        verOrndenesDetalle()
        btnEliminarOrdenesDetalle.setOnClickListener{ eliminar() }
        /*callServiceGetOrdenDetalle()
        btnEliminarOrdenDetalle.setOnClickListener { callServiceDeleteOrdenDetalle() }*/
    }

    private fun eliminar() {
        intent = Intent(this@Buscar_OrdenDetalle_Activity, Eliminar_Orden_Detalle_Activity::class.java)
        startActivity(intent)
    }

    private fun verOrndenesDetalle() {
        val ordenDetalleService:OrdenDetalleService = RestEngine.buildService().create(OrdenDetalleService::class.java)
        var ordenId = intent.getSerializableExtra("ordenId").toString().toLong()
        var result: Call<List<OrdenDetalleDataCollectionItem>> = ordenDetalleService.listOrdenesDetalleByOrdenId(ordenId)
        var ordenesDetalleByOrdenId = ArrayList<String>()

        result.enqueue(object :  Callback<List<OrdenDetalleDataCollectionItem>> {
            override fun onFailure(call: Call<List<OrdenDetalleDataCollectionItem>>, t: Throwable) {
                Toast.makeText(this@Buscar_OrdenDetalle_Activity,"Error al encontrar los ordenes detalle",Toast.LENGTH_SHORT).show()
            }

            override fun onResponse(
                call: Call<List<OrdenDetalleDataCollectionItem>>,
                response: Response<List<OrdenDetalleDataCollectionItem>>
            ) {
                ordenesDetalleByOrdenId.add("ID | Producto ID | Cantidad | Precio")
                for (ordenDetalle in response.body()!!){
                    ordenesDetalleByOrdenId.add("${ordenDetalle.ordenDetalleId} | ${ordenDetalle.productoId} | ${ordenDetalle.cantidad} | ${ordenDetalle.precio}")
                }

                val adapterOrdenesEncabezadoByOrdenId = ArrayAdapter(this@Buscar_OrdenDetalle_Activity, android.R.layout.simple_list_item_1, ordenesDetalleByOrdenId)
                lstvOrdenesEncabezado.adapter = adapterOrdenesEncabezadoByOrdenId

                Toast.makeText(this@Buscar_OrdenDetalle_Activity,"Ordenes detalle encotradas",Toast.LENGTH_SHORT).show()
            }
        })
    }


    /* //GET

     private fun callServiceGetOrdenDetalle() {

         txvMostrarOrdenDetalleID.setText(intent.getSerializableExtra("ordenId").toString())

         val ordenDetalleService:OrdenDetalleService = RestEngine.buildService().create(OrdenDetalleService::class.java)
         var result: Call<OrdenDetalleDataCollectionItem> = ordenDetalleService.getOrdenDetalleById(txvMostrarOrdenDetalleID.text.toString().toLong())

         result.enqueue(object : Callback<OrdenDetalleDataCollectionItem> {
             override fun onFailure(call: Call<OrdenDetalleDataCollectionItem>, t: Throwable) {
                 Toast.makeText(this@Buscar_OrdenDetalle_Activity,"Error al encontrar el orden detalle",
                     Toast.LENGTH_SHORT).show()
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
                         Toast.LENGTH_SHORT).show()
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
                 Toast.makeText(this@Buscar_OrdenDetalle_Activity,"Error al eliminar el orden detalle",Toast.LENGTH_SHORT).show()
             }

             override fun onResponse(
                 call: Call<ResponseBody>,
                 response: Response<ResponseBody>
             ) {
                 if (response.isSuccessful) {
                     Toast.makeText(this@Buscar_OrdenDetalle_Activity,"Orden detalle eliminado",Toast.LENGTH_SHORT).show()
                 }
                 else if (response.code() == 401){
                     Toast.makeText(this@Buscar_OrdenDetalle_Activity,"Sesion expirada",Toast.LENGTH_SHORT).show()
                 }
                 else{
                     Toast.makeText(this@Buscar_OrdenDetalle_Activity,"Fallo al traer el item",Toast.LENGTH_SHORT).show()
                 }
             }
         })
     }*/


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