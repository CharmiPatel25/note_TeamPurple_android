package com.example.note_teampurple_android.Activities;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.note_teampurple_android.Adapter.HomeAdapter;
import com.example.note_teampurple_android.Adapter.NavigationAdapter;
import com.example.note_teampurple_android.R;
import com.example.note_teampurple_android.database.AppDatabase;
import com.example.note_teampurple_android.database.AppExecutors;
import com.example.note_teampurple_android.models.UserData;
import com.google.android.material.navigation.NavigationView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.github.yavski.fabspeeddial.FabSpeedDial;
import io.github.yavski.fabspeeddial.SimpleMenuListenerAdapter;
import io.opencensus.stats.ViewData;

import static com.example.note_teampurple_android.Adapter.NavigationAdapter.recposition;

public class MainActivity extends BaseActivity implements View.OnClickListener {

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

    static int cattitle = 0;
    String cattext = "";

    String movetext = "";

    ArrayList<UserData> notesarray;
    ArrayList<UserData> categoryarray;
    ArrayList<String> catcompare;
    ItemTouchHelper itemTouchHelper;
    int defaultposition=-1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        userData = new UserData();
        mDb = AppDatabase.getInstance(getApplicationContext());
        getcurrentdate();
        imgSort.setOnClickListener(this);
        edtSearchnote.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                filter(editable.toString());
            }
        });


        dialog = new Dialog(MainActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        MainActivity.this.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCancelable(false);


        notesarray = new ArrayList<UserData>();
        catcompare = new ArrayList<String>();
        categoryarray = new ArrayList<UserData>();
        FabSpeedDial fabSpeedDial = (FabSpeedDial) findViewById(R.id.fab_add);
        fabSpeedDial.setMenuListener(new SimpleMenuListenerAdapter() {
            @Override
            public boolean onMenuItemSelected(MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.faboptions_addnote:

                        boolean move = false;

                        try {
                            String cat = user.get(cattitle).getCategory();
                            move = true;
                        } catch (Exception e) {
                            move = false;
                        }

                        if (!move) {
                            displayAlert(MainActivity.this, "Please add a category for the new  note!");
                        } else {
                            intent = new Intent(MainActivity.this, WriteNoteActivity.class);
                            intent.putExtra("category", cattext);
                            intent.putExtra("categoryposition", cattitle);
                            startActivity(intent);
                        }
                        return true;


                    case R.id.faboptions_deletedata:
                        //Toast.makeText(MainActivity.this, "Clean Database", Toast.LENGTH_LONG).show();
                        //  deletedata();
                        break;

                    case R.id.faboptions_viewdatabase:
                        // Toast.makeText(MainActivity.this, "View Database", Toast.LENGTH_LONG).show();
                        intent = new Intent(MainActivity.this, ViewData.class);
                        startActivity(intent);
                        break;

                    case R.id.faboptions_setting:
                        Toast.makeText(MainActivity.this, "setting note", Toast.LENGTH_LONG).show();
                        break;
                }
                return false;
            }
        });

        retrieveTasks();

    }

    private void filter(String toString) {

    }

    private void displayAlert(MainActivity mainActivity, String s) {

    }

    private void retrieveTasks() {

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.but_add_cat:
                showdialog();
                break;

            case R.id.img_sort:
                sort();
                break;
        }
    }

    private void sort() {

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
                addcategory();
            }
        });

        dialog.show();
    }

    private void addcategory() {

    }

    public String getcurrentdate()
    {
        Date c = Calendar.getInstance().getTime();
        System.out.println("Current time => " + c);
        SimpleDateFormat df = new SimpleDateFormat("dd MMM yyyy");
        String formattedDate = df.format(c);
        System.out.println("  "+formattedDate);
        return formattedDate;
    }

    public void setreccolor(int position, String s) {
        drawer.closeDrawers();
        notesarray = new ArrayList<UserData>();
        cattitle = position;
        cattext = s;
        recposition(position);
        navigationAdapter.notifyDataSetChanged();
        txtHometitle.setText(cattext);

        for (int i = 0; i < user.size(); i++) {
            if (user.get(i).getCategory().equals(s)) {
                if (user.get(i).getTitle() == null || user.get(i).getTitle().isEmpty() || user.get(i).getTitle().equals("")) {

                } else {
                    notesarray.add(user.get(i));
                }
            }
        }

        if (notesarray.isEmpty()) {
            llEmptynote.setVisibility(View.VISIBLE);
            myRecyclerView.setVisibility(View.GONE);
        } else {
            llEmptynote.setVisibility(View.GONE);
            myRecyclerView.setVisibility(View.VISIBLE);
            homeAdapter = new HomeAdapter(MainActivity.this, cattitle, notesarray);
            layoutManager = new LinearLayoutManager(MainActivity.this);
            myRecyclerView.setLayoutManager(new GridLayoutManager(MainActivity.this, 2));
            myRecyclerView.setHasFixedSize(true);
            myRecyclerView.setItemAnimator(new DefaultItemAnimator());
            myRecyclerView.setAdapter(homeAdapter);
        }
    }
    @Override
    public void onBackPressed() {
        if (exit) {
            finishAffinity();
        } else {
            Toast.makeText(this, "Press Back to Exit.",
                    Toast.LENGTH_SHORT).show();
            exit = true;
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    exit = false;
                }
            }, 3 * 1000);
        }
    }

}