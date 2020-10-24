package com.ng.gbv_tracker.ui.fragment.act_login

import android.Manifest
import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.content.Context.LOCATION_SERVICE
import android.content.DialogInterface
import android.content.pm.PackageManager
import android.content.res.Resources
import android.location.*
import android.os.Bundle
import android.os.Parcelable
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.FrameLayout
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.maps.model.LatLng
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.ng.gbv_tracker.R
import com.ng.gbv_tracker.adapter.PlacesAutoCompleteAdapter
import com.ng.gbv_tracker.databinding.FragmentPickAddressBinding
import com.ng.gbv_tracker.model.LatLong
import com.ng.gbv_tracker.ui.fragment.act_main.FragmentNYSCToSupportReport
import com.ng.gbv_tracker.utils.ClassUtilities
import com.ng.gbv_tracker.utils.toast
import com.ng.gbv_tracker.viewmodel.ModelAddressPick
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.Math.*
import java.text.DecimalFormat
import java.util.*


private const val LAT_LONG = "lat_long"

class FragmentPickAddress : BottomSheetDialogFragment(), PlacesAutoCompleteAdapter.ClickListener {
    lateinit var binding: FragmentPickAddressBinding
    lateinit var modelAddressPick: ModelAddressPick

    var latLong = LatLong(0.0,0.0)

    val latLongA = LatLong(12.0,-2.4, "Amalla")
    val latLongB = LatLong(132.0,-29.4, "Uwal")

