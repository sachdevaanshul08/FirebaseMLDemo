package com.p.objectidentification.ui;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Build.VERSION_CODES;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.MediaStore.MediaColumns;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import com.p.objectidentification.R;
import com.p.objectidentification.adapter.ObjectRecyclerViewAdapter;
import com.p.objectidentification.model.ItemObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by anshulsachdeva on 06/12/18.
 */

public class MainActivity extends AppCompatActivity {


    private List<ItemObject> allFiles;
    ProgressBar progressBar;
    Toolbar toolbar;
    RecyclerView recyclerView;
    ObjectRecyclerViewAdapter objectRecyclerViewAdapter;

    private static final String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //
        init();
        //
        setupViews();

    }


    /**
     * Initiate the views
     */
    private void init() {
        progressBar = (ProgressBar) findViewById(R.id.progressbar);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
    }


    /**
     *
     */
    private void setupViews() {

        setSupportActionBar(toolbar);
        setRecyclerView();

        if (isReadStoragePermissionGranted()) {
            //BitmapFactory.decodeFile(pathToPicture);
            loadAllImageOnToRecyclerView();

        }
        //
        allFiles = new ArrayList<>();
    }

    /**
     *
     */
    private void setRecyclerView() {
        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(3, 1);
        recyclerView.setLayoutManager(staggeredGridLayoutManager);

        objectRecyclerViewAdapter = new ObjectRecyclerViewAdapter(MainActivity.this);
        recyclerView.setAdapter(objectRecyclerViewAdapter);
    }

    /**
     * Refresh the recycler view
     */
    private void refreshRecycler() {
        if (objectRecyclerViewAdapter != null) {
            objectRecyclerViewAdapter.notifyDataSetChanged();
        }
    }


    /**
     * Get the path of all images stored in internal storage
     *
     * @param activity
     * @return
     */
    private List<ItemObject> getAllImageFiles(Activity activity) {
        Uri uri;
        Cursor cursor;
        int column_index_data;

        String absolutePathOfImage = null;
        uri = android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI;

        String[] projection = {MediaColumns.DATA,
                MediaStore.Images.Media.BUCKET_DISPLAY_NAME};

        cursor = activity.getContentResolver().query(uri, projection, null,
                null, null);

        column_index_data = cursor.getColumnIndexOrThrow(MediaColumns.DATA);
        ItemObject itemObject = null;
        int iLoop = 0;
        while (cursor.moveToNext()) {
            absolutePathOfImage = cursor.getString(column_index_data);
            itemObject = new ItemObject();
            itemObject.setImagePath(absolutePathOfImage);
            itemObject.setName("");
            itemObject.setIndex(iLoop);
            allFiles.add(itemObject);
        }
        return allFiles;
    }


    /**
     *
     */
    private void loadAllImageOnToRecyclerView() {

        progressBar.setVisibility(View.VISIBLE);
        new Thread(new Runnable() {
            @Override
            public void run() {

                getAllImageFiles(MainActivity.this);

                MainActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progressBar.setVisibility(View.GONE);
                        objectRecyclerViewAdapter.setItems(allFiles);
                        refreshRecycler();
                    }
                });

            }
        }).start();


    }


    /**
     * Permission granter
     *
     * @return
     */
    public boolean isReadStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.v(TAG, "Permission is granted");
                return true;
            } else {

                Log.v(TAG, "Permission is revoked");
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                return false;
            }

        } else {
            Log.v(TAG, "Permission is granted");
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {

            case 1:
                Log.d(TAG, "External storage");
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.v(TAG, "Permission: " + permissions[0] + "was " + grantResults[0]);
                    loadAllImageOnToRecyclerView();
                } else {

                }
                break;
        }
    }
}
