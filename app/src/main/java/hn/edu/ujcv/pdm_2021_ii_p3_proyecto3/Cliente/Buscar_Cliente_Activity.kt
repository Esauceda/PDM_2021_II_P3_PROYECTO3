package hn.edu.ujcv.pdm_2021_ii_p3_proyecto3.Cliente

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
import hn.edu.ujcv.pdm_2021_ii_p3_proyecto3.entities.ClienteDataCollectionItem
import kotlinx.android.synthetic.main.activity_buscar_cliente.*
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Buscar_Cliente_Activity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_buscar_cliente)
        btnBuscarCli.setOnClickListener { callServiceGetCliente() }
        btnEliminarCli.setOnClickListener { callServiceDeleteCliente() }

        MyToolbar().show(this,"Buscar Cliente", false)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_contextual, menu)
        return super.onCreateOptionsMenu(menu)
    }

    private fun callServiceGetCliente() {
        val clienteService: ClienteService = RestEngine.buildService().create(ClienteService::class.java)
        var result: Call<ClienteDataCollectionItem> = clienteService.getClienteById(txtMostrarClienteId.text.toString().toInt())

        result.enqueue(object : Callback<ClienteDataCollectionItem> {
            override fun onFailure(call: Call<ClienteDataCollectionItem>, t: Throwable) {
                Toast.makeText(this@Buscar_Cliente_Activity,"Error", Toast.LENGTH_LONG).show()
            }

            override fun onResponse(
                call: Call<ClienteDataCollectionItem>,
                response: Response<ClienteDataCollectionItem>
            ) {
                txtMostrarClienteId.setText(response.body()!!.clienteId.toString())
                txvMostrarCompCli.setText(response.body()!!.nombreCompania)
                txvMostrarNomCli.setText(response.body()!!.nombre)
                txvMostrarTelCli.setText(response.body()!!.telefono.toString())
                txvMostrarCorrCli.setText(response.body()!!.correo)
                txvMostrarPaisCli.setText(response.body()!!.pais)
                txvMostrarDirCli.setText(response.body()!!.direccion)
                txvMostrarCatCli.setText(response.body()!!.categoria)
                Toast.makeText(this@Buscar_Cliente_Activity,"OK"+response.body()!!.nombre,
                    Toast.LENGTH_LONG).show()
            }
        })
    }

    private fun callServiceDeleteCliente() {
        val clienteService: ClienteService = RestEngine.buildService().create(ClienteService::class.java)
        var result: Call<ResponseBody> = clienteService.deleteCliente(txtMostrarClienteId.text.toString().toInt())

        result.enqueue(object : Callback<ResponseBody> {
            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Toast.makeText(this@Buscar_Cliente_Activity,"Error", Toast.LENGTH_LONG).show()
            }

            override fun onResponse(
                call: Call<ResponseBody>,
                response: Response<ResponseBody>
            ) {
                if (response.isSuccessful) {
                    Toast.makeText(this@Buscar_Cliente_Activity,"DELETE", Toast.LENGTH_LONG).show()
                }
                else if (response.code() == 401){
                    Toast.makeText(this@Buscar_Cliente_Activity,"Sesion expirada", Toast.LENGTH_LONG).show()
                }
                else{
                    Toast.makeText(this@Buscar_Cliente_Activity,"Fallo al traer el item", Toast.LENGTH_LONG).show()
                }
            }
        })
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        if (item.itemId == R.id.option_one)
            startActivity(Intent(this, Registro_Clientes_Activity::class.java))
        if (item.itemId == R.id.option_two)
            startActivity(Intent(this, Buscar_Cliente_Activity::class.java))
        if (item.itemId == R.id.option_three)
            startActivity(Intent(this, MenuActivity::class.java))

        return super.onOptionsItemSelected(item)
    }
}