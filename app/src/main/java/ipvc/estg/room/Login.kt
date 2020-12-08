package ipvc.estg.room

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.maps.model.LatLng
import ipvc.estg.room.api.AddAcidente
import ipvc.estg.room.api.EndPoints
import ipvc.estg.room.api.OutputPost
import ipvc.estg.room.api.ServiceBuilder
import kotlinx.android.synthetic.main.activity_login.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*
import java.util.Objects.hash
import java.util.Objects.requireNonNullElse

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
                                Toast.makeText(
                                    this@Login,
                                    "Username ou palavra passe incorreta",
                                    Toast.LENGTH_SHORT
                                ).show()


                                val intent = Intent(this@Login, AddAcidente::class.java)
                                intent.putExtra("utilizador", utilizador)
                                startActivity(intent)
                            }
                            else
                            {

                                // SHARED PREFERENCES + MANTER SESSÃO INCICIADA ATRAVÉS DO TOOGLE

                                val switch1: Switch = findViewById(R.id.switch1)
                                if(switch1.isEnabled){

                                        //Toast.makeText(this@Login, "Toogle On", Toast.LENGTH_SHORT).show()

                                        var token = getSharedPreferences("utilizador", Context.MODE_PRIVATE)
                                        intent.putExtra("utilizador", utilizador)
                                        var editor = token.edit()
                                        editor.putString("loginutilizador", utilizador)
                                        editor.commit()
                                        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                    }
                                    else
                                    {
                                        //Toast.makeText(this@Login, "Toogle Of",Toast.LENGTH_SHORT).show()
                                    }
                                }

                                    //Abrir atividade do maps
                                    val intent = Intent(this@Login, MapsActivity::class.java)
                                    startActivity(intent)

                                    //Login com sucesso
                                    Toast.makeText(
                                        this@Login,
                                        "Bem vindo " + utilizador,
                                        Toast.LENGTH_LONG
                                    ).show()

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
        if (token.getString("loginutilizador", " ") != " "){
            val intent = Intent(applicationContext, MapsActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }
    }



}






