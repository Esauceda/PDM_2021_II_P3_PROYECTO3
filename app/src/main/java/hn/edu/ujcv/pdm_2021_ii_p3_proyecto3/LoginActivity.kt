package hn.edu.ujcv.pdm_2021_ii_p3_proyecto3

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import hn.edu.ujcv.pdm_2021_ii_p3_proyecto3.Empleado.Registro_Empleado_Activity
import hn.edu.ujcv.pdm_2021_ii_p3_proyecto3.MenuPrincipal.MenuActivity
import kotlinx.android.synthetic.main.activity_login.*

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
        val intent = Intent(this, MenuActivity::class.java)
        startActivity(intent)
    }

}