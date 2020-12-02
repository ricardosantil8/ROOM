package ipvc.estg.room

import android.app.Service
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import ipvc.estg.room.api.EndPoints
import ipvc.estg.room.api.ServiceBuilder
import ipvc.estg.room.api.User
import retrofit2.Call
import retrofit2.Response
import javax.security.auth.callback.Callback

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var users: List<User>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        //Chamar o webservice e adicionar os marcadores

        val request = ServiceBuilder.buildService(EndPoints::class.java)
        val call = request.getUsers()
        var position: LagLng

        call.enqueue(object : retrofit2.Callback<List<User>>

        {
            override fun onResponse(call: Call<List<User>>, response: Response<List<User>>){
              if (response.isSuccessful){
                  users = response.body()!!
                  for (user in users){
                      position = LatLng(user.address.geo.lat.toString().toDouble(),
                      user.address.geo.lng.toString().toDouble())
                      mMap.addMarker(MarkerOptions().position(position).title(user.address.street + " -" + user.address.city))
                  }
            }
         }
            override fun onFailure(call: Call<List<User>>, t: Throwable){
                Toast.makeText(this@MapsActivity, "${t.message}", Toast.LENGTH_SHORT).show()
            }
            })
        }













    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        // Add a marker in Sydney and move the camera
       /* val sydney = LatLng(-34.0, 151.0)
        mMap.addMarker(MarkerOptions().position(sydney).title("Marker in Sydney"))
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))*/
    }
}