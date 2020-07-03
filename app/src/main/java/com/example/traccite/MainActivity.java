package com.example.traccite;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;


public class MainActivity extends AppCompatActivity {
    EditText Name, Phone;
    Button Comfirm;
    int REQUEST_ENABLE_BT = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Get permissions
        BluetoothAdapter BluetoothAdapter = android.bluetooth.BluetoothAdapter.getDefaultAdapter();
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        if(BluetoothAdapter == null){
            // Phone does not support bluetooth
            alertDialogBuilder.setTitle("Device not supported!");
            alertDialogBuilder.setMessage("Your Phone does not support bluetooth!");
            alertDialogBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    // exit the app
                    finish();
                    System.exit(0);
                }
            });
            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();
        }else {
            // need to find code to see if bluetooth is enabled all the time
            if (!BluetoothAdapter.isEnabled()) {
                Intent itEnableBluetooth = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(itEnableBluetooth, REQUEST_ENABLE_BT);
            }

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
                        if (Phone.length() == 8 && Name.length() != 0) {
                            // get Resident/Non-Resident- can put not selected as default
                            s.getCount();
                            // get name
                            Name.getText();
                            // get number- check if number is 8 digits long
                            Phone.getText();
                        } else if (Phone.length() != 8 && Name.length() != 0) {
                            Phone.setText("");
                            Phone.setHint("Invalid Number!");

                        } else if (Name.length() == 0 && Name.length() == 8) {
                            Name.setText("");
                            Phone.setHint("Invalid Name!");
                        }else if(Phone.length() != 8 && Name.length() == 0){
                            Name.setText("");
                            Name.setHint("Field Required!");
                            Phone.setText("");
                            Phone.setHint("Invalid Number!");
                        }

                    }
                });
        }
    }
}
