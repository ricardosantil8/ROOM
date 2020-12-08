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


    @FormUrlEncoded
    @POST("/myslim/api/novoacidente")
    fun getAcidente(@Field("latitude") latitude: String?,
                    @Field("longitude") longitude: String?,
                    @Field("nomeutilizador") nomeutilizador: String?,
                    @Field("descricao") descr_acidente: String): Call<List<Acidentes>>
}


