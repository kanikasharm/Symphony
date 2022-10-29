package com.example.symphony;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.io.File;
import java.lang.reflect.Array;
import java.util.ArrayList;

public class MainActivity<state> extends AppCompatActivity {
ListView listView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ListView listView = (ListView) findViewById(R.id.listView);
        Dexter.withContext(this)
                .withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
//                        Toast.makeText(MainActivity.this, "Runtime permission granted", Toast.LENGTH_SHORT).show();
                        ArrayList<File> mySongs= fetchSongs(Environment.getExternalStorageDirectory());
                        String[] items= new String[mySongs.size()];
                        for(int i=0; i<mySongs.size(); i++) {
                        items[i] = mySongs.get(i).getName().replace(".mp3", "");
                        }

                        ArrayAdapter<String> adapter= new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_list_item_1, items);

                        listView.setAdapter(adapter);

                          listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                              @Override
                              public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                                  Intent intent= new Intent(MainActivity.this, PlaySong.class);

                                  String currentSong= listView.getItemAtPosition(position).toString();
                                  intent.putExtra("songList", mySongs);
                                  intent.putExtra("currentSong", currentSong);
                                  intent.putExtra("position", position);
                                  startActivity(intent);
                              }
                          });
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {

                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
                    permissionToken.continuePermissionRequest();
                    }
                })
                .check();
    }



    public ArrayList<File> fetchSongs(File file) {
        ArrayList arr = new ArrayList();
        File[] songs = file.listFiles();
        if (songs != null) {
            for (File myFile : songs) {
                if (!myFile.isHidden() && myFile.isDirectory()) {
                    arr.addAll(fetchSongs(myFile));
                } else {
                    if (myFile.getName().endsWith(".mp3") ) {
                        arr.add((myFile));
                    }
                }
            }
        }

        return arr;
    }
    }

