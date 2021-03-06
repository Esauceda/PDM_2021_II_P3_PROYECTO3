package hn.edu.ujcv.pdm_2021_ii_p3_proyecto3.Delivery

import android.app.DatePickerDialog
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
import kotlinx.android.synthetic.main.activity_registro_compra_encabezado.*
import kotlinx.android.synthetic.main.activity_registro_delivery.*
import kotlinx.android.synthetic.main.activity_registro_materia_prima.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*
import kotlin.collections.ArrayList

class Registro_Delivery_Activity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registro_delivery)

        MyToolbar().show(this,"Registrar Delivery", false)
        callServiceGetOrdenes()
        btnRegistrarDeliver.setOnClickListener { callServicePostDelivery() }
        btnActualizarDeliver.setOnClickListener { callServicePutDelivery() }
        btnBuscarDelivery2.setOnClickListener { callServiceGetDelivery() }
        txtFechaEntregaDeli.setOnClickListener{ calendario() }
    }


    private fun calendario() {
        val c = Calendar.getInstance()
        val date = Date()

        var año = c.get(Calendar.YEAR)
        var mes = c.get(Calendar.MONTH)
        var dia = c.get(Calendar.DAY_OF_MONTH)

        val dpd = DatePickerDialog(this, { view, Año, Mes, Dia ->
            txtFechaEntregaDeli.setText(""+Año+"-"+Mes+"-"+Dia)
            c.set(Año,(Mes+1),(Dia+3))
            dia = c.get(Calendar.DAY_OF_MONTH)
            var año = c.get(Calendar.YEAR)
            var mes = c.get(Calendar.MONTH) + 1
        },año,mes,dia)
        dpd.show()
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
                Toast.makeText(this@Registro_Delivery_Activity,"Error al actualizar el delivery", Toast.LENGTH_SHORT).show()
            }

            override fun onResponse(call: Call<DeliveryDataCollecionItem>,
                                    response: Response<DeliveryDataCollecionItem>
            ) {
                if (response.isSuccessful) {
                    val updatedAlmacen = response.body()!!
                    Toast.makeText(this@Registro_Delivery_Activity,"Delivery Actualizado"+response.body()!!.deliveryId,
                        Toast.LENGTH_LONG).show()
                }
                else if (response.code() == 401){
                    Toast.makeText(this@Registro_Delivery_Activity,"Sesion expirada", Toast.LENGTH_SHORT).show()
                }
                else{
                    Toast.makeText(this@Registro_Delivery_Activity,"Fallo al traer el item", Toast.LENGTH_SHORT).show()
                }
            }

        })
    }

    private fun callServicePostDelivery() {
        val clienteInfo = DeliveryDataCollecionItem(
            deliveryId =      null,//txtDeliveryId.text.toString().toInt(),
            ordenId =         spOrderDeli.selectedItem.toString().toInt(),
            nombreCompania =  txtNombreCom.text.toString(),
            numero =          txtTelefonoDeli.text.toString().toInt(),
            correo =          txtCorreoDeli.text.toString(),
            fechaEntrega =    txtFechaEntregaDeli.text.toString(),
        )

        addCliente(clienteInfo) {
            if (it?.deliveryId != null) {
                Toast.makeText(this@Registro_Delivery_Activity,"Delivery Registrado "+it?.deliveryId, Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(this@Registro_Delivery_Activity,"Error", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun callServiceGetDelivery() {
        val deliveryService: DeliveryService = RestEngine.buildService().create(DeliveryService::class.java)
        var result: Call<DeliveryDataCollecionItem> = deliveryService.getDeliveryById(txtDeliveryId.text.toString().toInt())
        var ordenesContador:Int = 0

        result.enqueue(object : Callback<DeliveryDataCollecionItem> {
            override fun onFailure(call: Call<DeliveryDataCollecionItem>, t: Throwable) {
                Toast.makeText(this@Registro_Delivery_Activity,"Error al encontrar el Delivery", Toast.LENGTH_SHORT).show()
            }

            override fun onResponse(
                call: Call<DeliveryDataCollecionItem>,
                response: Response<DeliveryDataCollecionItem>
            ) {


                if (response.code() == 404){
                    Toast.makeText(this@Registro_Delivery_Activity, "Delivery no existe",Toast.LENGTH_SHORT).show()
                }else {
                    txtDeliveryId.setText(response.body()!!.deliveryId.toString())
                    for (item in ordenes){
                        if (item == response.body()!!.ordenId.toString()){
                            spOrderDeli.setSelection(ordenesContador)
                        }
                        ordenesContador++
                    }
                    txtNombreCom.setText(response.body()!!.nombreCompania)
                    txtTelefonoDeli.setText(response.body()!!.numero.toString())
                    txtCorreoDeli.setText(response.body()!!.correo)
                    txtFechaEntregaDeli.setText(response.body()!!.fechaEntrega)
                    Toast.makeText(this@Registro_Delivery_Activity,
                        "Delivery encontrado "+ response.body()!!.deliveryId,
                        Toast.LENGTH_LONG).show()
                }
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

    var ordenes = ArrayList<String>()
    private fun callServiceGetOrdenes() {
        val ordenService: OrdenEncabezadoService = RestEngine.buildService().create(OrdenEncabezadoService::class.java)
        var result: Call<List<OrdenEncabezadoDataCollectionItem>> = ordenService.listOrdenesEncabezado()

        result.enqueue(object :  Callback<List<OrdenEncabezadoDataCollectionItem>> {
            override fun onFailure(call: Call<List<OrdenEncabezadoDataCollectionItem>>, t: Throwable) {
                Toast.makeText(this@Registro_Delivery_Activity,"Error al encontrar ordenes",Toast.LENGTH_LONG).show()
            }

            override fun onResponse(
                call: Call<List<OrdenEncabezadoDataCollectionItem>>,
                response: Response<List<OrdenEncabezadoDataCollectionItem>>
            ) {
                for (ordenEncabezado in response.body()!!){
                    ordenes.add("${ordenEncabezado.ordenId}")
                }

                val adapterEmpleados = ArrayAdapter(this@Registro_Delivery_Activity, android.R.layout.simple_spinner_item, ordenes)
                spOrderDeli.adapter = adapterEmpleados

                Toast.makeText(this@Registro_Delivery_Activity,"Ordenes encontradas",Toast.LENGTH_LONG).show()
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