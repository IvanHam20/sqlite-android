package edu.iest.sqlite_android.activities

import android.annotation.SuppressLint
import android.content.ContentValues
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import edu.iest.sqlite_android.db.ManejadorBaseDatos
import edu.iest.sqlite_android.modelos.Personaje
import com.google.android.material.snackbar.Snackbar
import edu.iest.sqlite_android.R

class EditarActivity : AppCompatActivity(), AdapterView.OnItemSelectedListener {
    private lateinit var bnGuardar: Button
    private lateinit var etJuego: EditText
    private lateinit var spAldea: Spinner
    private val aldeas = arrayOf("Konohagakure", "Kirigakure", "Kumogakure", "Iwagakure", "Amegakure", "Sunagakure","Otogakure","Uzushiogakure")
    private var aldeasSeleccionadas: String = ""
    private lateinit var tvJuego: TextView
    var personaje: Personaje? = null
    var id: Int = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_editar)
        //  setSupportActionBar(toolbar)
        getSupportActionBar()?.title = "Edición"
        getSupportActionBar()?.setHomeButtonEnabled(true);
        getSupportActionBar()?.setDisplayHomeAsUpEnabled(true);
        inicializarVistas()
        id = intent.getIntExtra("id", 0)
        buscarJuego(id)
        poblarCampos()
    }

    private fun poblarCampos() {
        etJuego.setText(personaje?.nombre)
        val position = aldeas.indexOf(personaje?.aldea)
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, aldeas)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spAldea.adapter = adapter
        spAldea.onItemSelectedListener = this
        if (position >= 0) {
            spAldea.setSelection(position)
            aldeasSeleccionadas = aldeas[position]
        }
    }

    private fun inicializarVistas() {
        etJuego = findViewById(R.id.etJuego)
        bnGuardar = findViewById(R.id.bnGuardar)
        spAldea = findViewById(R.id.spAldea)
        tvJuego = findViewById(R.id.tvJuego)
        bnGuardar.setOnClickListener {
            actualizarJuego(etJuego.text.toString(), aldeasSeleccionadas)
        }
    }

    val columnaNombreJuego = "nombre"
    val columnaAldea = "aldea"

    private fun actualizarJuego(nombreJuego: String, aldea: String) {
        if (!TextUtils.isEmpty(aldea)) {
            val baseDatos = ManejadorBaseDatos(this)
            val contenido = ContentValues()
            contenido.put(columnaNombreJuego, nombreJuego)
            contenido.put(columnaAldea, aldea)
            if ( id > 0) {
                val argumentosWhere = arrayOf(id.toString())
                val id_actualizado = baseDatos.actualizar(contenido, "id = ?", argumentosWhere)
                if (id_actualizado > 0) {
                    Snackbar.make(etJuego, "Juego actualizado", Snackbar.LENGTH_LONG).show()
                } else {
                    val alerta = AlertDialog.Builder(this)
                    alerta.setTitle("Atención")
                        .setMessage("No fue posible actualizarlo")
                        .setCancelable(false)
                        .setPositiveButton("Aceptar") { dialog, which ->

                        }
                        .show()
                }
            } else {
                Toast.makeText(this, "no hiciste id", Toast.LENGTH_LONG).show()
            }
            baseDatos.cerrarConexion()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
        }
        return super.onOptionsItemSelected(item)
    }

    @SuppressLint("Range")
    private fun buscarJuego(idJuego: Int) {

        if (idJuego > 0) {
            val baseDatos = ManejadorBaseDatos(this)
            val columnasATraer = arrayOf("id", "nombre", "aldea")
            val condicion = " id = ?"
            val argumentos = arrayOf(idJuego.toString())
            val ordenarPor = "id"
            val cursor = baseDatos.seleccionar(columnasATraer, condicion, argumentos, ordenarPor)

            if (cursor.moveToFirst()) {
                do {
                    val personaje_id = cursor.getInt(cursor.getColumnIndex("id"))
                    val nombre = cursor.getString(cursor.getColumnIndex("nombre"))
                    val aldea = cursor.getString(cursor.getColumnIndex("aldea"))
                    personaje = Personaje(personaje_id, nombre, aldea)
                } while (cursor.moveToNext())
            }
            baseDatos.cerrarConexion()
        }
    }

    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, position: Int, p3: Long) {
        aldeasSeleccionadas = aldeas[position]
    }

    override fun onNothingSelected(p0: AdapterView<*>?) {

    }
}