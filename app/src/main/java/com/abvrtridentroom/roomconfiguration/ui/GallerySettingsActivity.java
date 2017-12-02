package com.abvrtridentroom.roomconfiguration.ui;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.abvrtridentroom.roomconfiguration.R;

public class GallerySettingsActivity extends AppCompatActivity {

    private final static String TAG = "GallerySettingsActivity";
    private TextView txt_gallery_name;
    private ImageView img_arrow;
    private RelativeLayout relativeLayout_list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_gallery_settings);

        ImageButton btn_back = (ImageButton)findViewById(R.id.imageButton_logo);
        Button btn_save = (Button)findViewById(R.id.button_save);
        RelativeLayout btn_gallery_name = (RelativeLayout)findViewById(R.id.relativeLayout_gallery_name);
        RelativeLayout btn_main = (RelativeLayout)findViewById(R.id.relativeLayout_main);
        Button btn_upload = (Button)findViewById(R.id.button_upload);

        img_arrow = (ImageView)findViewById(R.id.imageView_arrow);
        txt_gallery_name = (TextView)findViewById(R.id.textView_gallery_name);
        relativeLayout_list = (RelativeLayout)findViewById(R.id.relativeLayout_list);
        relativeLayout_list.setVisibility(View.GONE);
        relativeLayout_list.setAlpha(0.0f);

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        btn_main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                removeDropMenuView(relativeLayout_list);
            }
        });

        btn_gallery_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDropMenuView(relativeLayout_list);
            }
        });

        btn_upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                removeDropMenuView(relativeLayout_list);

            }
        });

        setGalleryNameList();
    }

    private void showDropMenuView(final RelativeLayout view) {

        if (view.getVisibility() == View.VISIBLE) {
            removeDropMenuView(view);
        } else {
            view.animate()
                    .alpha(1.0f)
                    .setDuration(150)
                    .setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            super.onAnimationEnd(animation);
                            view.setVisibility(View.VISIBLE);
                            img_arrow.setRotation((float) 180);
                        }
                    });
        }
    }

    private void removeDropMenuView(final RelativeLayout view) {

        view.animate()
                .alpha(0.0f)
                .setDuration(150)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        view.setVisibility(View.GONE);
                        img_arrow.setRotation((float) 0.0);
                    }
                });
    }

    private void setGalleryNameList() {

        for (int i=0; i<10; i++) {
            onShowGalleryNames(i, "Gallery name");
        }
    }

    public void onShowGalleryNames(int number, final String name) {

        LinearLayout list = (LinearLayout) findViewById(R.id.linearLayout_list);

        final LinearLayout newCell = (LinearLayout) (View.inflate(this, R.layout.row_list, null));
        TextView txt_title = (TextView)newCell.findViewById(R.id.textView_list);
        txt_title.setText(name);

        newCell.setTag(number);
        registerForContextMenu(newCell);
        list.addView(newCell);

        newCell.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                txt_gallery_name.setText(name);
                removeDropMenuView(relativeLayout_list);
            }
        });

    }
}
