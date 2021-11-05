package nl.com.lucianoluzzi.mapinha.ui.map

import android.Manifest
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.*
import androidx.lifecycle.lifecycleScope
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.GoogleMap.OnCameraMoveStartedListener.REASON_GESTURE
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import nl.com.lucianoluzzi.mapinha.R
import nl.com.lucianoluzzi.mapinha.databinding.ActivityMapsBinding
import nl.com.lucianoluzzi.mapinha.ui.ItemMarginDecorator
import nl.com.lucianoluzzi.mapinha.ui.extensions.*
import nl.com.lucianoluzzi.mapinha.ui.map.adapter.VenueListAdapter
import nl.com.lucianoluzzi.mapinha.ui.map.model.LocationState
import nl.com.lucianoluzzi.mapinha.ui.map.model.MapIntents
import nl.com.lucianoluzzi.mapinha.ui.map.model.MapState
import nl.com.lucianoluzzi.mapinha.ui.model.UserLocation
import org.koin.androidx.viewmodel.ext.android.viewModel

@ExperimentalCoroutinesApi
class MapsActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityMapsBinding.inflate(layoutInflater)
    }
    private lateinit var mGoogleMap: GoogleMap
    private val venueAdapter = VenueListAdapter()
    private val viewModel by viewModel<MapsViewModel>()

    private val locationRequestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
            val permissionIntent = if (isGranted) {
                MapIntents.LocationPermissionAllowed
            } else {
                MapIntents.LocationPermissionDenied
            }
            viewModel.onIntent(permissionIntent)
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        setContentView(binding.root)
        setVenueList()
        setErrorViews()
    }

    private fun setErrorViews() = with(binding) {
        ViewCompat.setOnApplyWindowInsetsListener(errorContainer) { view, windowInsets ->
            val insets = windowInsets.getInsets(WindowInsetsCompat.Type.systemBars())
            view.updateLayoutParams<ConstraintLayout.LayoutParams> {
                bottomMargin = insets.bottom
            }
            WindowInsetsCompat.CONSUMED
        }
        tryAgainButton.setOnClickListener {
            errorContainer.isVisible = false
            val currentLatitude = mGoogleMap.cameraPosition.target.latitude
            val currentLongitude = mGoogleMap.cameraPosition.target.longitude
            viewModel.onIntent(
                MapIntents.TryAgain(
                    latitude = currentLatitude,
                    longitude = currentLongitude
                )
            )
        }
    }

    private fun setVenueList() = with(binding.venueList) {
        ViewCompat.setOnApplyWindowInsetsListener(this) { view, windowInsets ->
            val insets = windowInsets.getInsets(WindowInsetsCompat.Type.systemBars())
            view.updateLayoutParams<ConstraintLayout.LayoutParams> {
                bottomMargin = insets.bottom
            }
            WindowInsetsCompat.CONSUMED
        }
        adapter = venueAdapter
        addItemDecoration(ItemMarginDecorator(8))
    }

    override fun onStart() {
        super.onStart()
        lifecycleScope.launch {
            createMap()
            viewModel.locationLiveData.observe(this@MapsActivity) { locationState ->
                handleLocationState(locationState)
            }
            viewModel.mapStateLiveData.observe(this@MapsActivity) { mapState ->
                handleMapState(mapState)
            }
        }
    }

    private suspend fun createMap() {
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.mapFragment) as SupportMapFragment
        val googleMap = mapFragment.awaitForMap()
        mGoogleMap = googleMap
        requestPermissionOr(
            permission = Manifest.permission.ACCESS_FINE_LOCATION,
            activityResultLauncher = locationRequestPermissionLauncher,
        ) {
            viewModel.onIntent(MapIntents.LocationPermissionAllowed)
        }
        setCameraListeners(mGoogleMap)
    }

    private fun handleLocationState(locationState: LocationState) {
        when (locationState) {
            is LocationState.DefaultLocation -> showNoLocationDialog(
                defaultLocation = locationState.defaultLocation,
                hasUserDeniedPermission = locationState.hasDeniedLocationPermission
            )
            is LocationState.DeviceLocation -> {
                setMapToLocation(
                    googleMap = mGoogleMap,
                    userLocation = locationState.location
                )
            }
        }
    }

    private fun setMapToLocation(
        googleMap: GoogleMap,
        userLocation: UserLocation
    ) = with(googleMap) {
        val latLng = LatLng(
            userLocation.latitude,
            userLocation.longitude
        )
        addMarker(
            MarkerOptions()
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_map_marker_black))
                .position(latLng)
        )
        moveCamera(CameraUpdateFactory.newLatLng(latLng))
        moveCamera(CameraUpdateFactory.zoomTo(INITIAL_ZOOM_LEVEL))
    }

    private fun setCameraListeners(googleMap: GoogleMap) = with(googleMap) {
        var userMovedCamera = false
        setOnCameraMoveStartedListener { moveReason ->
            userMovedCamera = moveReason == REASON_GESTURE
            if (userMovedCamera) {
                binding.venueList.animateHide()
            }
        }
        setOnCameraIdleListener {
            if (userMovedCamera) {
                val currentLatitude = googleMap.cameraPosition.target.latitude
                val currentLongitude = googleMap.cameraPosition.target.longitude
                viewModel.onIntent(
                    MapIntents.MapDragged(
                        latitude = currentLatitude,
                        longitude = currentLongitude
                    )
                )
            }
        }
    }

    private fun handleMapState(mapState: MapState) {
        when (mapState) {
            is MapState.ExplorationMode -> handleExplorationState(mapState)
            MapState.Error -> binding.errorContainer.isVisible = true
        }
    }

    private fun handleExplorationState(mapState: MapState.ExplorationMode) = with(binding) {
        errorContainer.isVisible = false
        venueAdapter.submitList(mapState.venues)
        if (mapState.venues.isNotEmpty()) {
            venueList.animateShow()
        }
    }

    private fun showNoLocationDialog(
        defaultLocation: UserLocation,
        hasUserDeniedPermission: Boolean
    ) {
        val message = if (hasUserDeniedPermission) {
            getString(R.string.denied_location_message)
        } else {
            getString(R.string.location_error_message)
        }
        showAlertDialog(
            message = message,
            positiveButtonText = "Ok",
            positiveButtonClickAction = {
                setMapToLocation(
                    googleMap = mGoogleMap,
                    userLocation = defaultLocation
                )
            }
        )
    }

    private companion object {
        private const val INITIAL_ZOOM_LEVEL = 15f
    }
}