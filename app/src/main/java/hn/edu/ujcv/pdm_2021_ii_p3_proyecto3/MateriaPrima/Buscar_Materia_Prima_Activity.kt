package hn.edu.ujcv.pdm_2021_ii_p3_proyecto3.MateriaPrima

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
import hn.edu.ujcv.pdm_2021_ii_p3_proyecto3.entities.MateriaPrimaDataCollectionItem
import kotlinx.android.synthetic.main.activity_buscar_materia_prima.*
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Buscar_Materia_Prima_Activity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_buscar_materia_prima)
        btnBuscarMateria.setOnClickListener { callServiceGetMateria() }
        btnEliminarMateria.setOnClickListener { callServiceDeleteMateria() }
        btnMostrarTodosMateriaPrima.setOnClickListener{ mostarMateriasPrimas() }
        MyToolbar().show(this,"Buscar Materia Prima", false)
    }

    private fun mostarMateriasPrimas() {
        intent = Intent(this@Buscar_Materia_Prima_Activity, GetAllActivity::class.java)
        intent.putExtra("numero", 9)
        startActivity(intent)
    }

    private fun callServiceGetMateria() {
        val materiaService: MateriaPrimaService = RestEngine.buildService().create(MateriaPrimaService::class.java)
        var result: Call<MateriaPrimaDataCollectionItem> = materiaService.getMateriaById(txtMostrarMateriaPrimaID.text.toString().toInt())

        result.enqueue(object : Callback<MateriaPrimaDataCollectionItem> {
            override fun onFailure(call: Call<MateriaPrimaDataCollectionItem>, t: Throwable) {
                Toast.makeText(this@Buscar_Materia_Prima_Activity,"Error al encontrar materia prima", Toast.LENGTH_SHORT).show()
            }

            override fun onResponse(
                call: Call<MateriaPrimaDataCollectionItem>,
                response: Response<MateriaPrimaDataCollectionItem>
            ) {
                if (response.code() == 404){
                    Toast.makeText(this@Buscar_Materia_Prima_Activity, "Materia Prima no encontrada",Toast.LENGTH_SHORT).show()
                }else {
                    txtMostrarMateriaPrimaID.setText(response.body()!!.materiaprimaId.toString())
                    txtMostrarNombreMateria.setText(response.body()!!.nombreMateria)
                    txtMostrarMateriaProveedor.setText(response.body()!!.proveedorId.toString())
                    txtMostrarMateriaAlmacen.setText(response.body()!!.almacenId.toString())
                    txtMostrarMateriaDescripcion.setText(response.body()!!.descripcion)
                    txtMostrarCantidadMateria.setText(response.body()!!.cantidad.toString())
                    Toast.makeText(this@Buscar_Materia_Prima_Activity,
                        "Materia Prima Encontrada " + response.body()!!.nombreMateria,
                        Toast.LENGTH_SHORT).show()
                }
            }
        })
    }

    private fun callServiceDeleteMateria() {
        val materiaService: MateriaPrimaService = RestEngine.buildService().create(MateriaPrimaService::class.java)
        var result: Call<ResponseBody> = materiaService.deleteMateria(txtMostrarMateriaPrimaID.text.toString().toInt())

        result.enqueue(object : Callback<ResponseBody> {
            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Toast.makeText(this@Buscar_Materia_Prima_Activity,"Error al eliminar la materia prima", Toast.LENGTH_SHORT).show()
            }

            override fun onResponse(
                call: Call<ResponseBody>,
                response: Response<ResponseBody>
            ) {
                if (response.isSuccessful) {
                    Toast.makeText(this@Buscar_Materia_Prima_Activity,"Materia prima eliminada", Toast.LENGTH_SHORT).show()
                }
                else if (response.code() == 401){
                    Toast.makeText(this@Buscar_Materia_Prima_Activity,"Sesion expirada", Toast.LENGTH_SHORT).show()
                }
                else{
                    Toast.makeText(this@Buscar_Materia_Prima_Activity,"Fallo al traer el item", Toast.LENGTH_SHORT).show()
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
            startActivity(Intent(this, Registro_MateriaPrima_Activity::class.java))
        if (item.itemId == R.id.option_two)
            startActivity(Intent(this, Buscar_Materia_Prima_Activity::class.java))
        if (item.itemId == R.id.option_three)
            startActivity(Intent(this, MenuActivity::class.java))

        return super.onOptionsItemSelected(item)
    }
}