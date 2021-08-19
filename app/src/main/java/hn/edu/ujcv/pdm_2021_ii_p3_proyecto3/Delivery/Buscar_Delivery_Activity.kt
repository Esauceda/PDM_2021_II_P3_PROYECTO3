package hn.edu.ujcv.pdm_2021_ii_p3_proyecto3.Delivery

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
import hn.edu.ujcv.pdm_2021_ii_p3_proyecto3.entities.DeliveryDataCollecionItem
import kotlinx.android.synthetic.main.activity_buscar_delivery.*
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Buscar_Delivery_Activity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_buscar_delivery)
        btnEliminarDelivery.setOnClickListener { callServiceDeleteDelivery() }
        btnBuscarDelivery.setOnClickListener { callServiceGetDelivery() }
        MyToolbar().show(this,"Buscar Delivery", false)
    }

    private fun callServiceGetDelivery() {
        val deliveryService: DeliveryService = RestEngine.buildService().create(DeliveryService::class.java)
        var result: Call<DeliveryDataCollecionItem> = deliveryService.getDeliveryById(txtDeliveryId2.text.toString().toInt())

        result.enqueue(object : Callback<DeliveryDataCollecionItem> {
            override fun onFailure(call: Call<DeliveryDataCollecionItem>, t: Throwable) {
                Toast.makeText(this@Buscar_Delivery_Activity,"Error", Toast.LENGTH_LONG).show()
            }

            override fun onResponse(
                call: Call<DeliveryDataCollecionItem>,
                response: Response<DeliveryDataCollecionItem>
            ) {
                txtDeliveryId2.setText(response.body()!!.deliveryId.toString())
                txvMostrarOrdernDeliver.setText(response.body()!!.ordenId.toString())
                txvMostrarNombreComDeliver.setText(response.body()!!.nombreCompania)
                txvMostrarTelDeliver.setText(response.body()!!.numero.toString())
                txvMostrarCorreoDeliver.setText(response.body()!!.correo)
                txvMostrarFechaEntreDeliver.setText(response.body()!!.fechaEntrega)
                Toast.makeText(this@Buscar_Delivery_Activity,"OK"+response.body()!!.nombreCompania,
                    Toast.LENGTH_LONG).show()
            }
        })
    }

    private fun callServiceDeleteDelivery() {
        val deliveryService: DeliveryService = RestEngine.buildService().create(DeliveryService::class.java)
        var result: Call<ResponseBody> = deliveryService.deleteDelivery(txtDeliveryId2.text.toString().toInt())

        result.enqueue(object : Callback<ResponseBody> {
            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Toast.makeText(this@Buscar_Delivery_Activity,"Error", Toast.LENGTH_LONG).show()
            }

            override fun onResponse(
                call: Call<ResponseBody>,
                response: Response<ResponseBody>
            ) {
                if (response.isSuccessful) {
                    Toast.makeText(this@Buscar_Delivery_Activity,"DELETE", Toast.LENGTH_LONG).show()
                }
                else if (response.code() == 401){
                    Toast.makeText(this@Buscar_Delivery_Activity,"Sesion expirada", Toast.LENGTH_LONG).show()
                }
                else{
                    Toast.makeText(this@Buscar_Delivery_Activity,"Fallo al traer el item", Toast.LENGTH_LONG).show()
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
            startActivity(Intent(this, Registro_Delivery_Activity::class.java))
        if (item.itemId == R.id.option_two)
            startActivity(Intent(this, Buscar_Delivery_Activity::class.java))
        if (item.itemId == R.id.option_three)
            startActivity(Intent(this, MenuActivity::class.java))

        return super.onOptionsItemSelected(item)
    }
}