//package com.example.womensateyapp;
//
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.core.app.ActivityCompat;
//
//import android.content.pm.PackageManager;
//import android.os.Bundle;
////
////public class LocationTraking extends AppCompatActivity {
////
////    @Override
////    protected void onCreate(Bundle savedInstanceState) {
////        super.onCreate(savedInstanceState);
////        setContentView(R.layout.activity_location_traking);
////    }
////}
//
//import android.content.Context;
//import android.location.Location;
//import android.location.LocationListener;
//import android.location.LocationManager;
//import android.os.Bundle;
//
//public class LocationTraking {
//    private LocationManager locationManager;
//    private LocationListener locationListener;
//
//    public LocationTraking(Context context) {
//        locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
//        locationListener = new LocationListener() {
//            @Override
//            public void onLocationChanged(Location location) {
//                // Handle the updated location here
//                double userLatitude = location.getLatitude();
//                double userLongitude = location.getLongitude();
//
//                // Calculate distances to red zone areas and send notifications if needed
//                // (Step 4).
//
//                // Replace the dummy data with real data for red zone areas (Step 2).
//                // You can create instances of RedZoneArea and use them for distance calculations.
//            }
//
//            @Override
//            public void onStatusChanged(String provider, int status, Bundle extras) {
//            }
//
//            @Override
//            public void onProviderEnabled(String provider) {
//            }
//
//            @Override
//            public void onProviderDisabled(String provider) {
//            }
//        };
//    }
//
//    public void startLocationTracking() {
//        // Request location updates
//        if (locationManager != null) {
//            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//                // TODO: Consider calling
//                //    ActivityCompat#requestPermissions
//                // here to request the missing permissions, and then overriding
//                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
//                //                                          int[] grantResults)
//                // to handle the case where the user grants the permission. See the documentation
//                // for ActivityCompat#requestPermissions for more details.
//                return;
//            }
//            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
//        }
//    }
//
//    public void stopLocationTracking() {
//        // Stop location updates
//        if (locationManager != null) {
//            locationManager.removeUpdates(locationListener);
//        }
//    }
//}
