package com.example.keepgoing;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.keepgoing.Guest.CreateAccount;
import com.example.keepgoing.Guest.SignIn;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class MainActivity extends AppCompatActivity {
    private TextView Title;
    private ImageView BackIcon;
    private TabLayout tabLayout;
    private ViewPager2 viewPager2;
    private String[] titles;
    private final int SIZE = 2;
    private PagerAdapter pagerAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
    }
    public void init(){
        setID();
        setPager();
    }
    private void setID(){
        Title = findViewById(R.id.Title);
        Title.setText("");
        BackIcon = findViewById(R.id.BackIcon);
        BackIcon.setVisibility(View.GONE);
        tabLayout = findViewById(R.id.tabLayout);
        viewPager2 = findViewById(R.id.viewPager2);
        titles = new String[SIZE];
        titles[0] = getResources().getString(R.string.SignIn);
        titles[1] = getResources().getString(R.string.CreateAccount);
    }
    private void setPager(){
        pagerAdapter = new PagerAdapter(MainActivity.this);
        viewPager2.setAdapter(pagerAdapter);
        new TabLayoutMediator(tabLayout,viewPager2, ((tab, position) -> tab.setText(titles[position]))).attach();
    }
    public static class PagerAdapter extends FragmentStateAdapter{
        private final int SIZE = 2;
        public PagerAdapter(@NonNull FragmentActivity fragmentActivity) {
            super(fragmentActivity);
        }
        @NonNull
        @Override
        public Fragment createFragment(int position) {
            switch (position){
                case 0:
                    return new SignIn();
                case 1:
                    return new CreateAccount();
            }
            return new SignIn();
        }
        @Override
        public int getItemCount() { return SIZE; }
    }
}