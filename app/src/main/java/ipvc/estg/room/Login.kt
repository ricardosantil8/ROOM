package ipvc.estg.room


import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.CheckBox
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import ipvc.estg.room.api.EndPoints
import ipvc.estg.room.api.OutputPost
import ipvc.estg.room.api.ServiceBuilder
import kotlinx.android.synthetic.main.activity_login.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class Login() : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val fab = findViewById<Button>(R.id.LoginButton)
        fab.setOnClickListener {

            // Verificação se os parametros foram preenchidos

                val utilizador = utilizador.text.toString().trim()
                val password = password.text.toString().trim()

                val request = ServiceBuilder.buildService(EndPoints::class.java)
                val call = request.userLogin(utilizador, password)  // Confirma com a camada de serviços a existencia do utilizador e a password inseridas



            if(utilizador.isEmpty() || password.isEmpty())   // Testar se os campos estão todos preenchidos
            {
                Toast.makeText(
                        this@Login, "Preencha todos os campos", Toast.LENGTH_SHORT).show()

            }

            else {


                call.enqueue(object : Callback<OutputPost> {

                    override fun onResponse(call: Call<OutputPost>, response: Response<OutputPost>) {
                        if (response.isSuccessful) {   //Se a resposta for positiva na camada de serviços significa que o utilizador existe
                            if (response.body()?.error == false) {
                                Toast.makeText(this@Login, "Username ou palavra passe incorreta", Toast.LENGTH_SHORT).show()

                            } else {

                                // SHARED PREFERENCES + MANTER SESSÃO INCICIADA ATRAVÉS DO TOOGLE

                                val checkBox1: CheckBox = findViewById(R.id.checkbox1)
                                if (checkBox1.isChecked) {

                                    Toast.makeText(this@Login, "CheckBox On", Toast.LENGTH_SHORT).show()

                                    var token = getSharedPreferences("utilizador", Context.MODE_PRIVATE)
                                    intent.putExtra("utilizador", utilizador)
                                    var editor = token.edit()
                                    editor.putString("loginutilizador", utilizador)
                                    editor.commit()
                                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK

                                } else
                                {
                                    Toast.makeText(this@Login, "CheckBox Off", Toast.LENGTH_SHORT).show()
                                }

                                //Abrir atividade do maps
                                val intent = Intent(this@Login, MapsActivity::class.java)
                                var a: OutputPost = response.body()!!

                                //Toast.makeText(this@Login, "Utilizador id:  " + a.utilizador_id, Toast.LENGTH_SHORT).show()
                                //Enviar o id do utilizador para a criacao de um novo acidente

                                intent.putExtra("utilizador_id", a.utilizador_id.toString())


                                if (checkBox1.isChecked)
                                {
                                    var token2 = getSharedPreferences("utilizador_id", Context.MODE_PRIVATE)
                                    intent.putExtra("utilizador_id", a.utilizador_id.toString())
                                    var editor2 = token2.edit()
                                    editor2.putString("loginutilizador_id", a.utilizador_id.toString())
                                    editor2.commit()
                                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                }

                                startActivity(intent)

                              //Login com sucesso
                                Toast.makeText(
                                        this@Login,
                                        "Bem vindo " + utilizador,
                                        Toast.LENGTH_LONG
                                ).show()

                            }
                        }
                    }

                    override fun onFailure(call: Call<OutputPost>, t: Throwable) {
                        Toast.makeText(
                                this@Login,
                                "Não foi possível efetuar o login",
                                Toast.LENGTH_SHORT
                        ).show()
                    }
                })
            }
        }
    }


    override fun onStart() {
        super.onStart()
        var token = getSharedPreferences("utilizador", Context.MODE_PRIVATE)
        var token2 = getSharedPreferences("utilizador_id", Context.MODE_PRIVATE)

        if (token.getString("loginutilizador", " ") != " "){
            val intent = Intent(applicationContext, MapsActivity::class.java)
            intent.putExtra("utilizador_id", token2.getString("loginutilizador_id"," "))
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }

    }
}






