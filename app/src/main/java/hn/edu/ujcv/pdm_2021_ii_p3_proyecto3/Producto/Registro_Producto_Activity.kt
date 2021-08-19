package hn.edu.ujcv.pdm_2021_ii_p3_proyecto3.Producto

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.ArrayAdapter
import android.widget.Toast
import com.google.gson.Gson
import hn.edu.ujcv.pdm_2021_ii_p3_proyecto3.Fabrica.FabricaService
import hn.edu.ujcv.pdm_2021_ii_p3_proyecto3.MenuPrincipal.MenuActivity
import hn.edu.ujcv.pdm_2021_ii_p3_proyecto3.R
import hn.edu.ujcv.pdm_2021_ii_p3_proyecto3.RestEngine
import hn.edu.ujcv.pdm_2021_ii_p3_proyecto3.Toolbar.MyToolbar
import hn.edu.ujcv.pdm_2021_ii_p3_proyecto3.entities.FabricaDataCollectionItem
import hn.edu.ujcv.pdm_2021_ii_p3_proyecto3.entities.ProductoDataCollectionItem
import hn.edu.ujcv.pdm_2021_ii_p3_proyecto3.entities.RestApiError
import kotlinx.android.synthetic.main.activity_registro_producto.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Registro_Producto_Activity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registro_producto)
        callServiceGetFabricas()
        btnGuardarProducto.setOnClickListener { callServicePostProducto() }
        btnActuProducto.setOnClickListener { callServicePutProducto() }
        btnBuscarProducto.setOnClickListener { callServiceGetProducto() }
        MyToolbar().show(this,"Registrar Producto", false)
    }

    private fun callServicePutProducto() {
        val productoInfo = ProductoDataCollectionItem(
            productoId =        txtProduID.text.toString().toInt(),
            fabricaId =         sProduFarbicaID.selectedItem.toString().toInt(),
            nombreProducto =    txtNombreProdu.text.toString(),
            descripcion =       txtDescripProdu.text.toString(),
            precio =            txtPrecioProdu.text.toString().toDouble(),
            unidadesEnAlmacen = txtUnidadesAlmacenadas.text.toString().toInt(),
            unidadesMaximas =   txtUnidadesMax.text.toString().toInt(),
            unidadesMinimas =   txtUnidadesMin.text.toString().toInt()
        )

        val retrofit = RestEngine.buildService().create(ProductoService::class.java)
        var result: Call<ProductoDataCollectionItem> = retrofit.updateProducto(productoInfo)

        result.enqueue(object : Callback<ProductoDataCollectionItem> {
            override fun onFailure(call: Call<ProductoDataCollectionItem>, t: Throwable) {
                Toast.makeText(this@Registro_Producto_Activity,"Error", Toast.LENGTH_LONG).show()
            }

            override fun onResponse(call: Call<ProductoDataCollectionItem>,
                                    response: Response<ProductoDataCollectionItem>
            ) {
                if (response.isSuccessful) {
                    val updatedProducto = response.body()!!
                    Toast.makeText(this@Registro_Producto_Activity,"OK"+response.body()!!.nombreProducto,
                        Toast.LENGTH_LONG).show()
                }
                else if (response.code() == 401){
                    Toast.makeText(this@Registro_Producto_Activity,"Sesion expirada", Toast.LENGTH_LONG).show()
                }
                else{
                    Toast.makeText(this@Registro_Producto_Activity,"Fallo al traer el item", Toast.LENGTH_LONG).show()
                }
            }

        })
    }

    private fun callServicePostProducto() {
        val productoInfo = ProductoDataCollectionItem(
            productoId =        txtProduID.text.toString().toInt(),
            fabricaId =         sProduFarbicaID.selectedItem.toString().toInt(),
            nombreProducto =    txtNombreProdu.text.toString(),
            descripcion =       txtDescripProdu.text.toString(),
            precio =            txtPrecioProdu.text.toString().toDouble(),
            unidadesEnAlmacen = txtUnidadesAlmacenadas.text.toString().toInt(),
            unidadesMaximas =   txtUnidadesMax.text.toString().toInt(),
            unidadesMinimas =   txtUnidadesMin.text.toString().toInt()
        )

        addProducto(productoInfo) {
            if (it?.productoId != null) {
                Toast.makeText(this@Registro_Producto_Activity,"OK"+it?.productoId, Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(this@Registro_Producto_Activity,"Error", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun callServiceGetProducto() {
        val productoService: ProductoService = RestEngine.buildService().create(ProductoService::class.java)
        var result: Call<ProductoDataCollectionItem> = productoService.getProductoById(txtProduID.text.toString().toInt())

        result.enqueue(object : Callback<ProductoDataCollectionItem> {
            override fun onFailure(call: Call<ProductoDataCollectionItem>, t: Throwable) {
                Toast.makeText(this@Registro_Producto_Activity,"Error", Toast.LENGTH_LONG).show()
            }

            override fun onResponse(
                call: Call<ProductoDataCollectionItem>,
                response: Response<ProductoDataCollectionItem>
            ) {
                txtProduID.setText(response.body()!!.productoId.toString())
                //fabrica
                txtNombreProdu.setText(response.body()!!.nombreProducto)
                txtDescripProdu.setText(response.body()!!.descripcion)
                txtPrecioProdu.setText(response.body()!!.precio.toString())
                txtUnidadesAlmacenadas.setText(response.body()!!.unidadesEnAlmacen.toString())
                txtUnidadesMax.setText(response.body()!!.unidadesMaximas.toString())
                txtUnidadesMin.setText(response.body()!!.unidadesMinimas.toString())
                Toast.makeText(this@Registro_Producto_Activity,"OK"+response.body()!!.nombreProducto,
                    Toast.LENGTH_LONG).show()
            }
        })
    }

    fun addProducto(productoData: ProductoDataCollectionItem, onResult: (ProductoDataCollectionItem?) -> Unit){
        val retrofit = RestEngine.buildService().create(ProductoService::class.java)
        var result: Call<ProductoDataCollectionItem> = retrofit.addProducto(productoData)

        result.enqueue(object : Callback<ProductoDataCollectionItem> {
            override fun onFailure(call: Call<ProductoDataCollectionItem>, t: Throwable) {
                onResult(null)
            }

            override fun onResponse(call: Call<ProductoDataCollectionItem>,
                                    response: Response<ProductoDataCollectionItem>
            ) {
                if (response.isSuccessful) {
                    val addedPerson = response.body()!!
                    onResult(addedPerson)
                }
                /*else if (response.code() == 401){
                    Toast.makeText(this@MainActivity,"Sesion expirada",Toast.LENGTH_LONG).show()
                }*/
                else if (response.code() == 500){
                    //val gson = Gson()
                    //val type = object : TypeToken<RestApiError>() {}.type
                    //var errorResponse1: RestApiError? = gson.fromJson(response.errorBody()!!.charStream(), type)
                    val errorResponse = Gson().fromJson(response.errorBody()!!.string()!!, RestApiError::class.java)

                    Toast.makeText(this@Registro_Producto_Activity,errorResponse.errorDetails, Toast.LENGTH_LONG).show()
                }
                else{
                    Toast.makeText(this@Registro_Producto_Activity,"Fallo al traer el item", Toast.LENGTH_LONG).show()
                }
            }

        }
        )
    }

    private fun callServiceGetFabricas() {
        val fabricaService: FabricaService = RestEngine.buildService().create(FabricaService::class.java)
        var result: Call<List<FabricaDataCollectionItem>> = fabricaService.listFabricas()
        var fabricas = ArrayList<String>()

        result.enqueue(object :  Callback<List<FabricaDataCollectionItem>> {
            override fun onFailure(call: Call<List<FabricaDataCollectionItem>>, t: Throwable) {
                Toast.makeText(this@Registro_Producto_Activity,"Error al encontrar fabricas",Toast.LENGTH_LONG).show()
            }

            override fun onResponse(
                call: Call<List<FabricaDataCollectionItem>>,
                response: Response<List<FabricaDataCollectionItem>>
            ) {
                for (fabrica in response.body()!!){
                    fabricas.add("${fabrica.fabricaId}")
                }

                val adapterEmpleados = ArrayAdapter(this@Registro_Producto_Activity, android.R.layout.simple_spinner_item, fabricas)
                sProduFarbicaID.adapter = adapterEmpleados

                Toast.makeText(this@Registro_Producto_Activity,"Fabricas encontrados",Toast.LENGTH_LONG).show()
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_contextual, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        if (item.itemId == R.id.option_one)
            startActivity(Intent(this, Registro_Producto_Activity::class.java))
        if (item.itemId == R.id.option_two)
            startActivity(Intent(this, Buscar_Producto_Activity::class.java))
        if (item.itemId == R.id.option_three)
            startActivity(Intent(this, MenuActivity::class.java))

        return super.onOptionsItemSelected(item)
    }
}