package com.example.registradoalumnos

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class AlumnoAdapter(
    private var listaAlumnos: ArrayList<Alumno>,
    private val onEditClick: (Alumno) -> Unit,
    private val onDeleteClick: (Alumno) -> Unit
) : RecyclerView.Adapter<AlumnoAdapter.AlumnoViewHolder>() {

    class AlumnoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val ivFoto: ImageView = itemView.findViewById(R.id.imageViewFoto)
        val tvNombre: TextView = itemView.findViewById(R.id.tvNombre)
        val tvCuenta: TextView = itemView.findViewById(R.id.tvCuenta)
        val tvCorreo: TextView = itemView.findViewById(R.id.tvCorreo)
        val btnEditar: ImageButton = itemView.findViewById(R.id.btnEditar)
        val btnEliminar: ImageButton = itemView.findViewById(R.id.btnEliminar)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlumnoViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_persona, parent, false)
        return AlumnoViewHolder(view)
    }

    override fun onBindViewHolder(holder: AlumnoViewHolder, position: Int) {
        val alumno = listaAlumnos[position]

        holder.tvNombre.text = alumno.nombre
        holder.tvCuenta.text = "Cuenta: ${alumno.cuenta}"
        holder.tvCorreo.text = alumno.correo

        if (alumno.imagen.isNotBlank()) {
            Glide.with(holder.itemView.context)
                .load(alumno.imagen)
                .placeholder(android.R.drawable.ic_menu_gallery)
                .error(android.R.drawable.ic_menu_report_image)
                .into(holder.ivFoto)
        } else {
            holder.ivFoto.setImageResource(android.R.drawable.ic_menu_gallery)
        }

        holder.btnEditar.setOnClickListener {
            onEditClick(alumno)
        }

        holder.btnEliminar.setOnClickListener {
            onDeleteClick(alumno)
        }
    }

    override fun getItemCount(): Int = listaAlumnos.size

    fun actualizarLista(nuevaLista: ArrayList<Alumno>) {
        listaAlumnos = nuevaLista
        notifyDataSetChanged()
    }
}
