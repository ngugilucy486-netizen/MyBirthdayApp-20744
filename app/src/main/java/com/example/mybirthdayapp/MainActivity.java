package com.example.mybirthdayapp;

import android.os.Bundle;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.textfield.TextInputEditText;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    private TextInputEditText nameInput;
    private TextInputEditText dateInput;
    private TextView resultText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        nameInput = findViewById(R.id.nameInput);
        dateInput = findViewById(R.id.dateInput);
        Button calculateButton = findViewById(R.id.calculateButton);
        resultText = findViewById(R.id.resultText);

        // Slide up animation for the input card
        View inputCard = findViewById(R.id.inputCard);
        Animation slideUp = AnimationUtils.loadAnimation(this, R.anim.slide_up);
        inputCard.startAnimation(slideUp);

        calculateButton.setOnClickListener(v -> calculateBirthday());
    }

    private void calculateBirthday() {
        if (nameInput.getText() == null || dateInput.getText() == null) return;
        
        String name = nameInput.getText().toString().trim();
        String dateStr = dateInput.getText().toString().trim();

        if (name.isEmpty() || dateStr.length() != 8) {
            Toast.makeText(this, "Please enter a valid name and date (YYYYMMDD)", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            int birthYear = Integer.parseInt(dateStr.substring(0, 4));
            int birthMonth = Integer.parseInt(dateStr.substring(4, 6)) - 1; // Calendar months are 0-indexed
            int birthDay = Integer.parseInt(dateStr.substring(6, 8));

            long daysRemaining = getDaysRemaining(birthYear, birthMonth, birthDay);

            String message = getString(R.string.hello_msg, name, daysRemaining);
            resultText.setText(message);
            
            // Professional Animation
            resultText.setVisibility(View.VISIBLE);
            Animation fadeIn = new AlphaAnimation(0, 1);
            fadeIn.setDuration(1000);
            resultText.startAnimation(fadeIn);

        } catch (Exception e) {
            Toast.makeText(this, "Invalid date format. Use YYYYMMDD.", Toast.LENGTH_SHORT).show();
        }
    }

    private long getDaysRemaining(int birthYear, int birthMonth, int birthDay) {
        // Validate and initialize birth date
        Calendar birthDate = Calendar.getInstance();
        birthDate.setLenient(false);
        birthDate.set(birthYear, birthMonth, birthDay);
        birthDate.getTime(); // Trigger validation

        Calendar today = getCleanCalendar();

        // Prepare next birthday calendar
        Calendar nextBirthday = getCleanCalendar();
        nextBirthday.set(Calendar.MONTH, birthMonth);
        nextBirthday.set(Calendar.DAY_OF_MONTH, birthDay);

        if (nextBirthday.before(today) || nextBirthday.equals(today)) {
            nextBirthday.add(Calendar.YEAR, 1);
        }

        long diff = nextBirthday.getTimeInMillis() - today.getTimeInMillis();
        return diff / (24 * 60 * 60 * 1000);
    }

    private Calendar getCleanCalendar() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar;
    }
}