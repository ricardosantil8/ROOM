package ipvc.estg.room.api

import android.icu.util.Output
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

        }
            val descricao = descricao.text.toString().trim()

            val extras = intent.extras

                val latitude = extras?.getString("lat")
                val longitude = extras?.getString("lng")
                val nomeutilizador = extras?.getString("utilizador")
                Toast.makeText(this@AddAcidente, latitude + " - " + longitude , Toast.LENGTH_SHORT).show()


            val request = ServiceBuilder.buildService(EndPoints::class.java)
            val call = request.getAcidente(latitude, longitude, nomeutilizador, descricao)



            call.enqueue(object : Callback<OutputPost> {

                override fun onResponse(call: Call<OutputPost>, response: Response<OutputPost>) {
                    if (response.isSuccessful) {   //Se a resposta for positiva na camada de serviços significa que o acidente foi criado
                        if (response.body()?.error == false) {
                            Toast.makeText(this@AddAcidente, "Acidente Adicionado", Toast.LENGTH_SHORT).show()
                        }
                      }
                    }

                override fun onFailure(call: Call<OutputPost>, t: Throwable) {
                    Toast.makeText(this@AddAcidente, "Erro na criação do acidente", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }





