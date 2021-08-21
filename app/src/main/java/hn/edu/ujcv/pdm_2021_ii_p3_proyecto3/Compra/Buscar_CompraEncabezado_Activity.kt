package hn.edu.ujcv.pdm_2021_ii_p3_proyecto3.Compra

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import hn.edu.ujcv.pdm_2021_ii_p3_proyecto3.GetAllActivity
import hn.edu.ujcv.pdm_2021_ii_p3_proyecto3.MenuPrincipal.MenuActivity
import hn.edu.ujcv.pdm_2021_ii_p3_proyecto3.R
import hn.edu.ujcv.pdm_2021_ii_p3_proyecto3.RestEngine
import hn.edu.ujcv.pdm_2021_ii_p3_proyecto3.Toolbar.MyToolbar
import hn.edu.ujcv.pdm_2021_ii_p3_proyecto3.entities.CompraEncabezadoDataCollectionItem
import kotlinx.android.synthetic.main.activity_buscar_compra_encabezado.*
import kotlinx.android.synthetic.main.activity_buscar_orden_encabezado.*
import kotlinx.android.synthetic.main.activity_registro_compra_encabezado.*
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Buscar_CompraEncabezado_Activity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_buscar_compra_encabezado)

        MyToolbar().show(this,"Buscar Compra Encabezado", false)
        btnBuscarCompraEncabezado2.setOnClickListener { callServiceGetCompraEncabezado() }
        btnEliminarCompraEncabezado.setOnClickListener { callServiceDeleteCompraEncabezado() }
        btnVerDetalleCompraEncabezado.setOnClickListener{ verDetalle() }
        btnMostrarTodosCompras.setOnClickListener{ mostrarCompras() }
    }

    private fun mostrarCompras() {
        intent = Intent(this@Buscar_CompraEncabezado_Activity, GetAllActivity::class.java)
        intent.putExtra("numero", 3)
        startActivity(intent)
    }


    //-----

    private fun verDetalle(){
        val intent = Intent(this, Buscar_CompraDetalle_Activity::class.java)
        intent.putExtra("compraId", txtMostrarCompraEncabezadoID.text.toString())
        startActivity(intent)
    }

    private fun callServiceDeleteCompraEncabezado() {
        val compraEncabezadoService: CompraEncabezadoService = RestEngine.buildService().create(CompraEncabezadoService::class.java)
        var result: Call<ResponseBody> = compraEncabezadoService.deleteCompraEncabezado(txtMostrarCompraEncabezadoID.text.toString().toLong())

        result.enqueue(object :  Callback<ResponseBody> { override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
            Toast.makeText(this@Buscar_CompraEncabezado_Activity,"Error",Toast.LENGTH_LONG).show()
        }
            override fun onResponse(
                call: Call<ResponseBody>,
                response: Response<ResponseBody>
            ) {
                if (response.isSuccessful) {
                    Toast.makeText(this@Buscar_CompraEncabezado_Activity,"Orden encabezado eliminada",Toast.LENGTH_LONG).show()
                }
                else if (response.code() == 401){
                    Toast.makeText(this@Buscar_CompraEncabezado_Activity,"Sesion expirada",Toast.LENGTH_LONG).show()
                }
                else{
                    Toast.makeText(this@Buscar_CompraEncabezado_Activity,"Fallo al traer el item",Toast.LENGTH_LONG).show()
                }
            }
        })
    }

    private fun callServiceGetCompraEncabezado() {
        val compraEncabezadoService:CompraEncabezadoService = RestEngine.buildService().create(CompraEncabezadoService::class.java)
        var result: Call<CompraEncabezadoDataCollectionItem> = compraEncabezadoService.getCompraEncabezadoById(txtMostrarCompraEncabezadoID.text.toString().toLong())
        result.enqueue(object : Callback<CompraEncabezadoDataCollectionItem> {
            override fun onFailure(call: Call<CompraEncabezadoDataCollectionItem>, t: Throwable) {
                Toast.makeText(this@Buscar_CompraEncabezado_Activity,"Error", Toast.LENGTH_LONG).show()
            }
            override fun onResponse(
                call: Call<CompraEncabezadoDataCollectionItem>,
                response: Response<CompraEncabezadoDataCollectionItem>
            ) {
                if (response.code() == 404){
                    Toast.makeText(this@Buscar_CompraEncabezado_Activity,"No existe compra encabezado con este id", Toast.LENGTH_LONG).show()
                }else{
                    txtMostrarCompraEncabezadoID.setText(response.body()!!.compraId.toString())
                    txvMostrarProveedorID.setText(response.body()!!.proveedorId.toString())
                    txvEmpleadoID.setText(response.body()!!.empleadoId.toString())
                    txvMostrarCompraTotal.setText(response.body()!!.total.toString())
                    txvMostrarFechaCompra.setText(response.body()!!.fechaCompra)
                    txvMostrarFechaRecepcion.setText(response.body()!!.fechaRecepcion)
                    txvMostrarEstadoCompra.setText(response.body()!!.estado)
                }
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