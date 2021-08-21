package hn.edu.ujcv.pdm_2021_ii_p3_proyecto3

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import hn.edu.ujcv.pdm_2021_ii_p3_proyecto3.Almacen.AlmacenService
import hn.edu.ujcv.pdm_2021_ii_p3_proyecto3.Cliente.ClienteService
import hn.edu.ujcv.pdm_2021_ii_p3_proyecto3.Compra.CompraEncabezadoService
import hn.edu.ujcv.pdm_2021_ii_p3_proyecto3.Delivery.DeliveryService
import hn.edu.ujcv.pdm_2021_ii_p3_proyecto3.Empleado.EmpleadoService
import hn.edu.ujcv.pdm_2021_ii_p3_proyecto3.Fabrica.FabricaService
import hn.edu.ujcv.pdm_2021_ii_p3_proyecto3.Factura.FacturaService
import hn.edu.ujcv.pdm_2021_ii_p3_proyecto3.Maquinaria.MaquinariaService
import hn.edu.ujcv.pdm_2021_ii_p3_proyecto3.MateriaPrima.MateriaPrimaService
import hn.edu.ujcv.pdm_2021_ii_p3_proyecto3.Orden.OrdenEncabezadoService
import hn.edu.ujcv.pdm_2021_ii_p3_proyecto3.Producto.ProductoService
import hn.edu.ujcv.pdm_2021_ii_p3_proyecto3.Proveedores.ProveedorService
import hn.edu.ujcv.pdm_2021_ii_p3_proyecto3.entities.*
import kotlinx.android.synthetic.main.activity_get_all.*
import kotlinx.android.synthetic.main.activity_registro_compra_detalle.*
import kotlinx.android.synthetic.main.activity_registro_maquinaria.*
import kotlinx.android.synthetic.main.activity_registro_orden_detalle.*
import kotlinx.android.synthetic.main.activity_registro_orden_encabezado.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class GetAllActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_get_all)
        eleccion()
    }

    private fun eleccion() {
        if (intent.getSerializableExtra("numero") == 1){

                val almacenService: AlmacenService = RestEngine.buildService().create(AlmacenService::class.java)
                var result: Call<List<AlmacenDataCollectionItem>> = almacenService.listAlmacen()
                var almacenes = ArrayList<String>()

                result.enqueue(object : Callback<List<AlmacenDataCollectionItem>> {
                    override fun onFailure(call: Call<List<AlmacenDataCollectionItem>>, t: Throwable) {
                        Toast.makeText(this@GetAllActivity,"Error al encontrar almacenes",
                            Toast.LENGTH_LONG).show()
                    }

                    override fun onResponse(
                        call: Call<List<AlmacenDataCollectionItem>>,
                        response: Response<List<AlmacenDataCollectionItem>>
                    ) {

                        almacenes.add("ID | Direccion  | telefono ")
                        for (almacen in response.body()!!){
                            almacenes.add("${almacen.almacenId}  |  ${almacen.direccion} |  ${almacen.telefono}")
                        }

                        val adapterAlmacenes = ArrayAdapter(this@GetAllActivity, android.R.layout.simple_list_item_1, almacenes)
                        lstvGetAll.adapter = adapterAlmacenes

                        Toast.makeText(this@GetAllActivity,"Almacenes encontrado",
                            Toast.LENGTH_LONG).show()
                    }
                })

        }
        //-----
        if (intent.getSerializableExtra("numero") == 2){
                val clienteService: ClienteService = RestEngine.buildService().create(ClienteService::class.java)
                var result: Call<List<ClienteDataCollectionItem>> = clienteService.listCliente()
                var clientes = ArrayList<String>()

                result.enqueue(object :  Callback<List<ClienteDataCollectionItem>> {
                    override fun onFailure(call: Call<List<ClienteDataCollectionItem>>, t: Throwable) {
                        Toast.makeText(this@GetAllActivity,"Error al encontrar clientes",Toast.LENGTH_LONG).show()
                    }

                    override fun onResponse(
                        call: Call<List<ClienteDataCollectionItem>>,
                        response: Response<List<ClienteDataCollectionItem>>
                    ) {

                        clientes.add("ID |  Nombre  | telefono ")
                        for (cliente in response.body()!!){
                            clientes.add("${cliente.clienteId}  |  ${cliente.nombre} | ${cliente.telefono}")
                        }

                        val adapterClientes = ArrayAdapter(this@GetAllActivity, android.R.layout.simple_list_item_1, clientes)
                        lstvGetAll.adapter = adapterClientes
                        Toast.makeText(this@GetAllActivity,"Clientes encontrados",Toast.LENGTH_LONG).show()
                    }
                })

        }
        //-----

        if (intent.getSerializableExtra("numero") == 3){
            val compraEncabezadoService: CompraEncabezadoService = RestEngine.buildService().create(
                CompraEncabezadoService::class.java)
            var result: Call<List<CompraEncabezadoDataCollectionItem>> = compraEncabezadoService.listCompraEncabezado()
            var comprasEncabezado = ArrayList<String>()

            result.enqueue(object : Callback<List<CompraEncabezadoDataCollectionItem>> {
                override fun onFailure(call: Call<List<CompraEncabezadoDataCollectionItem>>, t: Throwable) {
                    Toast.makeText(this@GetAllActivity,"Error al encontrar productos",
                        Toast.LENGTH_LONG).show()
                }

                override fun onResponse(
                    call: Call<List<CompraEncabezadoDataCollectionItem>>,
                    response: Response<List<CompraEncabezadoDataCollectionItem>>
                ) {
                    comprasEncabezado.add("ID | Estado  |  Total  |  Fecha Recepcion")
                    for (compraEncabezado in response.body()!!){
                        comprasEncabezado.add("${compraEncabezado.compraId} |  ${compraEncabezado.estado} | ${compraEncabezado.total}  | ${compraEncabezado.fechaRecepcion}" )
                    }

                    val adapterCompraEncabezado = ArrayAdapter(this@GetAllActivity, android.R.layout.simple_list_item_1, comprasEncabezado)
                    lstvGetAll.adapter = adapterCompraEncabezado

                    Toast.makeText(this@GetAllActivity,"Productos encontrados", Toast.LENGTH_LONG).show()
                }
            })
        }
        //-----

        if(intent.getSerializableExtra("numero") == 4){

                val deliveryService:DeliveryService = RestEngine.buildService().create(DeliveryService::class.java)
                var result: Call<List<DeliveryDataCollecionItem>> = deliveryService.listDelivery()
                var deliveries = ArrayList<String>()

                result.enqueue(object :  Callback<List<DeliveryDataCollecionItem>> {
                    override fun onFailure(call: Call<List<DeliveryDataCollecionItem>>, t: Throwable) {
                        Toast.makeText(this@GetAllActivity,"Error al encontrar delivery",Toast.LENGTH_LONG).show()
                    }

                    override fun onResponse(
                        call: Call<List<DeliveryDataCollecionItem>>,
                        response: Response<List<DeliveryDataCollecionItem>>
                    ) {
                        deliveries.add("ID | Orden ID | Fecha Entrega")
                        for (delivery in response.body()!!){
                            deliveries.add("${delivery.deliveryId}   |   ${delivery.ordenId}  | ${delivery.fechaEntrega}")
                        }

                        var adapterDeliveries = ArrayAdapter(this@GetAllActivity, android.R.layout.simple_list_item_1, deliveries)
                        lstvGetAll.adapter = adapterDeliveries

                        Toast.makeText(this@GetAllActivity,"Delivery encontrado",Toast.LENGTH_LONG).show()
                    }
                })

        }

        if (intent.getSerializableExtra("numero") == 5){
            val empleadoService: EmpleadoService = RestEngine.buildService().create(EmpleadoService::class.java)
            var result: Call<List<EmpleadoDataCollectionItem>> = empleadoService.listEmpleado()
            var empleados = ArrayList<String>()

            result.enqueue(object :  Callback<List<EmpleadoDataCollectionItem>> {
                override fun onFailure(call: Call<List<EmpleadoDataCollectionItem>>, t: Throwable) {
                    Toast.makeText(this@GetAllActivity,"Error al encontrar empleados",Toast.LENGTH_LONG).show()
                }

                override fun onResponse(
                    call: Call<List<EmpleadoDataCollectionItem>>,
                    response: Response<List<EmpleadoDataCollectionItem>>
                ) {
                    empleados.add("ID  |  Nombre  |  Puesto")
                    for (empleado in response.body()!!){
                        empleados.add("${empleado.empleadoId}  |  ${empleado.nombre}  | ${empleado.puesto}")
                    }

                    val adapterEmpleados = ArrayAdapter(this@GetAllActivity, android.R.layout.simple_list_item_1, empleados)
                    lstvGetAll.adapter = adapterEmpleados

                    Toast.makeText(this@GetAllActivity,"Empelados encontrados",Toast.LENGTH_LONG).show()
                }
            })
        }

        if (intent.getSerializableExtra("numero") == 6){
            val fabricaService: FabricaService = RestEngine.buildService().create(FabricaService::class.java)
            var result: Call<List<FabricaDataCollectionItem>> = fabricaService.listFabricas()
            var fabricas = ArrayList<String>()

            result.enqueue(object :  Callback<List<FabricaDataCollectionItem>> {
                override fun onFailure(call: Call<List<FabricaDataCollectionItem>>, t: Throwable) {
                    Toast.makeText(this@GetAllActivity,"Error al encontrar fabricas",Toast.LENGTH_SHORT).show()
                }

                override fun onResponse(
                    call: Call<List<FabricaDataCollectionItem>>,
                    response: Response<List<FabricaDataCollectionItem>>
                ) {
                    fabricas.add("ID  |  Direccion  |  Telefono")
                    for (fabrica in response.body()!!){
                        fabricas.add("${fabrica.fabricaId} | ${fabrica.direccion} | ${fabrica.telefono}")
                    }

                    val adapterFabricas = ArrayAdapter(this@GetAllActivity, android.R.layout.simple_list_item_1, fabricas)
                    lstvGetAll.adapter = adapterFabricas



                    Toast.makeText(this@GetAllActivity,"Fabricas encontradas con exito",Toast.LENGTH_SHORT).show()

                }
            })
        }

        if(intent.getSerializableExtra("numero") == 7){
                val facturaService: FacturaService = RestEngine.buildService().create(FacturaService::class.java)
                var result: Call<List<FacturaDataCollectionItem>> = facturaService.listFactura()
                var facturas = ArrayList<String>()

                result.enqueue(object :  Callback<List<FacturaDataCollectionItem>> {
                    override fun onFailure(call: Call<List<FacturaDataCollectionItem>>, t: Throwable) {
                        Toast.makeText(this@GetAllActivity,"Error Al encontrar las Facturas",Toast.LENGTH_LONG).show()
                    }

                    override fun onResponse(
                        call: Call<List<FacturaDataCollectionItem>>,
                        response: Response<List<FacturaDataCollectionItem>>
                    ) {
                        facturas.add("ID  |  Orden ID  |  Fecha")
                        for (factura in response.body()!!){
                            facturas.add("${factura.facturaId} | ${factura.ordenId} | ${factura.fechaFactura}")
                        }

                        val adapterFacturas = ArrayAdapter(this@GetAllActivity, android.R.layout.simple_list_item_1, facturas)
                        lstvGetAll.adapter = adapterFacturas

                        Toast.makeText(this@GetAllActivity,"Facturas Encontradas",Toast.LENGTH_LONG).show()
                    }
                })

        }

        if (intent.getSerializableExtra("numero") == 8){
            val maquinariaService: MaquinariaService = RestEngine.buildService().create(
                MaquinariaService::class.java)
            var result: Call<List<MaquinariaDataCollectionItem>> = maquinariaService.listMaquinarias()
            var maquinas = ArrayList<String>()

            result.enqueue(object :  Callback<List<MaquinariaDataCollectionItem>> {
                override fun onFailure(call: Call<List<MaquinariaDataCollectionItem>>, t: Throwable) {
                    Toast.makeText(this@GetAllActivity,"Error al traer las maquinarias",Toast.LENGTH_SHORT).show()
                }

                override fun onResponse(
                    call: Call<List<MaquinariaDataCollectionItem>>,
                    response: Response<List<MaquinariaDataCollectionItem>>
                ) {


                    maquinas.add("ID  |  Tipo maquina  |  Marca")
                    for (maquina in response.body()!!){
                        maquinas.add("${maquina.maquinaId} | ${maquina.tipoMaquina} | ${maquina.marca}")
                    }

                    val adapterMaquinas = ArrayAdapter(this@GetAllActivity, android.R.layout.simple_list_item_1, maquinas)
                    lstvGetAll.adapter = adapterMaquinas
                    Toast.makeText(this@GetAllActivity,"Maquinarias encontradas",Toast.LENGTH_SHORT).show()
                }
            })
        }

        if (intent.getSerializableExtra("numero") == 9){

                val materiaPrimaService:MateriaPrimaService = RestEngine.buildService().create(MateriaPrimaService::class.java)
                var result: Call<List<MateriaPrimaDataCollectionItem>> = materiaPrimaService.listMateria()
                var materiasPrimas = ArrayList<String>()

                result.enqueue(object :  Callback<List<MateriaPrimaDataCollectionItem>> {
                    override fun onFailure(call: Call<List<MateriaPrimaDataCollectionItem>>, t: Throwable) {
                        Toast.makeText(this@GetAllActivity,"Error al encontrar Materias Primas",Toast.LENGTH_LONG).show()
                    }

                    override fun onResponse(
                        call: Call<List<MateriaPrimaDataCollectionItem>>,
                        response: Response<List<MateriaPrimaDataCollectionItem>>
                    ) {

                        materiasPrimas.add("ID  |  Almacen ID  |  Cantidad Almacenada")
                        for (materiaPima in response.body()!!){
                            materiasPrimas.add("${materiaPima.materiaprimaId} | ${materiaPima.almacenId} | ${materiaPima.cantidad}")
                        }

                        val adapterMateriaPrima = ArrayAdapter(this@GetAllActivity, android.R.layout.simple_list_item_1, materiasPrimas)
                        lstvGetAll.adapter = adapterMateriaPrima

                        Toast.makeText(this@GetAllActivity,"Materias Primas encontradas",Toast.LENGTH_LONG).show()
                    }
                })

        }

        if(intent.getSerializableExtra("numero") == 10){
            val ordenEncabezadoService: OrdenEncabezadoService = RestEngine.buildService().create(
                OrdenEncabezadoService::class.java)
            var result: Call<List<OrdenEncabezadoDataCollectionItem>> = ordenEncabezadoService.listOrdenesEncabezado()
            val ordenes = ArrayList<String>()

            result.enqueue(object :  Callback<List<OrdenEncabezadoDataCollectionItem>> {
                override fun onFailure(call: Call<List<OrdenEncabezadoDataCollectionItem>>, t: Throwable) {
                    Toast.makeText(this@GetAllActivity,"Error al encontrar ordenes",Toast.LENGTH_LONG).show()
                }

                override fun onResponse(
                    call: Call<List<OrdenEncabezadoDataCollectionItem>>,
                    response: Response<List<OrdenEncabezadoDataCollectionItem>>
                ) {

                    ordenes.add("ID  |  Cliente ID  |  Estado")
                    for (orden in response.body()!!){
                        ordenes.add("${orden.ordenId} | ${orden.clienteId} | ${orden.estado}")
                    }

                    val adapterMateriaPrima = ArrayAdapter(this@GetAllActivity, android.R.layout.simple_list_item_1, ordenes)
                    lstvGetAll.adapter = adapterMateriaPrima

                    Toast.makeText(this@GetAllActivity,"Ordenes encontradas",Toast.LENGTH_LONG).show()
                }
            })
        }

        if(intent.getSerializableExtra("numero") == 11) {
            val productoService: ProductoService = RestEngine.buildService().create(ProductoService::class.java)
            var result: Call<List<ProductoDataCollectionItem>> = productoService.listProducto()
            var productos = ArrayList<String>()

            result.enqueue(object :  Callback<List<ProductoDataCollectionItem>> {
                override fun onFailure(call: Call<List<ProductoDataCollectionItem>>, t: Throwable) {
                    Toast.makeText(this@GetAllActivity,"Error al encontrar productos",Toast.LENGTH_LONG).show()
                }

                override fun onResponse(
                    call: Call<List<ProductoDataCollectionItem>>,
                    response: Response<List<ProductoDataCollectionItem>>
                ) {
                    productos.add("Producto Id | Nombre Producto | Precio")
                    for (producto in response.body()!!){
                        productos.add("${producto.productoId} | ${producto.nombreProducto} | ${producto.precio}")
                    }

                    val adapterProductos = ArrayAdapter(this@GetAllActivity, android.R.layout.simple_list_item_1, productos)
                    lstvGetAll.adapter = adapterProductos

                    Toast.makeText(this@GetAllActivity,"Productos encontrados",Toast.LENGTH_LONG).show()
                }
            })
        }

        if(intent.getSerializableExtra("numero") == 12) {
            val proveedorService: ProveedorService = RestEngine.buildService().create(ProveedorService::class.java)
            var result: Call<List<ProveedorDataCollectionItem>> = proveedorService.listProveedor()
            var proveedores = ArrayList<String>()

            result.enqueue(object :  Callback<List<ProveedorDataCollectionItem>> {
                override fun onFailure(call: Call<List<ProveedorDataCollectionItem>>, t: Throwable) {
                    Toast.makeText(this@GetAllActivity,"Error al encontrar el proveedor",Toast.LENGTH_LONG).show()
                }

                override fun onResponse(
                    call: Call<List<ProveedorDataCollectionItem>>,
                    response: Response<List<ProveedorDataCollectionItem>>
                ) {
                    proveedores.add("Proveedor ID| Nombre Contacto |  telefono")
                    for (proveedor in response.body()!!){
                        proveedores.add("${proveedor.proveedorId} | ${proveedor.nombreContacto} | ${proveedor.numero}")
                    }

                    val adapterProveedor = ArrayAdapter(this@GetAllActivity, android.R.layout.simple_list_item_1, proveedores)
                    lstvGetAll.adapter = adapterProveedor

                    Toast.makeText(this@GetAllActivity,"Empelados encontrados",Toast.LENGTH_LONG).show()
                }
            })
        }


        //-----
    }

}