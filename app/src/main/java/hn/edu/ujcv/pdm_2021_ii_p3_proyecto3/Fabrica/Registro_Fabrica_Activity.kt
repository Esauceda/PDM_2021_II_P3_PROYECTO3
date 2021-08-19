package hn.edu.ujcv.pdm_2021_ii_p3_proyecto3.Fabrica

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_registro_fabrica.*
import hn.edu.ujcv.pdm_2021_ii_p3_proyecto3.MenuPrincipal.MenuActivity
import hn.edu.ujcv.pdm_2021_ii_p3_proyecto3.R
import hn.edu.ujcv.pdm_2021_ii_p3_proyecto3.RestEngine
import hn.edu.ujcv.pdm_2021_ii_p3_proyecto3.Toolbar.MyToolbar
import hn.edu.ujcv.pdm_2021_ii_p3_proyecto3.entities.FabricaDataCollectionItem
import hn.edu.ujcv.pdm_2021_ii_p3_proyecto3.entities.RestApiError
import kotlinx.android.synthetic.main.activiry_registro_almacen.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Registro_Fabrica_Activity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registro_fabrica)

        MyToolbar().show(this,"Registrar Fabrica", false)
        btnRegistrarFabrica.setOnClickListener { callServicePostFabrica() }
        btnActualizarFabrica.setOnClickListener { callServicePutFabrica() }
        btnBuscarFabrica.setOnClickListener { callServiceGetFabrica() }
    }

    //-----
    private fun callServiceGetFabrica() {
        val fabricaService: FabricaService =
            RestEngine.buildService().create(FabricaService::class.java)
        var result: Call<FabricaDataCollectionItem> =
            fabricaService.getFabricaById(txtFabricaID.text.toString().toLong())

        result.enqueue(object :  Callback<FabricaDataCollectionItem> {

            override fun onFailure(call: Call<FabricaDataCollectionItem>, t: Throwable) {
                Toast.makeText(this@Registro_Fabrica_Activity,"Error",Toast.LENGTH_LONG).show()
            }

            override fun onResponse(
                call: Call<FabricaDataCollectionItem>,
                response: Response<FabricaDataCollectionItem>
            ) {
                if (response.code() == 404){
                    Toast.makeText(this@Registro_Fabrica_Activity,"No existe",Toast.LENGTH_LONG).show()
                }else{
                    txtFabricaID.setText(response.body()!!.fabricaId.toString())
                    txtFabricaNombre.setText(response.body()!!.nombreFabrica)
                    txtDireccionFabrica.setText(response.body()!!.direccion)
                    txtEncargadoFabrica.setText(response.body()!!.encargado)
                    txtTelefonoFabrica.setText(response.body()!!.telefono.toString())
                    txtProduccionFabrica.setText(response.body()!!.tipoProduccion)
                    Toast.makeText(this@Registro_Fabrica_Activity,"OK"+response.body()!!.encargado,Toast.LENGTH_LONG).show()
                }

            }
        })
    }

    private fun callServicePutFabrica() {
        val fabricaInfo = FabricaDataCollectionItem(
            fabricaId = txtFabricaID.text.toString().toLong(),
            nombreFabrica = txtFabricaNombre.text.toString(),
            telefono = txtTelefonoFabrica.text.toString().toLong(),
            tipoProduccion = txtProduccionFabrica.text.toString(),
            direccion = txtDireccionFabrica.text.toString(),
            encargado = txtEncargadoFabrica.text.toString()
        )

        val retrofit = RestEngine.buildService().create(FabricaService::class.java)
        var result: Call<FabricaDataCollectionItem> = retrofit.updateFabrica(fabricaInfo)

        result.enqueue(object : Callback<FabricaDataCollectionItem> {
            override fun onFailure(call: Call<FabricaDataCollectionItem>, t: Throwable) {
                Toast.makeText(this@Registro_Fabrica_Activity,"Error",Toast.LENGTH_LONG).show()
            }
            override fun onResponse(call: Call<FabricaDataCollectionItem>,
                                    response: Response<FabricaDataCollectionItem>
            ) {
                if (response.isSuccessful) {
                    val updatedFabrica = response.body()!!
                    Toast.makeText(this@Registro_Fabrica_Activity,"OK"+response.body()!!.encargado,Toast.LENGTH_LONG).show()
                }
                else if (response.code() == 401){
                    Toast.makeText(this@Registro_Fabrica_Activity,"Sesion expirada",Toast.LENGTH_LONG).show()
                }else if (response.code() == 500) {
                    val errorResponse =
                        Gson().fromJson(response.errorBody()!!.string()!!, RestApiError::class.java)

                    Toast.makeText(this@Registro_Fabrica_Activity, errorResponse.errorDetails, Toast.LENGTH_LONG)
                        .show()
                }
                else{
                    Toast.makeText(this@Registro_Fabrica_Activity,"Fallo al traer el item",Toast.LENGTH_LONG).show()
                }
            }
        })
    }

    private fun callServicePostFabrica() {
        val fabricaInfo = FabricaDataCollectionItem(
            fabricaId = txtFabricaID.text.toString().toLong(),
            nombreFabrica = txtFabricaNombre.text.toString(),
            telefono = txtTelefonoFabrica.text.toString().toLong(),
            tipoProduccion = txtProduccionFabrica.text.toString(),
            direccion = txtDireccionFabrica.text.toString(),
            encargado = txtEncargadoFabrica.text.toString()
        )
        addFabrica(fabricaInfo){
            if (it?.fabricaId != null){
                Toast.makeText(this@Registro_Fabrica_Activity,"OK"+it?.fabricaId, Toast.LENGTH_SHORT).show()
            }else{
                Toast.makeText(this@Registro_Fabrica_Activity,"Error", Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun addFabrica(fabricaData: FabricaDataCollectionItem, onResult: (FabricaDataCollectionItem?)->Unit){
        val retrofit = RestEngine.buildService().create(FabricaService::class.java)
        val result: Call<FabricaDataCollectionItem> = retrofit.addFabrica(fabricaData)

        result.enqueue(object : Callback<FabricaDataCollectionItem> {
            override fun onFailure(call: Call<FabricaDataCollectionItem>, t: Throwable) {
                onResult(null)
            }

            override fun onResponse(
                call: Call<FabricaDataCollectionItem>, response: Response<FabricaDataCollectionItem>
            ) {
                if (response.isSuccessful){
                    val addedFabrica = response.body()!!
                    onResult(addedFabrica)
                }else if (response.code() == 500){
                    val errorResponse = Gson().fromJson(response.errorBody()!!.string()!!, RestApiError::class.java)
                    Toast.makeText(this@Registro_Fabrica_Activity, errorResponse.errorDetails, Toast.LENGTH_SHORT).show()
                }else{
                    Toast.makeText(this@Registro_Fabrica_Activity,"Fallo al traer el item", Toast.LENGTH_SHORT).show()
                }
            }
        }
        )
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