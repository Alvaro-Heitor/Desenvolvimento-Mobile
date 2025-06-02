package com.example.saferide

import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import org.maplibre.android.MapLibre
import org.maplibre.android.WellKnownTileServer
import org.maplibre.android.camera.CameraPosition
import org.maplibre.android.camera.CameraUpdateFactory
import org.maplibre.android.geometry.LatLng
import org.maplibre.android.location.LocationComponentActivationOptions
import org.maplibre.android.location.modes.CameraMode
import org.maplibre.android.location.modes.RenderMode
import org.maplibre.android.maps.MapView
import org.maplibre.android.maps.Style


class InicioActivity : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    private lateinit var mapView: MapView

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        MapLibre.getInstance(this, "qFwfNGxpcnQAfq5ArO8h", WellKnownTileServer.MapTiler)
        setContentView(R.layout.activity_inicio)
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId){
                R.id.navigation_home -> return@setOnItemSelectedListener true
                R.id.navigation_configuration -> startActivity(Intent(this, ConfiguracoesActivity::class.java))
                R.id.navigation_emergencia -> startActivity(Intent(this, EmergenciaActivity::class.java))
            }
            true
        }
        val emergencia = findViewById<Button>(R.id.emergenciaButton)
        emergencia.setOnClickListener {
            val intent = Intent(this, EmergenciaActivity::class.java)
            startActivity(intent)
        }
        mapView = findViewById(R.id.mapView)
        mapView.onCreate(savedInstanceState)

        // Verifica a permissão antes de iniciar o mapa
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ||
            ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this, arrayOf(
                android.Manifest.permission.ACCESS_FINE_LOCATION,
                android.Manifest.permission.ACCESS_COARSE_LOCATION
            ), 1)
        } else {
            iniciarMapa() // Permissão já concedida, então inicia o mapa
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.mapView)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    // Método separado para iniciar o mapa
    private fun iniciarMapa() {
        mapView.getMapAsync { map ->
            map.setStyle(Style.Builder().fromUri("https://api.maptiler.com/maps/streets/style.json?key=qFwfNGxpcnQAfq5ArO8h")) { style ->
                val locationComponent = map.locationComponent
                locationComponent.activateLocationComponent(LocationComponentActivationOptions.builder(this, style).build())

                if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    locationComponent.isLocationComponentEnabled = true
                    locationComponent.cameraMode = CameraMode.TRACKING
                    locationComponent.renderMode = RenderMode.COMPASS

                    val lastLocation= locationComponent.lastKnownLocation
                    if (lastLocation != null) {
                        val userLocation = LatLng(lastLocation.latitude, lastLocation.longitude)
                        map.moveCamera(
                            CameraUpdateFactory.newCameraPosition(
                                CameraPosition.Builder()
                                    .target(userLocation)
                                    .zoom(17.0)
                                    .build()

                            )
                        )
                    }
                    val btnMinhaLocalizacao = findViewById<FloatingActionButton>(R.id.minhaLocalizacao)

                    btnMinhaLocalizacao.setOnClickListener {
                        val lastLocation = map.locationComponent.lastKnownLocation
                        if (lastLocation != null) {
                            val userLocation = LatLng(lastLocation.latitude, lastLocation.longitude)
                            map.moveCamera(CameraUpdateFactory.newCameraPosition(
                                CameraPosition.Builder()
                                    .target(userLocation)
                                    .zoom(17.0)
                                    .build()
                            ))
                        }
                    }

                }
            }
        }
    }

    // Método para capturar a resposta do usuário ao pedido de permissão
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == 1) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                iniciarMapa() // Agora que a permissão foi concedida, inicia o mapa
            } else {
                Toast.makeText(this, "Permissão de localização negada!", Toast.LENGTH_LONG).show()
            }
        }
    }

    override fun onStart() { super.onStart(); mapView.onStart() }
    override fun onResume() { super.onResume(); mapView.onResume() }
    override fun onPause() { super.onPause(); mapView.onPause() }
    override fun onStop() { super.onStop(); mapView.onStop() }
    override fun onDestroy() { super.onDestroy(); mapView.onDestroy() }
}


