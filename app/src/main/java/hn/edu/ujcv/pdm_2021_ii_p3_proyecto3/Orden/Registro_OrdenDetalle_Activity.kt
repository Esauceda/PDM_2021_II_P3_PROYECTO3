package hn.edu.ujcv.pdm_2021_ii_p3_proyecto3.Orden

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.ArrayAdapter
import android.widget.Toast
import com.google.gson.Gson
import hn.edu.ujcv.pdm_2021_ii_p3_proyecto3.Almacen.AlmacenService
import hn.edu.ujcv.pdm_2021_ii_p3_proyecto3.MenuPrincipal.MenuActivity
import hn.edu.ujcv.pdm_2021_ii_p3_proyecto3.Producto.ProductoService
import hn.edu.ujcv.pdm_2021_ii_p3_proyecto3.R
import hn.edu.ujcv.pdm_2021_ii_p3_proyecto3.RestEngine
import hn.edu.ujcv.pdm_2021_ii_p3_proyecto3.Toolbar.MyToolbar
import hn.edu.ujcv.pdm_2021_ii_p3_proyecto3.entities.*
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_registro_orden_detalle.*
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Registro_OrdenDetalle_Activity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registro_orden_detalle)

        MyToolbar().show(this,"Registrar Orden Detalle", false)
        callServiceGetAlmacenes()
        callServiceGetProductos()
        callServiceGetOrdenesEncabezado()
        btnRegistrarOrdenDetalle.setOnClickListener { callServicePostOrdenDetalle() }
    }

    var almacenes = ArrayList<String>()
    var productos = ArrayList<String>()
    var ordenesEncabezado = ArrayList<String>()
    //-----

    //POST
    private fun callServicePostOrdenDetalle() {
        val fecha = "2021-04-10"
        val prueba      = spOrdenDetalleID.selectedItem.toString().toInt()
        val ordenDetalleInfo = OrdenDetalleDataCollectionItem(
            ordenDetalleId = null,
            ordenId         = spOrdenDetalleID.selectedItem.toString().toInt(),
            almacenId       = spOrdenDetalleAlmacenID.selectedItem.toString().toInt(),
            productoId      = spOrdenDetalleProductoID.selectedItem.toString().toInt(),
            cantidad        = txtOrdenDetalleCantidad.text.toString().toInt(),
            precio          = txtOrdenDetallePrecio.text.toString().toDouble()
        )

        addOrdenDetalle(ordenDetalleInfo) {
            if (it?.ordenId != null) {
                Toast.makeText(this@Registro_OrdenDetalle_Activity,"Orden Detalle Registrada", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this@Registro_OrdenDetalle_Activity,"Error al registrar el orden detalle", Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun addOrdenDetalle(ordenDetalleData: OrdenDetalleDataCollectionItem, onResult: (OrdenDetalleDataCollectionItem?) -> Unit){
        val retrofit = RestEngine.buildService().create(OrdenDetalleService::class.java)
        var result: Call<OrdenDetalleDataCollectionItem> = retrofit.addOrdenDetalle(ordenDetalleData)

        result.enqueue(object : Callback<OrdenDetalleDataCollectionItem> {
            override fun onFailure(call: Call<OrdenDetalleDataCollectionItem>, t: Throwable) {
                onResult(null)
            }

            override fun onResponse(call: Call<OrdenDetalleDataCollectionItem>,
                                    response: Response<OrdenDetalleDataCollectionItem>) {
                if (response.isSuccessful) {
                    val addedOrdenDetalle = response.body()!!
                    onResult(addedOrdenDetalle)
                }
                else if (response.code() == 401) {
                    Toast.makeText(this@Registro_OrdenDetalle_Activity, "Sesion expirada", Toast.LENGTH_SHORT).show()
                }
                else if (response.code() == 500){

                    val errorResponse = Gson().fromJson(response.errorBody()!!.string()!!, RestApiError::class.java)

                    Toast.makeText(this@Registro_OrdenDetalle_Activity,errorResponse.errorDetails, Toast.LENGTH_SHORT).show()
                }
                else{
                    Toast.makeText(this@Registro_OrdenDetalle_Activity,"Fallo al traer el item", Toast.LENGTH_SHORT).show()
                }
            }

        }
        )
    }


    //PUT

    private fun callServicePutOrdenDetalle() {
        val fecha = "2021-04-11"
        val orndeDetalleInfo = OrdenDetalleDataCollectionItem(
            ordenDetalleId = null,
            ordenId         = spOrdenDetalleID.selectedItem.toString().toInt(),
            almacenId       = spOrdenDetalleAlmacenID.selectedItem.toString().toInt(),
            productoId      = spOrdenDetalleProductoID.selectedItem.toString().toInt(),
            cantidad        = txtOrdenDetalleCantidad.text.toString().toInt(),
            precio          = txtOrdenDetallePrecio.text.toString().toDouble()
        )

        val retrofit = RestEngine.buildService().create(OrdenDetalleService::class.java)
        var result: Call<OrdenDetalleDataCollectionItem> = retrofit.updateOrdenDetalle(orndeDetalleInfo)

        result.enqueue(object : Callback<OrdenDetalleDataCollectionItem> {
            override fun onFailure(call: Call<OrdenDetalleDataCollectionItem>, t: Throwable) {
                Toast.makeText(this@Registro_OrdenDetalle_Activity,"Error al actualizar el ordend detalle",Toast.LENGTH_SHORT).show()
            }

            override fun onResponse(call: Call<OrdenDetalleDataCollectionItem>,
                                    response: Response<OrdenDetalleDataCollectionItem>) {
                if (response.isSuccessful) {
                    val updatedPerson = response.body()!!
                    Toast.makeText(this@Registro_OrdenDetalle_Activity,"Orden Detalle Actualizada",Toast.LENGTH_SHORT).show()
                }
                else if (response.code() == 401){
                    Toast.makeText(this@Registro_OrdenDetalle_Activity,"Sesion expirada",Toast.LENGTH_SHORT).show()
                }
                else{
                    Toast.makeText(this@Registro_OrdenDetalle_Activity,"Fallo al traer el item",Toast.LENGTH_SHORT).show()
                }
            }

        })
    }


    //GET

    private fun callServiceGetOrdenDetalle() {
        val ordenDetalleService:OrdenDetalleService = RestEngine.buildService().create(OrdenDetalleService::class.java)
        var result: Call<OrdenDetalleDataCollectionItem> = ordenDetalleService.getOrdenDetalleById(spOrdenDetalleID.selectedItem.toString().toLong())
        var contadorAlmacenes = 0
        var contadorProductos = 0
        var contadorOrdenesEncabezado =0

        result.enqueue(object :  Callback<OrdenDetalleDataCollectionItem> {
            override fun onFailure(call: Call<OrdenDetalleDataCollectionItem>, t: Throwable) {
                Toast.makeText(this@Registro_OrdenDetalle_Activity,"Error al encontrar el orden detalle",Toast.LENGTH_SHORT).show()
            }

            override fun onResponse(
                call: Call<OrdenDetalleDataCollectionItem>,
                response: Response<OrdenDetalleDataCollectionItem>
            ) {
                if (response.code() == 404){
                    Toast.makeText(this@Registro_OrdenDetalle_Activity, "Ese orden detalle no existe", Toast.LENGTH_SHORT).show()
                }else {

                    for (item in ordenesEncabezado){
                        if (item == response.body()!!.ordenId.toString()){
                            spOrdenDetalleID.setSelection(contadorOrdenesEncabezado)
                        }
                        contadorOrdenesEncabezado++
                    }

                    for (item in almacenes) {
                        if (item == response.body()!!.almacenId.toString()) {
                            spOrdenDetalleAlmacenID.setSelection(contadorAlmacenes)
                        }
                        contadorAlmacenes++
                    }

                    for (item in productos) {
                        if (item == response.body()!!.almacenId.toString()) {
                            spOrdenDetalleProductoID.setSelection(contadorProductos)
                        }
                        contadorProductos++
                    }
                    txtOrdenDetalleCantidad.setText(response.body()!!.cantidad.toString())
                    txtOrdenDetallePrecio.setText(response.body()!!.precio.toString())

                    Toast.makeText(this@Registro_OrdenDetalle_Activity,
                        "Orden detalle encontrado",
                        Toast.LENGTH_SHORT).show()
                }
            }
        })
    }


    //DELETE

    private fun callServiceDeleteOrdenDetalle() {
        val ordenDetalleService:OrdenDetalleService = RestEngine.buildService().create(OrdenDetalleService::class.java)
        var result: Call<ResponseBody> = ordenDetalleService.deleteOrdenDetalle(spOrdenDetalleID.selectedItem.toString().toLong())

        result.enqueue(object :  Callback<ResponseBody> {
            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Toast.makeText(this@Registro_OrdenDetalle_Activity,"Error al eliminar el orden detalle",Toast.LENGTH_SHORT).show()
            }

            override fun onResponse(
                call: Call<ResponseBody>,
                response: Response<ResponseBody>
            ) {
                if (response.isSuccessful) {
                    Toast.makeText(this@Registro_OrdenDetalle_Activity,"Orden detalle eliminado",Toast.LENGTH_SHORT).show()
                }
                else if (response.code() == 401){
                    Toast.makeText(this@Registro_OrdenDetalle_Activity,"Sesion expirada",Toast.LENGTH_SHORT).show()
                }
                else{
                    Toast.makeText(this@Registro_OrdenDetalle_Activity,"Fallo al traer el item",Toast.LENGTH_SHORT).show()
                }
            }
        })
    }

    //GETAlMACENES

    private fun callServiceGetAlmacenes() {
        val almacenService:AlmacenService = RestEngine.buildService().create(AlmacenService::class.java)
        var result: Call<List<AlmacenDataCollectionItem>> = almacenService.listAlmacen()

        result.enqueue(object :  Callback<List<AlmacenDataCollectionItem>> {
            override fun onFailure(call: Call<List<AlmacenDataCollectionItem>>, t: Throwable) {
                Toast.makeText(this@Registro_OrdenDetalle_Activity,"Error al encontrar almacenes",Toast.LENGTH_SHORT).show()
            }

            override fun onResponse(
                call: Call<List<AlmacenDataCollectionItem>>,
                response: Response<List<AlmacenDataCollectionItem>>
            ) {

                for (almacen in response.body()!!){
                    almacenes.add("${almacen.almacenId}")
                }

                val adapterAlmacenes = ArrayAdapter(this@Registro_OrdenDetalle_Activity, android.R.layout.simple_spinner_item, almacenes)
                spOrdenDetalleAlmacenID.adapter = adapterAlmacenes

//                Toast.makeText(this@Registro_OrdenDetalle_Activity,"Almacenes encontrado",Toast.LENGTH_SHORT).show()
            }
        })
    }

    //GETPRODUCTOS

    private fun callServiceGetProductos() {
        val productoService:ProductoService = RestEngine.buildService().create(ProductoService::class.java)
        var result: Call<List<ProductoDataCollectionItem>> = productoService.listProducto()

        result.enqueue(object :  Callback<List<ProductoDataCollectionItem>> {
            override fun onFailure(call: Call<List<ProductoDataCollectionItem>>, t: Throwable) {
                Toast.makeText(this@Registro_OrdenDetalle_Activity,"Error al encontrar productos",Toast.LENGTH_SHORT).show()
            }

            override fun onResponse(
                call: Call<List<ProductoDataCollectionItem>>,
                response: Response<List<ProductoDataCollectionItem>>
            ) {
                for (producto in response.body()!!){
                    productos.add("${producto.productoId}")
                }

                val adapterProductos = ArrayAdapter(this@Registro_OrdenDetalle_Activity, android.R.layout.simple_spinner_item, productos)
                spOrdenDetalleProductoID.adapter = adapterProductos

//                Toast.makeText(this@Registro_OrdenDetalle_Activity,"Productos encontrados",Toast.LENGTH_SHORT).show()
            }
        })
    }

    //GETORDENESENCABEZADO
    private fun callServiceGetOrdenesEncabezado() {
        val ordenEncabezadoService: OrdenEncabezadoService = RestEngine.buildService().create(
            OrdenEncabezadoService::class.java)
        var result: Call<List<OrdenEncabezadoDataCollectionItem>> = ordenEncabezadoService.listOrdenesEncabezado()

        result.enqueue(object :  Callback<List<OrdenEncabezadoDataCollectionItem>> {
            override fun onFailure(call: Call<List<OrdenEncabezadoDataCollectionItem>>, t: Throwable) {
                Toast.makeText(this@Registro_OrdenDetalle_Activity,"Error al encontrar ordenes",Toast.LENGTH_SHORT).show()
            }

            override fun onResponse(
                call: Call<List<OrdenEncabezadoDataCollectionItem>>,
                response: Response<List<OrdenEncabezadoDataCollectionItem>>
            ) {
                for (ordenEncabezado in response.body()!!){
                    ordenesEncabezado.add("${ordenEncabezado.ordenId}")
                }

                val adapterOrdenesEncabezado = ArrayAdapter(this@Registro_OrdenDetalle_Activity,android.R.layout.simple_spinner_item, ordenesEncabezado)
                spOrdenDetalleID.adapter = adapterOrdenesEncabezado

//                Toast.makeText(this@Registro_OrdenDetalle_Activity,"Ordenes encontradas",Toast.LENGTH_SHORT).show()
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