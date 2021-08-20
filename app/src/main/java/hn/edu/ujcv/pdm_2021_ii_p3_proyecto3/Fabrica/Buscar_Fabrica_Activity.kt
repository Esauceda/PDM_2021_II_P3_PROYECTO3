package hn.edu.ujcv.pdm_2021_ii_p3_proyecto3.Fabrica

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
import hn.edu.ujcv.pdm_2021_ii_p3_proyecto3.entities.FabricaDataCollectionItem
import kotlinx.android.synthetic.main.activity_buscar_almacen.*
import kotlinx.android.synthetic.main.activity_buscar_fabrica.*
import kotlinx.android.synthetic.main.activity_registro_fabrica.*
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Buscar_Fabrica_Activity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_buscar_fabrica)

        MyToolbar().show(this,"Buscar Fabrica", false)
        btnBuscarFabrica2.setOnClickListener { callServiceGetFabrica() }
        btnEliminarFabrica.setOnClickListener { callServiceDeleteAlmacen() }
    }

    //-----
    private fun callServiceGetFabrica() {
        val fabricaService: FabricaService = RestEngine.buildService().create(FabricaService::class.java)
        var result: Call<FabricaDataCollectionItem> =
            fabricaService.getFabricaById(txtMostrarFabricaID.text.toString().toLong())

        result.enqueue(object : Callback<FabricaDataCollectionItem> {

            override fun onFailure(call: Call<FabricaDataCollectionItem>, t: Throwable) {
                Toast.makeText(this@Buscar_Fabrica_Activity,"Error", Toast.LENGTH_LONG).show()
            }

            override fun onResponse(
                call: Call<FabricaDataCollectionItem>,
                response: Response<FabricaDataCollectionItem>
            ) {
                if (response.code() == 404){
                    Toast.makeText(this@Buscar_Fabrica_Activity, "Fabrica no existe",Toast.LENGTH_SHORT).show()
                }else {
                    txtMostrarFabricaID.setText(response.body()!!.fabricaId.toString())
                    txvMostrarNombreFabrica.setText(response.body()!!.nombreFabrica)
                    txvMostrarDireccionFabrica.setText(response.body()!!.direccion)
                    txvMostrarEncargadoFabrica.setText(response.body()!!.encargado)
                    txvMostrarTelefonoFabrica.setText(response.body()!!.telefono.toString())
                    txvMostrarProduccionFabrica.setText(response.body()!!.tipoProduccion)
                    Toast.makeText(this@Buscar_Fabrica_Activity,
                        "OK" + response.body()!!.encargado,
                        Toast.LENGTH_LONG).show()
                }
            }
        })
    }

    private fun callServiceDeleteAlmacen() {
        val fabricaService: FabricaService = RestEngine.buildService().create(FabricaService::class.java)
        var result: Call<ResponseBody> = fabricaService.deleteFabrica(txtMostrarFabricaID.text.toString().toLong())

        result.enqueue(object : Callback<ResponseBody> {
            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Toast.makeText(this@Buscar_Fabrica_Activity,"Error",Toast.LENGTH_LONG).show()
            }

            override fun onResponse(
                call: Call<ResponseBody>,
                response: Response<ResponseBody>
            ) {
                if (response.isSuccessful) {
                    Toast.makeText(this@Buscar_Fabrica_Activity,"DELETE",Toast.LENGTH_LONG).show()
                }
                else if (response.code() == 401){
                    Toast.makeText(this@Buscar_Fabrica_Activity,"Sesion expirada",Toast.LENGTH_LONG).show()
                }
                else{
                    Toast.makeText(this@Buscar_Fabrica_Activity,"Fallo al traer el item",Toast.LENGTH_LONG).show()
                }
            }
        })
    }
    //-----


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_contextual, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        if (item.itemId == R.id.option_one)
            startActivity(Intent(this, Registro_Fabrica_Activity::class.java))
        if (item.itemId == R.id.option_two)
            startActivity(Intent(this, Buscar_Fabrica_Activity::class.java))
        if (item.itemId == R.id.option_three)
            startActivity(Intent(this, MenuActivity::class.java))

        return super.onOptionsItemSelected(item)
    }
}