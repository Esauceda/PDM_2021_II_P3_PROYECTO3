package hn.edu.ujcv.pdm_2021_ii_p3_proyecto3.Delivery

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
import hn.edu.ujcv.pdm_2021_ii_p3_proyecto3.entities.DeliveryDataCollecionItem
import hn.edu.ujcv.pdm_2021_ii_p3_proyecto3.entities.OrdenEncabezadoDataCollectionItem
import hn.edu.ujcv.pdm_2021_ii_p3_proyecto3.entities.RestApiError
import kotlinx.android.synthetic.main.activity_registro_delivery.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Registro_Delivery_Activity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registro_delivery)

        MyToolbar().show(this,"Registrar Delivery", false)
        callServiceGetOrdenes()
        btnRegistrarDeliver.setOnClickListener { callServicePostDelivery() }
        btnActualizarDeliver.setOnClickListener { callServicePutDelivery() }
        btnBuscarDelivery2.setOnClickListener { callServiceGetDelivery() }
    }

    private fun callServicePutDelivery() {
        val deliveryInfo = DeliveryDataCollecionItem(
            deliveryId =      txtDeliveryId.text.toString().toInt(),
            ordenId =         spOrderDeli.selectedItem.toString().toInt(),
            nombreCompania =  txtNombreCom.text.toString(),
            numero =          txtTelefonoDeli.text.toString().toInt(),
            correo =          txtCorreoDeli.text.toString(),
            fechaEntrega =    txtFechaEntregaDeli.text.toString(),
        )

        val retrofit = RestEngine.buildService().create(DeliveryService::class.java)
        var result: Call<DeliveryDataCollecionItem> = retrofit.updateDelivery(deliveryInfo)

        result.enqueue(object : Callback<DeliveryDataCollecionItem> {
            override fun onFailure(call: Call<DeliveryDataCollecionItem>, t: Throwable) {
                Toast.makeText(this@Registro_Delivery_Activity,"Error", Toast.LENGTH_LONG).show()
            }

            override fun onResponse(call: Call<DeliveryDataCollecionItem>,
                                    response: Response<DeliveryDataCollecionItem>
            ) {
                if (response.isSuccessful) {
                    val updatedAlmacen = response.body()!!
                    Toast.makeText(this@Registro_Delivery_Activity,"OK"+response.body()!!.nombreCompania,
                        Toast.LENGTH_LONG).show()
                }
                else if (response.code() == 401){
                    Toast.makeText(this@Registro_Delivery_Activity,"Sesion expirada", Toast.LENGTH_LONG).show()
                }
                else{
                    Toast.makeText(this@Registro_Delivery_Activity,"Fallo al traer el item", Toast.LENGTH_LONG).show()
                }
            }

        })
    }

    private fun callServicePostDelivery() {
        val clienteInfo = DeliveryDataCollecionItem(
            deliveryId =      txtDeliveryId.text.toString().toInt(),
            ordenId =         spOrderDeli.selectedItem.toString().toInt(),
            nombreCompania =  txtNombreCom.text.toString(),
            numero =          txtTelefonoDeli.text.toString().toInt(),
            correo =          txtCorreoDeli.text.toString(),
            fechaEntrega =    txtFechaEntregaDeli.text.toString(),
        )

        addCliente(clienteInfo) {
            if (it?.deliveryId != null) {
                Toast.makeText(this@Registro_Delivery_Activity,"OK"+it?.deliveryId, Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(this@Registro_Delivery_Activity,"Error", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun callServiceGetDelivery() {
        val deliveryService: DeliveryService = RestEngine.buildService().create(DeliveryService::class.java)
        var result: Call<DeliveryDataCollecionItem> = deliveryService.getDeliveryById(txtDeliveryId.text.toString().toInt())

        result.enqueue(object : Callback<DeliveryDataCollecionItem> {
            override fun onFailure(call: Call<DeliveryDataCollecionItem>, t: Throwable) {
                Toast.makeText(this@Registro_Delivery_Activity,"Error", Toast.LENGTH_LONG).show()
            }

            override fun onResponse(
                call: Call<DeliveryDataCollecionItem>,
                response: Response<DeliveryDataCollecionItem>
            ) {
                txtDeliveryId.setText(response.body()!!.deliveryId.toString())
                //falta orden id
                txtNombreCom.setText(response.body()!!.nombreCompania)
                txtTelefonoDeli.setText(response.body()!!.numero.toString())
                txtCorreoDeli.setText(response.body()!!.correo)
                txtFechaEntregaDeli.setText(response.body()!!.fechaEntrega)
                Toast.makeText(this@Registro_Delivery_Activity,"OK"+response.body()!!.nombreCompania,
                    Toast.LENGTH_LONG).show()
            }
        })
    }

    fun addCliente(deliveryData: DeliveryDataCollecionItem, onResult: (DeliveryDataCollecionItem?) -> Unit){
        val retrofit = RestEngine.buildService().create(DeliveryService::class.java)
        var result: Call<DeliveryDataCollecionItem> = retrofit.addDelivery(deliveryData)

        result.enqueue(object : Callback<DeliveryDataCollecionItem> {
            override fun onFailure(call: Call<DeliveryDataCollecionItem>, t: Throwable) {
                onResult(null)
            }

            override fun onResponse(call: Call<DeliveryDataCollecionItem>,
                                    response: Response<DeliveryDataCollecionItem>
            ) {
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

                    Toast.makeText(this@Registro_Delivery_Activity,errorResponse.errorDetails, Toast.LENGTH_LONG).show()
                }
                else{
                    Toast.makeText(this@Registro_Delivery_Activity,"Fallo al traer el item", Toast.LENGTH_LONG).show()
                }
            }

        }
        )
    }

    private fun callServiceGetOrdenes() {
        val ordenService: OrdenEncabezadoService = RestEngine.buildService().create(OrdenEncabezadoService::class.java)
        var result: Call<List<OrdenEncabezadoDataCollectionItem>> = ordenService.listOrdenesEncabezado()
        var ordenes = ArrayList<String>()

        result.enqueue(object :  Callback<List<OrdenEncabezadoDataCollectionItem>> {
            override fun onFailure(call: Call<List<OrdenEncabezadoDataCollectionItem>>, t: Throwable) {
                Toast.makeText(this@Registro_Delivery_Activity,"Error al encontrar ordenes",Toast.LENGTH_LONG).show()
            }

            override fun onResponse(
                call: Call<List<OrdenEncabezadoDataCollectionItem>>,
                response: Response<List<OrdenEncabezadoDataCollectionItem>>
            ) {
                for (ordenEncabezado in response.body()!!){
                    ordenes.add("${ordenEncabezado.empleadoId}")
                }

                val adapterEmpleados = ArrayAdapter(this@Registro_Delivery_Activity, android.R.layout.simple_spinner_item, ordenes)
                spOrderDeli.adapter = adapterEmpleados

                Toast.makeText(this@Registro_Delivery_Activity,"Empelados encontrados",Toast.LENGTH_LONG).show()
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_contextual, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        if (item.itemId == R.id.option_one)
            startActivity(Intent(this, Registro_Delivery_Activity::class.java))
        if (item.itemId == R.id.option_two)
            startActivity(Intent(this, Buscar_Delivery_Activity::class.java))
        if (item.itemId == R.id.option_three)
            startActivity(Intent(this, MenuActivity::class.java))

        return super.onOptionsItemSelected(item)
    }
}