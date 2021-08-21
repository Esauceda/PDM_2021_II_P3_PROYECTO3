package hn.edu.ujcv.pdm_2021_ii_p3_proyecto3.Empleado

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
import hn.edu.ujcv.pdm_2021_ii_p3_proyecto3.entities.EmpleadoDataCollectionItem
import kotlinx.android.synthetic.main.activity_actualizar_empleado.*
import kotlinx.android.synthetic.main.activity_buscar_empleado.*
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Buscar_Empleado_Activity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_buscar_empleado)
        btnBuscarFabrica.setOnClickListener { callServiceGetEmpleado() }
        btnEliminarEmpleado.setOnClickListener { callServiceDeleteEmpleado() }
        MyToolbar().show(this,"Buscar Empleado", false)
    }

    private fun callServiceGetEmpleado() {
        val empleadoService: EmpleadoService = RestEngine.buildService().create(EmpleadoService::class.java)
        var result: Call<EmpleadoDataCollectionItem> = empleadoService.getEmpleadoById(txvMostrarEmpleID2.text.toString().toInt())

        result.enqueue(object : Callback<EmpleadoDataCollectionItem> {
            override fun onFailure(call: Call<EmpleadoDataCollectionItem>, t: Throwable) {
                Toast.makeText(this@Buscar_Empleado_Activity,"Error", Toast.LENGTH_LONG).show()
            }

            override fun onResponse(
                call: Call<EmpleadoDataCollectionItem>,
                response: Response<EmpleadoDataCollectionItem>
            ) {
                if (response.code() == 404){
                    Toast.makeText(this@Buscar_Empleado_Activity, "Empleado no existe",Toast.LENGTH_SHORT).show()
                }else {
                    txvMostrarEmpleID2.setText(response.body()!!.empleadoId.toString())
                    txvMostrarEmpleNombre2.setText(response.body()!!.nombre)
                    txtMostrarDireccionEmple2.setText(response.body()!!.direccion)
                    txvMostrarTelEmple2.setText(response.body()!!.telefono.toString())
                    txvMostrarSalarioEmple2.setText(response.body()!!.salario.toString())
                    txvMostrarPuestoEmple2.setText(response.body()!!.puesto)
                    txvMostrarFechaContraEmple2.setText(response.body()!!.fechaContratacion.toString())
                    txvMostrarFechaNaciEmple2.setText(response.body()!!.fechaNacimiento.toString())
                    txvMostrarContraEmple2.setText(response.body()!!.contrasena)
                    Toast.makeText(this@Buscar_Empleado_Activity, "OK" + response.body()!!.nombre,
                        Toast.LENGTH_LONG).show()
                }
            }
        })
    }

    private fun callServiceDeleteEmpleado() {
        val empleadoService: EmpleadoService = RestEngine.buildService().create(
            EmpleadoService::class.java)
        var result: Call<ResponseBody> = empleadoService.deleteEmpleado(txvMostrarEmpleID2.text.toString().toInt())

        result.enqueue(object : Callback<ResponseBody> {
            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Toast.makeText(this@Buscar_Empleado_Activity,"Error", Toast.LENGTH_LONG).show()
            }

            override fun onResponse(
                call: Call<ResponseBody>,
                response: Response<ResponseBody>
            ) {
                if (response.isSuccessful) {
                    Toast.makeText(this@Buscar_Empleado_Activity,"DELETE", Toast.LENGTH_LONG).show()
                }
                else if (response.code() == 401){
                    Toast.makeText(this@Buscar_Empleado_Activity,"Sesion expirada", Toast.LENGTH_LONG).show()
                }
                else{
                    Toast.makeText(this@Buscar_Empleado_Activity,"Fallo al traer el item", Toast.LENGTH_LONG).show()
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
            startActivity(Intent(this, Actualizar_Empleado_Activity::class.java))
        if (item.itemId == R.id.option_two)
            startActivity(Intent(this, Buscar_Empleado_Activity::class.java))
        if (item.itemId == R.id.option_three)
            startActivity(Intent(this, MenuActivity::class.java))

        return super.onOptionsItemSelected(item)
    }
}