package hn.edu.ujcv.pdm_2021_ii_p3_proyecto3.Orden

import android.app.DatePickerDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.ArrayAdapter
import android.widget.Toast
import com.google.gson.Gson
import hn.edu.ujcv.pdm_2021_ii_p3_proyecto3.Cliente.ClienteService
import hn.edu.ujcv.pdm_2021_ii_p3_proyecto3.Empleado.EmpleadoService
import hn.edu.ujcv.pdm_2021_ii_p3_proyecto3.MenuPrincipal.MenuActivity
import hn.edu.ujcv.pdm_2021_ii_p3_proyecto3.R
import hn.edu.ujcv.pdm_2021_ii_p3_proyecto3.RestEngine
import hn.edu.ujcv.pdm_2021_ii_p3_proyecto3.Toolbar.MyToolbar
import hn.edu.ujcv.pdm_2021_ii_p3_proyecto3.entities.ClienteDataCollectionItem
import hn.edu.ujcv.pdm_2021_ii_p3_proyecto3.entities.EmpleadoDataCollectionItem
import hn.edu.ujcv.pdm_2021_ii_p3_proyecto3.entities.OrdenEncabezadoDataCollectionItem
import hn.edu.ujcv.pdm_2021_ii_p3_proyecto3.entities.RestApiError
import kotlinx.android.synthetic.main.activity_registro_compra_encabezado.*
import kotlinx.android.synthetic.main.activity_registro_orden_encabezado.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*
import kotlin.collections.ArrayList

