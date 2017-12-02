package com.abvrtridentroom.roomconfiguration.ui;

import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.abvrtridentroom.roomconfiguration.R;
import com.abvrtridentroom.roomconfiguration.adapter.LeftListAdapter;
import com.abvrtridentroom.roomconfiguration.adapter.MainSectionedAdapter;
import com.abvrtridentroom.roomconfiguration.model.TextureData;
import com.abvrtridentroom.roomconfiguration.view.PinnedHeaderListView;
import com.bigkoo.svprogresshud.SVProgressHUD;
import com.panoramagl.PLConstants;
import com.panoramagl.PLImage;
import com.panoramagl.PLManager;
import com.panoramagl.PLSphericalPanorama;
import com.panoramagl.PLTexture;
import com.panoramagl.hotspots.PLHotspot;
import com.panoramagl.utils.PLUtils;
import com.tarek360.instacapture.InstaCapture;
import com.tarek360.instacapture.listener.SimpleScreenCapturingListener;
import com.wevey.selector.dialog.NormalAlertDialog;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity {

    private final static String TAG = "HomeActivity";

    private SVProgressHUD mSVProgressHUD;
    private NormalAlertDialog dialog;
    private Dialog diaNewGallery = null;
    private int scrWid, scrHei;

    private int KidMin = 1;
    private int KidMax = 1000;
    PLManager plManager;
    PLSphericalPanorama panorama;
    PLHotspot hotspotWall, hotspotFloor, hotspotCurtains;
    Boolean toggleWallIsOn, toggleFloorIsOn, toggleCurtainsIsOn;

    private RelativeLayout relativeLayout_title, relativeLayout_menu, relativeLayout_menu_design, relativeLayout_menu_gallery, relativeLayout_menu_settings;
    private Button btn_menu_design, btn_menu_gallery, btn_menu_save;
    private ImageButton btn_menu_settings, btn_menu_back;

    private String selected_menu;

    private TextView txt_design_name;
    private ImageView img_arrow;
    private RelativeLayout relativeLayout_design_list;
    private GridView gridDesign;
    private Adapter mAdapterDesign;
    private String[] designNames = { "Bedsheet", "Comforter", "Sham", "Pillow", "Eurosham", "Wall", "Curtain", "Floor" };

    ListView leftListview;
    PinnedHeaderListView pinnedListView;
    private boolean isScroll = true;
    private LeftListAdapter mAdapterLeftList;
    private String[] leftStr = new String[]{"Pasta category", "Cover rice", "sushi", "barbecue", "Drinks", "cold dish", "snack", "Porridge", "Leisure"};
    private boolean[] flagArray = {true, false, false, false, false, false, false, false, false};
    private String[][] rightStr = new String[][]{{"Hot dry noodles", "Sao face", "Braised noodles"},
            {"Tomato egg", "Braised ribs", "Farm small fried meat"},
            {"Cheese", "Ugly little Ya", "tuna"},
            {"Kebab", "Roast chicken wings", "rack of lamb"},
            {"Great Wall dry red", "beer", "fresh beer"},
            {"Mix with fans", "Big side dishes", "Spinach peanut"},
            {"Snack group", "Purple potato"},
            {"Millet gruel", "Rice porridge", "Pumpkin porridge", "Polenta", "Purple rice porridge"},
            {"Child car", "Bear big", " Bear two", "Bald Strong"}
    };

    private static final int REQUEST_EXTERNAL_STORAGE = 2;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        DisplayMetrics mec = new DisplayMetrics();
        this.getWindowManager().getDefaultDisplay().getMetrics(mec);
        scrWid = mec.widthPixels;
        scrHei = mec.heightPixels;

        if (scrWid <= 480) {
            PLConstants.kViewportScale = 5.0f;
        } else if (scrWid <= 960 && scrWid > 480) {
            PLConstants.kViewportScale = 4.5f;
        } else if (scrWid <= 1280 && scrWid > 960) {
            PLConstants.kViewportScale = 3.5f;
        } else if (scrWid > 1280) {
            PLConstants.kViewportScale = 2.3f;
        }

        plManager = new PLManager(this);
        plManager.setContentView(R.layout.activity_home);
        plManager.onCreate();

        mSVProgressHUD = new SVProgressHUD(this);

        initSetPanorama();

        selected_menu = "Design";

        //=======  Home View =================

        relativeLayout_title = (RelativeLayout)findViewById(R.id.relativeLayout_title);
        ImageButton btn_back = (ImageButton)findViewById(R.id.imageButton_logo);
        ImageButton btn_capture = (ImageButton)findViewById(R.id.imageButton_capture);
        ImageButton btn_menu = (ImageButton)findViewById(R.id.imageButton_menu);

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        btn_capture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                verifyStoragePermissions(HomeActivity.this);
                onSaveScreenshot();
            }
        });

        btn_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onShowMenu(selected_menu);
            }
        });

        //========= Slide Menu View ===================

        relativeLayout_menu = (RelativeLayout)findViewById(R.id.relativeLayout_menu);
        relativeLayout_menu_design = (RelativeLayout)findViewById(R.id.relativeLayout_menu_design);
        relativeLayout_menu_gallery = (RelativeLayout)findViewById(R.id.relativeLayout_menu_gallery);
        relativeLayout_menu_settings = (RelativeLayout)findViewById(R.id.relativeLayout_menu_settings);

        btn_menu_design = (Button)findViewById(R.id.button_design);
        btn_menu_gallery = (Button)findViewById(R.id.button_gallery);
        btn_menu_save = (Button)findViewById(R.id.button_save);
        btn_menu_settings = (ImageButton) findViewById(R.id.imageButton_settings);
        btn_menu_back = (ImageButton) findViewById(R.id.imageButton_back);

        relativeLayout_menu.setVisibility(View.INVISIBLE);
        relativeLayout_menu.setTranslationX(relativeLayout_menu.getWidth());

        relativeLayout_menu_design.setVisibility(View.VISIBLE);
        relativeLayout_menu_gallery.setVisibility(View.INVISIBLE);
        relativeLayout_menu_settings.setVisibility(View.INVISIBLE);

        btn_menu_design.setBackgroundColor(getResources().getColor(R.color.menu_gray));
        btn_menu_gallery.setBackgroundColor(getResources().getColor(R.color.menu_dark));
        btn_menu_settings.setImageDrawable(getResources().getDrawable(R.mipmap.settings));

        btn_menu_design.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selected_menu = "Design";
                relativeLayout_menu_design.setVisibility(View.VISIBLE);
                relativeLayout_menu_gallery.setVisibility(View.INVISIBLE);
                relativeLayout_menu_settings.setVisibility(View.INVISIBLE);

                btn_menu_design.setBackgroundColor(getResources().getColor(R.color.menu_gray));
                btn_menu_gallery.setBackgroundColor(getResources().getColor(R.color.menu_dark));
                btn_menu_settings.setImageDrawable(getResources().getDrawable(R.mipmap.settings));
            }
        });

        btn_menu_gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selected_menu = "Gallery";
                relativeLayout_menu_design.setVisibility(View.INVISIBLE);
                relativeLayout_menu_gallery.setVisibility(View.VISIBLE);
                relativeLayout_menu_settings.setVisibility(View.INVISIBLE);

                btn_menu_design.setBackgroundColor(getResources().getColor(R.color.menu_dark));
                btn_menu_gallery.setBackgroundColor(getResources().getColor(R.color.menu_gray));
                btn_menu_settings.setImageDrawable(getResources().getDrawable(R.mipmap.settings));
            }
        });

        btn_menu_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onCloseMenu();
            }
        });

        btn_menu_settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selected_menu = "Settings";
                relativeLayout_menu_design.setVisibility(View.INVISIBLE);
                relativeLayout_menu_gallery.setVisibility(View.INVISIBLE);
                relativeLayout_menu_settings.setVisibility(View.VISIBLE);

                btn_menu_design.setBackgroundColor(getResources().getColor(R.color.menu_dark));
                btn_menu_gallery.setBackgroundColor(getResources().getColor(R.color.menu_dark));
                btn_menu_settings.setImageDrawable(getResources().getDrawable(R.mipmap.settings_white));
            }
        });

        btn_menu_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        //========= Settings Menu View ==========

        Button btn_gallery_options = (Button)findViewById(R.id.button_option);
        Button btn_settings_update = (Button)findViewById(R.id.button_update);
        Button btn_settings_upload = (Button)findViewById(R.id.button_upload);
        Button btn_settings_tutorials = (Button)findViewById(R.id.button_tutorials);
        Button btn_settings_reset = (Button)findViewById(R.id.button_reset);

        btn_gallery_options.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeActivity.this, GallerySettingsActivity.class);
                startActivity(intent);
            }
        });

        btn_settings_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        btn_settings_upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        btn_settings_tutorials.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        btn_settings_reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        //========= Design Menu View ============

        gridDesign = (GridView) findViewById(R.id.gridview_texture);
        gridDesign.setAdapter(mAdapterDesign = new Adapter(this));

        img_arrow = (ImageView)findViewById(R.id.imageView_arrow);
        txt_design_name = (TextView)findViewById(R.id.textView_design_name);
        relativeLayout_design_list = (RelativeLayout)findViewById(R.id.relativeLayout_design_list);
        relativeLayout_design_list.setVisibility(View.GONE);
        relativeLayout_design_list.setAlpha(0.0f);

        txt_design_name.setText(designNames[0]);

        setDesignNameList();
        initLoadDesignTextureData();

        RelativeLayout relativeLayout_design_name = (RelativeLayout)findViewById(R.id.relativeLayout_design_name);
        relativeLayout_design_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDropMenuView(relativeLayout_design_list);
            }
        });

        //========= Gallery Menu View ===========

        ImageButton btn_add_folder = (ImageButton)findViewById(R.id.imageButton_add);
        btn_add_folder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onShowNewGalleryDialog();
            }
        });

        pinnedListView = (PinnedHeaderListView) findViewById(R.id.pinnedListView);
        leftListview = (ListView)findViewById(R.id.left_listview);
        final MainSectionedAdapter sectionedAdapter = new MainSectionedAdapter(this, leftStr, rightStr);
        pinnedListView.setAdapter(sectionedAdapter);
        mAdapterLeftList = new LeftListAdapter(this, leftStr, flagArray);
        leftListview.setAdapter(mAdapterLeftList);

        leftListview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View view, int position, long arg3) {
                isScroll = false;

                for (int i = 0; i < leftStr.length; i++) {
                    if (i == position) {
                        flagArray[i] = true;
                    } else {
                        flagArray[i] = false;
                    }
                }
                mAdapterLeftList.notifyDataSetChanged();
                int rightSection = 0;
                for (int i = 0; i < position; i++) {
                    rightSection += sectionedAdapter.getCountForSection(i) + 1;
                }
                pinnedListView.setSelection(rightSection);

            }

        });

        pinnedListView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView arg0, int scrollState) {
                switch (scrollState) {
                    case AbsListView.OnScrollListener.SCROLL_STATE_IDLE:
                        if (pinnedListView.getLastVisiblePosition() == (pinnedListView.getCount() - 1)) {
                            leftListview.setSelection(ListView.FOCUS_DOWN);
                        }

                        if (pinnedListView.getFirstVisiblePosition() == 0) {
                            leftListview.setSelection(0);
                        }

                        break;
                }
            }

            int y = 0;
            int x = 0;
            int z = 0;

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (isScroll) {
                    for (int i = 0; i < rightStr.length; i++) {
                        if (i == sectionedAdapter.getSectionForPosition(pinnedListView.getFirstVisiblePosition())) {
                            flagArray[i] = true;
                            x = i;
                        } else {
                            flagArray[i] = false;
                        }
                    }
                    if (x != y) {
                        mAdapterLeftList.notifyDataSetChanged();
                        y = x;
                        if (y == leftListview.getLastVisiblePosition()) {
//                            z = z + 3;
                            leftListview.setSelection(z);
                        }
                        if (x == leftListview.getFirstVisiblePosition()) {
//                            z = z - 1;
                            leftListview.setSelection(z);
                        }
                        if (firstVisibleItem + visibleItemCount == totalItemCount - 1) {
                            leftListview.setSelection(ListView.FOCUS_DOWN);
                        }
                    }
                } else {
                    isScroll = true;
                }
            }
        });

    }

    //============== Load Panorama ======================

    private void initSetPanorama() {

        toggleCurtainsIsOn = false; toggleFloorIsOn = false; toggleWallIsOn = false;

        panorama = new PLSphericalPanorama();
        panorama.getCamera().lookAt(0.0f, 0.0f);

        panorama.setImage(new PLImage(PLUtils.getBitmap(this, R.raw.room_pano), false));

        PLTexture hotspotFloorTexture = new PLTexture(new PLImage(PLUtils.getBitmap(this, R.raw.empty), false));
        hotspotFloor = new PLHotspot((KidMin + modulo((int)Math.random(),((KidMax + 1) - KidMin))), hotspotFloorTexture, 0.0f, 0.0f, 1.0f, 1.0f);

        PLTexture hotspotWallTexture = new PLTexture(new PLImage(PLUtils.getBitmap(this, R.raw.empty), false));
        hotspotWall = new PLHotspot((KidMin + modulo((int)Math.random(),((KidMax + 1) - KidMin))), hotspotWallTexture, 0.0f, 0.0f, 1.0f, 1.0f);

        PLTexture hotspotCurtainsTexture = new PLTexture(new PLImage(PLUtils.getBitmap(this, R.raw.empty), false));
        hotspotCurtains = new PLHotspot((KidMin + modulo((int)Math.random(),((KidMax + 1) - KidMin))), hotspotCurtainsTexture, 0.0f, 0.0f, 1.0f, 1.0f);

        panorama.addHotspot(hotspotCurtains);
        panorama.addHotspot(hotspotFloor);
        panorama.addHotspot(hotspotWall);

        plManager.setPanorama(panorama);
    }

    public int modulo(int m, int n){
        int mod =  m % n ;
        return ( mod < 0 ) ? mod + n : mod;
    }

    @Override
    protected void onResume() {
        super.onResume();
        plManager.onResume();
    }

    @Override
    protected void onPause() {
        plManager.onPause();
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        plManager.onDestroy();
        super.onDestroy();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return plManager.onTouchEvent(event);
    }

    public void onSetRoomDesignTexture(String textureName, boolean selected) {

        if (textureName.equals("Floor")) {
            if (toggleFloorIsOn) {
                toggleFloorIsOn = false;
                panorama.removeHotspot(hotspotFloor);
            } else {
                toggleFloorIsOn = true;
                PLTexture hotspotFloorTexture = new PLTexture(new PLImage(PLUtils.getBitmap(getApplicationContext(), R.raw.floor), false));
                hotspotFloor = new PLHotspot((KidMin + modulo((int)Math.random(),((KidMax + 1) - KidMin))), hotspotFloorTexture, 0.0f, 0.0f, 1.0f, 1.0f);
                panorama.addHotspot(hotspotFloor);
            }
        } else if (textureName.equals("Wall")) {
            if (toggleWallIsOn) {
                toggleWallIsOn = false;
                panorama.removeHotspot(hotspotWall);
            } else {
                toggleWallIsOn = true;
                PLTexture hotspotWallTexture = new PLTexture(new PLImage(PLUtils.getBitmap(getApplicationContext(), R.raw.wall), false));
                hotspotWall = new PLHotspot((KidMin + modulo((int)Math.random(),((KidMax + 1) - KidMin))), hotspotWallTexture, 0.0f, 0.0f, 1.0f, 1.0f);
                panorama.addHotspot(hotspotWall);
            }
        } else if (textureName.equals("Curtain")) {
            if (toggleCurtainsIsOn) {
                toggleCurtainsIsOn = false;
                panorama.removeHotspot(hotspotCurtains);
            } else {
                toggleCurtainsIsOn = true;
                PLTexture hotspotCurtainsTexture = new PLTexture(new PLImage(PLUtils.getBitmap(getApplicationContext(), R.raw.curtains), false));
                hotspotCurtains = new PLHotspot((KidMin + modulo((int)Math.random(),((KidMax + 1) - KidMin))), hotspotCurtainsTexture, 0.0f, 0.0f, 1.0f, 1.0f);
                panorama.addHotspot(hotspotCurtains);
            }
        }
    }

    //========= Save Screenshot to Photo Gallery ===========

    private void onSaveScreenshot() {

        mSVProgressHUD.showWithStatus(getString(R.string.please_wait), SVProgressHUD.SVProgressHUDMaskType.Clear);

        relativeLayout_title.setVisibility(View.INVISIBLE);

        InstaCapture.getInstance(this)
                .capture(plManager.getGLSurfaceView().getRootView())
                .setScreenCapturingListener(new SimpleScreenCapturingListener() {

                    @Override public void onCaptureStarted() {
                        super.onCaptureStarted();
                    }

                    @Override public void onCaptureComplete(Bitmap bitmap) {

                        relativeLayout_title.setVisibility(View.VISIBLE);
                        saveImageToGallery(getApplicationContext(), bitmap);
                    }
                });

    }

    private void openScreenshot(File imageFile) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        Uri uri = Uri.fromFile(imageFile);
        intent.setDataAndType(uri, "image/*");
        startActivity(intent);
    }

    public void saveImageToGallery(Context context, Bitmap bmp) {
        File appDir = new File(Environment.getExternalStorageDirectory(), "roomconfigurator");
        if (!appDir.exists()) {
            appDir.mkdir();
        }
        String fileName = System.currentTimeMillis() + ".jpg";
        final File file = new File(appDir, fileName);
        try {
            FileOutputStream fos = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
        } catch (Exception e) {

            mSVProgressHUD.dismiss();

            if (bmp != null) {
                dialog = new NormalAlertDialog.Builder(HomeActivity.this)
                        .setHeight(0.22f)
                        .setWidth(0.4f)
                        .setTitleVisible(true)
                        .setTitleText(getString(R.string.alert))
                        .setTitleTextColor(R.color.black)
                        .setContentText(getString(R.string.alert_allow_permission))
                        .setContentTextColor(R.color.dark)
                        .setSingleMode(true)
                        .setSingleButtonText(getString(R.string.ok))
                        .setSingleButtonTextColor(R.color.green)
                        .setCanceledOnTouchOutside(false)
                        .setSingleListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                dialog.dismiss();
                                verifyStoragePermissions(HomeActivity.this);
                            }
                        })
                        .build();
                dialog.show();
                return;
            } else {
                dialog = new NormalAlertDialog.Builder(HomeActivity.this)
                        .setHeight(0.22f)
                        .setWidth(0.4f)
                        .setTitleVisible(true)
                        .setTitleText(getString(R.string.alert))
                        .setTitleTextColor(R.color.black)
                        .setContentText(getString(R.string.alert_save_failed))
                        .setContentTextColor(R.color.dark)
                        .setSingleMode(true)
                        .setSingleButtonText(getString(R.string.ok))
                        .setSingleButtonTextColor(R.color.green)
                        .setCanceledOnTouchOutside(false)
                        .setSingleListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                dialog.dismiss();
                            }
                        })
                        .build();
                dialog.show();
                return;
            }
        }

        try {

            mSVProgressHUD.dismiss();

            MediaStore.Images.Media.insertImage(context.getContentResolver(), file.getAbsolutePath(), fileName, null);

            dialog = new NormalAlertDialog.Builder(HomeActivity.this)
                    .setHeight(0.22f)
                    .setWidth(0.4f)
                    .setTitleVisible(true)
                    .setTitleText(getString(R.string.alert))
                    .setTitleTextColor(R.color.black)
                    .setContentText(getString(R.string.alert_saved_successfully))
                    .setContentTextColor(R.color.dark)
                    .setSingleMode(true)
                    .setSingleButtonText(getString(R.string.ok))
                    .setSingleButtonTextColor(R.color.green)
                    .setCanceledOnTouchOutside(false)
                    .setSingleListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            dialog.dismiss();
                            openScreenshot(file);
                        }
                    })
                    .build();
            dialog.show();
            return;

        } catch (FileNotFoundException e) {

            mSVProgressHUD.dismiss();

            dialog = new NormalAlertDialog.Builder(HomeActivity.this)
                    .setHeight(0.22f)
                    .setWidth(0.4f)
                    .setTitleVisible(true)
                    .setTitleText(getString(R.string.alert))
                    .setTitleTextColor(R.color.black)
                    .setContentText(getString(R.string.alert_save_failed))
                    .setContentTextColor(R.color.dark)
                    .setSingleMode(true)
                    .setSingleButtonText(getString(R.string.ok))
                    .setSingleButtonTextColor(R.color.green)
                    .setCanceledOnTouchOutside(false)
                    .setSingleListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            dialog.dismiss();
                        }
                    })
                    .build();
            dialog.show();
            return;
        }
    }

    //========= Show Slide Menu ===============

    private void onShowMenu(final String menu) {

        relativeLayout_menu.setVisibility(View.VISIBLE);
        relativeLayout_menu.animate()
                .translationX(0)
                .alpha(1.0f)
                .setDuration(300)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);

                    }
                });
    }

    private void onCloseMenu() {

        relativeLayout_menu.animate()
                .translationX(relativeLayout_menu.getWidth())
                .setDuration(300)
                .alpha(1.0f)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        relativeLayout_menu.clearAnimation();
                    }
                });

    }

    //============= Design DropDown Menu ======================

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

    private void setDesignNameList() {

        for (int i=0; i<designNames.length; i++) {
            onShowDesignNames(i, designNames[i]);
        }
    }

    public void onShowDesignNames(int number, final String name) {

        LinearLayout list = (LinearLayout) findViewById(R.id.linearLayout_list);

        final LinearLayout newCell = (LinearLayout) (View.inflate(this, R.layout.row_list, null));
        TextView txt_title = (TextView)newCell.findViewById(R.id.textView_list);
        txt_title.setText(name);

        newCell.setTag(number);
        registerForContextMenu(newCell);
        list.addView(newCell);

        newCell.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                txt_design_name.setText(name);
                removeDropMenuView(relativeLayout_design_list);


            }
        });

    }

    //================= Design Texture Data ====================

    private void initLoadDesignTextureData() {

        TextureData textureData0 = new TextureData();
        textureData0.setName("floor");
        textureData0.setSelectIsOn(false);
        textureData0.setStyle("Floor");
        textureData0.setImage("floor");

        mAdapterDesign.addItem(0, textureData0);

        TextureData textureData1 = new TextureData();
        textureData1.setName("wall");
        textureData1.setSelectIsOn(false);
        textureData1.setStyle("Wall");
        textureData1.setImage("wall");

        mAdapterDesign.addItem(1, textureData1);

        TextureData textureData2 = new TextureData();
        textureData2.setName("curtain");
        textureData2.setSelectIsOn(false);
        textureData2.setStyle("Curtain");
        textureData2.setImage("curtain");

        mAdapterDesign.addItem(2, textureData2);
    }

    public class Adapter extends BaseAdapter {
        private Context mContext;
        private List<TextureData> mTextureList = new ArrayList<>();

        public Adapter(Context context) {
            mContext = context;
        }

        public void addItem(TextureData item) {
            mTextureList.add(item);
            notifyDataSetChanged();
        }

        public void addItem(int pos, TextureData item) {
            mTextureList.add(pos, item);
            notifyDataSetChanged();
        }

        public void replaceItem(int pos, TextureData item) {
            mTextureList.set(pos, item);
            notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            return mTextureList.size();
        }

        @Override
        public TextureData getItem(int position) {
            return mTextureList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            final ViewHolder viewHolder;
            if (null == convertView) {
                convertView = LayoutInflater.from(mContext).inflate(R.layout.row_grid_design, parent, false);
                convertView.setTag(viewHolder = new ViewHolder(convertView));
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }

            final TextureData textureData = mTextureList.get(position);

//            int drawableResourceId = getApplicationContext().getResources().getIdentifier(textureData.getImage(), "mipmap", getApplicationContext().getPackageName());
//            viewHolder.img_texture.setImageResource(drawableResourceId);
            viewHolder.txt_name.setText(textureData.getName());

            if (textureData.isSelectIsOn()) {
                viewHolder.img_selected.setVisibility(View.VISIBLE);
            } else {
                viewHolder.img_selected.setVisibility(View.INVISIBLE);
            }

            viewHolder.relativeLayout_texture.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if (textureData.isSelectIsOn()) {
                        textureData.setSelectIsOn(false);
                        replaceItem(position, textureData);
                    } else {
                        textureData.setSelectIsOn(true);
                        replaceItem(position, textureData);
                    }

                    onSetRoomDesignTexture(textureData.getStyle(), textureData.isSelectIsOn());
                }
            });

            return convertView;
        }

        class ViewHolder {
            protected ImageView img_texture, img_selected;
            protected RelativeLayout relativeLayout_texture;
            protected TextView txt_name;

            ViewHolder(View rootView) {
                initView(rootView);
            }

            private void initView(View rootView) {
                img_texture = (ImageView) rootView.findViewById(R.id.imageView_design);
                img_selected = (ImageView) rootView.findViewById(R.id.imageView_selected);
                relativeLayout_texture = (RelativeLayout) rootView.findViewById(R.id.relativeLayout_texture);
                txt_name = (TextView) rootView.findViewById(R.id.textView_name);
            }
        }
    }

    //================= Gallery Menu =======================

    public void onShowNewGalleryDialog() {

        if(diaNewGallery == null){
            diaNewGallery = new Dialog(HomeActivity.this, R.style.CustomTheme);
            diaNewGallery.setContentView(R.layout.dialog_new_gallery);
            Window drawWin = diaNewGallery.getWindow();
            WindowManager.LayoutParams diaParam = drawWin.getAttributes();
            diaParam.gravity = Gravity.CENTER;
            drawWin.setAttributes(diaParam);
            diaNewGallery.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            diaNewGallery.getWindow().setLayout(scrWid*3/5, scrHei/2);

            Button btn_cancel = (Button)diaNewGallery.findViewById(R.id.button_cancel);
            btn_cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    diaNewGallery.dismiss();
                }
            });
        }

        final EditText edit_new_gallery_name = (EditText)diaNewGallery.findViewById(R.id.editText_folder_name);
        edit_new_gallery_name.setText("");

        Button btn_submit = (Button)diaNewGallery.findViewById(R.id.button_submit);
        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String strNewGalleryName = edit_new_gallery_name.getText().toString();
                if (strNewGalleryName.equals("")) {

                    dialog = new NormalAlertDialog.Builder(HomeActivity.this)
                            .setHeight(0.22f)
                            .setWidth(0.4f)
                            .setTitleVisible(true)
                            .setTitleText(getString(R.string.alert))
                            .setTitleTextColor(R.color.black)
                            .setContentText(getString(R.string.alert_input_gallery_name))
                            .setContentTextColor(R.color.dark)
                            .setSingleMode(true)
                            .setSingleButtonText(getString(R.string.ok))
                            .setSingleButtonTextColor(R.color.green)
                            .setCanceledOnTouchOutside(false)
                            .setSingleListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    dialog.dismiss();
                                }
                            })
                            .build();
                    dialog.show();
                    return;
                }


                diaNewGallery.dismiss();
            }
        });

        diaNewGallery.show();
    }

    //====================== Permission checking ===================

    public static void verifyStoragePermissions(Activity activity) {
        // Check if we have write permission
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        }
    }
}
