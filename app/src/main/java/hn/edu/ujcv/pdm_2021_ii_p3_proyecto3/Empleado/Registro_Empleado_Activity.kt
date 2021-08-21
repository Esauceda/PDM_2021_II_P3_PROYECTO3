package hn.edu.ujcv.pdm_2021_ii_p3_proyecto3.Empleado

import android.app.DatePickerDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.gson.Gson
import hn.edu.ujcv.pdm_2021_ii_p3_proyecto3.R
import hn.edu.ujcv.pdm_2021_ii_p3_proyecto3.RestEngine
import hn.edu.ujcv.pdm_2021_ii_p3_proyecto3.entities.EmpleadoDataCollectionItem
import hn.edu.ujcv.pdm_2021_ii_p3_proyecto3.entities.RestApiError
import kotlinx.android.synthetic.main.activity_registro_empleado.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*

class Registro_Empleado_Activity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registro_empleado)
        btnGuardarEmpleado.setOnClickListener { callServicePostEmpleado() }
        txtFechaNaciEmple.setOnClickListener{ calendario() }
        txtFechaContraEmple.setOnClickListener{ calendario2() }
    }

    private fun calendario() {
        val c = Calendar.getInstance()
        val date = Date()

        var año = c.get(Calendar.YEAR)
        var mes = c.get(Calendar.MONTH)
        var dia = c.get(Calendar.DAY_OF_MONTH)

        val dpd = DatePickerDialog(this, { view, Año, Mes, Dia ->
            txtFechaNaciEmple.setText(""+Año+"-"+Mes+"-"+Dia)
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
            txtFechaContraEmple.setText(""+Año+"-"+Mes+"-"+Dia)
            c.set(Año,(Mes+1),(Dia+3))
            dia = c.get(Calendar.DAY_OF_MONTH)
            var año = c.get(Calendar.YEAR)
            var mes = c.get(Calendar.MONTH) + 1
        },año,mes,dia)
        dpd.show()
    }

    private fun callServicePostEmpleado() {
        val empleadoInfo = EmpleadoDataCollectionItem(
            empleadoId =        txtEmpleID.text.toString().toInt(),
            nombre =            txtEmpleNombre.text.toString(),
            direccion =         txtDireccionEmple.text.toString(),
            telefono =          txtTelEmple.text.toString().toInt(),
            salario =           txtSalarioEmple.text.toString().toDouble(),
            puesto =            txtPuestoEmple.text.toString(),
            fechaNacimiento =   txtFechaNaciEmple.text.toString(),
            fechaContratacion = txtFechaContraEmple.text.toString(),
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
                else if (response.code() == 401){
                    Toast.makeText(this@Registro_Empleado_Activity,"Sesion expirada",Toast.LENGTH_LONG).show()
                }
                else if (response.code() == 500){

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

    fun String.toDate(format: String, locale: Locale = Locale.getDefault()): Date = SimpleDateFormat(format, locale).parse(this)
}