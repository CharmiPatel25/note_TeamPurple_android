package com.example.note_teampurple_android.Activities;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
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
import com.example.note_teampurple_android.Adapter.NavigationAdapter;
import com.example.note_teampurple_android.R;
import com.example.note_teampurple_android.database.AppDatabase;
import com.example.note_teampurple_android.models.UserData;
import com.google.android.material.navigation.NavigationView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.github.yavski.fabspeeddial.FabSpeedDial;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    HomeAdapter homeAdapter;
    LinearLayoutManager layoutManager;
    RecyclerView recyclerView;
    DrawerLayout drawer;
    NavigationView navigationView;
    NavigationAdapter navigationAdapter;
    Button addcategory;
    Dialog dialog;
    private AppDatabase mDb;
    List<UserData> user;
    EditText categorytext;
    UserData userData;
    private Boolean exit = false;
    public boolean sortting = false;
    RecyclerView rec_move;
    Button but_move;

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
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.but_add_cat:
                showdialog();
                break;

            case R.id.img_sort:



                break;
        }
    }

    public void showdialog() {

        dialog.setContentView(R.layout.addcategory_layout);


        categorytext = dialog.findViewById(R.id.edt_cat);
        Button cancel = dialog.findViewById(R.id.but_cancel);
        Button add = dialog.findViewById(R.id.but_add_cat);

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                MainActivity.this.getWindow().setSoftInputMode(
                        WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
                dialog.dismiss();
            }
        });

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.this.getWindow().setSoftInputMode(
                        WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
                dialog.dismiss();

            }
        });

        dialog.show();
    }
}