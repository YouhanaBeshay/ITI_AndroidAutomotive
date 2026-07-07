package com.example.mycalculator;

import android.os.Bundle;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.button.MaterialButton;


// javascript libs for lazy calc :)
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;


public class MainActivity extends AppCompatActivity {
    // Display
    TextView tvInput;
    TextView tvOutput;
    // The expression
    StringBuilder expression = new StringBuilder();

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

        tvInput = findViewById(R.id.tvInput);
        tvOutput = findViewById(R.id.tvOutput);


        // --- Find buttons ---
        MaterialButton btn0 = findViewById(R.id.btn0);
        MaterialButton btn1 = findViewById(R.id.btn1);
        MaterialButton btn2 = findViewById(R.id.btn2);
        MaterialButton btn3 = findViewById(R.id.btn3);
        MaterialButton btn4 = findViewById(R.id.btn4);
        MaterialButton btn5 = findViewById(R.id.btn5);
        MaterialButton btn6 = findViewById(R.id.btn6);
        MaterialButton btn7 = findViewById(R.id.btn7);
        MaterialButton btn8 = findViewById(R.id.btn8);
        MaterialButton btn9 = findViewById(R.id.btn9);
        MaterialButton btnDecimal = findViewById(R.id.btnDecimal);
        MaterialButton btnOpenParen = findViewById(R.id.btnOpenParen);
        MaterialButton btnCloseParen = findViewById(R.id.btnCloseParen);
        MaterialButton btnAdd = findViewById(R.id.btnAdd);
        MaterialButton btnSub = findViewById(R.id.btnSubtract);
        MaterialButton btnMult = findViewById(R.id.btnMultiply);
        MaterialButton btnDiv = findViewById(R.id.btnDivide);


        MaterialButton btnClear = findViewById(R.id.btnClear);
        MaterialButton btnBackspace = findViewById(R.id.btnBackspace);

        MaterialButton btnEquals = findViewById(R.id.btnEquals);

        // --- normal button handler ---
        android.view.View.OnClickListener normalClickListener = v -> {
            MaterialButton btn = (MaterialButton) v; // cast our view "v" to a btn
            String digit = btn.getText().toString();

            expression.append(digit);
            tvOutput.setText("");
            updateInputDisplay();
        };

        // Attach the same listener to all normal buttons
        btn0.setOnClickListener(normalClickListener);
        btn1.setOnClickListener(normalClickListener);
        btn2.setOnClickListener(normalClickListener);
        btn3.setOnClickListener(normalClickListener);
        btn4.setOnClickListener(normalClickListener);
        btn5.setOnClickListener(normalClickListener);
        btn6.setOnClickListener(normalClickListener);
        btn7.setOnClickListener(normalClickListener);
        btn8.setOnClickListener(normalClickListener);
        btn9.setOnClickListener(normalClickListener);
        btnDecimal.setOnClickListener(normalClickListener);
        btnOpenParen.setOnClickListener(normalClickListener);
        btnCloseParen.setOnClickListener(normalClickListener);
        btnAdd.setOnClickListener(normalClickListener);
        btnSub.setOnClickListener(normalClickListener);
        btnDiv.setOnClickListener(normalClickListener);
        btnMult.setOnClickListener(normalClickListener);


        //-- Backspace Button --
        btnBackspace.setOnClickListener(v -> {
            if (expression.length() != 0) {
                expression.setLength(expression.length() - 1);
                tvOutput.setText("");
                updateInputDisplay();
            }

        });


        // --- Clear button ---
        btnClear.setOnClickListener(v -> {
            expression.setLength(0);
            tvInput.setText("");
            tvOutput.setText("");
        });

        // --- Equal button ---
        btnEquals.setOnClickListener(v -> {
            if (expression.length() == 0) return;

            String result = calculateJs(expression.toString());

            if (result.equals("Infinity") || result.equals("-Infinity") || result.equals("NaN")) {
                tvOutput.setText("Math Error");
            } else {

                try {
                    double mathResult = Double.parseDouble(result);

                    if (mathResult == Math.floor(mathResult)) {
                        tvOutput.setText(String.valueOf((long) mathResult));
                    } else {
                        tvOutput.setText(result);
                    }

                } catch (NumberFormatException e) {
                    tvOutput.setText("Syntax Error");
                }
            }
        });
    }


    // Helper functions
    private void updateInputDisplay() {
        tvInput.setText(expression.toString());
    }


    private String calculateJs(String expression) {
        try {
            // some replaces to make it work with eval
            String mathExpr = expression
                    .replace("×", "*")
                    .replace("÷", "/")
                    .replace("−", "-")
                    .replaceAll("(\\d)\\(", "$1*(")
                    .replaceAll("\\)\\(", ")*(");

            // === eval ===
            ScriptEngineManager manager = new ScriptEngineManager();
            ScriptEngine engine = manager.getEngineByName("rhino");

            if (engine == null) {
                return "Engine Error";
            }

            Object result = engine.eval(mathExpr);
            return result.toString();

        } catch (ScriptException e) {
            return "Syntax Error";
        }
    }
}
