package com.example.adhitya_1202150103_modul6;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.ogaclejapan.smarttablayout.SmartTabLayout;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItem;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItems;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentStatePagerItemAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    //Dekalarasi View
    @BindView(R.id.btnAdd) //@BindView declare sekaligus inisialisasi view dengan menggunakan library ButterKnife
    FloatingActionButton btnAdd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);  //Binding ButterKnife pada activity
        setTitle("List Photo");

        //Mengatur tab dan fragment pada tab menggunakan fragmentstatepageritemadapter dari library SmartTabLayout
        FragmentStatePagerItemAdapter adapter = new FragmentStatePagerItemAdapter(
                getSupportFragmentManager(), FragmentPagerItems.with(this)
                .add(FragmentPagerItem.of("New", FotoFragment.class, FotoFragment.arguments("new")))
                .add(FragmentPagerItem.of("My Photo", FotoFragment.class, FotoFragment.arguments("myphoto")))
                .create());

        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        SmartTabLayout viewPagerTab = (SmartTabLayout) findViewById(R.id.viewpagertab);

        if (Constant.currentUser == null) { //jika belum login
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
            finish();
        } else { //jika sudah login
            viewPager.setAdapter(adapter); //masukkan fragment pada adapter viewpager
            viewPagerTab.setViewPager(viewPager); //mengatur tab pada viewpager
        }
    }

    //method untuk handling tombol add
    @OnClick(R.id.btnAdd)
    public void add() {
        startActivity(new Intent(MainActivity.this, ActivityTambahFoto.class)); // panggil add photo activity
    }

    //method untuk implement menu pada activity
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu); // inflate atau memasukkan menu
        return super.onCreateOptionsMenu(menu);
    }

    //method untuk handling menu yang di klik dari daftar di menu yang di implement
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.mnLogout:
                Constant.mAuth.signOut(); //logout firebase
                Constant.currentUser = null; //set global variable user null
                startActivity(new Intent(MainActivity.this, LoginActivity.class)); //panggil login activity
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
