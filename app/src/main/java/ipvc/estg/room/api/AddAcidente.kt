package ipvc.estg.room.api

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import ipvc.estg.room.R
import kotlinx.android.synthetic.main.activity_add_acidente.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class AddAcidente : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_acidente)


        val fab = findViewById<Button>(R.id.Adicionar)
        fab.setOnClickListener {

            val descricao = descricao.text.toString().trim()
            val extras = intent.extras

            val latitude = extras?.getString("lat")
            val longitude = extras?.getString("lng")
            val utilizador_id = extras?.getString("utilizador_id")

            //Toast.makeText(this@AddAcidente, "ID :" + utilizador_id, Toast.LENGTH_SHORT).show()

            val utilizadorID = utilizador_id?.toInt()

            val request = ServiceBuilder.buildService(EndPoints::class.java)
            val call = request.getAcidente(latitude, longitude, utilizadorID, descricao)


            call.enqueue(object : Callback<OutputPost> {

                override fun onResponse(call: Call<OutputPost>, response: Response<OutputPost>) {
                    if (response.isSuccessful) {
                        //Se a resposta for positiva na camada de serviços significa que o acidente foi criado
                        if (response.body()?.error == true) {
                            Toast.makeText(this@AddAcidente, "Acidente Adicionado", Toast.LENGTH_SHORT).show()
                        }
                        else{
                            Toast.makeText(this@AddAcidente, "Acidente não adicionado", Toast.LENGTH_SHORT).show()
                        }
                    }
                }

                override fun onFailure(call: Call<OutputPost>, t: Throwable) {
                    Toast.makeText(this@AddAcidente, "Erro na criação do acidente", Toast.LENGTH_SHORT).show()
                }
            })
        }
    }
}



