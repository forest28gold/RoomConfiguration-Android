package com.abvrtridentroom.roomconfiguration.ui;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import com.abvrtridentroom.roomconfiguration.R;
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

public class MainActivity extends AppCompatActivity {

    private final static String TAG = "MainActivity";

    private SVProgressHUD mSVProgressHUD;
    private NormalAlertDialog dialog;
    private int scrWid, scrHei;

    private int KidMin = 1;
    private int KidMax = 1000;
    PLManager plManager;
    PLSphericalPanorama panorama;

    PLHotspot hotspotWall, hotspotFloor, hotspotCurtains;
    Boolean toggleWallIsOn, toggleFloorIsOn, toggleCurtainsIsOn;

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
        plManager.setContentView(R.layout.activity_main);
        plManager.onCreate();

        mSVProgressHUD = new SVProgressHUD(this);

        panorama = new PLSphericalPanorama();
//        panorama.getCamera().lookAt(0.0f, 180.0f);

        panorama.setImage(new PLImage(PLUtils.getBitmap(this, R.raw.room_pano), false));

        PLTexture hotspotFloorTexture = new PLTexture(new PLImage(PLUtils.getBitmap(this, R.raw.floor), false));
        hotspotFloor = new PLHotspot((KidMin + modulo((int)Math.random(),((KidMax + 1) - KidMin))), hotspotFloorTexture, 0.0f, 0.0f, 1.0f, 1.0f);

        PLTexture hotspotWallTexture = new PLTexture(new PLImage(PLUtils.getBitmap(this, R.raw.empty), false));
        hotspotWall = new PLHotspot((KidMin + modulo((int)Math.random(),((KidMax + 1) - KidMin))), hotspotWallTexture, 0.0f, 0.0f, 1.0f, 1.0f);

        PLTexture hotspotCurtainsTexture = new PLTexture(new PLImage(PLUtils.getBitmap(this, R.raw.empty), false));
        hotspotCurtains = new PLHotspot((KidMin + modulo((int)Math.random(),((KidMax + 1) - KidMin))), hotspotCurtainsTexture, 0.0f, 0.0f, 1.0f, 1.0f);

        panorama.addHotspot(hotspotCurtains);
        panorama.addHotspot(hotspotFloor);
        panorama.addHotspot(hotspotWall);

        plManager.setPanorama(panorama);

        toggleCurtainsIsOn = false; toggleFloorIsOn = false; toggleWallIsOn = false;

        Button btn_floor = (Button)findViewById(R.id.button_floor);
        Button btn_curtains = (Button)findViewById(R.id.button_curtains);
        Button btn_wall = (Button)findViewById(R.id.button_wall);
        Button btn_save = (Button)findViewById(R.id.button_save);

        btn_floor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (toggleFloorIsOn) {
                    toggleFloorIsOn = false;
                    panorama.removeHotspot(hotspotFloor);
                } else {
                    toggleFloorIsOn = true;
                    PLTexture hotspotFloorTexture = new PLTexture(new PLImage(PLUtils.getBitmap(getApplicationContext(), R.raw.floor), false));
                    hotspotFloor = new PLHotspot((KidMin + modulo((int)Math.random(),((KidMax + 1) - KidMin))), hotspotFloorTexture, 0.0f, 0.0f, 1.0f, 1.0f);
                    panorama.addHotspot(hotspotFloor);
                }
            }
        });

        btn_curtains.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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
        });

        btn_wall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (toggleWallIsOn) {
                    toggleWallIsOn = false;
                    panorama.removeHotspot(hotspotWall);
                } else {
                    toggleWallIsOn = true;
                    PLTexture hotspotWallTexture = new PLTexture(new PLImage(PLUtils.getBitmap(getApplicationContext(), R.raw.wall), false));
                    hotspotWall = new PLHotspot((KidMin + modulo((int)Math.random(),((KidMax + 1) - KidMin))), hotspotWallTexture, 0.0f, 0.0f, 1.0f, 1.0f);
                    panorama.addHotspot(hotspotWall);
                }
            }
        });

        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                verifyStoragePermissions(MainActivity.this);
                onSaveScreenshot();
            }
        });

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

    //============= Save screenshot ===================

    private void onSaveScreenshot() {

        mSVProgressHUD.showWithStatus(getString(R.string.please_wait), SVProgressHUD.SVProgressHUDMaskType.Clear);

        InstaCapture.getInstance(this)
                .capture(plManager.getGLSurfaceView().getRootView())
                .setScreenCapturingListener(new SimpleScreenCapturingListener() {

                    @Override public void onCaptureStarted() {
                        super.onCaptureStarted();
                    }

                    @Override public void onCaptureComplete(Bitmap bitmap) {
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
                dialog = new NormalAlertDialog.Builder(MainActivity.this)
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
                                verifyStoragePermissions(MainActivity.this);
                            }
                        })
                        .build();
                dialog.show();
                return;
            } else {
                dialog = new NormalAlertDialog.Builder(MainActivity.this)
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

            dialog = new NormalAlertDialog.Builder(MainActivity.this)
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

            dialog = new NormalAlertDialog.Builder(MainActivity.this)
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
