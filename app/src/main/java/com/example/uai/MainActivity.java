package com.example.uai;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.ArrayList;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
            private static final String TAG = "MainActivity"; //buat date di datepicker
            private LinearLayout tambah;
            Spinner spAgama,spjaket,sppilihan1,sppilihan2,spjalur,sptahun;
            private EditText tanggallahir;
            private DatePickerDialog.OnDateSetListener mDateSetListener;
            private CheckBox checkbox;
            NavigationView navigationView;
            private String URLstring="https://studentdesk.uai.ac.id/rest/index.php/api/camaru/getProdi/format/json"; //API
            private static ProgressBar mprogressBar; //API
            private ArrayList<GoodModel> goodModelArrayList; //API
            private ArrayList<String> names = new ArrayList<String>();//API


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        spAgama = (Spinner) findViewById(R.id.spinneragama);
        spAgama = (Spinner) findViewById(R.id.spinneragama);
        spjaket = (Spinner) findViewById(R.id.spinnerjaket);
        sppilihan1 = (Spinner) findViewById(R.id.spinerpilihan1);
        sppilihan2 = (Spinner) findViewById(R.id.spinerpilihan2);
        spjalur = (Spinner) findViewById(R.id.spinnerjalurmasuk);
        sptahun = (Spinner) findViewById(R.id.spinnertahun);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Pendaftaran Test On Site");
        retrieveJSON();

        //spinner android
        ArrayAdapter<String> myAdapter = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.agama)); //ambil dari string.xml

        myAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spAgama.setAdapter(myAdapter);

        ArrayAdapter<String> Adapterjaket = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.jaket));//ambil dari string.xml

        Adapterjaket.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spjaket.setAdapter(Adapterjaket);

        ArrayAdapter<String> Adapterpilihan1 = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.pilihan)); //ambil dari string.xml

        Adapterjaket.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sppilihan1.setAdapter(Adapterpilihan1);

        ArrayAdapter<String> Adapterpilihan2 = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.pilihan));//ambil dari string.xml

        Adapterjaket.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sppilihan2.setAdapter(Adapterpilihan2);

        ArrayAdapter<String> Adapterjalurmasuk = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.masuk));//ambil dari string.xml
        Adapterjalurmasuk.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spjalur.setAdapter(Adapterjalurmasuk);

        ArrayAdapter<String> Adaptertahun = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.tahun));//ambil dari string.xml

        Adaptertahun.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sptahun.setAdapter(Adaptertahun);

        tanggallahir = (EditText) findViewById(R.id.etxtanggallahir);

        tanggallahir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog( MainActivity.this, android.R.style.Theme_Material_Dialog_MinWidth, mDateSetListener, year, month, day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });

        mDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                month = month + 1;
                Log.d(TAG, "onDateSet: mm/dd/yyy"  + month + "/" + day + "/" + year);

                String date = month + "/" + day + "/" + year;
                tanggallahir.setText(date);
            }
        };

        checkbox = (CheckBox) findViewById(R.id.cbcek);
        tambah = (LinearLayout) findViewById(R.id.layouttambahsekolah);

        checkbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkbox.isChecked()) {
                    tambah.setVisibility(View.VISIBLE);
                } else if (!checkbox.isChecked()) {
                    tambah.setVisibility(View.GONE);
                }
            }
        });

        //default dari drawer layout
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

    }

    private void retrieveJSON(){

    }//API

    @Override//default dari drawer layout
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override //default dari drawer layout
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //default dari drawer layout
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    //default dari drawer layout
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.halamanutama) {
            Toast.makeText(getApplicationContext(),"halaman utama",Toast.LENGTH_LONG).show();
        } else if (id == R.id.formulirpendaftaran) {
            Toast.makeText(getApplicationContext(),"formulir pendaftara",Toast.LENGTH_LONG).show();
        } else if (id == R.id.biayaperkuliahan) {
            Toast.makeText(getApplicationContext(),"biaya perkuliahan",Toast.LENGTH_LONG).show();
        } else if (id == R.id.potonganbiaya) {
            Toast.makeText(getApplicationContext(),"potongan biaya",Toast.LENGTH_LONG).show();
        } else if (id == R.id.jalurmasuk) {
            Toast.makeText(getApplicationContext(),"jalur masuk",Toast.LENGTH_LONG).show();
        } else if (id == R.id.faq) {
            Toast.makeText(getApplicationContext(),"FAQ",Toast.LENGTH_LONG).show();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
