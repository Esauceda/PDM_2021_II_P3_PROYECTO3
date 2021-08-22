package hn.edu.ujcv.pdm_2021_ii_p3_proyecto3.Orden

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import com.google.gson.Gson
import hn.edu.ujcv.pdm_2021_ii_p3_proyecto3.GetAllActivity
import hn.edu.ujcv.pdm_2021_ii_p3_proyecto3.MenuPrincipal.MenuActivity
import hn.edu.ujcv.pdm_2021_ii_p3_proyecto3.R
import hn.edu.ujcv.pdm_2021_ii_p3_proyecto3.RestEngine
import hn.edu.ujcv.pdm_2021_ii_p3_proyecto3.Toolbar.MyToolbar
import hn.edu.ujcv.pdm_2021_ii_p3_proyecto3.entities.OrdenEncabezadoDataCollectionItem
import hn.edu.ujcv.pdm_2021_ii_p3_proyecto3.entities.RestApiError
import kotlinx.android.synthetic.main.activity_buscar_empleado.*
import kotlinx.android.synthetic.main.activity_buscar_orden_encabezado.*
import kotlinx.android.synthetic.main.activity_registro_orden_encabezado.*
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Buscar_OrdenEncabezado_Activity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_buscar_orden_encabezado)

        MyToolbar().show(this,"Buscar Orden Encabezado", false)
        btnBuscarOrdenEncabezado2.setOnClickListener { callServiceGetOrdenEncabezado() }
        btnEliminarOrdenEncabezado.setOnClickListener { callServiceDeleteOrdenEncabezado() }
        btnVerDetalle.setOnClickListener { verDetalle() }
        btnMostrarTodosOrdenes.setOnClickListener{ mostrarOrdenes() }
    }

    private fun mostrarOrdenes() {
        intent = Intent(this@Buscar_OrdenEncabezado_Activity, GetAllActivity::class.java)
        intent.putExtra("numero", 10)
        startActivity(intent)
    }

    private fun verDetalle() {
        val intent = Intent(this, Buscar_OrdenDetalle_Activity::class.java)
        intent.putExtra("ordenId", txvMostrarOrdenID.text.toString())
        startActivity(intent)
    }

    //-----
    //GET

    private fun callServiceGetOrdenEncabezado() {
        val ordenEncabezadoService:OrdenEncabezadoService = RestEngine.buildService().create(OrdenEncabezadoService::class.java)
        var result: Call<OrdenEncabezadoDataCollectionItem> = ordenEncabezadoService.getOrdenEncabezadoById(txvMostrarOrdenID.text.toString().toLong())

        result.enqueue(object : Callback<OrdenEncabezadoDataCollectionItem> {
            override fun onFailure(call: Call<OrdenEncabezadoDataCollectionItem>, t: Throwable) {
                Toast.makeText(this@Buscar_OrdenEncabezado_Activity,"Error al encontrar Orden Encabezado", Toast.LENGTH_SHORT).show()
            }

            override fun onResponse(
                call: Call<OrdenEncabezadoDataCollectionItem>,
                response: Response<OrdenEncabezadoDataCollectionItem>
            ) {
                if (response.code() == 404){
                    Toast.makeText(this@Buscar_OrdenEncabezado_Activity, "Orden Encabezado no existe",Toast.LENGTH_SHORT).show()
                }else if (response.code() == 500){
                    val errorResponse = Gson().fromJson(response.errorBody()!!.string()!!, RestApiError::class.java)

                    Toast.makeText(this@Buscar_OrdenEncabezado_Activity,errorResponse.errorDetails, Toast.LENGTH_SHORT).show()
                }else {
                    txvMostrarOrdenID.setText(response.body()!!.ordenId.toString())
                    txvMostrarOrdenEmpleadoID.setText(response.body()!!.empleadoId.toString())
                    txvMostrarClienteIDOrden.setText(response.body()!!.clienteId.toString())
                    txvMostrarFechaOrden.setText(response.body()!!.fechaOrden)
                    txvMostrarFechaEnvio.setText(response.body()!!.fechaEnvio)
                    txvMostrarDireccionEnvioOrden.setText(response.body()!!.direccionEnvio)
                    txvMostrarEstadoOrden.setText(response.body()!!.estado)
                    txvMostrarTotalOrden.setText(response.body()!!.total.toString())

                    Toast.makeText(this@Buscar_OrdenEncabezado_Activity,
                        "Orden Encabezado encontrado " + response.body()!!.ordenId,
                        Toast.LENGTH_SHORT).show()
                }
            }
        })
    }

    //DELETE

     private fun callServiceDeleteOrdenEncabezado() {
         val ordenEncabezadoService:OrdenEncabezadoService = RestEngine.buildService().create(OrdenEncabezadoService::class.java)
         var result: Call<ResponseBody> = ordenEncabezadoService.deleteOrdenEncabezado(txvMostrarOrdenID.text.toString().toLong())

         result.enqueue(object :  Callback<ResponseBody> {
             override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                 Toast.makeText(this@Buscar_OrdenEncabezado_Activity,"Error al eliminar el orden encabezado",Toast.LENGTH_SHORT).show()
             }

             override fun onResponse(
                 call: Call<ResponseBody>,
                 response: Response<ResponseBody>
             ) {
                 if (response.isSuccessful) {
                     Toast.makeText(this@Buscar_OrdenEncabezado_Activity,"Orden encabezado eliminada",Toast.LENGTH_SHORT).show()
                 }
                 else if (response.code() == 401){
                     Toast.makeText(this@Buscar_OrdenEncabezado_Activity,"Sesion expirada",Toast.LENGTH_SHORT).show()
                 }else{
                     Toast.makeText(this@Buscar_OrdenEncabezado_Activity,"Fallo al traer el item",Toast.LENGTH_SHORT).show()
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