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

    val activity = context as ItemClicked
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
        holder.alunoItemView.text =
                current.id.toString() + " |  " + current.aluno + " | " + current.escola

        //holder.itemView.setOnClickListener(activity.onClickListener(alunos.get(position)))
        holder.itemView.setOnClickListener(View.OnClickListener { activity.onClickListener(alunos.get(position)) })

    }

    internal fun setAlunos(alunos: List<Aluno>) {
        this.alunos = alunos
        notifyDataSetChanged()
    }

    override fun getItemCount() = alunos.size

    interface ItemClicked {
        fun onClickListener(aluno: Aluno) {

        }
    }

    fun getWordAtPosition(position: Int): Aluno {
        return alunos[position]
    }

}






