package com.example.registradoalumnos

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.textfield.TextInputEditText

class MainActivity : AppCompatActivity() {

    private lateinit var dbHelper: DBHelperAlumno
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: AlumnoAdapter
    private lateinit var tvEmptyState: TextView
    private lateinit var fabAgregar: FloatingActionButton
    private var listaAlumnos: ArrayList<Alumno> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, 0)
            insets
        }

        dbHelper = DBHelperAlumno(this)

        recyclerView = findViewById(R.id.recyclerViewAlumnos)
        tvEmptyState = findViewById(R.id.tvEmptyState)
        fabAgregar = findViewById(R.id.fabAgregar)

        recyclerView.layoutManager = LinearLayoutManager(this)

        adapter = AlumnoAdapter(
            listaAlumnos = listaAlumnos,
            onEditClick = { alumno -> mostrarDialogoEditarAlumno(alumno) },
            onDeleteClick = { alumno -> mostrarDialogoEliminarAlumno(alumno) }
        )
        recyclerView.adapter = adapter

        fabAgregar.setOnClickListener {
            mostrarDialogoAgregarAlumno()
        }

        cargarAlumnos()
    }

    private fun cargarAlumnos() {
        listaAlumnos = dbHelper.obtenerAlumnos()
        adapter.actualizarLista(listaAlumnos)

        if (listaAlumnos.isEmpty()) {
            tvEmptyState.visibility = View.VISIBLE
            recyclerView.visibility = View.GONE
        } else {
            tvEmptyState.visibility = View.GONE
            recyclerView.visibility = View.VISIBLE
        }
    }

    private fun mostrarDialogoAgregarAlumno() {
        val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_alumno, null)
        val etCuenta = dialogView.findViewById<TextInputEditText>(R.id.etCuenta)
        val etNombre = dialogView.findViewById<TextInputEditText>(R.id.etNombre)
        val etCorreo = dialogView.findViewById<TextInputEditText>(R.id.etCorreo)
        val etImagen = dialogView.findViewById<TextInputEditText>(R.id.etImagen)

        AlertDialog.Builder(this)
            .setTitle("Agregar Alumno")
            .setView(dialogView)
            .setPositiveButton("Guardar") { dialog, _ ->
                val cuenta = etCuenta.text.toString().trim()
                val nombre = etNombre.text.toString().trim()
                val correo = etCorreo.text.toString().trim()
                val imagen = etImagen.text.toString().trim()

                if (cuenta.isEmpty() || nombre.isEmpty()) {
                    Toast.makeText(this, "La cuenta y el nombre son obligatorios", Toast.LENGTH_SHORT).show()
                    return@setPositiveButton
                }

                val nuevoAlumno = Alumno(
                    nombre = nombre,
                    cuenta = cuenta,
                    correo = correo,
                    imagen = imagen
                )

                val insertado = dbHelper.insertarAlumno(nuevoAlumno)
                if (insertado) {
                    Toast.makeText(this, "Alumno registrado exitosamente", Toast.LENGTH_SHORT).show()
                    cargarAlumnos()
                } else {
                    Toast.makeText(this, "Error al registrar (Número de cuenta duplicado)", Toast.LENGTH_SHORT).show()
                }
                dialog.dismiss()
            }
            .setNegativeButton("Cancelar") { dialog, _ ->
                dialog.dismiss()
            }
            .create()
            .show()
    }

    private fun mostrarDialogoEditarAlumno(alumno: Alumno) {
        val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_alumno, null)
        val etCuenta = dialogView.findViewById<TextInputEditText>(R.id.etCuenta)
        val etNombre = dialogView.findViewById<TextInputEditText>(R.id.etNombre)
        val etCorreo = dialogView.findViewById<TextInputEditText>(R.id.etCorreo)
        val etImagen = dialogView.findViewById<TextInputEditText>(R.id.etImagen)

        // Pre-llenar campos
        etCuenta.setText(alumno.cuenta)
        etCuenta.isEnabled = false // No modificar la cuenta (clave primaria)
        etNombre.setText(alumno.nombre)
        etCorreo.setText(alumno.correo)
        etImagen.setText(alumno.imagen)

        AlertDialog.Builder(this)
            .setTitle("Editar Alumno")
            .setView(dialogView)
            .setPositiveButton("Actualizar") { dialog, _ ->
                val nombre = etNombre.text.toString().trim()
                val correo = etCorreo.text.toString().trim()
                val imagen = etImagen.text.toString().trim()

                if (nombre.isEmpty()) {
                    Toast.makeText(this, "El nombre es obligatorio", Toast.LENGTH_SHORT).show()
                    return@setPositiveButton
                }

                val alumnoActualizado = Alumno(
                    nombre = nombre,
                    cuenta = alumno.cuenta,
                    correo = correo,
                    imagen = imagen
                )

                val actualizado = dbHelper.actualizarAlumno(alumno.cuenta, alumnoActualizado)
                if (actualizado) {
                    Toast.makeText(this, "Alumno actualizado correctamente", Toast.LENGTH_SHORT).show()
                    cargarAlumnos()
                } else {
                    Toast.makeText(this, "Error al actualizar alumno", Toast.LENGTH_SHORT).show()
                }
                dialog.dismiss()
            }
            .setNegativeButton("Cancelar") { dialog, _ ->
                dialog.dismiss()
            }
            .create()
            .show()
    }

    private fun mostrarDialogoEliminarAlumno(alumno: Alumno) {
        AlertDialog.Builder(this)
            .setTitle("Eliminar Alumno")
            .setMessage("¿Está seguro de que desea eliminar a ${alumno.nombre} (Cuenta: ${alumno.cuenta})?")
            .setPositiveButton("Eliminar") { dialog, _ ->
                val eliminado = dbHelper.eliminarAlumno(alumno.cuenta)
                if (eliminado) {
                    Toast.makeText(this, "Alumno eliminado", Toast.LENGTH_SHORT).show()
                    cargarAlumnos()
                } else {
                    Toast.makeText(this, "Error al eliminar alumno", Toast.LENGTH_SHORT).show()
                }
                dialog.dismiss()
            }
            .setNegativeButton("Cancelar") { dialog, _ ->
                dialog.dismiss()
            }
            .create()
            .show()
    }
}
