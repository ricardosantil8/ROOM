package ipvc.estg.room.api


import retrofit2.Call
import retrofit2.http.*

interface EndPoints
{

    //Login Method Post
   @FormUrlEncoded
   @POST("/myslim/api/utilizador")

   fun userLogin(@Field("nomeutilizador") nomeutilizador: String?,
                 @Field("passutilizador") passutilizador: String?) : Call<OutputPost>


    @GET("/myslim/api/acidente")
    fun getUsers(): Call<List<Acidentes>>
}


