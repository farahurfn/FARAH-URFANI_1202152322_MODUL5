package com.example.android.farah_1202152322_modul5;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    //deklarasi variabel yang akan digunakan
    private Database dtBase;
    private RecyclerView rcView;
    private Adapter adapter;
    private ArrayList<AddData> data_list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //set title menjadi To Do List
        setTitle("To Do List App");

        //refer semua variabel yang ada
        //akses recyclerview
        rcView = findViewById(R.id.rec_view);
        //buat araylist baru
        data_list = new ArrayList<>();
        //buat database baru
        dtBase = new Database(this);
        //manggil method readdata
        dtBase.readdata(data_list);

        //inisialisasi shared preference
        SharedPreferences sharedP = this.getApplicationContext().getSharedPreferences("Preferences", 0);
        int color = sharedP.getInt("Colourground", R.color.white);

        //buat adapter baru
        adapter = new Adapter(this, data_list,color);
        //mengatur perubahan ukuran
        rcView.setHasFixedSize(true);
        rcView.setLayoutManager(new LinearLayoutManager(this));
        //inisiasi adapter untuk recycler view
        rcView.setAdapter(adapter);

        //menjalankan method hapus data pada list to do
        hapusgeser();
    }

    //buat method untuk menghapus item pada to do list
    public void hapusgeser(){
        //buat touch helper baru untuk recycler view
        ItemTouchHelper.SimpleCallback touchcall = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();
                AddData current = adapter.getData(position);
                //apabila item di swipe ke arah kiri
                if(direction==ItemTouchHelper.LEFT){
                    //remove item yang dipilih dengan mengenali todonya sebagai primary key
                    if(dtBase.removedata(current.getTodo())){
                        //menghapus data
                        adapter.deleteData(position);
                        //membuat snack bar dan pemberitahuan bahwa item sudah terhapus dengan durasi 2 sekon
                        Snackbar.make(findViewById(R.id.coordinator), "List Telah Terhapus", 2000).show();
                    }
                }
            }
        };
        //menentukan item touch helper untuk recycler view
        ItemTouchHelper touchhelp = new ItemTouchHelper(touchcall);
        touchhelp.attachToRecyclerView(rcView);
    }

    //ketika menu pada activity di buat
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    //method yang dijalankan ketika item di pilih
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //mendapatkan id dari item yang
        int id = item.getItemId();
        //apabila item yang dipilih adalah setting
        if (id==R.id.action_settings){
            //membuat intent ke menu Settings
            Intent intent = new Intent(MainActivity.this, Settings.class);
            //memulai intent
            startActivity(intent);
            //menutup aktivitas setelah intent dijalankan
            finish();
        }
        return true;
    }

    //method yang akan dijalankan ketika tombol add di klik
    public void addlist(View view) {
        //intent ke class add to do
        Intent intent = new Intent(MainActivity.this, AddToDo.class);
        //memulai intent
        startActivity(intent);
    }
}