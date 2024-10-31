package com.example.womensateyapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.Manifest;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.IntentFilter;
import android.location.Location;
import android.media.Image;
import android.media.MediaPlayer;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.Manifest;
import android.location.Location;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.core.Context;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private static final int PERMISSION_REQUEST = 1;
    private List<String> contacts;
    private static final int REQUEST_CALL_PHONE = 1;
    private DrawerLayout drawerLayout;
    private MediaPlayer mediaPlayer;
    private FusedLocationProviderClient fusedLocationClient;
    private LocationRequest locationRequest;
    private LocationCallback locationCallback;
    private DatabaseReference databaseReference;
    private boolean locationSharingActive = false;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent serviceIntent = new Intent(this, ShakeDetectionService.class);
        serviceIntent.putExtra("play_alarm", true); // Signal to start the alarm
        startService(serviceIntent);

        boolean returnToMain = getIntent().getBooleanExtra("returnToMain", false);
//        if (getIntent().getBooleanExtra("returnToMain", false)) {
//            Toast.makeText(this, "call ends.", Toast.LENGTH_SHORT).show();
//        }
        if (returnToMain) {
            // The call has been completed, and you want to return to MainActivity
            // You can perform any necessary actions here

            // Example: You can finish this activity to return to MainActivity
            finish();
        }

        BroadcastReceiver shakeReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(android.content.Context context, Intent intent) {
                stopAlarm();
            }


        };
        LocalBroadcastManager.getInstance(this).registerReceiver(shakeReceiver, new IntentFilter("shake_detected"));

        mediaPlayer = MediaPlayer.create(this, R.raw.alarm);
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        locationRequest = new LocationRequest();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(30000);

        String[] permissions = {Manifest.permission.SEND_SMS, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.CALL_PHONE};
        ActivityCompat.requestPermissions(this, permissions, PERMISSION_REQUEST);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Emergency Contact");
        drawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open_nav, R.string.close_nav);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();





    }

    public void setAlarm(View view) {
        if (ShakeDetectionService.isAudioPlaying) {

            if (mediaPlayer != null) {
                System.out.println("hello");
               mediaPlayer.stop();

                ShakeDetectionService.isAudioPlaying = false;
            }
        } else {
            if (!mediaPlayer.isPlaying()) {
                mediaPlayer.start();
                ShakeDetectionService.isAudioPlaying = true;
            }
        }
    }

    private void stopAlarm() {
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
            Toast.makeText(this, "Alarm stopped.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == MainAdapter2.REQUEST_CALL_PHONE) {
            // Handle the result of the call action, e.g., return to the MainActivity
            // You can finish the current activity or navigate back to the MainActivity here.
            // For example:
            if (resultCode == Activity.RESULT_OK) {
                // Call was made successfully
                // You can navigate back to the MainActivity using an Intent
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
            }
        }
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.home) {
            // Handle navigation to AddUserActivity
            Intent intent = new Intent(this, AddUser.class);
            startActivity(intent);
        } else if (itemId == R.id.video) {
            // Handle navigation to Videos
            Intent intent = new Intent(this, Videos.class);
            startActivity(intent);
        } else if (itemId == R.id.nav_logout) {
            // Handle logout
            Toast.makeText(this, "Logout!", Toast.LENGTH_SHORT).show();
            FirebaseAuth.getInstance().signOut();
            Intent intent = new Intent(this, Login.class);
            startActivity(intent);
            finish();
        } else if (itemId == R.id.nav_faq) {
            // Handle navigation to FAQ
            Intent intent = new Intent(this, FAQ.class);
            startActivity(intent);
        } else if (itemId == R.id.nav_settings) {
            // Handle navigation to Set_Timer
            Intent intent = new Intent(this, Set_Timer.class);
            startActivity(intent);
        }

        // Close the drawer
        DrawerLayout drawerLayout = findViewById(R.id.drawer_layout);
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }


    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    public void callPolice(View view) {
        String phoneNumber = "9569403452";

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
            makeCall(phoneNumber);
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE}, REQUEST_CALL_PHONE);
        }
    }

    private void makeCall(String phoneNumber) {
        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phoneNumber));
        startActivity(intent);
    }


    private void sendLocationMessage(String phoneNumber, String message) {
        try {
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(phoneNumber, null, message, null, null);
            Toast.makeText(MainActivity.this, "Location sent successfully to " + phoneNumber, Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(MainActivity.this, "Failed to send location: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
    private void stopSharingLocation() {
        if (locationCallback != null) {
            fusedLocationClient.removeLocationUpdates(locationCallback);
            Toast.makeText(this, "Location sharing has been stopped.", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Location sharing is not active.", Toast.LENGTH_SHORT).show();
        }
    }


    public void messageEveryone(View view) {
        if (!locationSharingActive) {
            System.out.println("locationSharingActive=false");
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_GRANTED
                    && ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                System.out.println("permission granted");
                fetchContactsAndSendLocation(); // Fetch contacts and send location when the button is clicked
            } else {
                Toast.makeText(this, "Both SMS and location permissions are required to send your location.", Toast.LENGTH_SHORT).show();
            }
        } else {
            stopSharingLocation();
        }
    }

    private void fetchContactsAndSendLocation() {
        System.out.println("In fetchContactsAndSendLocation"+locationSharingActive);
        fetchContactsFromDatabase(new ContactCallback() {
            @Override
            public void onContactsFetched(List<String> contactNumbers) {
                contacts = contactNumbers; // Store the fetched contacts

                if (contacts.isEmpty()) {
                    Toast.makeText(MainActivity.this, "No contacts found.", Toast.LENGTH_SHORT).show();
                } else {
                    // Get the current location and send it to the fetched contacts
                    startLocationSharing();
                }
            }
        });
    }

    private void startLocationSharing() {
        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(@NonNull LocationResult locationResult) {
                Location location = locationResult.getLastLocation();
                String latitude = String.valueOf(location.getLatitude());
                String longitude = String.valueOf(location.getLongitude());

                for (String contact : contacts) {
                    String message = "Help me!! This is my current location: https://maps.google.com/?q=" + latitude + "," + longitude;
                    sendLocationMessage("+91" + contact, message);
                }
                System.out.println("startLocationSharing"+locationSharingActive);
                // Stop location sharing after sending
//                stopSharingLocation();
            }
        };

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            // Request location updates
            fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, null);
//            locationSharingActive = true;
            Toast.makeText(this, "Location sharing is active.", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Location permission is required to send your location.", Toast.LENGTH_SHORT).show();
        }
    }


    private void fetchContactsFromDatabase(final ContactCallback callback) {
        if(!locationSharingActive){
            locationSharingActive=false;
            System.out.println("locationSharingActive=false");
        final List<String> contactNumbers = new ArrayList<>();

        databaseReference.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(!locationSharingActive) {
                    System.out.println(locationSharingActive);
                    for (DataSnapshot contactSnapshot : dataSnapshot.getChildren()) {
                        String phoneNumber = contactSnapshot.child("Phoneno").getValue(String.class);
                        if (phoneNumber != null) {
                            contactNumbers.add(phoneNumber);
                        }
                    }
                    callback.onContactsFetched(contactNumbers);

                }
                locationSharingActive=true;
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle errors if necessary
            }
        });
    }


    }
    public void callPerson(View view) {
        Intent intent = new Intent(this,Calltoperson.class);
        startActivity(intent);
        finish();
    }

    public interface ContactCallback {
        void onContactsFetched(List<String> contacts);
    }





    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Release the MediaPlayer when the activity is destroyed to avoid memory leaks
        if (mediaPlayer != null) {
            mediaPlayer.release();
        }
    }


}



