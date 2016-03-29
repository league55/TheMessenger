package com.vk.santa.messenger.activities;

import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.vk.santa.messenger.OnFileClickListener;
import com.vk.santa.messenger.R;
import com.vk.santa.messenger.adapters.RecyclerViewAdapter;
import com.vk.santa.messenger.fragments.FilePickerActivityFragment;

public class FilePickerActivity extends AppCompatActivity implements FilePickerActivityFragment.OnFragmentChangeListener {

    final String HOME_PATH = Environment.getExternalStorageDirectory().toString();
    OnFileClickListener listener;
    private String HOME_DIR = Environment.getExternalStorageDirectory().getName();
    private RelativeLayout content;
    private Fragment fragment;
    private TextView title;
    private Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_picker);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        title = (TextView) findViewById(R.id.title);
        content = (RelativeLayout) findViewById(R.id.file_picker_relative_l);
        fragment = FilePickerActivityFragment.newInstance(HOME_PATH);
        button = (Button) findViewById(R.id.sort_button);
        changeButtonState(100);

        FragmentManager fm = getSupportFragmentManager();
        fm.beginTransaction()
                .replace(R.id.file_picker_relative_l, fragment)
                .commit();
    }

    @Override
    public void onFragmentChange(String title) {
        this.title.setText(title);
    }

    private int changeButtonState(int oldState) {
        switch (oldState) {
            case RecyclerViewAdapter.BY_NAME:
                button.setText("size");
                return RecyclerViewAdapter.BY_SIZE;
            case RecyclerViewAdapter.BY_SIZE:
                button.setText("date");
                return RecyclerViewAdapter.BY_DATE;
            case RecyclerViewAdapter.BY_DATE:
                button.setText("name");
                return RecyclerViewAdapter.BY_NAME;
            default:
                button.setText("name");
                return RecyclerViewAdapter.BY_NAME;

        }
    }

    public interface OnSortingChangeListener {
        void OnSortChange(int sortType);
    }

}
