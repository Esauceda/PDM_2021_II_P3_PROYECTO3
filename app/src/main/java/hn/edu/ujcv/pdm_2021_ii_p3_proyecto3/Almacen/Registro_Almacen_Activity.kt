package hn.edu.ujcv.pdm_2021_ii_p3_proyecto3.Almacen

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import com.google.gson.Gson
import hn.edu.ujcv.pdm_2021_ii_p3_proyecto3.MenuPrincipal.MenuActivity
import hn.edu.ujcv.pdm_2021_ii_p3_proyecto3.R
import hn.edu.ujcv.pdm_2021_ii_p3_proyecto3.RestEngine
import hn.edu.ujcv.pdm_2021_ii_p3_proyecto3.Toolbar.MyToolbar
import hn.edu.ujcv.pdm_2021_ii_p3_proyecto3.entities.AlmacenDataCollectionItem
import hn.edu.ujcv.pdm_2021_ii_p3_proyecto3.entities.RestApiError
import kotlinx.android.synthetic.main.activiry_registro_almacen.*
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Registro_Almacen_Activity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activiry_registro_almacen)
        btnActualizarAlmacen.setOnClickListener { callServicePutAlmacen() }
        btnGuardarAlmacen.setOnClickListener { callServicePostAlmacen() }
        btnBuscarAlmacen2.setOnClickListener { callServiceGetAlmacen() }


        MyToolbar().show(this,"Registro Almacen", false)
    }

    private fun callServicePutAlmacen() {
        val almancenInfo = AlmacenDataCollectionItem(
            almacenId = txtMostrarAlmacenID.text.toString().toInt(),
            telefono = txtTelefonoAlmacen.text.toString().toInt(),
            direccion = txtDireccionAlmacen.text.toString(),
            encargado = txtEncargado.text.toString()
        )

        val retrofit = RestEngine.buildService().create(AlmacenService::class.java)
        var result: Call<AlmacenDataCollectionItem> = retrofit.updateAlmacen(almancenInfo)

        result.enqueue(object : Callback<AlmacenDataCollectionItem> {
            override fun onFailure(call: Call<AlmacenDataCollectionItem>, t: Throwable) {
                Toast.makeText(this@Registro_Almacen_Activity,"Error al actualizar el almacen",Toast.LENGTH_SHORT).show()
            }

            override fun onResponse(call: Call<AlmacenDataCollectionItem>,
                                    response: Response<AlmacenDataCollectionItem>
            ) {
                if (response.isSuccessful) {
                    val updatedAlmacen = response.body()!!
                    Toast.makeText(this@Registro_Almacen_Activity,"Almacen Actualizado "+response.body()!!.almacenId,Toast.LENGTH_SHORT).show()
                }
                else if (response.code() == 401){
                    Toast.makeText(this@Registro_Almacen_Activity,"Sesion expirada",Toast.LENGTH_SHORT).show()
                }
                else{
                    Toast.makeText(this@Registro_Almacen_Activity,"Fallo al traer el item",Toast.LENGTH_SHORT).show()
                }
            }

        })
    }

    private fun callServicePostAlmacen() {
        val almancenInfo = AlmacenDataCollectionItem(
            almacenId = null, //txtMostrarAlmacenID.text.toString().toInt(),
            telefono =  txtTelefonoAlmacen.text.toString().toInt(),
            direccion = txtDireccionAlmacen.text.toString(),
            encargado = txtEncargado.text.toString()
        )

        addAlmacen(almancenInfo) {
            if (it?.almacenId != null) {
                Toast.makeText(this@Registro_Almacen_Activity,"Almacen registrado "+it?.almacenId,Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this@Registro_Almacen_Activity,"Error al registrar el almacen",Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun callServiceGetAlmacen() {
        val almacenService:AlmacenService = RestEngine.buildService().create(AlmacenService::class.java)
        var result: Call<AlmacenDataCollectionItem> = almacenService.getAlmacenById(txtMostrarAlmacenID.text.toString().toInt())

        result.enqueue(object :  Callback<AlmacenDataCollectionItem> {
            override fun onFailure(call: Call<AlmacenDataCollectionItem>, t: Throwable) {
                Toast.makeText(this@Registro_Almacen_Activity,"Error al encontar el almacen",Toast.LENGTH_SHORT).show()
            }

            override fun onResponse(
                call: Call<AlmacenDataCollectionItem>,
                response: Response<AlmacenDataCollectionItem>
            ) {
                if (response.code() == 404){
                    Toast.makeText(this@Registro_Almacen_Activity, "Almacen no existe",Toast.LENGTH_SHORT).show()
                }else {
                    txtMostrarAlmacenID.setText(response.body()!!.almacenId.toString())
                    txtTelefonoAlmacen.setText(response.body()!!.telefono.toString())
                    txtDireccionAlmacen.setText(response.body()!!.direccion)
                    txtEncargado.setText(response.body()!!.encargado)
                    Toast.makeText(this@Registro_Almacen_Activity,
                        "Almacen encontrado " + response.body()!!.almacenId,
                        Toast.LENGTH_SHORT).show()
                }

            }
        })
    }

    fun addAlmacen(almacenData: AlmacenDataCollectionItem, onResult: (AlmacenDataCollectionItem?) -> Unit){
        val retrofit = RestEngine.buildService().create(AlmacenService::class.java)
        var result: Call<AlmacenDataCollectionItem> = retrofit.addAlmacen(almacenData)

        result.enqueue(object : Callback<AlmacenDataCollectionItem> {
            override fun onFailure(call: Call<AlmacenDataCollectionItem>, t: Throwable) {
                onResult(null)
            }

            override fun onResponse(call: Call<AlmacenDataCollectionItem>,
                                    response: Response<AlmacenDataCollectionItem>) {
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

                    Toast.makeText(this@Registro_Almacen_Activity,errorResponse.errorDetails,Toast.LENGTH_LONG).show()
                }
                else{
                    Toast.makeText(this@Registro_Almacen_Activity,"Fallo al traer el item",Toast.LENGTH_LONG).show()
                }
            }

        }
        )
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