package com.example.salaryestimator;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;


import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    // Yoe ui components
    TextInputLayout tilYoe;
    TextInputEditText etYoe;

    // Job ui components
    TextInputLayout tilJob;
    AutoCompleteTextView actvJob;
    // Country ui components
    TextInputLayout tilCountry;
    AutoCompleteTextView actvCountry;

    // btn
    MaterialButton btnCalculate;

    // the result ui components
    MaterialCardView cardResult;
    TextView tvResultSalary;
    TextView tvResultComment;


    // drop down lists
    private static final String[] jobs = {
            "Embedded Software Engineer", "Backend Developer", "Frontend Developer",
            "DevOps Engineer", "QA Engineer","AI Engineer"
    };

    private static final String[] countries = {
            "Egypt", "USA", "UK", "UAE", "Germany", "Australia",
            "Japan", "France", "Netherlands"
    };

    // the "database"
    private Map <String, Map<String, Integer>> salaryDatabase;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main); // inflation
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // get references to the java objects created by the inflation
        tilYoe = findViewById(R.id.tilYoe);
        etYoe = findViewById(R.id.etYoe);

        tilJob = findViewById(R.id.tilJob);
        actvJob = findViewById(R.id.actvJob);

        tilCountry = findViewById(R.id.tilCountry);
        actvCountry = findViewById(R.id.actvCountry);

        btnCalculate = findViewById(R.id.btnCalculate);

        cardResult = findViewById(R.id.cardResult);
        tvResultSalary = findViewById(R.id.tvResultSalary);
        tvResultComment = findViewById(R.id.tvResultComment);


        // fill and config  the dropdown lists
        actvJob.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, jobs));
        actvCountry.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, countries));

        // show the dropdown on focus
        actvJob.setOnFocusChangeListener(((v, hasFocus) -> {
            if (hasFocus) {
                hideKeyboard();
                actvJob.showDropDown();
            }
        }));
        actvCountry.setOnFocusChangeListener(((v, hasFocus) -> {
            if (hasFocus) {
                hideKeyboard();
                actvCountry.showDropDown();
            }
        }));


        // fill the database
        buildSalaryDatabase();

        // btn handler
        btnCalculate.setOnClickListener(v -> onCalculateClicked());
    }


    // ================= helper functions ======================

    // hides the keyboard (as without sometimes keyboard floats)
    private void hideKeyboard() {
        android.view.View view = getCurrentFocus();
        if (view != null) {
            android.view.inputmethod.InputMethodManager imm =
                    (android.view.inputmethod.InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
    private boolean validateYoe() {
        String yoeText = etYoe.getText().toString().trim();
        if (yoeText.isEmpty()) {
            tilYoe.setError("Years of experience is required");
            return false;
        }
        return true;
    }
    private boolean validateJob() {
        String job = actvJob.getText().toString().trim();
        if (job.isEmpty()) {
            tilJob.setError("Please select a job");
            return false;
        }
        return true;
    }
    private boolean validateCountry() {
        String country = actvCountry.getText().toString().trim();
        if (country.isEmpty()) {
            tilCountry.setError("Please select a country");
            return false;
        }
        return true;
    }
    private int parseYoe() {
        String yoeText = etYoe.getText().toString().trim();
        try {
            int yoe = Integer.parseInt(yoeText);
            if (yoe < 0 || yoe > 30) {
                tilYoe.setError("Enter a realistic number (0-30)");
                return -1;
            }
            return yoe;
        } catch (NumberFormatException e) {
            tilYoe.setError("Invalid number");
            return -1;
        }
    }

    private int lookupSalary(String country, String job) {
        Map<String, Integer> countryMap = salaryDatabase.get(country);
        if (countryMap == null || !countryMap.containsKey(job)) {
            return -1;
        }
        return countryMap.get(job);
    }

    private void clear() {
        tilYoe.setError(null);
        tilJob.setError(null);
        tilCountry.setError(null);
        tvResultSalary.setText("$0");
        tvResultComment.setText("Enter your details and tap calculate salary");
    }

    private void onCalculateClicked() {
        hideKeyboard();
        clear();
        boolean yoeValid = validateYoe();
        boolean jobValid = validateJob();
        boolean countryValid = validateCountry();

        if (!yoeValid || !jobValid || !countryValid) {
            return;
        }

        // the only param we actually need to parse
        int yoe = parseYoe();
        if (yoe == -1) {
            return;
        }

        String job = actvJob.getText().toString().trim();
        String country = actvCountry.getText().toString().trim();


        int baseSalary = lookupSalary(country, job);
        if (baseSalary == -1) { // shouldnt even trigger but why not
            tvResultSalary.setText("$0");
            tvResultComment.setText("No salary data for " + job + " in " + country);
            return;
        }

        // only 3% increase per year ( that would be slavery but isnt that life)
        double multiplier = 1.0 + (yoe * 0.03);
        int estimatedSalary = (int) (baseSalary * multiplier);

        tvResultSalary.setText("$" + String.format("%,d", estimatedSalary));
        tvResultComment.setText("Based on " + yoe + " years as " + job + " in " + country);
    }


    // ====== AI generated map for salary :) =======================
    private void buildSalaryDatabase() {
        salaryDatabase = new HashMap<>();

        // Egypt
        Map<String, Integer> egypt = new HashMap<>();
        egypt.put("Embedded Software Engineer", 12000);
        egypt.put("Backend Developer", 14000);
        egypt.put("Frontend Developer", 12000);
        egypt.put("DevOps Engineer", 16000);
        egypt.put("QA Engineer", 9000);
        egypt.put("AI Engineer", 18000);
        salaryDatabase.put("Egypt", egypt);

        // USA
        Map<String, Integer> usa = new HashMap<>();
        usa.put("Embedded Software Engineer", 110000);
        usa.put("Backend Developer", 130000);
        usa.put("Frontend Developer", 115000);
        usa.put("DevOps Engineer", 135000);
        usa.put("QA Engineer", 95000);
        usa.put("AI Engineer", 160000);
        salaryDatabase.put("USA", usa);

        // UK
        Map<String, Integer> uk = new HashMap<>();
        uk.put("Embedded Software Engineer", 65000);
        uk.put("Backend Developer", 80000);
        uk.put("Frontend Developer", 70000);
        uk.put("DevOps Engineer", 85000);
        uk.put("QA Engineer", 55000);
        uk.put("AI Engineer", 95000);
        salaryDatabase.put("UK", uk);

        // UAE
        Map<String, Integer> uae = new HashMap<>();
        uae.put("Embedded Software Engineer", 70000);
        uae.put("Backend Developer", 85000);
        uae.put("Frontend Developer", 75000);
        uae.put("DevOps Engineer", 90000);
        uae.put("QA Engineer", 60000);
        uae.put("AI Engineer", 100000);
        salaryDatabase.put("UAE", uae);

        // Germany
        Map<String, Integer> germany = new HashMap<>();
        germany.put("Embedded Software Engineer", 70000);
        germany.put("Backend Developer", 80000);
        germany.put("Frontend Developer", 70000);
        germany.put("DevOps Engineer", 85000);
        germany.put("QA Engineer", 55000);
        germany.put("AI Engineer", 95000);
        salaryDatabase.put("Germany", germany);

        // Australia
        Map<String, Integer> australia = new HashMap<>();
        australia.put("Embedded Software Engineer", 90000);
        australia.put("Backend Developer", 105000);
        australia.put("Frontend Developer", 95000);
        australia.put("DevOps Engineer", 110000);
        australia.put("QA Engineer", 80000);
        australia.put("AI Engineer", 125000);
        salaryDatabase.put("Australia", australia);

        // Japan
        Map<String, Integer> japan = new HashMap<>();
        japan.put("Embedded Software Engineer", 55000);
        japan.put("Backend Developer", 65000);
        japan.put("Frontend Developer", 60000);
        japan.put("DevOps Engineer", 70000);
        japan.put("QA Engineer", 45000);
        japan.put("AI Engineer", 80000);
        salaryDatabase.put("Japan", japan);

        // France
        Map<String, Integer> france = new HashMap<>();
        france.put("Embedded Software Engineer", 60000);
        france.put("Backend Developer", 70000);
        france.put("Frontend Developer", 62000);
        france.put("DevOps Engineer", 75000);
        france.put("QA Engineer", 50000);
        france.put("AI Engineer", 85000);
        salaryDatabase.put("France", france);

        // Netherlands
        Map<String, Integer> netherlands = new HashMap<>();
        netherlands.put("Embedded Software Engineer", 68000);
        netherlands.put("Backend Developer", 78000);
        netherlands.put("Frontend Developer", 70000);
        netherlands.put("DevOps Engineer", 82000);
        netherlands.put("QA Engineer", 58000);
        netherlands.put("AI Engineer", 92000);
        salaryDatabase.put("Netherlands", netherlands);
    }

}