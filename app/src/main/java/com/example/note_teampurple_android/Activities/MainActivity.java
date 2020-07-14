package com.example.note_teampurple_android.Activities;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.note_teampurple_android.Adapter.HomeAdapter;
import com.example.note_teampurple_android.R;
import com.google.android.material.navigation.NavigationView;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.github.yavski.fabspeeddial.FabSpeedDial;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    HomeAdapter homeAdapter;
    LinearLayoutManager layoutManager;
    RecyclerView recyclerView;

    @BindView(R.id.txt_hometitle)
    TextView txtHometitle;
    @BindView(R.id.edt_searchnote)
    EditText edtSearchnote;
    @BindView(R.id.img_sort)
    ImageView imgSort;
    @BindView(R.id.ll_emptynote)
    LinearLayout llEmptynote;
    @BindView(R.id.my_recycler_view)
    RecyclerView myRecyclerView;
    @BindView(R.id.fab_add)
    FabSpeedDial fabAdd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
    }

    @Override
    public void onClick(View view) {

    }
}