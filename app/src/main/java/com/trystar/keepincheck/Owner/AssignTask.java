package com.trystar.keepincheck.Owner;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.widget.Toast;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.FirebaseFirestore;
import com.trystar.keepincheck.Owner.Adapter.ToDoAdapter;
import com.trystar.keepincheck.Owner.Model.ToDoModel;
import com.trystar.keepincheck.R;
import com.trystar.keepincheck.SelectIdentity;
import com.trystar.keepincheck.mapfiles.MapsActivity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class AssignTask extends AppCompatActivity {

    private FirebaseFirestore firestore;
    private ToDoAdapter adapter;
    private List<ToDoModel> mList;
    DrawerLayout mDrawerLayout;
    ActionBarDrawerToggle mToggle;
    Toolbar mToolbar;
    NavigationView navigationView;
    Menu menu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assign_task);


        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        FloatingActionButton wFab = findViewById(R.id.assigntask);
        firestore = FirebaseFirestore.getInstance();

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(AssignTask.this));

        wFab.setOnClickListener(view -> new AddNewTask().show(getSupportFragmentManager(), AddNewTask.TAG));

        mList = new ArrayList<>();
        adapter = new ToDoAdapter(AssignTask.this, mList);

        recyclerView.setAdapter(adapter);
        showData();

        // Navigation view header
        navigationView = findViewById(R.id.navigation);
        //toolbar=findViewById(R.id.mToolbar);

       mToolbar = findViewById(R.id.nav_action);

        navigationView.bringToFront();
        mDrawerLayout = findViewById(R.id.drawerLayout);
        mToggle = new ActionBarDrawerToggle(this, mDrawerLayout, mToolbar, R.string.open, R.string.close);

        mDrawerLayout.addDrawerListener(mToggle);
        mToggle.syncState();

        setUpNavigationView();

        menu = navigationView.getMenu();
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //end od slider menu

    }

    @Override
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    private void setUpNavigationView() {
        //Setting Navigation View Item Selected Listener to handle the item click of the navigation menu
        navigationView.setNavigationItemSelectedListener(menuItem -> {
            switch (menuItem.getItemId()) {
                case R.id.ownerprofile:
                    //Toast.makeText(getApplicationContext(),"View Profile",Toast.LENGTH_LONG).show();
                    startActivity(new Intent(AssignTask.this,ViewProfile.class));
                    return true;
                case R.id.item2:
                        startActivity(new Intent(AssignTask.this,WorkerList.class));
                    return true;
                case R.id.item3:
                    return true;
                case R.id.item4:
                    startActivity(new Intent(AssignTask.this, MapsActivity.class));
                    Toast.makeText(getApplicationContext(),"Location",Toast.LENGTH_SHORT).show();
                    return true;
                case R.id.item5:
                    FirebaseAuth.getInstance().signOut();
                    startActivity(new Intent(AssignTask.this, SelectIdentity.class));
                    Toast.makeText(AssignTask.this, "Signing Out", Toast.LENGTH_SHORT).show();
                    return true;
            }
            mDrawerLayout.closeDrawer(GravityCompat.START);
            return true;
        });
    }

    private void showData(){
        firestore.collection("task").addSnapshotListener((value, error) -> {
            for (DocumentChange documentChange: value.getDocumentChanges()){
                if (documentChange.getType() == DocumentChange.Type.ADDED){
                    String id = documentChange.getDocument().getId();
                    ToDoModel toDoModel = documentChange.getDocument().toObject(ToDoModel.class).withId(id);

                    mList.add(toDoModel);
                    adapter.notifyDataSetChanged();
                }
            }
            Collections.reverse(mList);
        });
    }
}
