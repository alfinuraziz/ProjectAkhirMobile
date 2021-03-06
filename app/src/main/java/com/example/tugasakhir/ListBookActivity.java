package com.example.tugasakhir;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.tugasakhir.adapters.CustomCursorAdapter;
import com.example.tugasakhir.adapters.DBHelper2;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;

public class ListBookActivity extends AppCompatActivity implements AdapterView.OnItemClickListener{

    ListView Is;
    DBHelper2 dbHelper2;
    int listData;
    SharedPreferences viewData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_book);
        this.setTitle("Book Store");
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ListBookActivity.this, com.example.tugasakhir.AddActivity.class));
            }
        });

        dbHelper2 = new DBHelper2(this);
        Is = (ListView)findViewById(R.id.list_buku);
        Is.setOnItemClickListener(this);

        viewData = getSharedPreferences("currentListView", 0);
        listData = viewData.getInt("currentListView", 0);

        allData();
    }

    public void allData(){
        Cursor cursor = dbHelper2.allData();
        CustomCursorAdapter customCursorAdapter = new CustomCursorAdapter(this, cursor, 1);
        Is.setAdapter(customCursorAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.logout){
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(ListBookActivity.this, LoginActivity.class));
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int i, long I) {
            TextView getID = (TextView)view.findViewById(R.id.listID);
            final long id = Long.parseLong(getID.getText().toString());
            Cursor cur = dbHelper2.oneData(id);
            cur.moveToFirst();

            Intent idBuku = new Intent(ListBookActivity.this, com.example.tugasakhir.AddActivity.class);
            idBuku.putExtra(DBHelper2.row_id,id);
            startActivity(idBuku);
    }

    @Override
    protected void onResume() {
        super.onResume();
        allData();
    }
}