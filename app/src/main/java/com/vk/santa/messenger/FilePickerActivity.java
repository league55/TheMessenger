package com.vk.santa.messenger;

import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.widget.RelativeLayout;

public class FilePickerActivity extends AppCompatActivity {

    final String HOME_PATH = Environment.getExternalStorageDirectory().toString();
    private RelativeLayout content;
    private Fragment home;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_picker);

        content = (RelativeLayout) findViewById(R.id.file_picker_relative_l);
        home = FilePickerActivityFragment.newInstance(HOME_PATH);

        FragmentManager fm = getSupportFragmentManager();
        fm.beginTransaction()
                .replace(R.id.file_picker_relative_l, home)
                .commit();

    }

}
