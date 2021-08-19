package hn.edu.ujcv.pdm_2021_ii_p3_proyecto3.Factura

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.ArrayAdapter
import android.widget.Toast
import com.google.gson.Gson
import hn.edu.ujcv.pdm_2021_ii_p3_proyecto3.MenuPrincipal.MenuActivity
import hn.edu.ujcv.pdm_2021_ii_p3_proyecto3.Orden.OrdenEncabezadoService
import hn.edu.ujcv.pdm_2021_ii_p3_proyecto3.R
import hn.edu.ujcv.pdm_2021_ii_p3_proyecto3.RestEngine
import hn.edu.ujcv.pdm_2021_ii_p3_proyecto3.Toolbar.MyToolbar
import hn.edu.ujcv.pdm_2021_ii_p3_proyecto3.entities.FacturaDataCollectionItem
import hn.edu.ujcv.pdm_2021_ii_p3_proyecto3.entities.OrdenEncabezadoDataCollectionItem
import hn.edu.ujcv.pdm_2021_ii_p3_proyecto3.entities.RestApiError
import kotlinx.android.synthetic.main.activity_registro_fabrica.*
import kotlinx.android.synthetic.main.activity_registro_factura.*
import kotlinx.android.synthetic.main.activity_registro_maquinaria.*
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Registro_Factura_Activity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registro_factura)

        MyToolbar().show(this,"Registrar Factura", false)
        callServiceGetOrdenesEncabezado()
        btnRegisFactura.setOnClickListener { callServicePostFactura() }
        btnBuscarFactura.setOnClickListener { callServiceGetFactura() }
        btnActuFactura.setOnClickListener { callServicePutFactura() }
    }

    //-----

    //POST

    private fun callServicePostFactura() {
        val fecha = "2021-04-10"
        val facturaInfo = FacturaDataCollectionItem(
            facturaId = null,
            ordenId = spFacturaOrdenID.selectedItem.toString().toInt(),
            fechaFactura = txtFechaFac.text.toString(),
            total = txtTotalFac.text.toString().toDouble()

        )

        addFactura(facturaInfo) {
            if (it?.facturaId != null) {
                Toast.makeText(this@Registro_Factura_Activity,"Factura Registada"+it?.facturaId, Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(this@Registro_Factura_Activity,"Error al registrar la factura", Toast.LENGTH_LONG).show()
            }
        }
    }

    fun addFactura(facturaData: FacturaDataCollectionItem, onResult: (FacturaDataCollectionItem?) -> Unit){
        val retrofit = RestEngine.buildService().create(FacturaService::class.java)
        var result: Call<FacturaDataCollectionItem> = retrofit.addFactura(facturaData)

        result.enqueue(object : Callback<FacturaDataCollectionItem> {
            override fun onFailure(call: Call<FacturaDataCollectionItem>, t: Throwable) {
                onResult(null)
            }

            override fun onResponse(call: Call<FacturaDataCollectionItem>,
                                    response: Response<FacturaDataCollectionItem>) {
                if (response.isSuccessful) {
                    val addedFactura = response.body()!!
                    onResult(addedFactura)
                }
                else if (response.code() == 401){
                    Toast.makeText(this@Registro_Factura_Activity,"Sesion expirada",Toast.LENGTH_LONG).show()
                }
                else if (response.code() == 500){
                    //val gson = Gson()
                    //val type = object : TypeToken<RestApiError>() {}.type
                    //var errorResponse1: RestApiError? = gson.fromJson(response.errorBody()!!.charStream(), type)
                    val errorResponse = Gson().fromJson(response.errorBody()!!.string()!!, RestApiError::class.java)

                    Toast.makeText(this@Registro_Factura_Activity,errorResponse.errorDetails,Toast.LENGTH_LONG).show()
                }
                else{
                    Toast.makeText(this@Registro_Factura_Activity,"Fallo al traer el item",Toast.LENGTH_LONG).show()
                }
            }

        }
        )
    }

    //PUT

    private fun callServicePutFactura() {
        val fecha = "2021-04-11"
        val facturaInfo = FacturaDataCollectionItem(
            facturaId = txtFacturaId.text.toString().toInt(),
            ordenId = spFacturaOrdenID.selectedItem.toString().toInt(),
            fechaFactura = txtFechaFac.text.toString(),
            total = txtTotalFac.text.toString().toDouble()

        )

        val retrofit = RestEngine.buildService().create(FacturaService::class.java)
        var result: Call<FacturaDataCollectionItem> = retrofit.updateFactura(facturaInfo)

        result.enqueue(object : Callback<FacturaDataCollectionItem> {
            override fun onFailure(call: Call<FacturaDataCollectionItem>, t: Throwable) {
                Toast.makeText(this@Registro_Factura_Activity,"Error al actualizar la factura",Toast.LENGTH_LONG).show()
            }

            override fun onResponse(call: Call<FacturaDataCollectionItem>,
                                    response: Response<FacturaDataCollectionItem>) {
                if (response.isSuccessful) {
                    val updatedPerson = response.body()!!
                    Toast.makeText(this@Registro_Factura_Activity,"Factura Actualizada"+response.body()!!.total,Toast.LENGTH_LONG).show()
                }
                else if (response.code() == 401){
                    Toast.makeText(this@Registro_Factura_Activity,"Sesion expirada",Toast.LENGTH_LONG).show()
                }
                else{
                    Toast.makeText(this@Registro_Factura_Activity,"Fallo al traer el item",Toast.LENGTH_LONG).show()
                }
            }

        })
    }

    //GET

    private fun callServiceGetFactura() {
        val facturaService:FacturaService = RestEngine.buildService().create(FacturaService::class.java)
        var result: Call<FacturaDataCollectionItem> = facturaService.getFacturaById(txtFacturaId.text.toString().toLong())

        result.enqueue(object :  Callback<FacturaDataCollectionItem> {
            override fun onFailure(call: Call<FacturaDataCollectionItem>, t: Throwable) {
                Toast.makeText(this@Registro_Factura_Activity,"Error al buscar la factura",Toast.LENGTH_LONG).show()
            }

            override fun onResponse(
                call: Call<FacturaDataCollectionItem>,
                response: Response<FacturaDataCollectionItem>
            ) {
                txtFacturaId.setText(response.body()!!.facturaId.toString())
//                spFacturaOrdenID.isSelected
                txtFechaFac.setText(response.body()!!.fechaFactura)
                txtTotalFac.setText(response.body()!!.total.toString())
                Toast.makeText(this@Registro_Factura_Activity,"Factura encontrada"+response.body()!!.facturaId,Toast.LENGTH_LONG).show()
            }
        })
    }



    //GETS

    private fun callServiceGetFacturas() {
        val facturaService:FacturaService = RestEngine.buildService().create(FacturaService::class.java)
        var result: Call<List<FacturaDataCollectionItem>> = facturaService.listFactura()

        result.enqueue(object :  Callback<List<FacturaDataCollectionItem>> {
            override fun onFailure(call: Call<List<FacturaDataCollectionItem>>, t: Throwable) {
                Toast.makeText(this@Registro_Factura_Activity,"Error Al encontrar las Facturas",Toast.LENGTH_LONG).show()
            }

            override fun onResponse(
                call: Call<List<FacturaDataCollectionItem>>,
                response: Response<List<FacturaDataCollectionItem>>
            ) {
                Toast.makeText(this@Registro_Factura_Activity,"Facturas Encontradas",Toast.LENGTH_LONG).show()
            }
        })
    }

    //GETSORDENESECABEZADO
    private fun callServiceGetOrdenesEncabezado() {
        val ordenEncabezadoService: OrdenEncabezadoService = RestEngine.buildService().create(
            OrdenEncabezadoService::class.java)
        var result: Call<List<OrdenEncabezadoDataCollectionItem>> = ordenEncabezadoService.listOrdenesEncabezado()
        val ordenes = ArrayList<String>()

        result.enqueue(object :  Callback<List<OrdenEncabezadoDataCollectionItem>> {
            override fun onFailure(call: Call<List<OrdenEncabezadoDataCollectionItem>>, t: Throwable) {
                Toast.makeText(this@Registro_Factura_Activity,"Error al encontrar ordenes",Toast.LENGTH_LONG).show()
            }

            override fun onResponse(
                call: Call<List<OrdenEncabezadoDataCollectionItem>>,
                response: Response<List<OrdenEncabezadoDataCollectionItem>>
            ) {
                for (orden in response.body()!!){
                    ordenes.add("${orden.ordenId}")
                }

                val  adapterOrdenes = ArrayAdapter(this@Registro_Factura_Activity,android.R.layout.simple_spinner_item, ordenes)
                spFacturaOrdenID.adapter = adapterOrdenes


                Toast.makeText(this@Registro_Factura_Activity,"Ordenes encontradas",Toast.LENGTH_LONG).show()
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
            startActivity(Intent(this, Registro_Factura_Activity::class.java))
        if (item.itemId == R.id.option_two)
            startActivity(Intent(this, Buscar_Factura_Activity::class.java))
        if (item.itemId == R.id.option_three)
            startActivity(Intent(this, MenuActivity::class.java))

        return super.onOptionsItemSelected(item)
    }
}