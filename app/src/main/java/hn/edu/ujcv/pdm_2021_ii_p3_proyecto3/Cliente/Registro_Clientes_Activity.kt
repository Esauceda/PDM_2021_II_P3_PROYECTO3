package hn.edu.ujcv.pdm_2021_ii_p3_proyecto3.Cliente

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
import hn.edu.ujcv.pdm_2021_ii_p3_proyecto3.entities.ClienteDataCollectionItem
import hn.edu.ujcv.pdm_2021_ii_p3_proyecto3.entities.RestApiError
import kotlinx.android.synthetic.main.activity_registro_clientes.*
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Registro_Clientes_Activity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registro_clientes)
        btnGuardarCliente.setOnClickListener { callServicePostCliente() }
        btnActuCliente.setOnClickListener { callServicePutCliente() }
        btnBuscarCliente.setOnClickListener { callServiceGetCliente() }

        MyToolbar().show(this,"Registro Cliente", false)
    }

    private fun callServicePutCliente() {
        val clienteInfo = ClienteDataCollectionItem(
            clienteId =       txtClienteID.text.toString().toInt(),
            nombreCompania =  txtNombreCompaniaCli.text.toString(),
            nombre =          txtNombreCli.text.toString(),
            telefono =        txtTelefonoCli.text.toString().toInt(),
            correo =          txtCorreoCli.text.toString(),
            pais =            txtPais.text.toString(),
            direccion =       txtDireccionCli.text.toString(),
            categoria =       txtCategoriaCli.text.toString()
        )

        val retrofit = RestEngine.buildService().create(ClienteService::class.java)
        var result: Call<ClienteDataCollectionItem> = retrofit.updateCliente(clienteInfo)

        result.enqueue(object : Callback<ClienteDataCollectionItem> {
            override fun onFailure(call: Call<ClienteDataCollectionItem>, t: Throwable) {
                Toast.makeText(this@Registro_Clientes_Activity,"Error al actualizar el cliente", Toast.LENGTH_SHORT).show()
            }

            override fun onResponse(call: Call<ClienteDataCollectionItem>,
                                    response: Response<ClienteDataCollectionItem>
            ) {
                if (response.isSuccessful) {
                    val updatedAlmacen = response.body()!!
                    Toast.makeText(this@Registro_Clientes_Activity,"Cliente Actualizado "+response.body()!!.nombre,
                        Toast.LENGTH_LONG).show()
                }
                else if (response.code() == 401){
                    Toast.makeText(this@Registro_Clientes_Activity,"Sesion expirada", Toast.LENGTH_SHORT).show()
                }
                else{
                    Toast.makeText(this@Registro_Clientes_Activity,"Fallo al traer el item", Toast.LENGTH_SHORT).show()
                }
            }

        })
    }

    private fun callServicePostCliente() {
        val clienteInfo = ClienteDataCollectionItem(
            clienteId =       null,//txtClienteID.text.toString().toInt(),
            nombreCompania =  txtNombreCompaniaCli.text.toString(),
            nombre =          txtNombreCli.text.toString(),
            telefono =        txtTelefonoCli.text.toString().toInt(),
            correo =          txtCorreoCli.text.toString(),
            pais =            txtPais.text.toString(),
            direccion =       txtDireccionCli.text.toString(),
            categoria =       txtCategoriaCli.text.toString()
        )

        addCliente(clienteInfo) {
            if (it?.clienteId != null) {
                Toast.makeText(this@Registro_Clientes_Activity,"Cliente registrado "+it?.clienteId, Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this@Registro_Clientes_Activity,"Error al registrar el cliente", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun callServiceGetCliente() {
        val clienteService: ClienteService = RestEngine.buildService().create(ClienteService::class.java)
        var result: Call<ClienteDataCollectionItem> = clienteService.getClienteById(txtClienteID.text.toString().toInt())

        result.enqueue(object : Callback<ClienteDataCollectionItem> {
            override fun onFailure(call: Call<ClienteDataCollectionItem>, t: Throwable) {
                Toast.makeText(this@Registro_Clientes_Activity,"Error al encontrar el cliente", Toast.LENGTH_SHORT).show()
            }

            override fun onResponse(
                call: Call<ClienteDataCollectionItem>,
                response: Response<ClienteDataCollectionItem>
            ) {
                if (response.code() == 404){
                    Toast.makeText(this@Registro_Clientes_Activity, "Cliente no existe",Toast.LENGTH_SHORT).show()
                }else {
                    txtClienteID.setText(response.body()!!.clienteId.toString())
                    txtNombreCompaniaCli.setText(response.body()!!.nombreCompania)
                    txtNombreCli.setText(response.body()!!.nombre)
                    txtTelefonoCli.setText(response.body()!!.telefono.toString())
                    txtCorreoCli.setText(response.body()!!.correo)
                    txtPais.setText(response.body()!!.pais)
                    txtDireccionCli.setText(response.body()!!.direccion)
                    txtCategoriaCli.setText(response.body()!!.categoria)
                    Toast.makeText(this@Registro_Clientes_Activity, "Cliente encontrado " + response.body()!!.nombre,
                        Toast.LENGTH_SHORT).show()
                }
            }
        })
    }

    fun addCliente(clienteData: ClienteDataCollectionItem, onResult: (ClienteDataCollectionItem?) -> Unit){
        val retrofit = RestEngine.buildService().create(ClienteService::class.java)
        var result: Call<ClienteDataCollectionItem> = retrofit.addCliente(clienteData)

        result.enqueue(object : Callback<ClienteDataCollectionItem> {
            override fun onFailure(call: Call<ClienteDataCollectionItem>, t: Throwable) {
                onResult(null)
            }

            override fun onResponse(call: Call<ClienteDataCollectionItem>,
                                    response: Response<ClienteDataCollectionItem>
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

                    Toast.makeText(this@Registro_Clientes_Activity,errorResponse.errorDetails, Toast.LENGTH_SHORT).show()
                }
                else{
                    Toast.makeText(this@Registro_Clientes_Activity,"Fallo al traer el item", Toast.LENGTH_SHORT).show()
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
            startActivity(Intent(this, Registro_Clientes_Activity::class.java))
        if (item.itemId == R.id.option_two)
            startActivity(Intent(this, Buscar_Cliente_Activity::class.java))
        if (item.itemId == R.id.option_three)
            startActivity(Intent(this, MenuActivity::class.java))

        return super.onOptionsItemSelected(item)
    }
}