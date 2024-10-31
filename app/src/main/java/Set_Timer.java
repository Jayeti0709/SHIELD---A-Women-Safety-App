package com.example.womensateyapp;


import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.os.Handler;
import android.os.Bundle;
import android.os.Looper;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.List;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class Set_Timer extends AppCompatActivity {
    private EditText timerDurationEditText;
    private Button setTimerButton;
    private Button stopTimerButton;
    private Handler timerHandler = new Handler(Looper.getMainLooper());
    private boolean timerRunning = false;
    private List<String> contacts;
    private FusedLocationProviderClient fusedLocationClient;
    private  boolean locationSharingActive=false;
    private LocationRequest locationRequest;
    private static final int PERMISSION_REQUEST = 1; // Request code for both SMS and location permissions
    private LocationCallback locationCallback; // Location callback
    private TextView timerTextView;

    private DatabaseReference databaseReference;
    private long remainingTime = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_timer);
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 2; // This will scale the image down by a factor of 2
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.alarmphoto, options);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Emergency Contact");

        timerTextView = findViewById(R.id.timerTextView);
        timerDurationEditText = findViewById(R.id.timerDurationEditText);
        setTimerButton = findViewById(R.id.setTimerButton);
        stopTimerButton = findViewById(R.id.stopTimerButton);

        setTimerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!timerRunning) {
                    String timerDurationStr = timerDurationEditText.getText().toString();
                    if (!timerDurationStr.isEmpty()) {
                        int timerDuration = Integer.parseInt(timerDurationStr) * 60 * 1000; // Convert seconds to milliseconds
                        startTimer(timerDuration);
                    } else {
                        Toast.makeText(Set_Timer.this, "Enter a valid timer duration.", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        stopTimerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (timerRunning) {
                    stopTimer();
                }
            }
        });


        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        locationRequest = new LocationRequest();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
//        locationRequest.setInterval(60000);  // Update location every 30 seconds

        // Request both SMS and location permissions
        String[] permissions = {android.Manifest.permission.SEND_SMS, Manifest.permission.ACCESS_FINE_LOCATION};
        ActivityCompat.requestPermissions(this, permissions, PERMISSION_REQUEST);


    }

    private void startTimer(long timerDuration) {
        remainingTime =  timerDuration;
        updateTimerText(remainingTime); // Initialize the timer display

        timerHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (timerRunning) {
                    remainingTime -= 1000;
                    if (remainingTime > 0) {
                        updateTimerText(remainingTime);
                        startTimer(remainingTime);
                    } else {
                        messageEveryone();
                    }
                }
            }
        }, 1000);

        timerRunning = true;
    }

    private void stopTimer() {
        timerHandler.removeCallbacksAndMessages(null);
        timerRunning = false;
        timerDurationEditText.setText(null);
    }
    public void messageEveryone() {
        if (!locationSharingActive) {
            System.out.println("locationSharingActive=false");
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_GRANTED
                    && ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                System.out.println("permission granted");
                fetchContactsAndSendLocation(); // Fetch contacts and send location when the button is clicked
            } else {
                Toast.makeText(this, "Both SMS and location permissions are required to send your location.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void fetchContactsAndSendLocation() {
        System.out.println("In fetchContactsAndSendLocation"+locationSharingActive);
        fetchContactsFromDatabase(new ContactCallback() {
            @Override
            public void onContactsFetched(List<String> contactNumbers) {
                contacts = contactNumbers; // Store the fetched contacts

                if (contacts.isEmpty()) {
                    Toast.makeText(Set_Timer.this, "No contacts found.", Toast.LENGTH_SHORT).show();
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
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST) {
            boolean allPermissionsGranted = true;

            for (int i = 0; i < permissions.length; i++) {
                if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                    allPermissionsGranted = false;
                    break;
                }
            }

            if (allPermissionsGranted) {
                // Permissions granted, you can now send the location.
            } else {
                // Permissions denied, inform the user.
                Toast.makeText(this, "Both SMS and location permissions are required to send your location.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void backbutton(View view) {
        Intent i=new Intent(this,MainActivity.class);
        startActivity(i);
        finish();
    }

    public interface ContactCallback {
        void onContactsFetched(List<String> contacts);
    }
    private void sendLocationMessage(String phoneNumber, String message) {
        try {
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(phoneNumber, null, message, null, null);
            Toast.makeText(Set_Timer.this, "Location sent successfully to " + phoneNumber, Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(Set_Timer.this, "Failed to send location: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
    private void updateTimerText(long remainingTime) {
        long minutes = (remainingTime / 60000); // 1 minute = 60,000 milliseconds
        long seconds = (remainingTime % 60000) / 1000; // remaining milliseconds converted to seconds
        String timerText = String.format("%02d:%02d", minutes, seconds);
        timerTextView.setText(timerText);
    }
}
