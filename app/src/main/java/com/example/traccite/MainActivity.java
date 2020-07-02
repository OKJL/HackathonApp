package com.example.traccite;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

public class MainActivity extends AppCompatActivity {
    EditText Name, Phone;
    Button Comfirm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Adding items into Spinner(Combo Box)
        String[] arraySpinner = new String[]{
                "Resident", "Non-Resident"
        };
        final Spinner s = (Spinner) findViewById(R.id.spinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, arraySpinner);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        s.setAdapter(adapter);

        Name = findViewById(R.id.edittxtName);
        Phone = findViewById(R.id.editTxtPhone);
        Comfirm = findViewById(R.id.btnComfirm);

        // Will run the commands when "Comfirm" Button is pressed
        Comfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // get Resident/Non-Resident- can put not selected as default
                s.getCount();
                // get name
                Name.getText();
                // get number- check if number is 8 digits long
                Phone.getText();
                if(Phone.length() == 8){
                    Phone.getText();
                }else{
                    Phone.setText("");
                    Phone.setHint("Invalid Number!");
                }

            }
        });
    }
}