package hn.edu.ujcv.pdm_2021_ii_p3_proyecto3.Almacen

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import hn.edu.ujcv.pdm_2021_ii_p3_proyecto3.GetAllActivity
import hn.edu.ujcv.pdm_2021_ii_p3_proyecto3.MenuPrincipal.MenuActivity
import hn.edu.ujcv.pdm_2021_ii_p3_proyecto3.R
import hn.edu.ujcv.pdm_2021_ii_p3_proyecto3.RestEngine
import hn.edu.ujcv.pdm_2021_ii_p3_proyecto3.Toolbar.MyToolbar
import hn.edu.ujcv.pdm_2021_ii_p3_proyecto3.entities.AlmacenDataCollectionItem
import kotlinx.android.synthetic.main.activity_buscar_almacen.*
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Buscar_Almacen_Activity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_buscar_almacen)
        btnEliminarAlmacen.setOnClickListener { callServiceDeleteAlmacen() }
        btnBuscarAlmacen2.setOnClickListener { callServiceGetAlmacen() }
        btnMostrarTodosAlmacenes.setOnClickListener{ mostrarAlmacenes() }

        MyToolbar().show(this,"Buscar Almacen", false)
    }

    private fun mostrarAlmacenes() {
        intent = Intent(this@Buscar_Almacen_Activity, GetAllActivity::class.java)
        intent.putExtra("numero", 1)
        startActivity(intent)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_contextual, menu)
        return super.onCreateOptionsMenu(menu)
    }

    private fun callServiceGetAlmacen() {
        val almacenService:AlmacenService = RestEngine.buildService().create(AlmacenService::class.java)
        var result: Call<AlmacenDataCollectionItem> = almacenService.getAlmacenById(txtMostrarAlmacenID.text.toString().toInt())

        result.enqueue(object :  Callback<AlmacenDataCollectionItem> {
            override fun onFailure(call: Call<AlmacenDataCollectionItem>, t: Throwable) {
                Toast.makeText(this@Buscar_Almacen_Activity,"Error",Toast.LENGTH_LONG).show()
            }

            override fun onResponse(
                call: Call<AlmacenDataCollectionItem>,
                response: Response<AlmacenDataCollectionItem>
            ) {
                if (response.code() == 404){
                    Toast.makeText(this@Buscar_Almacen_Activity, "Almacen no existe",Toast.LENGTH_SHORT).show()
                }else {
                    txtMostrarAlmacenID.setText(response.body()!!.almacenId.toString())
                    txvMostrarTelefonoAlmacen.setText(response.body()!!.telefono.toString())
                    txvMostrarDireccionAlmacen.setText(response.body()!!.direccion)
                    txvMostrarEncargado.setText(response.body()!!.encargado)
                    Toast.makeText(this@Buscar_Almacen_Activity,
                        "OK" + response.body()!!.encargado,
                        Toast.LENGTH_LONG).show()
                }
            }
        })
    }

    private fun callServiceDeleteAlmacen() {
        val personService:AlmacenService = RestEngine.buildService().create(AlmacenService::class.java)
        var result: Call<ResponseBody> = personService.deleteAlmacen(txtMostrarAlmacenID.text.toString().toInt())

        result.enqueue(object : Callback<ResponseBody> {
            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Toast.makeText(this@Buscar_Almacen_Activity,"Error",Toast.LENGTH_LONG).show()
            }

            override fun onResponse(
                call: Call<ResponseBody>,
                response: Response<ResponseBody>
            ) {
                if (response.isSuccessful) {
                    Toast.makeText(this@Buscar_Almacen_Activity,"DELETE",Toast.LENGTH_LONG).show()
                }
                else if (response.code() == 401){
                    Toast.makeText(this@Buscar_Almacen_Activity,"Sesion expirada",Toast.LENGTH_LONG).show()
                }
                else{
                    Toast.makeText(this@Buscar_Almacen_Activity,"Fallo al traer el item",Toast.LENGTH_LONG).show()
                }
            }
        })
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