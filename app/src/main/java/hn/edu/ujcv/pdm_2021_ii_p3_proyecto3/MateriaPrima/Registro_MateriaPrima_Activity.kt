package hn.edu.ujcv.pdm_2021_ii_p3_proyecto3.MateriaPrima

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.ArrayAdapter
import android.widget.Toast
import com.google.gson.Gson
import hn.edu.ujcv.pdm_2021_ii_p3_proyecto3.Almacen.AlmacenService
import hn.edu.ujcv.pdm_2021_ii_p3_proyecto3.MenuPrincipal.MenuActivity
import hn.edu.ujcv.pdm_2021_ii_p3_proyecto3.Proveedores.ProveedorService
import hn.edu.ujcv.pdm_2021_ii_p3_proyecto3.R
import hn.edu.ujcv.pdm_2021_ii_p3_proyecto3.RestEngine
import hn.edu.ujcv.pdm_2021_ii_p3_proyecto3.Toolbar.MyToolbar
import hn.edu.ujcv.pdm_2021_ii_p3_proyecto3.entities.AlmacenDataCollectionItem
import hn.edu.ujcv.pdm_2021_ii_p3_proyecto3.entities.MateriaPrimaDataCollectionItem
import hn.edu.ujcv.pdm_2021_ii_p3_proyecto3.entities.ProveedorDataCollectionItem
import hn.edu.ujcv.pdm_2021_ii_p3_proyecto3.entities.RestApiError
import kotlinx.android.synthetic.main.activity_registro_materia_prima.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Registro_MateriaPrima_Activity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registro_materia_prima)
        btnGuardarMateria.setOnClickListener { callServicePostMateria() }
        btnActualizarMateria.setOnClickListener { callServicePutMateria() }
        btnBuscarMateria.setOnClickListener { callServiceGetMateria() }

        MyToolbar().show(this,"Registrar Materia Prima", false)
    }

    private fun callServicePutMateria() {
        val materiaInfo = MateriaPrimaDataCollectionItem(
            materiaprimaId = txtMateriaPrimaID.text.toString().toInt(),
            nombreMateria =  txtMateriaNombre.text.toString(),
            proveedorId =    spMateriaProveedor.selectedItem.toString().toInt(),
            almacenId =      spMateriaAlmacen.selectedItem.toString().toInt(),
            descripcion =    txtMateriaDescripcion.text.toString(),
            cantidad =       txtCantidadMateria.text.toString().toInt(),
        )

        val retrofit = RestEngine.buildService().create(MateriaPrimaService::class.java)
        var result: Call<MateriaPrimaDataCollectionItem> = retrofit.updateMateria(materiaInfo)

        result.enqueue(object : Callback<MateriaPrimaDataCollectionItem> {
            override fun onFailure(call: Call<MateriaPrimaDataCollectionItem>, t: Throwable) {
                Toast.makeText(this@Registro_MateriaPrima_Activity,"Error", Toast.LENGTH_LONG).show()
            }

            override fun onResponse(call: Call<MateriaPrimaDataCollectionItem>,
                                    response: Response<MateriaPrimaDataCollectionItem>
            ) {
                if (response.isSuccessful) {
                    val updatedProducto = response.body()!!
                    Toast.makeText(this@Registro_MateriaPrima_Activity,"OK"+response.body()!!.nombreMateria,
                        Toast.LENGTH_LONG).show()
                }
                else if (response.code() == 401){
                    Toast.makeText(this@Registro_MateriaPrima_Activity,"Sesion expirada", Toast.LENGTH_LONG).show()
                }
                else{
                    Toast.makeText(this@Registro_MateriaPrima_Activity,"Fallo al traer el item", Toast.LENGTH_LONG).show()
                }
            }

        })
    }

    private fun callServicePostMateria() {
        val materiaInfo = MateriaPrimaDataCollectionItem(
            materiaprimaId = txtMateriaPrimaID.text.toString().toInt(),
            nombreMateria =  txtMateriaNombre.text.toString(),
            proveedorId =    spMateriaProveedor.selectedItem.toString().toInt(),
            almacenId =      spMateriaAlmacen.selectedItem.toString().toInt(),
            descripcion =    txtMateriaDescripcion.text.toString(),
            cantidad =       txtCantidadMateria.text.toString().toInt(),
        )

        addMateria(materiaInfo) {
            if (it?.materiaprimaId != null) {
                Toast.makeText(this@Registro_MateriaPrima_Activity,"OK"+it?.materiaprimaId, Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(this@Registro_MateriaPrima_Activity,"Error", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun callServiceGetMateria() {
        val materiaService: MateriaPrimaService = RestEngine.buildService().create(MateriaPrimaService::class.java)
        var result: Call<MateriaPrimaDataCollectionItem> = materiaService.getMateriaById(txtMateriaPrimaID.text.toString().toInt())

        result.enqueue(object : Callback<MateriaPrimaDataCollectionItem> {
            override fun onFailure(call: Call<MateriaPrimaDataCollectionItem>, t: Throwable) {
                Toast.makeText(this@Registro_MateriaPrima_Activity,"Error", Toast.LENGTH_LONG).show()
            }

            override fun onResponse(
                call: Call<MateriaPrimaDataCollectionItem>,
                response: Response<MateriaPrimaDataCollectionItem>
            ) {
                txtMateriaPrimaID.setText(response.body()!!.materiaprimaId.toString())
                txtMateriaNombre.setText(response.body()!!.nombreMateria)
                //Falta proveedor ID
                //Falta almacen ID
                txtMateriaDescripcion.setText(response.body()!!.descripcion)
                txtCantidadMateria.setText(response.body()!!.cantidad.toString())
                Toast.makeText(this@Registro_MateriaPrima_Activity,"OK"+response.body()!!.nombreMateria,
                    Toast.LENGTH_LONG).show()
            }
        })
    }

    fun addMateria(materiaData: MateriaPrimaDataCollectionItem, onResult: (MateriaPrimaDataCollectionItem?) -> Unit){
        val retrofit = RestEngine.buildService().create(MateriaPrimaService::class.java)
        var result: Call<MateriaPrimaDataCollectionItem> = retrofit.addMateria(materiaData)

        result.enqueue(object : Callback<MateriaPrimaDataCollectionItem> {
            override fun onFailure(call: Call<MateriaPrimaDataCollectionItem>, t: Throwable) {
                onResult(null)
            }

            override fun onResponse(call: Call<MateriaPrimaDataCollectionItem>,
                                    response: Response<MateriaPrimaDataCollectionItem>
            ) {
                if (response.isSuccessful) {
                    val addedMateria = response.body()!!
                    onResult(addedMateria)
                }
                /*else if (response.code() == 401){
                    Toast.makeText(this@MainActivity,"Sesion expirada",Toast.LENGTH_LONG).show()
                }*/
                else if (response.code() == 500){
                    //val gson = Gson()
                    //val type = object : TypeToken<RestApiError>() {}.type
                    //var errorResponse1: RestApiError? = gson.fromJson(response.errorBody()!!.charStream(), type)
                    val errorResponse = Gson().fromJson(response.errorBody()!!.string()!!, RestApiError::class.java)

                    Toast.makeText(this@Registro_MateriaPrima_Activity,errorResponse.errorDetails, Toast.LENGTH_LONG).show()
                }
                else{
                    Toast.makeText(this@Registro_MateriaPrima_Activity,"Fallo al traer el item", Toast.LENGTH_LONG).show()
                }
            }

        }
        )
    }

    private fun callServiceGetProveedores() {
        val proveedorService: ProveedorService = RestEngine.buildService().create(ProveedorService::class.java)
        var result: Call<List<ProveedorDataCollectionItem>> = proveedorService.listProveedor()
        var proveedores = ArrayList<String>()

        result.enqueue(object :  Callback<List<ProveedorDataCollectionItem>> {
            override fun onFailure(call: Call<List<ProveedorDataCollectionItem>>, t: Throwable) {
                Toast.makeText(this@Registro_MateriaPrima_Activity,"Error al encontrar proveedores",Toast.LENGTH_LONG).show()
            }

            override fun onResponse(
                call: Call<List<ProveedorDataCollectionItem>>,
                response: Response<List<ProveedorDataCollectionItem>>
            ) {
                for (proveedor in response.body()!!){
                    proveedores.add("${proveedor.proveedorId}")
                }

                val adapterEmpleados = ArrayAdapter(this@Registro_MateriaPrima_Activity, android.R.layout.simple_spinner_item, proveedores)
                spMateriaProveedor.adapter = adapterEmpleados

                Toast.makeText(this@Registro_MateriaPrima_Activity,"Proveedores encontrados",Toast.LENGTH_LONG).show()
            }
        })
    }

    private fun callServiceGetAlmacenes() {
        val almacenService: AlmacenService = RestEngine.buildService().create(AlmacenService::class.java)
        var result: Call<List<AlmacenDataCollectionItem>> = almacenService.listAlmacen()
        var almacenes = ArrayList<String>()

        result.enqueue(object :  Callback<List<AlmacenDataCollectionItem>> {
            override fun onFailure(call: Call<List<AlmacenDataCollectionItem>>, t: Throwable) {
                Toast.makeText(this@Registro_MateriaPrima_Activity,"Error al encontrar almacenes",Toast.LENGTH_LONG).show()
            }

            override fun onResponse(
                call: Call<List<AlmacenDataCollectionItem>>,
                response: Response<List<AlmacenDataCollectionItem>>
            ) {
                for (almacen in response.body()!!){
                    almacenes.add("${almacen.almacenId}")
                }

                val adapterEmpleados = ArrayAdapter(this@Registro_MateriaPrima_Activity, android.R.layout.simple_spinner_item, almacenes)
                spMateriaAlmacen.adapter = adapterEmpleados

                Toast.makeText(this@Registro_MateriaPrima_Activity,"Almacenes encontrados",Toast.LENGTH_LONG).show()
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_contextual, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        if (item.itemId == R.id.option_one)
            startActivity(Intent(this, Registro_MateriaPrima_Activity::class.java))
        if (item.itemId == R.id.option_two)
            startActivity(Intent(this, Buscar_Materia_Prima_Activity::class.java))
        if (item.itemId == R.id.option_three)
            startActivity(Intent(this, MenuActivity::class.java))

        return super.onOptionsItemSelected(item)
    }
}