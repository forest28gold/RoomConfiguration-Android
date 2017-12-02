package com.abvrtridentroom.roomconfiguration.ui;

import android.os.Bundle;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;

import com.abvrtridentroom.roomconfiguration.R;
import com.abvrtridentroom.roomconfiguration.adapter.ViewPagerAdapter;

import java.util.ArrayList;

public class SlideShowActivity extends AppCompatActivity {

    private final static String TAG = "SildeShowActivity";

    private ArrayList<Integer> images;
    private ViewPager viewPager;
    private FragmentStatePagerAdapter adapter;
    private ImageButton btn_slide_back, btn_slide_forward;
    private final static int[] resourceIDs = new int[]{R.mipmap.screen01, R.mipmap.screen02, R.mipmap.screen03,
            R.mipmap.screen01, R.mipmap.screen02, R.mipmap.screen03};

    private int initGalleryIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_slide_show);

        images = new ArrayList<>();

        viewPager = (ViewPager) findViewById(R.id.view_pager);
        ImageButton btn_logo = (ImageButton)findViewById(R.id.imageButton_logo);
        Button btn_back = (Button)findViewById(R.id.button_back);
        btn_slide_back = (ImageButton)findViewById(R.id.imageButton_back);
        btn_slide_forward = (ImageButton)findViewById(R.id.imageButton_forward);

        setImagesData();

        adapter = new ViewPagerAdapter(getSupportFragmentManager(), images);
        viewPager.setAdapter(adapter);

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        btn_logo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        btn_slide_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (viewPager.getCurrentItem() > 0) {
                    viewPager.setCurrentItem(viewPager.getCurrentItem() - 1);
                }
            }
        });

        btn_slide_forward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (viewPager.getCurrentItem() < viewPager.getAdapter().getCount() - 1) {
                    viewPager.setCurrentItem(viewPager.getCurrentItem() + 1);
                }
            }
        });

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                setSlideButton(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        initGalleryIndex = 0;
        setInitGalleryView(initGalleryIndex);
    }

    private void setImagesData() {
        for (int i = 0; i < resourceIDs.length; i++) {
            images.add(resourceIDs[i]);
        }
    }

    private void setInitGalleryView(int position) {
        viewPager.setCurrentItem(position);
        setSlideButton(position);
    }

    private void setSlideButton(int position) {
        if (position == 0) {
            btn_slide_back.setEnabled(false);
            btn_slide_forward.setEnabled(true);
            btn_slide_back.setImageDrawable(getResources().getDrawable(R.mipmap.slide_back_disable));
            btn_slide_forward.setImageDrawable(getResources().getDrawable(R.mipmap.slide_forward));
        } else if (position == images.size() - 1) {
            btn_slide_back.setEnabled(true);
            btn_slide_forward.setEnabled(false);
            btn_slide_back.setImageDrawable(getResources().getDrawable(R.mipmap.slide_back));
            btn_slide_forward.setImageDrawable(getResources().getDrawable(R.mipmap.slide_forward_disable));
        } else {
            btn_slide_back.setEnabled(true);
            btn_slide_forward.setEnabled(true);
            btn_slide_back.setImageDrawable(getResources().getDrawable(R.mipmap.slide_back));
            btn_slide_forward.setImageDrawable(getResources().getDrawable(R.mipmap.slide_forward));
        }
    }
}