class Registro_OrdenEncabezado_Activity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registro_orden_encabezado)

        MyToolbar().show(this,"Registrar Orden Encabezado", false)
        callServiceGetEmpleados()
        callServiceGetClientes()
        txtFechaOrden.setOnClickListener{calendario()}
        txtFechaEnvio.setOnClickListener{calendario2()}
        btnRegistrarOrdenEncabezado.setOnClickListener { callServicePostPerson() }
        btnBuscarOrdenEncabezado.setOnClickListener { callServiceGetOrdenEncabezado() }
        btnActualizarOrdenEncabezado.setOnClickListener { callServicePutOrdenEncabezado() }
    }
    private fun calendario() {
        val c = Calendar.getInstance()
        val date = Date()

        var año = c.get(Calendar.YEAR)
        var mes = c.get(Calendar.MONTH)
        var dia = c.get(Calendar.DAY_OF_MONTH)

        val dpd = DatePickerDialog(this, { view, Año, Mes, Dia ->
            txtFechaOrden.setText(""+Año+"-"+Mes+"-"+Dia)
            c.set(Año,(Mes+1),(Dia+3))
            dia = c.get(Calendar.DAY_OF_MONTH)
            var año = c.get(Calendar.YEAR)
            var mes = c.get(Calendar.MONTH) + 1
        },año,mes,dia)
        dpd.show()
    }

    private fun calendario2() {
        val c = Calendar.getInstance()
        val date = Date()

        var año = c.get(Calendar.YEAR)
        var mes = c.get(Calendar.MONTH)
        var dia = c.get(Calendar.DAY_OF_MONTH)

        val dpd = DatePickerDialog(this, { view, Año, Mes, Dia ->
            txtFechaEnvio.setText(""+Año+"-"+Mes+"-"+Dia)
            c.set(Año,(Mes+1),(Dia+3))
            dia = c.get(Calendar.DAY_OF_MONTH)
            var año = c.get(Calendar.YEAR)
            var mes = c.get(Calendar.MONTH) + 1
        },año,mes,dia)
        dpd.show()
    }
        var empleados = ArrayList<String>()
        val clientes = ArrayList<String>()
    //-----

    //POST
   private fun callServicePostPerson() {
        val fecha = "2021-04-10"
        val ordenEncabezadoInfo = OrdenEncabezadoDataCollectionItem(
            ordenId         = txtOrdenID.text.toString().toInt(),
            empleadoId      = spOrdenEmpleadoID.selectedItem.toString().toInt(),
            clienteId       = spClienteIDOrden.selectedItem.toString().toInt(),
            fechaOrden      = txtFechaOrden.text.toString(),
            fechaEnvio      = txtFechaEnvio.text.toString(),
            direccionEnvio  = txtDireccionEnvioOrden.text.toString(),
            estado          = txtEstadoOrden.text.toString(),
            total           = txtTotalOrden.text.toString().toDouble()
        )

        addOrdenEncabezado(ordenEncabezadoInfo) {
            if (it?.ordenId!= null) {
                Toast.makeText(this@Registro_OrdenEncabezado_Activity,"Orden Registrada", Toast.LENGTH_LONG).show()

                val intent = Intent(this, Registro_OrdenDetalle_Activity::class.java)
                startActivity(intent)
            } else {
                Toast.makeText(this@Registro_OrdenEncabezado_Activity,"Error al registrar", Toast.LENGTH_LONG).show()
            }
        }
    }

    fun addOrdenEncabezado(ordenEncabezadoData: OrdenEncabezadoDataCollectionItem, onResult: (OrdenEncabezadoDataCollectionItem?) -> Unit){
        val retrofit = RestEngine.buildService().create(OrdenEncabezadoService::class.java)
        var result: Call<OrdenEncabezadoDataCollectionItem> = retrofit.addOrdenEncabezado(ordenEncabezadoData)

        result.enqueue(object : Callback<OrdenEncabezadoDataCollectionItem> {
            override fun onFailure(call: Call<OrdenEncabezadoDataCollectionItem>, t: Throwable) {
                onResult(null)
            }

            override fun onResponse(call: Call<OrdenEncabezadoDataCollectionItem>,
                                    response: Response<OrdenEncabezadoDataCollectionItem>
            ) {
                if (response.isSuccessful) {
                    val addedPerson = response.body()!!
                    onResult(addedPerson)
                }
                else if (response.code() == 401){
                    Toast.makeText(this@Registro_OrdenEncabezado_Activity,"Sesion expirada",Toast.LENGTH_LONG).show()
                }
                else if (response.code() == 500){

                    val errorResponse = Gson().fromJson(response.errorBody()!!.string()!!, RestApiError::class.java)

                    Toast.makeText(this@Registro_OrdenEncabezado_Activity,errorResponse.errorDetails, Toast.LENGTH_LONG).show()
                }
                else{
                    Toast.makeText(this@Registro_OrdenEncabezado_Activity,"Fallo al traer el item", Toast.LENGTH_LONG).show()
                }
            }

        }
        )
    }

    //PUT

    private fun callServicePutOrdenEncabezado() {
        val fecha = "2021-04-11"
        val ordenEncabezadoInfo = OrdenEncabezadoDataCollectionItem(
            ordenId         = txtOrdenID.text.toString().toInt(),
            empleadoId      = spOrdenEmpleadoID.selectedItem.toString().toInt(),
            clienteId       = spClienteIDOrden.selectedItem.toString().toInt(),
            fechaOrden      = txtFechaOrden.text.toString(),
            fechaEnvio      = txtFechaEnvio.text.toString(),
            direccionEnvio  = txtDireccionEnvioOrden.text.toString(),
            estado          = txtEstadoOrden.text.toString(),
            total           = txtTotalOrden.text.toString().toDouble()
        )

        val retrofit = RestEngine.buildService().create(OrdenEncabezadoService::class.java)
        var result: Call<OrdenEncabezadoDataCollectionItem> = retrofit.updateOrdenEncabezado(ordenEncabezadoInfo)

        result.enqueue(object : Callback<OrdenEncabezadoDataCollectionItem> {
            override fun onFailure(call: Call<OrdenEncabezadoDataCollectionItem>, t: Throwable) {
                Toast.makeText(this@Registro_OrdenEncabezado_Activity,"Error",Toast.LENGTH_LONG).show()
            }

            override fun onResponse(call: Call<OrdenEncabezadoDataCollectionItem>,
                                    response: Response<OrdenEncabezadoDataCollectionItem>) {
                if (response.isSuccessful) {
                    val updatedPerson = response.body()!!
                    Toast.makeText(this@Registro_OrdenEncabezado_Activity,"Orden Encabezado actualizado",Toast.LENGTH_LONG).show()
                }
                else if (response.code() == 401){
                    Toast.makeText(this@Registro_OrdenEncabezado_Activity,"Sesion expirada",Toast.LENGTH_LONG).show()
                }/*else if (response.code() == 500){

                    val errorResponse = Gson().fromJson(response.errorBody()!!.string()!!, RestApiError::class.java)

                    Toast.makeText(this@Registro_OrdenEncabezado_Activity,errorResponse.errorDetails, Toast.LENGTH_LONG).show()
                }*/else{
                    Toast.makeText(this@Registro_OrdenEncabezado_Activity,"Fallo al traer el item",Toast.LENGTH_LONG).show()
                }
            }

        })
    }

    //GET

    private fun callServiceGetOrdenEncabezado() {
        val ordenEncabezadoService:OrdenEncabezadoService = RestEngine.buildService().create(OrdenEncabezadoService::class.java)
        var result: Call<OrdenEncabezadoDataCollectionItem> = ordenEncabezadoService.getOrdenEncabezadoById(txtOrdenID.text.toString().toLong())
        var contadorEmpleados = 0
        var contadorClientes  = 0

        result.enqueue(object :  Callback<OrdenEncabezadoDataCollectionItem> {
            override fun onFailure(call: Call<OrdenEncabezadoDataCollectionItem>, t: Throwable) {
                Toast.makeText(this@Registro_OrdenEncabezado_Activity,"Error",Toast.LENGTH_LONG).show()
            }

            override fun onResponse(
                call: Call<OrdenEncabezadoDataCollectionItem>,
                response: Response<OrdenEncabezadoDataCollectionItem>
            ) {

                if (response.code() == 404){
                    Toast.makeText(this@Registro_OrdenEncabezado_Activity, "Orden no existe",Toast.LENGTH_SHORT).show()
                }else if (response.code() == 500){
                    val errorResponse = Gson().fromJson(response.errorBody()!!.string()!!, RestApiError::class.java)

                    Toast.makeText(this@Registro_OrdenEncabezado_Activity,errorResponse.errorDetails, Toast.LENGTH_LONG).show()
                }else{
                    txtOrdenID.setText(response.body()!!.ordenId.toString())

                    for (item in empleados){
                        if (item == response.body()!!.empleadoId.toString()){
                            spOrdenEmpleadoID.setSelection(contadorEmpleados)
                        }
                        contadorEmpleados++
                    }

                    for (item in clientes){
                        if (item == response.body()!!.clienteId.toString()){
                            spClienteIDOrden.setSelection(contadorClientes)
                        }
                        contadorClientes++
                    }

                    txtFechaOrden.setText(response.body()!!.fechaOrden)
                    txtFechaEnvio.setText(response.body()!!.fechaEnvio)
                    txtDireccionEnvioOrden.setText(response.body()!!.direccionEnvio)
                    txtEstadoOrden.setText(response.body()!!.estado)
                    txtTotalOrden.setText(response.body()!!.total.toString())

                    Toast.makeText(this@Registro_OrdenEncabezado_Activity,
                        "Orden Encabezado encontrado "+response.body()!!.ordenId,Toast.LENGTH_LONG).show()
                }

            }
        })
    }

    //GETS
    private fun callServiceGetOrdenesEncabezado() {
        val ordenEncabezadoService: OrdenEncabezadoService = RestEngine.buildService().create(
            OrdenEncabezadoService::class.java)
        var result: Call<List<OrdenEncabezadoDataCollectionItem>> = ordenEncabezadoService.listOrdenesEncabezado()
        val ordenes = ArrayList<String>()

        result.enqueue(object :  Callback<List<OrdenEncabezadoDataCollectionItem>> {
            override fun onFailure(call: Call<List<OrdenEncabezadoDataCollectionItem>>, t: Throwable) {
                Toast.makeText(this@Registro_OrdenEncabezado_Activity,"Error al encontrar ordenes",Toast.LENGTH_LONG).show()
            }

            override fun onResponse(
                call: Call<List<OrdenEncabezadoDataCollectionItem>>,
                response: Response<List<OrdenEncabezadoDataCollectionItem>>
            ) {
                Toast.makeText(this@Registro_OrdenEncabezado_Activity,"Ordenes encontradas",Toast.LENGTH_LONG).show()
            }
        })
    }


    //GETSEMPLEADO

    private fun callServiceGetEmpleados() {
        val empleadoService:EmpleadoService = RestEngine.buildService().create(EmpleadoService::class.java)
        var result: Call<List<EmpleadoDataCollectionItem>> = empleadoService.listEmpleado()


        result.enqueue(object :  Callback<List<EmpleadoDataCollectionItem>> {
            override fun onFailure(call: Call<List<EmpleadoDataCollectionItem>>, t: Throwable) {
                Toast.makeText(this@Registro_OrdenEncabezado_Activity,"Error al encontrar empleados",Toast.LENGTH_LONG).show()
            }

            override fun onResponse(
                call: Call<List<EmpleadoDataCollectionItem>>,
                response: Response<List<EmpleadoDataCollectionItem>>
            ) {
                for (empleado in response.body()!!){
                    empleados.add("${empleado.empleadoId}")
                }

                val adapterEmpleados = ArrayAdapter(this@Registro_OrdenEncabezado_Activity, android.R.layout.simple_spinner_item, empleados)
                spOrdenEmpleadoID.adapter = adapterEmpleados

                Toast.makeText(this@Registro_OrdenEncabezado_Activity,"Empelados encontrados",Toast.LENGTH_LONG).show()
            }
        })
    }

    //GETSCLIENTES

    private fun callServiceGetClientes() {
        val clienteService:ClienteService = RestEngine.buildService().create(ClienteService::class.java)
        var result: Call<List<ClienteDataCollectionItem>> = clienteService.listCliente()

        result.enqueue(object :  Callback<List<ClienteDataCollectionItem>> {
            override fun onFailure(call: Call<List<ClienteDataCollectionItem>>, t: Throwable) {
                Toast.makeText(this@Registro_OrdenEncabezado_Activity,"Error al encontrar clientes",Toast.LENGTH_LONG).show()
            }

            override fun onResponse(
                call: Call<List<ClienteDataCollectionItem>>,
                response: Response<List<ClienteDataCollectionItem>>
            ) {

                for (cliente in response.body()!!){
                    clientes.add("${cliente.clienteId}")
                }

                val adapterEmpleados = ArrayAdapter(this@Registro_OrdenEncabezado_Activity, android.R.layout.simple_spinner_item, clientes)
                spClienteIDOrden.adapter = adapterEmpleados
                Toast.makeText(this@Registro_OrdenEncabezado_Activity,"Clientes encontrados",Toast.LENGTH_LONG).show()
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