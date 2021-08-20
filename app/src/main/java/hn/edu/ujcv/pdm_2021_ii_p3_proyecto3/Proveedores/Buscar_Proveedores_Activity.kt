package hn.edu.ujcv.pdm_2021_ii_p3_proyecto3.Proveedores

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
import hn.edu.ujcv.pdm_2021_ii_p3_proyecto3.entities.ProveedorDataCollectionItem
import kotlinx.android.synthetic.main.activity_buscar_proveedores.*
import kotlinx.android.synthetic.main.activity_registro_proveedores.*
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Buscar_Proveedores_Activity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_buscar_proveedores)

        MyToolbar().show(this,"Buscar Proveedores", false)
        btnEliminarProveedor.setOnClickListener { callServiceDeleteProveedor() }
        btnBuscarProveedor2.setOnClickListener { callServiceGetProveedor() }
    }

    //-----
    private fun callServiceGetProveedor() {
        val proveedorService:ProveedorService = RestEngine.buildService().create(ProveedorService::class.java)
        var result: Call<ProveedorDataCollectionItem> = proveedorService.getProveedorById(txtProveedorID2.text.toString().toLong())

        result.enqueue(object :  Callback<ProveedorDataCollectionItem> {
            override fun onFailure(call: Call<ProveedorDataCollectionItem>, t: Throwable) {
                Toast.makeText(this@Buscar_Proveedores_Activity,"Error",Toast.LENGTH_LONG).show()
            }

            override fun onResponse(
                call: Call<ProveedorDataCollectionItem>,
                response: Response<ProveedorDataCollectionItem>
            ) {
                if (response.code() == 404){
                    Toast.makeText(this@Buscar_Proveedores_Activity, "Proveedores no existe",Toast.LENGTH_SHORT).show()
                }else {
                    txtProveedorID2.setText(response.body()!!.proveedorId.toString())
                    txvMostrarNombreProveedor.setText(response.body()!!.nombreCompania)
                    txvMostrarNombreContactoProveedor.setText(response.body()!!.nombreContacto)
                    txvMostrarTelProveedor.setText(response.body()!!.numero.toString())
                    txvMostrarCorrProveedor.setText(response.body()!!.correo)
                    txvMostrarPaisProveedor.setText(response.body()!!.pais)
                    txvMostrarDireccionProveedor.setText(response.body()!!.direccion)
                    Toast.makeText(this@Buscar_Proveedores_Activity,
                        "OK" + response.body()!!.nombreCompania,
                        Toast.LENGTH_LONG).show()
                }
            }
        })
    }

    private fun callServiceDeleteProveedor() {
        val proveedorService:ProveedorService = RestEngine.buildService().create(ProveedorService::class.java)
        var result: Call<ResponseBody> = proveedorService.deleteProveedor(txtProveedorID2.text.toString().toLong())

        result.enqueue(object : Callback<ResponseBody> {
            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Toast.makeText(this@Buscar_Proveedores_Activity,"Error", Toast.LENGTH_LONG).show()
            }

            override fun onResponse(
                call: Call<ResponseBody>,
                response: Response<ResponseBody>
            ) {
                if (response.isSuccessful) {
                    Toast.makeText(this@Buscar_Proveedores_Activity,"DELETE", Toast.LENGTH_LONG).show()
                }
                else if (response.code() == 401){
                    Toast.makeText(this@Buscar_Proveedores_Activity,"Sesion expirada", Toast.LENGTH_LONG).show()
                }
                else{
                    Toast.makeText(this@Buscar_Proveedores_Activity,"Fallo al traer el item", Toast.LENGTH_LONG).show()
                }
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
            startActivity(Intent(this, Registro_Proveedores_Activity::class.java))
        if (item.itemId == R.id.option_two)
            startActivity(Intent(this, Buscar_Proveedores_Activity::class.java))
        if (item.itemId == R.id.option_three)
            startActivity(Intent(this, MenuActivity::class.java))

        return super.onOptionsItemSelected(item)
    }
}