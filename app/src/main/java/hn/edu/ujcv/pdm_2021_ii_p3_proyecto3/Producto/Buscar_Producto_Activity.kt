package hn.edu.ujcv.pdm_2021_ii_p3_proyecto3.Producto

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
import hn.edu.ujcv.pdm_2021_ii_p3_proyecto3.entities.ProductoDataCollectionItem
import kotlinx.android.synthetic.main.activity_buscar_producto.*
import kotlinx.android.synthetic.main.activity_registro_producto.*
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Buscar_Producto_Activity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_buscar_producto)
        btnBuscarProducto2.setOnClickListener { callServiceGetProducto() }
        btnEliminarProducto.setOnClickListener { callServiceDeleteDelivery() }
        MyToolbar().show(this,"Buscar Producto", false)
    }

    private fun callServiceGetProducto() {
        val productoService: ProductoService = RestEngine.buildService().create(ProductoService::class.java)
        var result: Call<ProductoDataCollectionItem> = productoService.getProductoById(txtProduID2.text.toString().toInt())

        result.enqueue(object : Callback<ProductoDataCollectionItem> {
            override fun onFailure(call: Call<ProductoDataCollectionItem>, t: Throwable) {
                Toast.makeText(this@Buscar_Producto_Activity,"Error", Toast.LENGTH_LONG).show()
            }

            override fun onResponse(
                call: Call<ProductoDataCollectionItem>,
                response: Response<ProductoDataCollectionItem>
            ) {
                if (response.code() == 404){
                    Toast.makeText(this@Buscar_Producto_Activity, "Producto no existe",Toast.LENGTH_SHORT).show()
                }else {
                    txtProduID2.setText(response.body()!!.productoId.toString())
                    txvMostrarProduFabricaID.setText(response.body()!!.fabricaId.toString())
                    txvMostrarNomProducto.setText(response.body()!!.nombreProducto)
                    txvMostrarDescripProdu.setText(response.body()!!.descripcion)
                    txvMostrarPrecioProdu.setText(response.body()!!.precio.toString())
                    txvMostrarUnidadesAlmacen.setText(response.body()!!.unidadesEnAlmacen.toString())
                    txvMostrarUnidadesMin.setText(response.body()!!.unidadesMaximas.toString())
                    txvMostrarUnidadesMax.setText(response.body()!!.unidadesMinimas.toString())
                    Toast.makeText(this@Buscar_Producto_Activity,
                        "OK" + response.body()!!.nombreProducto,
                        Toast.LENGTH_LONG).show()
                }
            }
        })
    }

    private fun callServiceDeleteDelivery() {
        val deliveryService: ProductoService = RestEngine.buildService().create(ProductoService::class.java)
        var result: Call<ResponseBody> = deliveryService.deleteProducto(txtProduID2.text.toString().toInt())

        result.enqueue(object : Callback<ResponseBody> {
            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Toast.makeText(this@Buscar_Producto_Activity,"Error", Toast.LENGTH_LONG).show()
            }

            override fun onResponse(
                call: Call<ResponseBody>,
                response: Response<ResponseBody>
            ) {
                if (response.isSuccessful) {
                    Toast.makeText(this@Buscar_Producto_Activity,"DELETE", Toast.LENGTH_LONG).show()
                }
                else if (response.code() == 401){
                    Toast.makeText(this@Buscar_Producto_Activity,"Sesion expirada", Toast.LENGTH_LONG).show()
                }
                else{
                    Toast.makeText(this@Buscar_Producto_Activity,"Fallo al traer el item", Toast.LENGTH_LONG).show()
                }
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