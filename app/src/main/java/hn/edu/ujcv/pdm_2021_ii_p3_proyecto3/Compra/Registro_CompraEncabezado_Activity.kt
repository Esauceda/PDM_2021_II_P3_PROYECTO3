package hn.edu.ujcv.pdm_2021_ii_p3_proyecto3.Compra

import android.app.DatePickerDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.ArrayAdapter
import android.widget.Toast
import com.google.gson.Gson
import hn.edu.ujcv.pdm_2021_ii_p3_proyecto3.Empleado.EmpleadoService
import hn.edu.ujcv.pdm_2021_ii_p3_proyecto3.MenuPrincipal.MenuActivity
import hn.edu.ujcv.pdm_2021_ii_p3_proyecto3.Orden.Registro_OrdenDetalle_Activity
import hn.edu.ujcv.pdm_2021_ii_p3_proyecto3.Proveedores.ProveedorService
import hn.edu.ujcv.pdm_2021_ii_p3_proyecto3.R
import hn.edu.ujcv.pdm_2021_ii_p3_proyecto3.RestEngine
import hn.edu.ujcv.pdm_2021_ii_p3_proyecto3.Toolbar.MyToolbar
import hn.edu.ujcv.pdm_2021_ii_p3_proyecto3.entities.CompraEncabezadoDataCollectionItem
import hn.edu.ujcv.pdm_2021_ii_p3_proyecto3.entities.EmpleadoDataCollectionItem
import hn.edu.ujcv.pdm_2021_ii_p3_proyecto3.entities.ProveedorDataCollectionItem
import hn.edu.ujcv.pdm_2021_ii_p3_proyecto3.entities.RestApiError
import kotlinx.android.synthetic.main.activity_registro_compra_encabezado.*
import kotlinx.android.synthetic.main.activity_registro_empleado.*
import kotlinx.android.synthetic.main.activity_registro_orden_encabezado.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*
import kotlin.collections.ArrayList

