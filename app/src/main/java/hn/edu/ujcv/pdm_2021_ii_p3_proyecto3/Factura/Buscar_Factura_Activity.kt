package hn.edu.ujcv.pdm_2021_ii_p3_proyecto3.Factura

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import hn.edu.ujcv.pdm_2021_ii_p3_proyecto3.GetAllActivity
import hn.edu.ujcv.pdm_2021_ii_p3_proyecto3.MenuPrincipal.MenuActivity
import hn.edu.ujcv.pdm_2021_ii_p3_proyecto3.R
import hn.edu.ujcv.pdm_2021_ii_p3_proyecto3.RestEngine
import hn.edu.ujcv.pdm_2021_ii_p3_proyecto3.Toolbar.MyToolbar
import hn.edu.ujcv.pdm_2021_ii_p3_proyecto3.entities.FacturaDataCollectionItem
import kotlinx.android.synthetic.main.activity_buscar_factura.*
import kotlinx.android.synthetic.main.activity_registro_factura.*
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Buscar_Factura_Activity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_buscar_factura)

        MyToolbar().show(this,"Buscar Factura", false)
        btnBuscarFac2.setOnClickListener { callServiceGetFactura() }
        btnEliminarFac.setOnClickListener { callServiceDeleteFactura() }
        btnMostrarTodosFacturas.setOnClickListener{ mostrarFacturas() }
    }

    private fun mostrarFacturas() {
        intent = Intent(this@Buscar_Factura_Activity, GetAllActivity::class.java)
        intent.putExtra("numero", 7)
        startActivity(intent)
    }

    //-----

    //GET
    private fun callServiceGetFactura() {
        val facturaService:FacturaService = RestEngine.buildService().create(FacturaService::class.java)
        var result: Call<FacturaDataCollectionItem> = facturaService.getFacturaById(txtMostrarFacturaId.text.toString().toLong())

        result.enqueue(object : Callback<FacturaDataCollectionItem> {
            override fun onFailure(call: Call<FacturaDataCollectionItem>, t: Throwable) {
                Toast.makeText(this@Buscar_Factura_Activity,"Error al buscar la factura", Toast.LENGTH_SHORT).show()
            }

            override fun onResponse(
                call: Call<FacturaDataCollectionItem>,
                response: Response<FacturaDataCollectionItem>
            ) {
                if (response.code() == 404){
                    Toast.makeText(this@Buscar_Factura_Activity, "Factura no existe",Toast.LENGTH_SHORT).show()
                }else {
                    txtMostrarFacturaId.setText(response.body()!!.facturaId.toString())
                    txvMostrarFacOrdenId.setText(response.body()!!.ordenId.toString())
                    txvMostrarFechaFac.setText(response.body()!!.fechaFactura)
                    txvMostrarTotalFac.setText(response.body()!!.total.toString())
                    Toast.makeText(this@Buscar_Factura_Activity,
                        "Factura encontrada " + response.body()!!.facturaId,
                        Toast.LENGTH_SHORT).show()
                }
            }
        })
    }

    //DELETE

    private fun callServiceDeleteFactura() {
        val facturaService:FacturaService = RestEngine.buildService().create(FacturaService::class.java)
        var result: Call<ResponseBody> = facturaService.deleteFactura(txtMostrarFacturaId.text.toString().toLong())

        result.enqueue(object :  Callback<ResponseBody> {
            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Toast.makeText(this@Buscar_Factura_Activity,"Error al eliminar la factura",Toast.LENGTH_SHORT).show()
            }

            override fun onResponse(
                call: Call<ResponseBody>,
                response: Response<ResponseBody>
            ) {
                if (response.isSuccessful) {
                    Toast.makeText(this@Buscar_Factura_Activity,"Factura Eliminada",Toast.LENGTH_SHORT).show()
                }
                else if (response.code() == 401){
                    Toast.makeText(this@Buscar_Factura_Activity,"Sesion expirada",Toast.LENGTH_SHORT).show()
                }
                else{
                    Toast.makeText(this@Buscar_Factura_Activity,"Fallo al traer el item",Toast.LENGTH_SHORT).show()
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
            startActivity(Intent(this, Registro_Factura_Activity::class.java))
        if (item.itemId == R.id.option_two)
            startActivity(Intent(this, Buscar_Factura_Activity::class.java))
        if (item.itemId == R.id.option_three)
            startActivity(Intent(this, MenuActivity::class.java))

        return super.onOptionsItemSelected(item)
    }
}