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
import java.util.Collections;
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
    int defaultposition = -1;


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
                            displayAlert(MainActivity.this, "Please add a category for the new note!");
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

    public String getcurrentdate() {
        Date c = Calendar.getInstance().getTime();
        System.out.println("Current time => " + c);
        SimpleDateFormat df = new SimpleDateFormat("dd MMM yyyy");
        String formattedDate = df.format(c);
        System.out.println("  " + formattedDate);
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

    public void attachswipelistener() {
        navigationAdapter = new NavigationAdapter(MainActivity.this, categoryarray, user);
        layoutManager = new LinearLayoutManager(MainActivity.this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(navigationAdapter);

        ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT | ItemTouchHelper.LEFT | ItemTouchHelper.DOWN | ItemTouchHelper.UP) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                Toast.makeText(MainActivity.this, "on Move", Toast.LENGTH_SHORT).show();
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDir) {
                Toast.makeText(MainActivity.this, "on Swiped ", Toast.LENGTH_SHORT).show();
                //Remove swiped item from list and notify the RecyclerView
                int position = viewHolder.getAdapterPosition();
                // arrayList.remove(position);
                // Toast.makeText(MainActivity.this, "on Move", Toast.LENGTH_SHORT).show();
                // navigationAdapter.notifyDataSetChanged();
                deletenote(user.get(viewHolder.getAdapterPosition()), true);
                navigationAdapter.notifyDataSetChanged();
            }
        };

        itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);
    }

    private void deletenote(final UserData userData, final boolean b) {
        dialog.setContentView(R.layout.delete_note_layout);
        Button deldata = dialog.findViewById(R.id.but_deldata);
        Button movedata = dialog.findViewById(R.id.but_move_data);
        TextView txtalert = dialog.findViewById(R.id.text_dataalert);
        Button cancel = dialog.findViewById(R.id.but_canceldata);

        if (b) {
            txtalert.setText("are you sure to delete category and its notes?");
        } else {
            txtalert.setText("are you sure to delete this notes?");
        }

        deldata.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppExecutors.getInstance().diskIO().execute(new Runnable() {
                    @Override
                    public void run() {
                        if (b) {
                            for (int i = 0; i < user.size(); i++) {
                                String cat = userData.getCategory();
                                if (cat.equals(user.get(i).getCategory())) {
                                    mDb.noteDao().delete(user.get(i));
                                }
                            }
                        } else {
                            mDb.noteDao().delete(userData);
                        }
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                // homeAdapter.notifyDataSetChanged();
                                dialog.cancel();
                                retrieveTasks();
                            }
                        });
                    }
                });
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.cancel();
            }
        });
        movedata.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
                //optiondialog(userData);
            }
        });
        dialog.show();
    }

    private void filter(String text) {
        //new array list that will hold the filtered data
        ArrayList<UserData> filterdata = new ArrayList<>();

        //looping through existing elements
        for (int i = 0; i < notesarray.size(); i++) {
            //if the existing elements contains the search input
            if (notesarray.get(i).getSubtitle().toLowerCase().contains(text.toLowerCase()) || notesarray.get(i).getTitle().toLowerCase().contains(text.toLowerCase())) {
                //adding the element to filtered list
                filterdata.add(notesarray.get(i));
            }
        }
        //calling a method of the adapter class and passing the filtered list
        //homeAdapter = new HomeAdapter(MainActivity.this, cattitle, filterdata);
        myRecyclerView.setAdapter(homeAdapter);
    }
    private void sort() {
        //new array list that will hold the filtered data
        ArrayList<UserData> filterdata = new ArrayList<>();
        ArrayList<String> textarray = new ArrayList<>();

        for (int i = 0; i < notesarray.size(); i++) {
            //if the existing elements contains the search input
            textarray.add(notesarray.get(i).getTitle());
        }

        if(sortting)
        {
            Collections.sort(textarray, Collections.reverseOrder());
            sortting=false;
        }
        else
        {
            Collections.sort(textarray);
            sortting=true;
        }
        System.out.println("++++++++"+textarray);

        for (int y = 0; y < textarray.size(); y++) {

            for (int z = 0; z < notesarray.size(); z++) {

                if (textarray.get(y)== notesarray.get(z).getTitle()) {

                    filterdata.add(notesarray.get(z));
                }
            }
        }
        //calling a method of the adapter class and passing the filtered list
        homeAdapter = new HomeAdapter(MainActivity.this, cattitle, filterdata);
        myRecyclerView.setAdapter(homeAdapter);
    }
}