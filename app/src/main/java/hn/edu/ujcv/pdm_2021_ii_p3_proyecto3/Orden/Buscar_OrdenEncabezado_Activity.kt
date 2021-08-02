package hn.edu.ujcv.pdm_2021_ii_p3_proyecto3.Orden

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import hn.edu.ujcv.pdm_2021_ii_p3_proyecto3.MenuPrincipal.MenuActivity
import hn.edu.ujcv.pdm_2021_ii_p3_proyecto3.R
import hn.edu.ujcv.pdm_2021_ii_p3_proyecto3.Toolbar.MyToolbar

class Buscar_OrdenEncabezado_Activity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_buscar_orden_encabezado)

        MyToolbar().show(this,"Buscar Orden Encabezado", false)
    }

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