package com.prm392.assignment.productsale.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.navigation.NavController;
import androidx.navigation.NavOptions;
import androidx.navigation.fragment.NavHostFragment;
import com.prm392.assignment.productsale.R;

public class PaymentNotification extends AppCompatActivity {

    TextView txtNotification;
    Button btnBackHome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_notification);

        txtNotification = findViewById(R.id.textViewNotify);

        Intent intent = getIntent();
        txtNotification.setText(intent.getStringExtra("result"));

        btnBackHome = findViewById(R.id.btnBackHome);
        btnBackHome.setOnClickListener((v) -> {
            // Navigate to MainActivity and then to home fragment
            Intent intent1 = new Intent(PaymentNotification.this, MainActivity.class);
            intent1.putExtra("navigate_to_home", true); // Add flag to indicate navigation to home
            intent1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK); // Clear all activities
            startActivity(intent1);
            finish(); // Close the PaymentNotification activity
        });

    }
}