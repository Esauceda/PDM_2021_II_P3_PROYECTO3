package hn.edu.ujcv.pdm_2021_ii_p3_proyecto3.Almacen

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import hn.edu.ujcv.pdm_2021_ii_p3_proyecto3.MenuPrincipal.MenuActivity
import hn.edu.ujcv.pdm_2021_ii_p3_proyecto3.R
import hn.edu.ujcv.pdm_2021_ii_p3_proyecto3.Toolbar.MyToolbar

class Registro_Almacen_Activity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activiry_registro_almacen)


        MyToolbar().show(this,"Registro Almacen", false)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_contextual, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        if (item.itemId == R.id.option_one)
            startActivity(Intent(this,Registro_Almacen_Activity::class.java))
        if (item.itemId == R.id.option_two)
            startActivity(Intent(this,Buscar_Almacen_Activity::class.java))
        if (item.itemId == R.id.option_three)
            startActivity(Intent(this,MenuActivity::class.java))

        return super.onOptionsItemSelected(item)
    }
}