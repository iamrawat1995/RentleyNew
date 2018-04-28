package com.example1.ankitrawat.rentley;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import de.hdodenhof.circleimageview.CircleImageView;

public class Home extends AppCompatActivity {

    private NavigationView navigationView;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private RecyclerView postList;
    private Toolbar mToolbar;
    private CircleImageView NavProfileImage;
    private TextView NavProfileName;
    private FirebaseAuth mAuth;
    private DatabaseReference databaseRef;
    String currentUserID;
    private FragmentTransaction fragmentTransaction;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_home);

        mAuth = FirebaseAuth.getInstance();
        currentUserID = mAuth.getCurrentUser().getUid();
        databaseRef = FirebaseDatabase.getInstance().getReference().child("Users");

        mToolbar =(Toolbar)findViewById(R.id.main_page_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Home");

        drawerLayout = (DrawerLayout) findViewById(R.id.drawable_layout1);
        actionBarDrawerToggle = new ActionBarDrawerToggle(Home.this,drawerLayout,R.string.drawer_open,R.string.drawer_close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.main_container,new HomeFragment());
        fragmentTransaction.commit();


        navigationView=(NavigationView)findViewById(R.id.navigation_view1);

        postList = (RecyclerView) findViewById(R.id.all_user_post_list);

        View navView = navigationView.inflateHeaderView(R.layout.navigation_header);
        NavProfileImage = (CircleImageView) navView.findViewById(R.id.nav_profile_image);
        NavProfileName = (TextView) navView.findViewById(R.id.nav_user_fullname);
        databaseRef.child(currentUserID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                if(dataSnapshot.exists()) {
                    if (dataSnapshot.hasChild("FullName"))
                    {
                        String fullname = dataSnapshot.child("FullName").getValue().toString();
                        NavProfileName.setText(fullname);
                    }
                    if(dataSnapshot.hasChild("profile_image"))
                    {
                        String image = dataSnapshot.child("profile_image").getValue().toString();
                        Picasso.get().load(image).placeholder(R.drawable.profile).into(NavProfileImage);
                    }
                    else 
                    {
                        Toast.makeText(Home.this, "Profile name does not exists", Toast.LENGTH_SHORT).show();
                    }
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item)
            {
                UserMenuSelector(item);
                return false;
            }
        });
    }

    @Override
    protected void onStart()
    {
        super.onStart();

        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser==null)
        {
            sendUserToLoginActivity();
        }
        else
        {
            CheckUserExistence();

        }
    }

    private void CheckUserExistence()
    {
        final String current_user_id = mAuth.getCurrentUser().getUid();
        databaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                if(! dataSnapshot.hasChild(current_user_id))
                {
                    SendUserToSetupActivity();

                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError)
            {

            }
        });
    }

    private void SendUserToSetupActivity()
    {
        Intent setupIntent = new Intent(Home.this,SetupActivity.class);
        setupIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(setupIntent);
        finish();

    }

    private void sendUserToLoginActivity()
    {
        Intent loginIntent = new Intent(Home.this,Login.class);
        loginIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(loginIntent);
        finish();



    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        if (actionBarDrawerToggle.onOptionsItemSelected(item))
        {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void UserMenuSelector(MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.nav_home:
                //Toast.makeText(this, "Home", Toast.LENGTH_SHORT).show();
                fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.main_container,new HomeFragment());
                fragmentTransaction.commit();
                getSupportActionBar().setTitle("Home");
                item.setChecked(true);
                drawerLayout.closeDrawers();
                break;
            case R.id.nav_account:
                //Toast.makeText(this, "Profile", Toast.LENGTH_SHORT).show();
                fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.main_container,new ProfileActivity());
                fragmentTransaction.commit();
                getSupportActionBar().setTitle("My Account");
                item.setChecked(true);
                drawerLayout.closeDrawers();
                break;
            case R.id.nav_details:
                //Toast.makeText(this, "Details", Toast.LENGTH_SHORT).show();
                fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.main_container,new DetailsFragment());
                fragmentTransaction.commit();
                getSupportActionBar().setTitle("Tenants Details");
                item.setChecked(true);
                drawerLayout.closeDrawers();
                break;
            case  R.id.nav_reports:
                //Toast.makeText(this, "Reports", Toast.LENGTH_SHORT).show();
                fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.main_container,new ReportsFragment());
                fragmentTransaction.commit();
                getSupportActionBar().setTitle("Reports");
                item.setChecked(true);
                drawerLayout.closeDrawers();
                break;
            case R.id.nav_logout:

                mAuth.signOut();
                sendUserToLoginActivity();
                break;

        }

    }

}