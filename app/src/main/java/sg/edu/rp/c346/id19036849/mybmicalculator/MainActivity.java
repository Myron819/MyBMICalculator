package sg.edu.rp.c346.id19036849.mybmicalculator;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    Button btnCalc, btnReset;
    EditText etWeight, etHeight;
    TextView tvLastCalcDate, tvLastCalcBMI, tvLastComment;
    RadioGroup rgMCm, rgKgG;

    @Override
    protected void onResume() {
        super.onResume();

        // Step 2a: Obtain an instance of the SharedPreferences
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);

        // Step 2b: Retrieve the saved data with the specified key from the SharedPreferences object
        String strDateTime = prefs.getString("datetime", "");
        String srtBMI = prefs.getString("BMI", "");
        String comment = prefs.getString("comment", "");

        // Step 3a: Update the UI element with the value
        tvLastCalcDate.setText("Last Calculated Date: " + strDateTime);
        tvLastCalcBMI.setText("Last Calculated BMI: " + srtBMI);
        tvLastComment.setText(comment);

    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    protected void save(String datetime, String bmi, String comment) {

        // Step 1a: Obtain an instance of the SharedPreferences
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);

        // Step 1b: Obtain an instance of the SharedPreferences Editor for update later
        SharedPreferences.Editor prefEdit = prefs.edit();

        // Step 1c: Add the key-value pair

        prefEdit.putString("datetime", datetime);
        prefEdit.putString("BMI", bmi);
        prefEdit.putString("comment", comment);

        // Step 1d: Call commit() to save the changes into SharedPreferences
        prefEdit.commit();

        Toast toast = Toast.makeText(MainActivity.this, "Saved", Toast.LENGTH_LONG);
        toast.show();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnCalc = findViewById(R.id.buttonCalculate);
        btnReset = findViewById(R.id.buttonResetData);
        etWeight = findViewById(R.id.editTextWeight);
        etHeight = findViewById(R.id.editTextHeight);
        tvLastCalcDate = findViewById(R.id.textViewLastCalcDate);
        tvLastCalcBMI = findViewById(R.id.textViewLastCalcBMI);
        tvLastComment = findViewById(R.id.textViewLastComment);
        rgMCm = findViewById(R.id.radioGroupUnits);
        rgKgG = findViewById(R.id.radioGroupWeightUnits);

        btnCalc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                double weight = Double.parseDouble(etWeight.getText().toString());
                double height = Double.parseDouble(etHeight.getText().toString());
                String comment = "";

                if (rgMCm.getCheckedRadioButtonId() == R.id.radioButtonCentimeters) {
                    height = height / 100;
                }

                if (rgKgG.getCheckedRadioButtonId() == R.id.radioButtonGrams) {
                    weight = weight / 100;
                }

                String BMI = String.format("%.3f", (weight / (height * height)));
                double BMI1 = Double.parseDouble(BMI);

                if (BMI1 < 18.5) {
                    comment = "You are underweight";
                } else if (BMI1 <= 24.9) {
                    comment = "Your BMI is normal";
                } else if (BMI1 <= 29.9) {
                    comment = "You are overweight";
                } else {
                    comment = "You are obese";
                }

                tvLastComment.setText(comment);

                //Create a Calendar object with current date and time
                Calendar now = Calendar.getInstance();

                String datetime = now.get(Calendar.DAY_OF_MONTH) + "/" +
                        (now.get(Calendar.MONTH)+1) + "/" +
                        now.get(Calendar.YEAR) + " " +
                        now.get(Calendar.HOUR_OF_DAY) + ":" +
                        now.get(Calendar.MINUTE);

                tvLastCalcDate.setText("Last Calculated Date: " + datetime);
                tvLastCalcBMI.setText("Last Calculated BMI: " + BMI);

                etHeight.getText().clear();
                etWeight.getText().clear();
                save(datetime, BMI, comment);
            }
        });

        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etHeight.getText().clear();
                etWeight.getText().clear();
                tvLastCalcDate.setText("Last Calculated Date: ");
                tvLastCalcBMI.setText("Last Calculated BMI: ");
                tvLastComment.setText("");
                save("", "", "");
            }
        });

    }
}
// Checkpoint