package ipvc.estg.room

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import ipvc.estg.room.api.Acidentes
import ipvc.estg.room.api.AddAcidente
import ipvc.estg.room.api.EndPoints
import ipvc.estg.room.api.ServiceBuilder
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var locationRequest: LocationRequest
    private lateinit var locationCallback: LocationCallback


    private lateinit var mMap: GoogleMap
    private lateinit var users: List<Acidentes>
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var lastLocation: Location
    private val LOCATION_PERMISSION_REQUEST_CODE = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        // Initialize fusedLocationClient
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        // call the service and add markers
        val request = ServiceBuilder.buildService(EndPoints::class.java)
        val call = request.getUsers()
        var position: LatLng


        //Introduzir todos os pontos no mapa que estao na Base de dados
        call.enqueue(object : Callback<List<Acidentes>> {
            override fun onResponse(call: Call<List<Acidentes>>, response: Response<List<Acidentes>>) {
                if (response.isSuccessful) {
                    users = response.body()!!
                    val extras = intent.extras
                    val utilizadorID = extras?.getString("utilizador_id")
                    val tipo = extras?.getInt("tipo_id")

                    //Toast.makeText(this@MapsActivity, "Tipo " + tipo, Toast.LENGTH_SHORT).show()




                    for (user in users) {
                    if(tipo == 0){
                        position = LatLng(user.lat.toString().toDouble(), user.long.toString().toDouble())
                        var utilizador_id = user.utilizador_id.toString()

                        if (user.utilizador_id == utilizadorID?.toInt()) {
                            mMap.addMarker(MarkerOptions()
                                    .position(position)
                                    .title(utilizador_id + " - " + user.descricao)
                                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)))
                        } else {
                            mMap.addMarker(
                                MarkerOptions()
                                    .position(position)
                                    .title(utilizador_id + " - " + user.descricao)
                                    .icon(
                                        BitmapDescriptorFactory.defaultMarker(
                                            BitmapDescriptorFactory.HUE_GREEN
                                        )
                                    )
                            )
                            }
                        }
                        else
                        {
                            position = LatLng(user.lat.toString().toDouble(), user.long.toString().toDouble())
                            var utilizador_id = user.utilizador_id.toString()
                            if(tipo == user.tipo_id)
                            {
                                if (user.utilizador_id == utilizadorID?.toInt()) {
                                    mMap.addMarker(
                                        MarkerOptions()
                                            .position(position)
                                            .title(utilizador_id + " - " + user.descricao)
                                            .icon(
                                                BitmapDescriptorFactory.defaultMarker(
                                                    BitmapDescriptorFactory.HUE_AZURE
                                                )
                                            )
                                    )
                                } else {
                                    mMap.addMarker(
                                        MarkerOptions()
                                            .position(position)
                                            .title(utilizador_id + " - " + user.descricao)
                                            .icon(
                                                BitmapDescriptorFactory.defaultMarker(
                                                    BitmapDescriptorFactory.HUE_GREEN
                                                )
                                            )
                                    )
                                }
                            }

                        }
                    }
                }
            }

            override fun onFailure(call: Call<List<Acidentes>>, t: Throwable) {
                Toast.makeText(this@MapsActivity, "${t.message}", Toast.LENGTH_SHORT).show()
            }
        })


        locationCallback = object : LocationCallback()
        {
            override fun onLocationResult(p0: LocationResult) {
            super.onLocationResult(p0)
            lastLocation = p0.lastLocation
            var loc = LatLng(lastLocation.latitude, lastLocation.longitude)
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(loc, 15.0f))
            //Toast.makeText(this@MapsActivity, "Lat: " + loc.latitude.toString() + " Long : " + loc.longitude.toString(), Toast.LENGTH_SHORT).show()
            }
        }
        createLocationRequest()
    }

        private fun createLocationRequest() {
            locationRequest = LocationRequest()
            // interval specifies the rate at which your app will like to receive updates. locationRequest.interval = 10000
            locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
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
        setUpMap()

       /* mMap.setOnMarkerClickListener { marker ->
            if (marker.isInfoWindowShown) {
                //val intent = Intent(this@MapsActivity, MapPoint::class.java)
                //startActivity(intent)
                Toast.makeText(
                    this@MapsActivity,
                    "Não foi possível efetuar o login",
                    Toast.LENGTH_SHORT
                ).show()


            } else {

            }
            true
        }*/
        }


    fun setUpMap() {
        if (ActivityCompat.checkSelfPermission(this,
                        android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), LOCATION_PERMISSION_REQUEST_CODE)
            return
        }
        else
        {

            // 1
            mMap.isMyLocationEnabled = true
            // 2
            fusedLocationClient.lastLocation.addOnSuccessListener(this) { location ->
                // Got last known location. In some rare situations this can be null.
                // 3
                if (location != null) {
                    lastLocation = location
                    Toast.makeText(this@MapsActivity, lastLocation.toString(), Toast.LENGTH_SHORT).show()
                    val currentLatLng = LatLng(location.latitude, location.longitude)
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 12f))


                }
            }
        }
    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.menu2, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val request = ServiceBuilder.buildService(EndPoints::class.java)
        val replyIntent = Intent()
        return when (item.itemId) {

            R.id.Criarponto -> {
                val extras = intent.extras
                val utilizador_id = extras?.getString("utilizador_id")

                var loc = LatLng(lastLocation.latitude, lastLocation.longitude)
                val intent = Intent(this@MapsActivity, AddAcidente::class.java)
                intent.putExtra("lat", loc.latitude.toString())
                intent.putExtra("lng", loc.longitude.toString())
                intent.putExtra("utilizador_id", utilizador_id)
                startActivity(intent)
                //finish()
                true

            }
            R.id.Logout -> {

                var token = getSharedPreferences("utilizador", Context.MODE_PRIVATE)
                intent.putExtra("utilizador", " ")
                var editor = token.edit()
                editor.putString("loginutilizador", " ")
                editor.commit()
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                finish()
                true
            }

            R.id.Filtrar -> {

                val extras = intent.extras
                val utilizador_id = extras?.getString("utilizador_id")


                val intent = Intent(this@MapsActivity, MapsActivity::class.java)
                var tipo = 2
                intent.putExtra("tipo_id", tipo)
                intent.putExtra("utilizador_id", utilizador_id)
                startActivity(intent)
                finish()
                true
                
            }

            R.id.apresentar -> {
                val extras = intent.extras
                val utilizador_id = extras?.getString("utilizador_id")


                val intent = Intent(this@MapsActivity, MapsActivity::class.java)
                var tipo = 0
                intent.putExtra("tipo_id", tipo)
                intent.putExtra("utilizador_id", utilizador_id)
                startActivity(intent)
                finish()
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun startLocationUpdates()
    {
        if (ActivityCompat.checkSelfPermission(this,
                        android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) { ActivityCompat.requestPermissions(this,
                arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                LOCATION_PERMISSION_REQUEST_CODE)
            return
        }

        fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, null)}


    override fun onPause() {
        super.onPause()
        fusedLocationClient.removeLocationUpdates(locationCallback)
    }

    public override fun onResume()
    {
        super.onResume()
        startLocationUpdates()
    }

    public override fun onRestart() {
        super.onRestart()
        val extras = intent.extras
        val utilizador_id = extras?.getString("utilizador_id")
        val intent = Intent(this@MapsActivity, MapsActivity::class.java)
        intent.putExtra("utilizador_id", utilizador_id)
        startActivity(intent)
        finish()

    }
}