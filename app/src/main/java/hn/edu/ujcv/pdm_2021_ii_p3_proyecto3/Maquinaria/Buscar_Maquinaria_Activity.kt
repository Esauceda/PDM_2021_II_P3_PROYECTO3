package hn.edu.ujcv.pdm_2021_ii_p3_proyecto3.Maquinaria

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
import hn.edu.ujcv.pdm_2021_ii_p3_proyecto3.entities.MaquinariaDataCollectionItem
import kotlinx.android.synthetic.main.activity_buscar_maquinaria.*
import kotlinx.android.synthetic.main.activity_registro_maquinaria.*
import kotlinx.android.synthetic.main.activity_registro_maquinaria.txtMaquinariaId
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Buscar_Maquinaria_Activity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_buscar_maquinaria)

        MyToolbar().show(this,"Buscar Maquinaria", false)
        btnBuscarMaqui2.setOnClickListener{ callServiceGetMaquinaria() }
        btnEliminarMaqui.setOnClickListener { callServiceDeleteMaquinaria() }
    }


    //GET
    private fun callServiceGetMaquinaria() {
        val maquinariaService:MaquinariaService = RestEngine.buildService().create(MaquinariaService::class.java)
        var result: Call<MaquinariaDataCollectionItem> = maquinariaService.getMaquinariaById(txtMostrarMaquinariaId.text.toString().toLong())

        result.enqueue(object : Callback<MaquinariaDataCollectionItem> {
            override fun onFailure(call: Call<MaquinariaDataCollectionItem>, t: Throwable) {
                Toast.makeText(this@Buscar_Maquinaria_Activity,"Error", Toast.LENGTH_LONG).show()
            }

            override fun onResponse(
                call: Call<MaquinariaDataCollectionItem>,
                response: Response<MaquinariaDataCollectionItem>
            ) {
                txtMostrarMaquinariaId.setText(response.body()!!.maquinaId.toString())
                txvMostrarNombreMarca.setText(response.body()!!.marca)
                txvMostrarFarbicaId.setText(response.body()!!.fabricaId.toString())
                txvMostrarHorasUsoMaqui.setText(response.body()!!.horasUso.toString())
                txvMostrarTipoMaquina.setText(response.body()!!.tipoMaquina)
                Toast.makeText(this@Buscar_Maquinaria_Activity,"Se encontró la maquina con Id"+response.body()!!.maquinaId, Toast.LENGTH_LONG).show()
            }
        })
    }

    //DELETE

    private fun callServiceDeleteMaquinaria() {
        val maquinariaService:MaquinariaService = RestEngine.buildService().create(MaquinariaService::class.java)
        var result: Call<ResponseBody> = maquinariaService.deleteMaquinaria(txtMostrarMaquinariaId.text.toString().toLong())

        result.enqueue(object :  Callback<ResponseBody> {
            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Toast.makeText(this@Buscar_Maquinaria_Activity,"Error",Toast.LENGTH_LONG).show()
            }

            override fun onResponse(
                call: Call<ResponseBody>,
                response: Response<ResponseBody>
            ) {
                if (response.isSuccessful) {
                    Toast.makeText(this@Buscar_Maquinaria_Activity,"Maquinaria Eliminada",Toast.LENGTH_LONG).show()
                }
                else if (response.code() == 401){
                    Toast.makeText(this@Buscar_Maquinaria_Activity,"Sesion expirada",Toast.LENGTH_LONG).show()
                }
                else{
                    Toast.makeText(this@Buscar_Maquinaria_Activity,"Fallo al traer el item",Toast.LENGTH_LONG).show()
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
            startActivity(Intent(this, Registro_Maquinaria_Activity::class.java))
        if (item.itemId == R.id.option_two)
            startActivity(Intent(this, Buscar_Maquinaria_Activity::class.java))
        if (item.itemId == R.id.option_three)
            startActivity(Intent(this, MenuActivity::class.java))

        return super.onOptionsItemSelected(item)
    }
}