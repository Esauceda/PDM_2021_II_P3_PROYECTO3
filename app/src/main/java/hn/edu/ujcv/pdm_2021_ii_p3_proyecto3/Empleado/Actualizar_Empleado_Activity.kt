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
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Actualizar_Empleado_Activity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_actualizar_empleado)
        btnGuardarEmpleado.setOnClickListener { callServicePutEmpleado() }
        btnBuscarEmpleado.setOnClickListener { callServiceGetEmpleado() }
        MyToolbar().show(this,"Actualizar Empleado", false)
    }

    private fun callServicePutEmpleado() {
        val empleadoInfo = EmpleadoDataCollectionItem(
            empleadoId =        txtEmpleID.text.toString().toInt(),
            nombre =            txtEmpleNombre.text.toString(),
            direccion =         txtDireccionEmple.text.toString(),
            telefono =          txtTelEmple.text.toString().toInt(),
            salario =           txtSalarioEmple.text.toString().toDouble(),
            puesto =            txtPuestoEmple.text.toString(),
            fechaNacimiento =   txtFechaContraEmple.text.toString(),
            fechaContratacion = txtFechaNaciEmple.text.toString(),
            contrasena =        txtContraEmple.text.toString()
        )

        val retrofit = RestEngine.buildService().create(EmpleadoService::class.java)
        var result: Call<EmpleadoDataCollectionItem> = retrofit.updateEmpleado(empleadoInfo)

        result.enqueue(object : Callback<EmpleadoDataCollectionItem> {
            override fun onFailure(call: Call<EmpleadoDataCollectionItem>, t: Throwable) {
                Toast.makeText(this@Actualizar_Empleado_Activity,"Error", Toast.LENGTH_LONG).show()
            }

            override fun onResponse(call: Call<EmpleadoDataCollectionItem>,
                                    response: Response<EmpleadoDataCollectionItem>
            ) {
                if (response.isSuccessful) {
                    val updatedEmpleado = response.body()!!
                    Toast.makeText(this@Actualizar_Empleado_Activity,"OK"+response.body()!!.nombre,
                        Toast.LENGTH_LONG).show()
                }
                else if (response.code() == 401){
                    Toast.makeText(this@Actualizar_Empleado_Activity,"Sesion expirada", Toast.LENGTH_LONG).show()
                }
                else{
                    Toast.makeText(this@Actualizar_Empleado_Activity,"Fallo al traer el item", Toast.LENGTH_LONG).show()
                }
            }

        })
    }

    private fun callServiceGetEmpleado() {
        val empleadoService: EmpleadoService = RestEngine.buildService().create(EmpleadoService::class.java)
        var result: Call<EmpleadoDataCollectionItem> = empleadoService.getEmpleadoById(txtEmpleID.text.toString().toInt())

        result.enqueue(object : Callback<EmpleadoDataCollectionItem> {
            override fun onFailure(call: Call<EmpleadoDataCollectionItem>, t: Throwable) {
                Toast.makeText(this@Actualizar_Empleado_Activity,"Error", Toast.LENGTH_LONG).show()
            }

            override fun onResponse(
                call: Call<EmpleadoDataCollectionItem>,
                response: Response<EmpleadoDataCollectionItem>
            ) {
                txtEmpleID.setText(response.body()!!.empleadoId.toString())
                txtEmpleNombre.setText(response.body()!!.nombre)
                txtDireccionEmple.setText(response.body()!!.direccion)
                txtTelEmple.setText(response.body()!!.telefono)
                txtSalarioEmple.setText(response.body()!!.salario.toString())
                txtPuestoEmple.setText(response.body()!!.puesto)
                txtFechaContraEmple.setText(response.body()!!.fechaContratacion)
                txtFechaNaciEmple.setText(response.body()!!.fechaNacimiento)
                txtContraEmple.setText(response.body()!!.contrasena)
                Toast.makeText(this@Actualizar_Empleado_Activity,"OK"+response.body()!!.nombre,
                    Toast.LENGTH_LONG).show()
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