class Registro_CompraEncabezado_Activity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registro_compra_encabezado)
        MyToolbar().show(this,"Registro Compra Encabezado", false)
        txtFechaCompra.setOnClickListener{ calendario() }
        txtFechaRecepcion.setOnClickListener{calendario2()}
        callServiceGetEmpleados()
        callServiceGetProveedores()
        btnRegistrarCompraEncabezado.setOnClickListener { callServicePostPerson() }
        btnActualizarCompraEncabezado.setOnClickListener { callServicePutPerson() }
        btnBuscarCompraEncabezado.setOnClickListener { callServiceGetCompraEncabezado() }

    }
    private fun calendario() {
        val c = Calendar.getInstance()
        val date = Date()

        var año = c.get(Calendar.YEAR)
        var mes = c.get(Calendar.MONTH)
        var dia = c.get(Calendar.DAY_OF_MONTH)

        val dpd = DatePickerDialog(this, { view, Año, Mes, Dia ->
            txtFechaCompra.setText(""+Año+"-"+Mes+"-"+Dia)
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
            txtFechaRecepcion.setText(""+Año+"-"+Mes+"-"+Dia)
            c.set(Año,(Mes+1),(Dia+3))
            dia = c.get(Calendar.DAY_OF_MONTH)
            var año = c.get(Calendar.YEAR)
            var mes = c.get(Calendar.MONTH) + 1
        },año,mes,dia)
        dpd.show()
    }
    var empleados = ArrayList<String>()
    var proveedores = ArrayList<String>()
    //-----

    private fun callServicePostPerson() {
        val compraEncabezadoInfo = CompraEncabezadoDataCollectionItem(
            compraId = null,
            proveedorId = sProveedorID.selectedItem.toString().toLong(),
            empleadoId = sEmpleadoID.selectedItem.toString().toLong(),
            fechaCompra = txtFechaCompra.text.toString(),
            total = txtTotalCompra.text.toString().toDouble(),
            estado = txtEstadoCompra.text.toString(),
            fechaRecepcion = txtFechaRecepcion.text.toString()
        )
        addCompraEncabezado(compraEncabezadoInfo) {
            if (it?.compraId != null) {
                Toast.makeText(this@Registro_CompraEncabezado_Activity,"OK"+it?.compraId,Toast.LENGTH_LONG).show()
                val intent = Intent(this, Registro_CompraDetalle_Activity::class.java)
                startActivity(intent)
            } else {
                Toast.makeText(this@Registro_CompraEncabezado_Activity,"Error",Toast.LENGTH_LONG).show()
            }
        }
    }

    fun addCompraEncabezado(personData: CompraEncabezadoDataCollectionItem, onResult: (CompraEncabezadoDataCollectionItem?) -> Unit){
        val retrofit = RestEngine.buildService().create(CompraEncabezadoService::class.java)
        var result: Call<CompraEncabezadoDataCollectionItem> = retrofit.addCompraEncabezado(personData)

        result.enqueue(object : Callback<CompraEncabezadoDataCollectionItem> {
            override fun onFailure(call: Call<CompraEncabezadoDataCollectionItem>, t: Throwable) {
                onResult(null)
            }

            override fun onResponse(call: Call<CompraEncabezadoDataCollectionItem>, response: Response<CompraEncabezadoDataCollectionItem>) {
                if (response.isSuccessful) {
                    val addedPerson = response.body()!!
                    onResult(addedPerson)
                }
                else if (response.code() == 401){
                    Toast.makeText(this@Registro_CompraEncabezado_Activity,"Sesion expirada",Toast.LENGTH_LONG).show()
                } else if (response.code() == 500){
                    val errorResponse = Gson().fromJson(response.errorBody()!!.string()!!, RestApiError::class.java)
                    Toast.makeText(this@Registro_CompraEncabezado_Activity,errorResponse.errorDetails, Toast.LENGTH_LONG).show()
                }
                else{
                    Toast.makeText(this@Registro_CompraEncabezado_Activity,"Fallo al traer el item", Toast.LENGTH_LONG).show()
                }
            }
        }
        )
    }

    private fun callServicePutPerson() {
        val compraEncabezadoInfo = CompraEncabezadoDataCollectionItem(
            compraId = txtCompraEncabezadoID.text.toString().toLong(),
            proveedorId = sProveedorID.selectedItem.toString().toLong(),
            empleadoId = sEmpleadoID.selectedItem.toString().toLong(),
            fechaCompra = txtFechaCompra.text.toString(),
            total = txtTotalCompra.text.toString().toDouble(),
            estado = txtEstadoCompra.text.toString(),
            fechaRecepcion = txtFechaRecepcion.text.toString()
        )

        val retrofit = RestEngine.buildService().create(CompraEncabezadoService::class.java)
        var result: Call<CompraEncabezadoDataCollectionItem> = retrofit.updateCompraEncabezado(compraEncabezadoInfo)

        result.enqueue(object : Callback<CompraEncabezadoDataCollectionItem> {
            override fun onFailure(call: Call<CompraEncabezadoDataCollectionItem>, t: Throwable) {
                Toast.makeText(this@Registro_CompraEncabezado_Activity,"Error",Toast.LENGTH_LONG).show()
            }

            override fun onResponse(call: Call<CompraEncabezadoDataCollectionItem>, response: Response<CompraEncabezadoDataCollectionItem>) {
                if (response.isSuccessful) {
                    val updatedPerson = response.body()!!
                    Toast.makeText(this@Registro_CompraEncabezado_Activity,"OK"+response.body()!!.compraId,Toast.LENGTH_LONG).show()
                }
                else if (response.code() == 401){
                    Toast.makeText(this@Registro_CompraEncabezado_Activity,"Sesion expirada",Toast.LENGTH_LONG).show()
                } else {
                    Toast.makeText(this@Registro_CompraEncabezado_Activity,"Fallo al traer el item", Toast.LENGTH_LONG).show()
                }/*if (response.code() == 500){
                    val errorResponse = Gson().fromJson(response.errorBody()!!.string()!!, RestApiError::class.java)
                    Toast.makeText(this@Registro_CompraEncabezado_Activity,errorResponse.errorDetails, Toast.LENGTH_LONG).show()
                }*/
            }
        })
    }

    private fun callServiceGetCompraEncabezado() {
        val compraEncabezadoService:CompraEncabezadoService = RestEngine.buildService().create(CompraEncabezadoService::class.java)
        var result: Call<CompraEncabezadoDataCollectionItem> = compraEncabezadoService.getCompraEncabezadoById(txtCompraEncabezadoID.text.toString().toLong())
        var contadorEmpleados = 0
        var contadorProveedor = 0

        result.enqueue(object :  Callback<CompraEncabezadoDataCollectionItem> {
            override fun onFailure(call: Call<CompraEncabezadoDataCollectionItem>, t: Throwable) {
                Toast.makeText(this@Registro_CompraEncabezado_Activity,"Error",Toast.LENGTH_LONG).show()
            }
            override fun onResponse(
                call: Call<CompraEncabezadoDataCollectionItem>,
                response: Response<CompraEncabezadoDataCollectionItem>
            ) {
                if (response.code() == 404){
                    Toast.makeText(this@Registro_CompraEncabezado_Activity,"No existe compra encabezado con este id",Toast.LENGTH_LONG).show()
                }else{
                    for (item in empleados){
                        if (item == response.body()!!.empleadoId.toString()){
                            sEmpleadoID.setSelection(contadorEmpleados)
                        }
                        contadorEmpleados++
                    }
                    for (item in proveedores){
                        if (item == response.body()!!.proveedorId.toString()){
                            sEmpleadoID.setSelection(contadorProveedor)
                        }
                        contadorProveedor++
                    }
                    txtCompraEncabezadoID.setText(response.body()!!.compraId.toString())
                    txtFechaCompra.setText(response.body()!!.fechaCompra)
                    txtFechaRecepcion.setText(response.body()!!.fechaRecepcion)
                    txtTotalCompra.setText(response.body()!!.total.toString())
                    txtEstadoCompra.setText(response.body()!!.estado)
                    Toast.makeText(this@Registro_CompraEncabezado_Activity,"OK"+response.body()!!.compraId,Toast.LENGTH_LONG).show()
                }

            }
        })
    }


    private fun callServiceGetEmpleados() {
        val empleadoService: EmpleadoService = RestEngine.buildService().create(EmpleadoService::class.java)
        var result: Call<List<EmpleadoDataCollectionItem>> = empleadoService.listEmpleado()


        result.enqueue(object :  Callback<List<EmpleadoDataCollectionItem>> {
            override fun onFailure(call: Call<List<EmpleadoDataCollectionItem>>, t: Throwable) {
                Toast.makeText(this@Registro_CompraEncabezado_Activity,"Error al encontrar empleados",Toast.LENGTH_LONG).show()
            }

            override fun onResponse(
                call: Call<List<EmpleadoDataCollectionItem>>,
                response: Response<List<EmpleadoDataCollectionItem>>
            ) {
                for (empleado in response.body()!!){
                    empleados.add("${empleado.empleadoId}")
                }

                val adapterEmpleados = ArrayAdapter(this@Registro_CompraEncabezado_Activity, android.R.layout.simple_spinner_item, empleados)
                sEmpleadoID.adapter = adapterEmpleados

                Toast.makeText(this@Registro_CompraEncabezado_Activity,"Empelados encontrados",Toast.LENGTH_LONG).show()
            }
        })
    }

    private fun callServiceGetProveedores() {
        val proveedorService: ProveedorService = RestEngine.buildService().create(ProveedorService::class.java)
        var result: Call<List<ProveedorDataCollectionItem>> = proveedorService.listProveedor()


        result.enqueue(object :  Callback<List<ProveedorDataCollectionItem>> {
            override fun onFailure(call: Call<List<ProveedorDataCollectionItem>>, t: Throwable) {
                Toast.makeText(this@Registro_CompraEncabezado_Activity,"Error al encontrar empleados",Toast.LENGTH_LONG).show()
            }

            override fun onResponse(
                call: Call<List<ProveedorDataCollectionItem>>,
                response: Response<List<ProveedorDataCollectionItem>>
            ) {
                for (proveedor in response.body()!!){
                    proveedores.add("${proveedor.proveedorId}")
                }
                val adapterProveedor = ArrayAdapter(this@Registro_CompraEncabezado_Activity, android.R.layout.simple_spinner_item, proveedores)
                sProveedorID.adapter = adapterProveedor

                Toast.makeText(this@Registro_CompraEncabezado_Activity,"Empelados encontrados",Toast.LENGTH_LONG).show()
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