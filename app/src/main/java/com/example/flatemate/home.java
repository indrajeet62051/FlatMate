package com.example.flatemate;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.view.WindowInsetsControllerCompat;
import androidx.viewpager2.widget.ViewPager2;

import com.example.flatemate.Adapter.MyViewPagerAdapter;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class home extends AppCompatActivity {
    private TabLayout tabLayout;
    private ViewPager2 viewPager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_home);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        //for status bar
        View decorView = getWindow().getDecorView();
        WindowInsetsControllerCompat windowInsetsController = ViewCompat.getWindowInsetsController(decorView);
        if (windowInsetsController != null) {
            // Set the status bar text color based on the background (light for dark backgrounds, dark for light backgrounds)
            windowInsetsController.setAppearanceLightStatusBars(false); // Use light text color if the background is dark
        }
        // Optional: Set a custom status bar color
        getWindow().setStatusBarColor(getResources().getColor(R.color.lighblue));

        tabLayout = findViewById(R.id.tab_layout);
        viewPager = findViewById(R.id.view_pager);

        MyViewPagerAdapter adapter = new MyViewPagerAdapter(this);
        viewPager.setAdapter(adapter); // Ensure the adapter is set first

        // Tab titles and icons
        final String[] tabTitles = {"Home", "Images", "Maintenance","Profile"};
        final int[] tabIcons = {R.drawable.baseline_home_24, R.drawable.baseline_image_24, R.drawable.baseline_payment_24, R.drawable.baseline_manage_accounts_24};

        // Attach TabLayout with ViewPager2 using TabLayoutMediator
        new TabLayoutMediator(tabLayout, viewPager, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                // Custom view for each tab
                View tabView = LayoutInflater.from(home.this).inflate(R.layout.custom_tab, null);

                TextView tabText = tabView.findViewById(R.id.tab_text);
                ImageView tabIcon = tabView.findViewById(R.id.tab_icon);


                tabText.setText(tabTitles[position]);
                tabIcon.setImageResource(tabIcons[position]);

                tabIcon.setVisibility(View.GONE);

                tab.setCustomView(tabView);

                tab.view.setBackgroundResource(R.drawable.tab_background);

                tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
                    @Override
                    public void onTabSelected(TabLayout.Tab selectedTab) {
                        selectedTab.view.setBackgroundResource(R.drawable.tab_selected_background);  // Selected tab background
                        View selectedTabView = selectedTab.getCustomView();
                        if (selectedTabView != null) {
                            ImageView selectedTabIcon = selectedTabView.findViewById(R.id.tab_icon);
                            selectedTabIcon.setVisibility(View.VISIBLE);  // Make icon visible
                        }


                    }

                    @Override
                    public void onTabUnselected(TabLayout.Tab unselectedTab) {
                        unselectedTab.view.setBackgroundResource(R.drawable.tab_background);  // Unselected tab background
                        View unselectedTabView = unselectedTab.getCustomView();
                        if (unselectedTabView != null) {
                            ImageView unselectedTabIcon = unselectedTabView.findViewById(R.id.tab_icon);
                            unselectedTabIcon.setVisibility(View.GONE);  // Hide icon
                        }
                    }

                    @Override
                    public void onTabReselected(TabLayout.Tab reselectedTab) {
                        // Optionally handle reselection
                    }
                });
            }
        }).attach();
    }
}