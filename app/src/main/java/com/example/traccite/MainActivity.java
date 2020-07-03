package com.example.traccite;

import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;


public class MainActivity extends AppCompatActivity {
  EditText Name, Phone;
  Button Confirm;
  int REQUEST_ENABLE_BT = 0;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    Intent intent = new Intent().setClass(
      MainActivity.this,
      AuthActivity.class
    );

    startActivity(intent);
    finish();

    // Get permissions
    BluetoothAdapter BluetoothAdapter = android.bluetooth.BluetoothAdapter.getDefaultAdapter();
    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
    if (BluetoothAdapter == null) {
      // Phone does not support bluetooth
      alertDialogBuilder.setMessage("Your Phone does not support bluetooth!");
      AlertDialog alertDialog = alertDialogBuilder.create();
      alertDialog.show();
    } else {
      if (!BluetoothAdapter.isEnabled()) {
        Intent itEnableBluetooth = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
        startActivityForResult(itEnableBluetooth, REQUEST_ENABLE_BT);
      }
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
    Confirm = findViewById(R.id.btnComfirm);

    // Will run the commands when "Comfirm" Button is pressed
    Confirm.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        if (Phone.length() == 8 && Name.length() != 0) {
          // get Resident/Non-Resident- can put not selected as default
          s.getCount();
          // get name
          Name.getText();
          // get number- check if number is 8 digits long
          Phone.getText();
        } else {
          Phone.setText("");
          Phone.setHint("Invalid Number!");

        }

      }
    });
  }
}