//public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
//    private static final int REQUEST_CALL_PHONE = 1;
//    private DrawerLayout drawerLayout;
//
//    private MediaPlayer mediaPlayer;
//    private FusedLocationProviderClient fusedLocationClient;
//    private LocationRequest locationRequest;
//    private static final int PERMISSION_REQUEST = 1; // Request code for both SMS and location permissions
//    private LocationCallback locationCallback; // Location callback
//    private DatabaseReference databaseReference;
//    private static final int LOCATION_PERMISSION_REQUEST = 1;
//    private static final int SMS_PERMISSION_REQUEST = 2;
//    private boolean locationSharingActive = false;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
//        locationRequest = new com.google.android.gms.location.LocationRequest();
//        locationRequest.setPriority(com.google.android.gms.location.LocationRequest.PRIORITY_HIGH_ACCURACY);
//        locationRequest.setInterval(60000);  // Update location every 30 seconds
//
//        // Request both SMS and location permissions
//        String[] permissions = {Manifest.permission.SEND_SMS, Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.CALL_PHONE};
//        ActivityCompat.requestPermissions(this, permissions, PERMISSION_REQUEST);
//
////        ImageButton sendLocationButton = findViewById(R.id.btnMessageFamilyMember);
////        sendLocationButton.setOnClickListener(v -> sendLocationToContacts());
//
////        mediaPlayer = MediaPlayer.create(this, R.raw.biohazard_alarm);
//        Toolbar toolbar = findViewById(R.id.toolbar); //Ignore red line errors
//
//        setSupportActionBar(toolbar);
//        databaseReference = FirebaseDatabase.getInstance().getReference().child("Emergency Contact");
//        drawerLayout = findViewById(R.id.drawer_layout);
//        NavigationView navigationView = findViewById(R.id.nav_view);
//        navigationView.setNavigationItemSelectedListener(this);
//
//        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open_nav,
//                R.string.close_nav);
//        drawerLayout.addDrawerListener(toggle);
//        toggle.syncState();
//
//        if (savedInstanceState == null) {
//            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new Home()).commit();
//            navigationView.setCheckedItem(R.id.nav_home);
//        }
//    }
//    @Override
//    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
//        int itemId = item.getItemId();
//        if (itemId == R.id.home) {
//            // Handle navigation to AddUserActivity
//            Intent intent = new Intent(this, AddUser.class);
//            startActivity(intent);
//        }  else if (itemId == R.id.nav_share) {
//            // Handle navigation to DeleteUserActivity
//            Intent intent = new Intent(this, DeleteUser.class);
//            startActivity(intent);
//        } else if (itemId == R.id.nav_about) {
//            // Handle navigation to ProfileActivity
//            Intent intent = new Intent(this, MyProfile.class);
//            startActivity(intent);
//        } else if (itemId == R.id.nav_logout) {
//            // Handle logout
//            Toast.makeText(this, "Logout!", Toast.LENGTH_SHORT).show();
//            FirebaseAuth.getInstance().signOut();
//            Intent intent = new Intent(this, Login.class);
//            startActivity(intent);
//            finish();
//        }
//
//        // Close the drawer
//        DrawerLayout drawerLayout = findViewById(R.id.drawer_layout); // Replace with the appropriate ID
//        drawerLayout.closeDrawer(GravityCompat.START);
//        return true;
//    }
//
//    @Override
//    public void onBackPressed() {
//        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
//            drawerLayout.closeDrawer(GravityCompat.START);
//        } else {
//            super.onBackPressed();
//        }
//    }
//
//    public void callPerson(View view) {
//        // Replace "phone_number" with the actual phone number you want to call.
//        String phoneNumber = "9569403452";
//
//        // Check for CALL_PHONE permission
//        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
//            // You have the permission, proceed with the call.
//            makeCall(phoneNumber);
//        } else {
//            // You don't have the permission, request it from the user.
//            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE}, REQUEST_CALL_PHONE);
//        }
//    }
//
//    private void makeCall(String phoneNumber) {
//        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phoneNumber));
//        startActivity(intent);
//    }
//
//    // Handle permission request result
//
//
//
//     public void messageEveryone(View view) {
//         if (!locationSharingActive) {
//             // Check if the user has granted both SMS and location permissions
//             if (ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_GRANTED
//                     && ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
//                 locationCallback = new LocationCallback() {
//
//                     @Override
//                     public void onLocationResult(@NonNull LocationResult locationResult) {
//
//                         Location location = locationResult.getLastLocation();
//                         String latitude = String.valueOf(location.getLatitude());
//                         String longitude = String.valueOf(location.getLongitude());
//
//                         fetchContactsFromDatabase(new ContactCallback() {
//                             @Override
//                             public void onContactsFetched(List<String> contacts) {
//                                 System.out.println(contacts);
//                                 for (String contact : contacts) {
//                                     String message = "Help me!! This is my current location: https://maps.google.com/?q=" + latitude + "," + longitude;
//                                     // Send messages to contacts
//                                     sendLocationMessage("+91" + contact, message);
//                                 }
//                             }
//                         });
//                     }
//                 };
//
//                 fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, null);
//                 locationSharingActive = true;
//                 Toast.makeText(this, "Location sharing is active.", Toast.LENGTH_SHORT).show();
//             } else {
//                 Toast.makeText(this, "this Both SMS and location permissions are required to send your location.", Toast.LENGTH_SHORT).show();
//             }
//         } else {
//             stopSharingLocation();
//             locationSharingActive = false;
//         }
//     }
//
//    private void sendLocationMessage(String phoneNumber, String message) {
//        try {
//            SmsManager smsManager = SmsManager.getDefault();
//            smsManager.sendTextMessage(phoneNumber, null, message, null, null);
//            Toast.makeText(MainActivity.this, "Location sent successfully to " + phoneNumber, Toast.LENGTH_SHORT).show();
//        } catch (Exception e) {
//            e.printStackTrace();
//            Toast.makeText(MainActivity.this, "Failed to send location: " + e.getMessage(), Toast.LENGTH_SHORT).show();
//        }
//    }
//    public interface ContactCallback {
//        void onContactsFetched(List<String> contacts);
//    }
//
//
//    private void stopSharingLocation() {
//        if (locationCallback != null) {
//            fusedLocationClient.removeLocationUpdates(locationCallback);
//            Toast.makeText(this, "Location sharing has been stopped.", Toast.LENGTH_SHORT).show();
//        } else {
//            Toast.makeText(this, "Location sharing is not active.", Toast.LENGTH_SHORT).show();
//        }
//    }
//    private void fetchContactsFromDatabase(final ContactCallback callback) {
//        final List<String> contacts = new ArrayList<>();
//        databaseReference.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                for (DataSnapshot contactSnapshot : dataSnapshot.getChildren()) {
//                    String phoneNumber = contactSnapshot.child("Phoneno").getValue(String.class);
//                    if (phoneNumber != null) {
//                        contacts.add(phoneNumber);
//                    }
//                }
//                // Call the callback with the contacts list
//                callback.onContactsFetched(contacts);
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//                // Handle errors if necessary
//            }
//        });
//
//    }
//
//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        if (requestCode == PERMISSION_REQUEST) {
//            boolean allPermissionsGranted = true;
//
//            for (int i = 0; i < permissions.length; i++) {
//                if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
//                    allPermissionsGranted = false;
//                    break;
//                }
//            }
//
//            if (allPermissionsGranted) {
//
//            } else {
//                // Permissions denied, inform the user.
//                Toast.makeText(this, "Both SMS and location permissions are required to send your location.", Toast.LENGTH_SHORT).show();
//            }
//        }
//    }
//
//
//
//    public void setAlarm(View view) {
//        // Check if the MediaPlayer is playing
//        if (mediaPlayer.isPlaying()) {
//            // If it's playing, stop it
//            mediaPlayer.stop();
//            mediaPlayer.prepareAsync(); // Prepare it for the next play
//        }
//
//        // Start playing the audio
//        mediaPlayer.start();
//
//        Toast.makeText(this, "Setting an alarm and playing a song.", Toast.LENGTH_SHORT).show();
//    }
//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        // Release the MediaPlayer when the activity is destroyed to avoid memory leaks
//        if (mediaPlayer != null) {
//            mediaPlayer.release();
//        }
//    }
//    public void callPolice(View view) {
//        // Replace "emergency_number" with the actual police emergency number (e.g., 911).
//        String policeNumber = "emergency_number";
//        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + policeNumber));
//        startActivity(intent);
//    }
//}
