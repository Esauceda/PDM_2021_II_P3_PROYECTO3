package hn.edu.ujcv.pdm_2021_ii_p3_proyecto3

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import hn.edu.ujcv.pdm_2021_ii_p3_proyecto3.Empleado.EmpleadoService
import hn.edu.ujcv.pdm_2021_ii_p3_proyecto3.Empleado.Registro_Empleado_Activity
import hn.edu.ujcv.pdm_2021_ii_p3_proyecto3.MenuPrincipal.MenuActivity
import hn.edu.ujcv.pdm_2021_ii_p3_proyecto3.entities.EmpleadoDataCollectionItem
import kotlinx.android.synthetic.main.activity_buscar_empleado.*
import kotlinx.android.synthetic.main.activity_login.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        btnLogin.setOnClickListener { IniciarSesion() }
        btnRegistrar.setOnClickListener { Registrar() }
    }

    private fun Registrar() {
        val intent = Intent(this, Registro_Empleado_Activity::class.java)
        startActivity(intent)
    }

    private fun IniciarSesion() {
            val empleadoService: EmpleadoService = RestEngine.buildService().create(EmpleadoService::class.java)
            var result: Call<EmpleadoDataCollectionItem> = empleadoService.getEmpleadoById(txtIdEmpleado.text.toString().toInt())

            result.enqueue(object : Callback<EmpleadoDataCollectionItem> {
                override fun onFailure(call: Call<EmpleadoDataCollectionItem>, t: Throwable) {
                    Toast.makeText(this@LoginActivity,"Error al iniciar secion", Toast.LENGTH_LONG).show()
                }

                override fun onResponse(
                    call: Call<EmpleadoDataCollectionItem>,
                    response: Response<EmpleadoDataCollectionItem>
                ) {
                    if (response.code() == 404){
                        Toast.makeText(this@LoginActivity, "Empleado no existe", Toast.LENGTH_SHORT).show()
                    }else {

                        if (response.body()!!.empleadoId.toString() == txtIdEmpleado.text.toString()
                            && response.body()!!.contrasena == txtContaseniaEmpleado.text.toString()){

                            Toast.makeText(this@LoginActivity, "Inicando Sesion",
                                Toast.LENGTH_LONG).show()

                            val intent = Intent(this@LoginActivity, MenuActivity::class.java)
                            startActivity(intent)

                        }else{
                            Toast.makeText(this@LoginActivity, "Credenciales invalidas", Toast.LENGTH_SHORT).show()
                        }


                    }
                }
            })

    }

}