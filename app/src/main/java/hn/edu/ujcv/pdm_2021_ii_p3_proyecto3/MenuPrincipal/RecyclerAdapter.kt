package hn.edu.ujcv.pdm_2021_ii_p3_proyecto3.MenuPrincipal

import android.content.Intent
import androidx.recyclerview.widget.RecyclerView
import  android.view.LayoutInflater
import android.widget.ImageView
import android.widget.TextView
import android.view.View
import android.view.ViewGroup
import com.google.android.material.snackbar.Snackbar
import hn.edu.ujcv.pdm_2021_ii_p3_proyecto3.Almacen.Registro_Almacen_Activity
import hn.edu.ujcv.pdm_2021_ii_p3_proyecto3.Cliente.Registro_Clientes_Activity
import hn.edu.ujcv.pdm_2021_ii_p3_proyecto3.Compra.Registro_CompraEncabezado_Activity
import hn.edu.ujcv.pdm_2021_ii_p3_proyecto3.Delivery.Registro_Delivery_Activity
import hn.edu.ujcv.pdm_2021_ii_p3_proyecto3.Empleado.Actualizar_Empleado_Activity
import hn.edu.ujcv.pdm_2021_ii_p3_proyecto3.Fabrica.Registro_Fabrica_Activity
import hn.edu.ujcv.pdm_2021_ii_p3_proyecto3.Factura.Registro_Factura_Activity
import hn.edu.ujcv.pdm_2021_ii_p3_proyecto3.Maquinaria.Registro_Maquinaria_Activity
import hn.edu.ujcv.pdm_2021_ii_p3_proyecto3.MateriaPrima.Registro_MateriaPrima_Activity
import hn.edu.ujcv.pdm_2021_ii_p3_proyecto3.Orden.Registro_OrdenEncabezado_Activity
import hn.edu.ujcv.pdm_2021_ii_p3_proyecto3.Producto.Registro_Producto_Activity
import hn.edu.ujcv.pdm_2021_ii_p3_proyecto3.Proveedores.Registro_Proveedores_Activity
import hn.edu.ujcv.pdm_2021_ii_p3_proyecto3.R

class RecyclerAdapter: RecyclerView.Adapter<RecyclerAdapter.ViewHolder>() {
    private val titles = arrayOf(
        "Almacen", "Clientes", "Compras", "Delivery", "Empleado", "Fabricas", "Factura"
        ,"Maquinaria","Materia Prima","Ordenes","Productos","Proveedores"
    )


    private val images = arrayOf(
        R.drawable.almacen, R.drawable.cliente,
        R.drawable.compra,R.drawable.delivery,
        R.drawable.empleado,R.drawable.fabrica,
        R.drawable.factura,R.drawable.maquinaria,
        R.drawable.materiaprima,R.drawable.orden,
        R.drawable.productos,R.drawable.proveedores
    )

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.card_layout, viewGroup, false)
        return ViewHolder(v)
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var itemImage: ImageView
        var itemTitle: TextView
        val prueba = itemView.context
        init{
            itemImage = itemView.findViewById(R.id.item_image)
            itemTitle = itemView.findViewById(R.id.item_title)

            itemView.setOnClickListener { v: View ->
                var position: Int = getAdapterPosition()
                Snackbar.make(v, "Click en el item $position", Snackbar.LENGTH_LONG).setAction("Action", null).show()
                if (position == 0){
                    val intent = Intent(prueba, Registro_Almacen_Activity::class.java)
                    prueba.startActivity(intent)
                }else if (position == 1){
                    val intent = Intent(prueba, Registro_Clientes_Activity::class.java)
                    prueba.startActivity(intent)
                }else if (position == 2){
                    val intent = Intent(prueba, Registro_CompraEncabezado_Activity::class.java)
                    prueba.startActivity(intent)
                }else if (position == 3){
                    val intent = Intent(prueba, Registro_Delivery_Activity::class.java)
                    prueba.startActivity(intent)
                }else if (position == 4){
                    val intent = Intent(prueba, Actualizar_Empleado_Activity::class.java)
                    prueba.startActivity(intent)
                }else if (position == 5){
                    val intent = Intent(prueba, Registro_Fabrica_Activity::class.java)
                    prueba.startActivity(intent)
                }else if (position == 6){
                    val intent = Intent(prueba, Registro_Factura_Activity::class.java)
                    prueba.startActivity(intent)
                }else if (position == 7){
                    val intent = Intent(prueba, Registro_Maquinaria_Activity::class.java)
                    prueba.startActivity(intent)
                }else if (position == 8){
                    val intent = Intent(prueba, Registro_MateriaPrima_Activity::class.java)
                    prueba.startActivity(intent)
                }else if (position == 9){
                    val intent = Intent(prueba, Registro_OrdenEncabezado_Activity::class.java)
                    prueba.startActivity(intent)
                }else if (position == 10){
                    val intent = Intent(prueba, Registro_Producto_Activity::class.java)
                    prueba.startActivity(intent)
                }else if (position == 11){
                    val intent = Intent(prueba, Registro_Proveedores_Activity::class.java)
                    prueba.startActivity(intent)
                }
            }
        }

    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        viewHolder.itemTitle.text = titles[position]
        viewHolder.itemImage.setImageResource(images[position])
    }


    override fun getItemCount(): Int {
        return titles.size
    }
}