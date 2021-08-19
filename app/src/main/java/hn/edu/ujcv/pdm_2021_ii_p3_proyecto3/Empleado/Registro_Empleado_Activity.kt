package hn.edu.ujcv.pdm_2021_ii_p3_proyecto3.Empleado

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.gson.Gson
import hn.edu.ujcv.pdm_2021_ii_p3_proyecto3.R
import hn.edu.ujcv.pdm_2021_ii_p3_proyecto3.RestEngine
import hn.edu.ujcv.pdm_2021_ii_p3_proyecto3.entities.EmpleadoDataCollectionItem
import hn.edu.ujcv.pdm_2021_ii_p3_proyecto3.entities.RestApiError
import kotlinx.android.synthetic.main.activity_actualizar_empleado.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Registro_Empleado_Activity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registro_empleado)
        btnActualizarEmpleado.setOnClickListener { callServicePostEmpleado() }
    }

    private fun callServicePostEmpleado() {
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

        addEmpleado(empleadoInfo) {
            if (it?.empleadoId != null) {
                Toast.makeText(this@Registro_Empleado_Activity,"OK"+it?.empleadoId, Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(this@Registro_Empleado_Activity,"Error", Toast.LENGTH_LONG).show()
            }
        }
    }

    fun addEmpleado(empleadoData: EmpleadoDataCollectionItem, onResult: (EmpleadoDataCollectionItem?) -> Unit){
        val retrofit = RestEngine.buildService().create(EmpleadoService::class.java)
        var result: Call<EmpleadoDataCollectionItem> = retrofit.addEmpleado(empleadoData)

        result.enqueue(object : Callback<EmpleadoDataCollectionItem> {
            override fun onFailure(call: Call<EmpleadoDataCollectionItem>, t: Throwable) {
                onResult(null)
            }

            override fun onResponse(call: Call<EmpleadoDataCollectionItem>,
                                    response: Response<EmpleadoDataCollectionItem>
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

                    Toast.makeText(this@Registro_Empleado_Activity,errorResponse.errorDetails, Toast.LENGTH_LONG).show()
                }
                else{
                    Toast.makeText(this@Registro_Empleado_Activity,"Fallo al traer el item", Toast.LENGTH_LONG).show()
                }
            }

        }
        )
    }
}