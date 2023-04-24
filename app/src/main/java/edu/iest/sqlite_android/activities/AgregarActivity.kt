package edu.iest.sqlite_android.activities

import android.content.ContentValues
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import edu.iest.sqlite_android.db.ManejadorBaseDatos
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import edu.iest.sqlite_android.R

class AgregarActivity : AppCompatActivity(), AdapterView.OnItemSelectedListener {
    private  lateinit var fabAgregar: FloatingActionButton
    private  lateinit var etJuego: EditText
    private  lateinit var spAldea: Spinner
    private val aldeas = arrayOf("Konohagakure", "Kirigakure", "Kumogakure", "Iwagakure", "Amegakure", "Sunagakure","Otogakure","Uzushiogakure")
    private var aldeasSeleccionadas: String = ""
    private  lateinit var tvJuego: TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_agregar)
        inicializarVistas()

        val adapter = ArrayAdapter(this,android.R.layout.simple_spinner_item, aldeas)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        spAldea.adapter = adapter
        spAldea.onItemSelectedListener = this
        fabAgregar.setOnClickListener{
            insertarJuego(etJuego.text.toString(),aldeasSeleccionadas)
        }
    }

    val columnaID = "id"
    val columnaNombreJuego = "nombre"
    val columnaAldea = "aldea"
    var id: Int = 0
    private fun insertarJuego(nombreJuego: String, aldea: String){
       if(!TextUtils.isEmpty(aldea)) {
           val baseDatos = ManejadorBaseDatos(this)
           //  val columnas = arrayOf(columnaID, columnaNombreJuego, columnaPrecio, columnaConsola)
           val contenido = ContentValues()
           contenido.put(columnaNombreJuego, nombreJuego)
           contenido.put(columnaAldea, aldea)
           //guardar imagen
            id = baseDatos.insertar(contenido).toInt()
           if (id > 0) {
               Toast.makeText(this, "juego " + nombreJuego + " agregado", Toast.LENGTH_LONG).show()
               finish()
           } else
               Toast.makeText(this, "Ups no se pudo guardar el juego", Toast.LENGTH_LONG).show()
           baseDatos.cerrarConexion()
       }else{
           Snackbar.make(tvJuego,"Favor seleccionar una consola", 0).show()
       }
    }

    private fun inicializarVistas(){
        etJuego = findViewById(R.id.etJuego)
        fabAgregar = findViewById(R.id.fabAgregar)
        spAldea = findViewById(R.id.spAldea)
        tvJuego = findViewById(R.id.tvJuego)
    }

    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, position: Int, id: Long) {
        aldeasSeleccionadas = aldeas[position]
    }

    override fun onNothingSelected(p0: AdapterView<*>?) {

    }
}