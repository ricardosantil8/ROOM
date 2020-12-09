package ipvc.estg.room.api


import android.icu.util.Output
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
                    @Field("id") utilizador_id: Int?,
                    @Field("descricao") descr_acidente: String,
                    @Field("tipo_id") tipo_id: Int?) : Call<OutputPost>
}


