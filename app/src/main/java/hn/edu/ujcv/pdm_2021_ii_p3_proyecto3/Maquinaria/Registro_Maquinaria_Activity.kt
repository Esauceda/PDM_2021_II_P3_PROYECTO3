package hn.edu.ujcv.pdm_2021_ii_p3_proyecto3.Maquinaria

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.ArrayAdapter
import android.widget.Toast
import com.google.gson.Gson
import hn.edu.ujcv.pdm_2021_ii_p3_proyecto3.Fabrica.FabricaService
import hn.edu.ujcv.pdm_2021_ii_p3_proyecto3.MenuPrincipal.MenuActivity
import hn.edu.ujcv.pdm_2021_ii_p3_proyecto3.Orden.OrdenEncabezadoService
import hn.edu.ujcv.pdm_2021_ii_p3_proyecto3.R
import hn.edu.ujcv.pdm_2021_ii_p3_proyecto3.RestEngine
import hn.edu.ujcv.pdm_2021_ii_p3_proyecto3.Toolbar.MyToolbar
import hn.edu.ujcv.pdm_2021_ii_p3_proyecto3.entities.FabricaDataCollectionItem
import hn.edu.ujcv.pdm_2021_ii_p3_proyecto3.entities.MaquinariaDataCollectionItem
import hn.edu.ujcv.pdm_2021_ii_p3_proyecto3.entities.OrdenEncabezadoDataCollectionItem
import hn.edu.ujcv.pdm_2021_ii_p3_proyecto3.entities.RestApiError
import kotlinx.android.synthetic.main.activity_registro_factura.*
import kotlinx.android.synthetic.main.activity_registro_maquinaria.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Registro_Maquinaria_Activity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registro_maquinaria)

        MyToolbar().show(this,"Registrar Maquinaria", false)

        callServicesGetFabricas()
        btnRegisMaqui.setOnClickListener { callServicePostMaquinaria() }
        btnActuMaqui.setOnClickListener { callServicePutMaquinaria() }
        btnBuscarMaqui.setOnClickListener { callServiceGetMaquinaria() }
    }
    val fabricas = ArrayList<String>()

    //-----

    //GETFABRICAS
    private fun callServicesGetFabricas(){

        val fabricaService:FabricaService = RestEngine.buildService().create(FabricaService::class.java)
        var result: Call<List<FabricaDataCollectionItem>> = fabricaService.listFabricas()

        result.enqueue(object :  Callback<List<FabricaDataCollectionItem>> {
            override fun onFailure(call: Call<List<FabricaDataCollectionItem>>, t: Throwable) {
                Toast.makeText(this@Registro_Maquinaria_Activity,"Error al encontrar fabricas",Toast.LENGTH_SHORT).show()
            }

            override fun onResponse(
                call: Call<List<FabricaDataCollectionItem>>,
                response: Response<List<FabricaDataCollectionItem>>
            ) {
                for (fabrica in response.body()!!){
                    fabricas.add("${fabrica.fabricaId}")
                }

                val adapterFabricas = ArrayAdapter(this@Registro_Maquinaria_Activity, android.R.layout.simple_spinner_item, fabricas)
                spMaquiFarbicaID.adapter = adapterFabricas



                Toast.makeText(this@Registro_Maquinaria_Activity,"Fabricas encontradas con exito",Toast.LENGTH_SHORT).show()

            }
        })

    }

    //POST
    private fun callServicePostMaquinaria() {
        val fecha = "2021-04-10"
        val maquinariaInfo = MaquinariaDataCollectionItem(
            maquinaId = null,
            fabricaId = spMaquiFarbicaID.selectedItem.toString().toInt(),
            marca = txtNombreMarca.text.toString(),
            horasUso = txtHorasUsoMaqui.text.toString().toDouble(),
            tipoMaquina = txtTipoMaquina.text.toString()
        )

        addMaquinaria(maquinariaInfo) {
            if (it?.maquinaId != null) {
                Toast.makeText(this@Registro_Maquinaria_Activity,"Maquina registrada"+it?.maquinaId,Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(this@Registro_Maquinaria_Activity,"Error",Toast.LENGTH_LONG).show()
            }
        }
    }

    fun addMaquinaria(maquinariaData: MaquinariaDataCollectionItem, onResult: (MaquinariaDataCollectionItem?) -> Unit){
        val retrofit = RestEngine.buildService().create(MaquinariaService::class.java)
        var result: Call<MaquinariaDataCollectionItem> = retrofit.addMaquinaria(maquinariaData)

        result.enqueue(object : Callback<MaquinariaDataCollectionItem> {
            override fun onFailure(call: Call<MaquinariaDataCollectionItem>, t: Throwable) {
                onResult(null)
            }

            override fun onResponse(call: Call<MaquinariaDataCollectionItem>,
                                    response: Response<MaquinariaDataCollectionItem>) {
                if (response.isSuccessful) {
                    val addedMaquinaria = response.body()!!
                    onResult(addedMaquinaria)
                }
                else if (response.code() == 401){
                    Toast.makeText(this@Registro_Maquinaria_Activity,"Sesion expirada",Toast.LENGTH_LONG).show()
                }
                else if (response.code() == 500){
                    //val gson = Gson()
                    //val type = object : TypeToken<RestApiError>() {}.type
                    //var errorResponse1: RestApiError? = gson.fromJson(response.errorBody()!!.charStream(), type)
                    val errorResponse = Gson().fromJson(response.errorBody()!!.string()!!, RestApiError::class.java)

                    Toast.makeText(this@Registro_Maquinaria_Activity,errorResponse.errorDetails,Toast.LENGTH_LONG).show()
                }
                else{
                    Toast.makeText(this@Registro_Maquinaria_Activity,"Fallo al traer el item",Toast.LENGTH_LONG).show()
                }
            }

        }
        )
    }

    //PUT

    private fun callServicePutMaquinaria() {
        val fecha = "2021-04-11"
        val personInfo = MaquinariaDataCollectionItem(
            maquinaId = txtMaquinariaId.text.toString().toInt(),
            fabricaId = spMaquiFarbicaID.selectedItem.toString().toInt(),
            marca = txtNombreMarca.text.toString(),
            horasUso = txtHorasUsoMaqui.text.toString().toDouble(),
            tipoMaquina = txtTipoMaquina.text.toString()
        )

        val retrofit = RestEngine.buildService().create(MaquinariaService::class.java)
        var result: Call<MaquinariaDataCollectionItem> = retrofit.updateMaquinaria(personInfo)

        result.enqueue(object : Callback<MaquinariaDataCollectionItem> {
            override fun onFailure(call: Call<MaquinariaDataCollectionItem>, t: Throwable) {
                Toast.makeText(this@Registro_Maquinaria_Activity,"Error",Toast.LENGTH_LONG).show()
            }

            override fun onResponse(call: Call<MaquinariaDataCollectionItem>,
                                    response: Response<MaquinariaDataCollectionItem>) {
                if (response.isSuccessful) {
                    val updatedPerson = response.body()!!
                    Toast.makeText(this@Registro_Maquinaria_Activity,"Actualizado"+response.body()!!.marca,Toast.LENGTH_LONG).show()
                }
                else if (response.code() == 401){
                    Toast.makeText(this@Registro_Maquinaria_Activity,"Sesion expirada",Toast.LENGTH_LONG).show()
                }
                else{
                    Toast.makeText(this@Registro_Maquinaria_Activity,"Fallo al traer el item",Toast.LENGTH_LONG).show()
                }
            }

        })
    }

    //GET
    private fun callServiceGetMaquinaria() {
        val maquinariaService:MaquinariaService = RestEngine.buildService().create(MaquinariaService::class.java)
        var result: Call<MaquinariaDataCollectionItem> = maquinariaService.getMaquinariaById(txtMaquinariaId.text.toString().toLong())
        var contadorFabircas = 0

        result.enqueue(object : Callback<MaquinariaDataCollectionItem> {
            override fun onFailure(call: Call<MaquinariaDataCollectionItem>, t: Throwable) {
                Toast.makeText(this@Registro_Maquinaria_Activity,"No se encontro la maquinaria", Toast.LENGTH_LONG).show()
            }

            override fun onResponse(
                call: Call<MaquinariaDataCollectionItem>,
                response: Response<MaquinariaDataCollectionItem>
            ) {
                if (response.code() == 404){
                    Toast.makeText(this@Registro_Maquinaria_Activity, "Maquina no existe",Toast.LENGTH_SHORT).show()
                }else {
                    txtMaquinariaId.setText(response.body()!!.maquinaId.toString())

                    for (item in fabricas){
                        if (item == response.body()!!.fabricaId.toString()){
                            spMaquiFarbicaID.setSelection(contadorFabircas)
                        }
                        contadorFabircas++
                    }

                    txtNombreMarca.setText(response.body()!!.marca)
                    txtHorasUsoMaqui.setText(response.body()!!.horasUso.toString())
                    txtTipoMaquina.setText(response.body()!!.tipoMaquina)
                    Toast.makeText(this@Registro_Maquinaria_Activity,
                        "Se encontró la maquina con Id" + response.body()!!.maquinaId,
                        Toast.LENGTH_LONG).show()
                }
            }
        })
    }


    //GETS
    private fun callServiceGetMaquinarias() {
        val maquinariaService:MaquinariaService = RestEngine.buildService().create(MaquinariaService::class.java)
        var result: Call<List<MaquinariaDataCollectionItem>> = maquinariaService.listMaquinarias()

        result.enqueue(object :  Callback<List<MaquinariaDataCollectionItem>> {
            override fun onFailure(call: Call<List<MaquinariaDataCollectionItem>>, t: Throwable) {
                Toast.makeText(this@Registro_Maquinaria_Activity,"Error al traer las maquinarias",Toast.LENGTH_SHORT).show()
            }

            override fun onResponse(
                call: Call<List<MaquinariaDataCollectionItem>>,
                response: Response<List<MaquinariaDataCollectionItem>>
            ) {
                Toast.makeText(this@Registro_Maquinaria_Activity,"Maquinarias encontradas",Toast.LENGTH_SHORT).show()
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
            startActivity(Intent(this, Registro_Maquinaria_Activity::class.java))
        if (item.itemId == R.id.option_two)
            startActivity(Intent(this, Buscar_Maquinaria_Activity::class.java))
        if (item.itemId == R.id.option_three)
            startActivity(Intent(this, MenuActivity::class.java))

        return super.onOptionsItemSelected(item)
    }
}