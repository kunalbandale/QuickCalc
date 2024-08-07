package com.quickcalc;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.button.MaterialButton;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.Scriptable;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    // Declare TextViews for displaying result and solution
    TextView result_tv, solution_tv;

    // Declare buttons for calculator operations and digits
    MaterialButton button_c, button_open_bracket, button_close_bracket;
    MaterialButton button_divide, button_mul, button_plus, button_minus;
    MaterialButton button_1, button_2, button_3, button_4, button_5, button_6;
    MaterialButton button_7, button_8, button_9, button_0, button_dot, button_ac, button_eq;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize TextViews by finding them in the layout
        result_tv = findViewById(R.id.result_tv);
        solution_tv = findViewById(R.id.solution_tv);

        // Assign IDs to buttons and set click listeners
        assignId(button_c, R.id.button_c);
        assignId(button_open_bracket, R.id.button_open_bracket);
        assignId(button_close_bracket, R.id.button_close_bracket);
        assignId(button_divide, R.id.button_divide);
        assignId(button_mul, R.id.button_mul);
        assignId(button_plus, R.id.button_plus);
        assignId(button_minus, R.id.button_minus);
        assignId(button_1, R.id.button_1);
        assignId(button_2, R.id.button_2);
        assignId(button_3, R.id.button_3);
        assignId(button_4, R.id.button_4);
        assignId(button_5, R.id.button_5);
        assignId(button_6, R.id.button_6);
        assignId(button_7, R.id.button_7);
        assignId(button_8, R.id.button_8);
        assignId(button_9, R.id.button_9);
        assignId(button_0, R.id.button_0);
        assignId(button_dot, R.id.button_dot);
        assignId(button_ac, R.id.button_ac);
        assignId(button_eq, R.id.button_eq);

        // Enable edge-to-edge UI
        EdgeToEdge.enable(this);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            // Set padding to avoid overlap with system bars (status bar, navigation bar)
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    // Helper method to assign IDs to buttons and set click listeners
    void assignId(MaterialButton btn, int id) {
        btn = findViewById(id);
        btn.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        // Get the clicked button
        MaterialButton button = (MaterialButton) view;
        String buttonText = button.getText().toString();
        String dataToCalculate = solution_tv.getText().toString();

        // Handle AC button click
        if (buttonText.equals("AC")) {
            solution_tv.setText("");
            result_tv.setText("");
            return;
        }

        // Handle Equals button click
        if (buttonText.equals("=")) {
            solution_tv.setText(result_tv.getText());
            return;
        }

        // Handle DEL button click
        if (buttonText.equals("DEL")) {
            if (!dataToCalculate.isEmpty()) {
                dataToCalculate = dataToCalculate.substring(0, dataToCalculate.length() - 1);
            }
        } else {
            dataToCalculate = dataToCalculate + buttonText;
        }

        // Update solution text view
        solution_tv.setText(dataToCalculate);

        // Get the result of the calculation
        String finalResult = getResult(dataToCalculate);
        if (!finalResult.equals("ERROR")) {
            result_tv.setText(finalResult);
        } else {
            result_tv.setText("");
        }
    }

    // Method to evaluate the mathematical expression using Rhino JavaScript engine
    String getResult(String data) {
        try {
            if (data.isEmpty()) {
                return "";
            }
            // Initialize the Rhino JavaScript context
            Context context = Context.enter();
            context.setOptimizationLevel(-1);
            Scriptable scriptable = context.initSafeStandardObjects();
            // Evaluate the expression
            String finalResult = context.evaluateString(scriptable, data, "Javascript", 1, null).toString();
            // Remove trailing .0 for integer results
            if (finalResult.endsWith(".0")) {
                finalResult = finalResult.replace(".0", "");
            }
            return finalResult;
        } catch (Exception e) {
            return "ERROR";
        } finally {
            // Exit the Rhino context
            Context.exit();
        }
    }
}
