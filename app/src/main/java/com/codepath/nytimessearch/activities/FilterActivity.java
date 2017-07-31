package com.codepath.nytimessearch.activities;

import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.content.SharedPreferences;

import android.os.Build;
import android.preference.PreferenceManager;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;

import com.codepath.nytimessearch.R;
import com.codepath.nytimessearch.fragments.DatePickerFragment;

import android.support.v4.app.DialogFragment;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;


public class FilterActivity extends AppCompatActivity implements OnDateSetListener {

    EditText etDate;
    Spinner spinner;
    CheckBox cbArts;
    CheckBox cbFashionStyle;
    CheckBox cbSports;
    Button btnSave;
    String value, arts, fashionStyle, sports;
    SharedPreferences.Editor editor;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        setContentView(R.layout.activity_filter);

        //Storing and Accessing SharedPreferences
        SharedPreferences mSettings = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        //create an Editor instance of SharedPreferences
         editor = mSettings.edit();

        setupView();


    }

    public void setupView(){

        etDate = (EditText) findViewById(R.id.etDate);
        spinner = (Spinner) findViewById(R.id.spinner);
        cbArts = (CheckBox) findViewById(R.id.cbArts);
        cbFashionStyle = (CheckBox) findViewById(R.id.cbFashionStyle);
        cbSports = (CheckBox) findViewById(R.id.cbSports);
        cbArts.setOnCheckedChangeListener(checkedChangeListener);
        cbFashionStyle.setOnCheckedChangeListener(checkedChangeListener);
        cbSports.setOnCheckedChangeListener(checkedChangeListener);
        btnSave = (Button) findViewById(R.id.btnSave);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editor.putString("date", etDate.getText().toString());
                editor.putString("sort_order",value);
                editor.putString("news_desk", arts+ "/" +fashionStyle+ "/"+ sports);
                // the edits need to be apply by calling 'apply()'
                editor.apply();

                Toast.makeText(getApplicationContext()," "+etDate.getText().toString()+" "+value+" "+arts+" "+fashionStyle+" "+sports,Toast.LENGTH_LONG).show();

                finish();
            }
        });

        //  The declaration of the spinner to use the R.layout.spinner_item:
        //// Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.sort_order_array, R.layout.spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // An item was selected. You can retrieve the selected item using
                value = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });




    }


    // Defines a listener for every time a checkbox is checked or unchecked
    CompoundButton.OnCheckedChangeListener checkedChangeListener = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

            // compoundButton is the checkbox
            // boolean is whether or not checkbox is checked
            // Check which checkbox was clicked
            switch (buttonView.getId()){

                case R.id.cbArts:
                    if(cbArts.isChecked()){
                        arts = "Arts";
                    }
                    else {
                        arts = "";
                    }
                    break;

                case R.id.cbFashionStyle:
                    if(cbFashionStyle.isChecked()){
                        fashionStyle = "Fashion Style";
                    }
                    else {
                        fashionStyle = "";
                    }
                    break;

                case R.id.cbSports:
                    if(cbSports.isChecked()){
                       sports = "Sports";
                    }
                    else {
                        sports = "";
                    }
                    break;

            }

        }
    };



    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        // store the values selected into a Calendar instance
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, month);
        c.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd");
        etDate.setText(simpleDateFormat.format(c.getTime()));



    }

    public void onArticleSearch(View view) {
        DatePickerFragment newFragment = new DatePickerFragment();
        newFragment.show(getSupportFragmentManager(),"datePicker");

    }
}
