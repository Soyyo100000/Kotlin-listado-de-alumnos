package com.example.registradoalumnos

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DBHelperAlumno(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "Alumnos.db"
        private const val DATABASE_VERSION = 1

        const val TABLE_ALUMNOS = "Alumnos"
        const val COLUMN_CUENTA = "cuenta"
        const val COLUMN_NOMBRE = "nombre"
        const val COLUMN_CORREO = "correo"
        const val COLUMN_IMAGEN = "imagen"
    }

    override fun onCreate(db: SQLiteDatabase) {
        val createTableQuery = ("CREATE TABLE $TABLE_ALUMNOS (" +
                "$COLUMN_CUENTA TEXT PRIMARY KEY, " +
                "$COLUMN_NOMBRE TEXT NOT NULL, " +
                "$COLUMN_CORREO TEXT, " +
                "$COLUMN_IMAGEN TEXT)")
        db.execSQL(createTableQuery)

        // Insertar alumnos de ejemplo iniciales
        insertarAlumnoInicial(db, Alumno("Juan Pérez", "20211001", "juan.perez@email.com", "https://picsum.photos/200/200?random=1"))
        insertarAlumnoInicial(db, Alumno("María López", "20211002", "maria.lopez@email.com", "https://picsum.photos/200/200?random=2"))
    }

    private fun insertarAlumnoInicial(db: SQLiteDatabase, alumno: Alumno) {
        val values = ContentValues().apply {
            put(COLUMN_CUENTA, alumno.cuenta)
            put(COLUMN_NOMBRE, alumno.nombre)
            put(COLUMN_CORREO, alumno.correo)
            put(COLUMN_IMAGEN, alumno.imagen)
        }
        db.insert(TABLE_ALUMNOS, null, values)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_ALUMNOS")
        onCreate(db)
    }

    fun insertarAlumno(alumno: Alumno): Boolean {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_CUENTA, alumno.cuenta)
            put(COLUMN_NOMBRE, alumno.nombre)
            put(COLUMN_CORREO, alumno.correo)
            put(COLUMN_IMAGEN, alumno.imagen)
        }
        val result = db.insert(TABLE_ALUMNOS, null, values)
        db.close()
        return result != -1L
    }

    fun obtenerAlumnos(): ArrayList<Alumno> {
        val listaAlumnos = ArrayList<Alumno>()
        val selectQuery = "SELECT * FROM $TABLE_ALUMNOS"
        val db = this.readableDatabase
        val cursor = db.rawQuery(selectQuery, null)

        if (cursor.moveToFirst()) {
            val idxCuenta = cursor.getColumnIndex(COLUMN_CUENTA)
            val idxNombre = cursor.getColumnIndex(COLUMN_NOMBRE)
            val idxCorreo = cursor.getColumnIndex(COLUMN_CORREO)
            val idxImagen = cursor.getColumnIndex(COLUMN_IMAGEN)

            do {
                val cuenta = if (idxCuenta != -1) cursor.getString(idxCuenta) else ""
                val nombre = if (idxNombre != -1) cursor.getString(idxNombre) else ""
                val correo = if (idxCorreo != -1) cursor.getString(idxCorreo) else ""
                val imagen = if (idxImagen != -1) cursor.getString(idxImagen) else ""

                listaAlumnos.add(Alumno(nombre = nombre, cuenta = cuenta, correo = correo, imagen = imagen))
            } while (cursor.moveToNext())
        }

        cursor.close()
        db.close()
        return listaAlumnos
    }

    fun actualizarAlumno(cuentaOriginal: String, alumno: Alumno): Boolean {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_CUENTA, alumno.cuenta)
            put(COLUMN_NOMBRE, alumno.nombre)
            put(COLUMN_CORREO, alumno.correo)
            put(COLUMN_IMAGEN, alumno.imagen)
        }
        val rowsAffected = db.update(TABLE_ALUMNOS, values, "$COLUMN_CUENTA = ?", arrayOf(cuentaOriginal))
        db.close()
        return rowsAffected > 0
    }

    fun eliminarAlumno(cuenta: String): Boolean {
        val db = this.writableDatabase
        val rowsAffected = db.delete(TABLE_ALUMNOS, "$COLUMN_CUENTA = ?", arrayOf(cuenta))
        db.close()
        return rowsAffected > 0
    }
}
