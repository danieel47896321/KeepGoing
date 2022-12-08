package com.example.keepgoing.User;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.keepgoing.Class.User;
import com.example.keepgoing.MainActivity;
import com.example.keepgoing.R;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Calendar;

public class Home extends AppCompatActivity {
    private TextView Title;
    private ImageView BackIcon;
    private TabLayout tabLayout;
    private ViewPager2 viewPager2;
    private String[] titles;
    private final int SIZE = 7;
    private Intent intent;
    private User user = new User();
    private PagerAdapter pagerAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        init();
    }
    private void init(){
        setID();
        setPager();
        SignOutIcon();
        setCurrentDay();
    }
    private void setID(){
        intent = getIntent();
        user = (User)intent.getSerializableExtra("user");
        BackIcon = findViewById(R.id.BackIcon);
        BackIcon.setImageResource(R.drawable.signout);
        Title = findViewById(R.id.Title);
        Title.setText(R.string.Home);
        tabLayout = findViewById(R.id.tabLayout);
        viewPager2 = findViewById(R.id.viewPager2);
        titles = new String[SIZE];
        titles = getResources().getStringArray(R.array.Home);
    }
    private void setPager(){
        pagerAdapter = new PagerAdapter(Home.this);
        viewPager2.setAdapter(pagerAdapter);
        new TabLayoutMediator(tabLayout,viewPager2,((tab, position) -> tab.setText(titles[position]))).attach();
    }
    public void setCurrentDay(){
        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_WEEK);
        TabLayout.Tab tab = tabLayout.getTabAt(0);
        switch (day) {
            case Calendar.SUNDAY:
                tab = tabLayout.getTabAt(0);
                break;
            case Calendar.MONDAY:
                tab = tabLayout.getTabAt(1);
                break;
            case Calendar.TUESDAY:
                tab = tabLayout.getTabAt(2);
                break;
            case Calendar.WEDNESDAY:
                tab = tabLayout.getTabAt(3);
                break;
            case Calendar.THURSDAY:
                tab = tabLayout.getTabAt(4);
                break;
            case Calendar.FRIDAY:
                tab = tabLayout.getTabAt(5);
                break;
            case Calendar.SATURDAY:
                tab = tabLayout.getTabAt(6);
                break;
        }
        tab.select();
    }
    public static class PagerAdapter extends FragmentStateAdapter {
        private final int SIZE = 7;
        public PagerAdapter(@NonNull FragmentActivity fragmentActivity) {
            super(fragmentActivity);
        }
        @NonNull
        @Override
        public Fragment createFragment(int position) {
            WorkoutFragment workoutFragment = new WorkoutFragment();
            switch (position){
                case 0:
                    workoutFragment.setDay("SUNDAY");
                    return workoutFragment;
                case 1:
                    workoutFragment.setDay("MONDAY");
                    return workoutFragment;
                case 2:
                    workoutFragment.setDay("TUESDAY");
                    return workoutFragment;
                case 3:
                    workoutFragment.setDay("WEDNESDAY");
                    return workoutFragment;
                case 4:
                    workoutFragment.setDay("THURSDAY");
                    return workoutFragment;
                case 5:
                    workoutFragment.setDay("FRIDAY");
                    return workoutFragment;
                case 6:
                    workoutFragment.setDay("SATURDAY");
                    return workoutFragment;
            }
            workoutFragment.setDay("SUNDAY");
            return workoutFragment;
        }
        @Override
        public int getItemCount() { return SIZE; }
    }
    private void SignOutIcon(){
        BackIcon.setOnClickListener( v -> onBackPressed() );
    }
    public void onBackPressed(){
        AlertDialog.Builder builder = new AlertDialog.Builder(Home.this);
        builder.setTitle(getResources().getString(R.string.SignOut)).setMessage(getResources().getString(R.string.AreYouSure)).setCancelable(true)
                .setPositiveButton(getResources().getString(R.string.Yes), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
                        if(firebaseAuth.getCurrentUser() != null)
                            firebaseAuth.signOut();
                        startActivity(new Intent(Home.this, MainActivity.class));
                        finish();
                    }
                }).setNegativeButton(getResources().getString(R.string.No), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) { }
                }).show();
    }
}