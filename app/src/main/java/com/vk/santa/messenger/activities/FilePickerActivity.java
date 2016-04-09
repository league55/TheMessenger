package com.vk.santa.messenger.activities;

import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.vk.santa.messenger.OnFileClickListener;
import com.vk.santa.messenger.R;
import com.vk.santa.messenger.adapters.FilesListRecyclerAdapter;
import com.vk.santa.messenger.fragments.FilePickerActivityFragment;
import com.vk.santa.messenger.fragments.FilePickerActivityFragment.OnFragmentChangeListener;

import java.io.File;
import java.util.ArrayList;

public class FilePickerActivity extends AppCompatActivity implements OnFragmentChangeListener, OnClickListener {

    public static final int SORT_MENU_ITEM = 0;
    public static final int RETURN_PATHES_ITEM = 1;
    public static final int CANCEL_ITEM = 2;
    public static ArrayList<File> addedFilesList = new ArrayList<>();
    public static boolean multiselect_mode = false;
    final String HOME_PATH = Environment.getExternalStorageDirectory().toString();
    OnFileClickListener listener;
    private String HOME_DIR = Environment.getExternalStorageDirectory().getName();
    private RelativeLayout multifiles_layout;
    private Fragment fragment;
    private TextView title;
    private Button button_files;
    private RecyclerView addedFiles;
    private FilesListRecyclerAdapter adapter;

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
        multifiles_layout = (RelativeLayout) findViewById(R.id.togglebutton_layout);
        fragment = FilePickerActivityFragment.newInstance(HOME_PATH);
        button_files = (Button) findViewById(R.id.button_files);
        addedFiles = (RecyclerView) findViewById(R.id.folder_content_files);
        this.adapter = new FilesListRecyclerAdapter(addedFilesList, this, FilesListRecyclerAdapter.BY_NAME, new OnFileClickListener() {
            @Override
            public void onFileClick(File file, boolean isLongClick, View item) {
                addedFilesList.remove(file);

            }
        });

        FragmentManager fm = getSupportFragmentManager();
        fm.beginTransaction()
                .replace(R.id.file_picker_relative_l, fragment)
                .commit();
    }


    @Override
    public void onFragmentChange(String title) {
        this.title.setText(title);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_files:
                selectedFilesVisability(button_files.isActivated());
        }
    }

    private void selectedFilesVisability(boolean state) {
        boolean activated = true;
        if (activated) {
            addedFiles.setVisibility(View.GONE);
        } else addedFiles.setVisibility(View.VISIBLE);
    }

    public void setMultiSelectionMode(boolean isMultiSelect) {
        multiselect_mode = isMultiSelect;
        showFilesLayout();
        invalidateOptionsMenu();
    }

    public void showFilesLayout() {
        if (multiselect_mode) {
            multifiles_layout.setVisibility(View.VISIBLE);
        } else multifiles_layout.setVisibility(View.GONE);
    }

    public interface OnSortingChangeListener {
        void OnSortChange(int sortType);
    }

}
