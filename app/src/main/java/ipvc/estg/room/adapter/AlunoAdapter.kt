package ipvc.estg.room.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ipvc.estg.room.R
import ipvc.estg.room.entities.Aluno


class AlunoAdapter internal constructor(
    context: Context
) : RecyclerView.Adapter<AlunoAdapter.AlunoViewHolder>() {

    private val inflater: LayoutInflater = LayoutInflater.from(context)
    private var alunos = emptyList<Aluno>()

    class AlunoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val alunoItemView: TextView = itemView.findViewById(R.id.textView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlunoViewHolder {
        val itemView = inflater.inflate(R.layout.recyclerview_item, parent, false)
        return AlunoViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: AlunoViewHolder, position: Int) {
        val current = alunos[position]
        holder.alunoItemView.text = current.id.toString() + " |  " + current.aluno + " | " + current.escola
    }

    internal fun setAlunos(cities: List<Aluno>) {
        this.alunos = cities
        notifyDataSetChanged()
    }

    override fun getItemCount() = alunos.size
}