    val _this by lazy { requireActivity() }
    private var mAutoCompleteAdapter: PlacesAutoCompleteAdapter? = null
    private var recyclerView: RecyclerView? = null



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(DialogFragment.STYLE_NORMAL, R.style.DialogStyle)
        arguments?.let {
            try {
                latLong = it.getParcelable(LAT_LONG)!!
                binding.placeSearch.setText(latLong.address)
            } catch (e: Exception) {e.printStackTrace();}

        }
    }




    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        modelAddressPick = requireActivity().run {
            ViewModelProvider(this).get(ModelAddressPick::class.java)
        }

        binding.currentLocation.setOnClickListener {
            checkLocation()
        }

        binding.continueBtn.setOnClickListener {
            if (latLong.lat==0.0||latLong.long==0.0||latLong.address.isEmpty()){
                context?.toast("No valid address entered")
            }else{
                modelAddressPick.setUserAddressPick(latLong)
            }
            dismiss()
            dialog?.dismiss()
        }
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,savedInstanceState: Bundle?): View? {
        binding = FragmentPickAddressBinding.inflate(inflater)

        Places.initialize(_this, resources.getString(R.string.google_maps_key))

        recyclerView = binding.placesRecyclerView
        binding.placeSearch.addTextChangedListener(filterTextWatcher)

        mAutoCompleteAdapter = PlacesAutoCompleteAdapter(_this)
        mAutoCompleteAdapter?.setClickListener(this)
        recyclerView?.setLayoutManager(LinearLayoutManager(_this))
        recyclerView?.setAdapter(mAutoCompleteAdapter)
        mAutoCompleteAdapter?.notifyDataSetChanged()

        ActivityCompat.requestPermissions(_this,arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION),
            LOCATION_REQUEST_CODE)

        return binding.root
    }

    private val filterTextWatcher: TextWatcher = object : TextWatcher {
        override fun afterTextChanged(s: Editable) {
            if (!s.toString().equals("")) {
                mAutoCompleteAdapter!!.filter.filter(s.toString())
                if (recyclerView!!.visibility == View.GONE) {
                    recyclerView?.visibility = View.VISIBLE
                }
            } else {
                if (recyclerView!!.visibility == View.VISIBLE) {
                    recyclerView?.visibility = View.GONE
                }
            }
        }

        override fun beforeTextChanged(s: CharSequence?,start: Int,count: Int,after: Int) {}

        override fun onTextChanged(s: CharSequence?,start: Int,before: Int, count: Int) {}
    }

    override fun click(place: Place?) {//Places recycler view click
        if (place!!.address==null) {
            context?.toast("Address unavailable, try another search")
            return
        }
        binding.placeSearch.setText(place.address.toString())

        latLong.address = place.address.toString()
        latLong.lat = place.latLng!!.latitude
        latLong.long = place.latLng!!.longitude


        if (recyclerView!!.visibility == View.VISIBLE) {//hide recyclerview
            recyclerView?.visibility = View.GONE
        }


        try {
            val view = dialog!!.currentFocus
            if(view != null){
                val inputManager = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                inputManager.hideSoftInputFromWindow(view.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
            }
            dialog!!.window!!.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN)
        } catch (e: Exception) {e.printStackTrace()}
    }


    val LOCATION_REFRESH_TIME = (5000.0).toLong()
    val LOCATION_REFRESH_DISTANCE = (10.0).toFloat()
    fun getMyLocation1(){
        val mLocationListener = object : android.location.LocationListener {
            override fun onLocationChanged(location: Location?) {

            }

            override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {}

            override fun onProviderEnabled(provider: String?) {}

            override fun onProviderDisabled(provider: String?) {}
        }


        val mLocationManager = activity?.getSystemService(LOCATION_SERVICE) as LocationManager
        if (ActivityCompat.checkSelfPermission(_this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
            && ActivityCompat.checkSelfPermission(_this,Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(_this,arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION),
                LOCATION_REQUEST_CODE)
            return
        }
        mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, LOCATION_REFRESH_TIME,  LOCATION_REFRESH_DISTANCE, mLocationListener)
    }

    private var locationManager: LocationManager? = null
    private var myLocationListener: android.location.LocationListener? = null
    var location:Location? = null

    private fun checkLocation() {
        val serviceString: String = Context.LOCATION_SERVICE
        locationManager = activity?.getSystemService(serviceString) as LocationManager?

        // getting GPS status
        val isGPSEnabled = locationManager!!.isProviderEnabled(LocationManager.GPS_PROVIDER)

        if (ActivityCompat.checkSelfPermission(_this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
            && ActivityCompat.checkSelfPermission(_this,Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(_this,arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION),
                LOCATION_REQUEST_CODE)
            return
        }
        myLocationListener = object : android.location.LocationListener {
            override fun onLocationChanged(locationListener: Location) {
                if (isGPSEnabled) {
                    if (ActivityCompat.checkSelfPermission(_this,Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                        && ActivityCompat.checkSelfPermission(_this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    ) {
                        ActivityCompat.requestPermissions(_this,arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION),
                            LOCATION_REQUEST_CODE)
                        return
                    }
                    if (locationManager != null) {
                        location = locationManager!!.getLastKnownLocation(LocationManager.GPS_PROVIDER)
                        if (location != null) {
                            latLong.lat = location!!.latitude
                            latLong.long = location!!.longitude
                            getAddressFromLatLong()
                        }
                    }
                } else if (locationManager != null) {
                    location = locationManager!!
                        .getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
                    if (location != null) {
                        latLong.lat = location!!.latitude
                        latLong.long = location!!.longitude
                        getAddressFromLatLong()
                    }
                }
            }

            override fun onProviderDisabled(provider: String) {}
            override fun onProviderEnabled(provider: String) {}
            override fun onStatusChanged(
                provider: String,
                status: Int,
                extras: Bundle
            ) {
            }
        }

        locationManager!!.requestLocationUpdates(
            LocationManager.GPS_PROVIDER,
            20000,//20 secs
            1f,
            myLocationListener!!
        )
    }

    override fun onRequestPermissionsResult(requestCode: Int,permissions: Array<out String>,grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            LOCATION_REQUEST_CODE -> {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, yay! Do the
                    if (ContextCompat.checkSelfPermission(_this,Manifest.permission.ACCESS_FINE_LOCATION)== PackageManager.PERMISSION_GRANTED) {
                        checkLocation()
                    }
                } else {
                    context?.toast("Enable location request to continue")
                }
                return
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        locationManager?.removeUpdates(myLocationListener!!)
    }
    private fun getAddressFromLatLong() {
        val geocoder: Geocoder
        val addresses: List<Address>
        try {
            geocoder = Geocoder(_this, Locale.getDefault())
            addresses = geocoder.getFromLocation(
                latLong.lat,
                latLong.long,
                1
            ) // Here 1 represent max location result to returned, by documents it recommended 1 to 5
        } catch (e: Exception) {e.printStackTrace(); return}


        try {
            val address: String = addresses[0].getAddressLine(0) // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()

            val city: String = addresses[0].locality
            val state: String = addresses[0].adminArea
            val country: String = addresses[0].countryName
//            val postalCode: String = addresses[0].postalCode
            val knownName: String = addresses[0].featureName // Only if available else return NULL

            latLong.address = "$knownName $city $country"
            Log.d("1111111111333", "$knownName $city $state $country")

            binding.placeSearch.setHint(latLong.address)
            locationManager?.removeUpdates(myLocationListener!!)
        } catch (e: Exception) { e.printStackTrace() }


    }

    fun  getLagLongFromAddress() {
        val coder = Geocoder(_this, Locale.getDefault())
        val address: List<Address>
        var p1: LatLng? = null
        try {
            address = coder.getFromLocationName(latLong.address,5)
            if (address==null) {
                context?.toast("Address unavailable, try another search")
                return
            }
            val location=address.get(0)
            location.latitude
            location.longitude

            p1 = LatLng((location.latitude * 1E6),
             (location.longitude * 1E6))

            latLong.lat = location.latitude
            latLong.long = location.longitude
        }catch (e:Exception){e.printStackTrace()}
    }








    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = BottomSheetDialog(requireContext(), theme)
        dialog.setOnShowListener {

            val bottomSheetDialog = it as BottomSheetDialog
            val parentLayout =  bottomSheetDialog.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
            parentLayout?.let { it ->
                val behaviour = BottomSheetBehavior.from(it)
                setupFullHeight(it)
                behaviour.state = BottomSheetBehavior.STATE_COLLAPSED
                behaviour.peekHeight = Resources.getSystem().displayMetrics.heightPixels-200
            }
        }
        return dialog
    }
    private fun setupFullHeight(bottomSheet: View) {
        val layoutParams = bottomSheet.layoutParams
        layoutParams.height = WindowManager.LayoutParams.MATCH_PARENT
        bottomSheet.layoutParams = layoutParams
    }



    companion object{
        val LOCATION_REQUEST_CODE = 0


        @JvmStatic
        fun newInstance(param1: Parcelable) =
            FragmentPickAddress().apply {
                arguments = Bundle().apply {
                    putParcelable(LAT_LONG, param1)
                }
            }
    }


    fun checkDistance(){

        Log.d("111111111", "${getDistance()[0]}")
        Log.d("111111112", "${CalculationByDistance()}")//use this(KM)
        Log.d("111111113", "${CalculationByDistance2()}")//(KM)
        Log.d("111111114", "${getDistanceMeters()}")//(M)
    }

    fun getDistance(): FloatArray{
        val results = FloatArray(1)
        Location.distanceBetween(
            latLongA.lat, latLongA.long,
            latLongB.lat, latLongB.long,
            results
        )
        return results
    }
    @SuppressLint("LogNotTimber")
    fun CalculationByDistance(): Double {
        val Radius = 6371 // radius of earth in Km
        val lat1 = latLongA.lat
        val lat2 = latLongB.lat
        val lon1 = latLongA.long
        val lon2 = latLongB.long
        val dLat = toRadians(lat2 - lat1)
        val dLon = toRadians(lon2 - lon1)
        val a = (Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + (Math.cos(toRadians(lat1))
                * Math.cos(toRadians(lat2)) * Math.sin(dLon / 2)
                * Math.sin(dLon / 2)))
        val c = 2 * Math.asin(Math.sqrt(a))
        val valueResult = Radius * c
        val km = valueResult / 1
        val newFormat = DecimalFormat("####")
        val kmInDec: Int = Integer.valueOf(newFormat.format(km))
        val meter = valueResult % 1000
        val meterInDec: Int = Integer.valueOf(newFormat.format(meter))

        return Radius * c
    }
    fun CalculationByDistance2(): Double {
        val Radius = 6371 //radius of earth in Km
        val lat1 = latLongA.lat
        val lat2 = latLongB.lat
        val lon1 = latLongA.long
        val lon2 = latLongB.long
        val dLat = toRadians(lat2 - lat1)
        val dLon = toRadians(lon2 - lon1)
        val a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.cos(toRadians(lat1)) * Math.cos(
            toRadians(
                lat2
            )
        ) *
                Math.sin(dLon / 2) * Math.sin(dLon / 2)
        val c = 2 * Math.asin(Math.sqrt(a))
        val valueResult = Radius * c
        val km = valueResult / 1
        val newFormat = DecimalFormat("####")
        val kmInDec: Int = Integer.valueOf(newFormat.format(km))
        val meter = valueResult % 1000
        val meterInDec: Int = Integer.valueOf(newFormat.format(meter))
        return Radius * c
    }
    fun getDistanceMeters(): Long {
        val l1: Double = toRadians(latLongA.lat)
        val g1: Double = toRadians(latLongA.long)

        val l2: Double = toRadians(latLongB.lat)
        val g2: Double = toRadians(latLongB.long)
        var dist: Double = acos(sin(l1) * sin(l2) + cos(l1) * cos(l2) * cos(g1 - g2))
        if (dist < 0) {
            dist += PI
        }
        return round(dist * 6378100)
    }
}