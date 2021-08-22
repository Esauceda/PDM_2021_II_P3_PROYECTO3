package hn.edu.ujcv.pdm_2021_ii_p3_proyecto3.Proveedores

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import com.google.gson.Gson
import hn.edu.ujcv.pdm_2021_ii_p3_proyecto3.MenuPrincipal.MenuActivity
import kotlinx.android.synthetic.main.activity_buscar_proveedores.*
import hn.edu.ujcv.pdm_2021_ii_p3_proyecto3.R
import hn.edu.ujcv.pdm_2021_ii_p3_proyecto3.RestEngine
import hn.edu.ujcv.pdm_2021_ii_p3_proyecto3.Toolbar.MyToolbar
import hn.edu.ujcv.pdm_2021_ii_p3_proyecto3.entities.ProveedorDataCollectionItem
import hn.edu.ujcv.pdm_2021_ii_p3_proyecto3.entities.RestApiError
import kotlinx.android.synthetic.main.activity_registro_proveedores.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Registro_Proveedores_Activity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registro_proveedores)

        MyToolbar().show(this,"Registrar Proveedores", false)
        btnGuardarProveedor.setOnClickListener { callServicePostProveedor() }
        btnBuscarProveedor.setOnClickListener { callServiceGetProveedor() }
        btnActuProveedor.setOnClickListener { callServicePutProveedor() }
    }
    //-----

    private fun callServicePutProveedor() {
        val proveedorInfo = ProveedorDataCollectionItem(
            proveedorId = txtProveedorID.text.toString().toLong(),
            nombreCompania= txtNombreCompañiaProveedor.text.toString(),
            nombreContacto = txtContactoProveedor.text.toString(),
            numero = txtTelefonoProveedor.text.toString().toInt(),
            correo = txtCorreoProveedor.text.toString(),
            pais = txtPaisProveedor.text.toString(),
            direccion = txtDireccionProveedor.text.toString()
        )

        val retrofit = RestEngine.buildService().create(ProveedorService::class.java)
        var result: Call<ProveedorDataCollectionItem> = retrofit.updateProveedor(proveedorInfo)

        result.enqueue(object : Callback<ProveedorDataCollectionItem> {
            override fun onFailure(call: Call<ProveedorDataCollectionItem>, t: Throwable) {
                Toast.makeText(this@Registro_Proveedores_Activity,"Error al actualizar el proveedor",Toast.LENGTH_SHORT).show()
            }

            override fun onResponse(call: Call<ProveedorDataCollectionItem>,
                                    response: Response<ProveedorDataCollectionItem>) {
                if (response.isSuccessful) {
                    val updatedPerson = response.body()!!
                    Toast.makeText(this@Registro_Proveedores_Activity,
                        "Proveedor Actualizado",Toast.LENGTH_SHORT).show()
                }
                else if (response.code() == 401){
                    Toast.makeText(this@Registro_Proveedores_Activity,"Sesion expirada",Toast.LENGTH_SHORT).show()
                }
                else{
                    Toast.makeText(this@Registro_Proveedores_Activity,"Fallo al traer el item",Toast.LENGTH_SHORT).show()
                }
            }

        })
    }


    private fun callServiceGetProveedor() {
        val proveedorService:ProveedorService = RestEngine.buildService().create(ProveedorService::class.java)
        var result: Call<ProveedorDataCollectionItem> = proveedorService.getProveedorById(txtProveedorID.text.toString().toLong())

        result.enqueue(object :  Callback<ProveedorDataCollectionItem> {
            override fun onFailure(call: Call<ProveedorDataCollectionItem>, t: Throwable) {
                Toast.makeText(this@Registro_Proveedores_Activity,"Error al encontrar el proveedor",Toast.LENGTH_SHORT).show()
            }

            override fun onResponse(
                call: Call<ProveedorDataCollectionItem>,
                response: Response<ProveedorDataCollectionItem>
            ) {
                if (response.code() == 404){
                    Toast.makeText(this@Registro_Proveedores_Activity, "Proveedores no existe",Toast.LENGTH_SHORT).show()
                }else {
                    txtProveedorID.setText(response.body()!!.proveedorId.toString())
                    txtNombreCompañiaProveedor.setText(response.body()!!.nombreCompania)
                    txtContactoProveedor.setText(response.body()!!.nombreContacto)
                    txtTelefonoProveedor.setText(response.body()!!.numero.toString())
                    txtCorreoProveedor.setText(response.body()!!.correo)
                    txtPaisProveedor.setText(response.body()!!.pais)
                    txtDireccionProveedor.setText(response.body()!!.direccion)
                    Toast.makeText(this@Registro_Proveedores_Activity,
                        "Proveedor encontrado " + response.body()!!.nombreContacto,
                        Toast.LENGTH_SHORT).show()
                }
            }
        })
    }



    private fun callServicePostProveedor() {
        val proveedorInfo = ProveedorDataCollectionItem(
            proveedorId = null,//txtProveedorID.text.toString().toLong(),
            nombreCompania= txtNombreCompañiaProveedor.text.toString(),
            nombreContacto = txtContactoProveedor.text.toString(),
            numero = txtTelefonoProveedor.text.toString().toInt(),
            correo = txtCorreoProveedor.text.toString(),
            pais = txtPaisProveedor.text.toString(),
            direccion = txtDireccionProveedor.text.toString()
        )

        addProveedor(proveedorInfo) {
            if (it?.proveedorId != null) {
                Toast.makeText(this@Registro_Proveedores_Activity,"Proveedor Registrado "+it?.proveedorId,Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this@Registro_Proveedores_Activity,"Error al registrar el proveedor ",Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun addProveedor(proveedorData: ProveedorDataCollectionItem, onResult: (ProveedorDataCollectionItem?)->Unit){
        val retrofit = RestEngine.buildService().create(ProveedorService::class.java)
        var result: Call<ProveedorDataCollectionItem> = retrofit.addProveedor(proveedorData)

        result.enqueue(object : Callback<ProveedorDataCollectionItem> {
            override fun onFailure(call: Call<ProveedorDataCollectionItem>, t: Throwable) {
                onResult(null)
            }

            override fun onResponse(call: Call<ProveedorDataCollectionItem>, response: Response<ProveedorDataCollectionItem>) {
                if (response.isSuccessful) {
                    val addedProveedor = response.body()!!
                    onResult(addedProveedor)
                }
                else if (response.code() == 500){
                    val errorResponse = Gson().fromJson(response.errorBody()!!.string()!!, RestApiError::class.java)
                    Toast.makeText(this@Registro_Proveedores_Activity,errorResponse.errorDetails, Toast.LENGTH_SHORT).show()
                }
                else{
                    Toast.makeText(this@Registro_Proveedores_Activity,"Fallo al traer el item", Toast.LENGTH_SHORT).show()
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
            startActivity(Intent(this, Registro_Proveedores_Activity::class.java))
        if (item.itemId == R.id.option_two)
            startActivity(Intent(this, Buscar_Proveedores_Activity::class.java))
        if (item.itemId == R.id.option_three)
            startActivity(Intent(this, MenuActivity::class.java))

        return super.onOptionsItemSelected(item)
    }

    //-----

}