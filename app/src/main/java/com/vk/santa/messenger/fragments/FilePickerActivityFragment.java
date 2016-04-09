package com.vk.santa.messenger.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.vk.santa.messenger.OnFileClickListener;
import com.vk.santa.messenger.R;
import com.vk.santa.messenger.activities.FilePickerActivity;
import com.vk.santa.messenger.adapters.FilesListRecyclerAdapter;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class FilePickerActivityFragment extends Fragment  {
    public static int sortType = FilesListRecyclerAdapter.BY_NAME;
    public static String newFolderPath;
    private FilePickerActivity activity;

    private File file;
    private List<File> files;
    private RecyclerView files_recycler_view;

    private FilesListRecyclerAdapter adapter;

    private List<String> file_names;
    private String path = Environment.getExternalStorageDirectory().toString();
    private String DIR;

    private OnFileClickListener onFileClickListener;
    private OnFragmentChangeListener onFragmentChangeListener;

    public FilePickerActivityFragment() {
    }

    public static FilePickerActivityFragment newInstance(String newPath){
        FilePickerActivityFragment fragment = new FilePickerActivityFragment();
        Bundle args = new Bundle();
        args.putString("newPath", newPath);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        if (getArguments() != null && getArguments().getString("newPath", path) != null)
            path = getArguments().getString("newPath", path);
        DIR = getDirName(path);
        activity = (FilePickerActivity) getActivity();
        if (onFragmentChangeListener == null) onFragmentChangeListener = activity;
        setFileNames();
        if (onFragmentChangeListener != null) onFragmentChangeListener.onFragmentChange(DIR);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        View view = inflater.inflate(R.layout.fragment_file_picker, container, false);

        files_recycler_view = (RecyclerView) view.findViewById(R.id.folder_content_files);
        this.adapter = new FilesListRecyclerAdapter(files, activity, sortType, new OnFileClickListener() {
            @Override
            public void onFileClick(File file, boolean isLongClick, View item) {
                activity.setMultiSelectionMode(isLongClick);
                if (FilePickerActivity.multiselect_mode && FilePickerActivity.addedFilesList.contains(file)) {
                    Log.i("REMOVED", FilePickerActivity.addedFilesList.remove(file) + "");
                    item.setSelected(false);
                } else if (!file.isDirectory() && !FilePickerActivity.multiselect_mode) {
                    FilePickerActivity.addedFilesList.add(file);
                    finishActivity(FilePickerActivity.addedFilesList);
                } else if (!file.isDirectory() && isLongClick) {
                    FilePickerActivity.addedFilesList.add(file);
                } else {
                    newFolderPath = file.getAbsolutePath();
                    stepIntoChildFolder(newFolderPath);
                }
            }
        });

        LinearLayoutManager layoutManager = new LinearLayoutManager(activity);
        files_recycler_view.setLayoutManager(layoutManager);
        files_recycler_view.setAdapter(adapter);
        return  view;
    }

    private void stepIntoChildFolder(String newFolderPath) {
        onFragmentChangeListener.onFragmentChange(DIR);
        Log.i("File picker", "Path" + newFolderPath);
        FragmentManager fm = activity.getSupportFragmentManager();
        Fragment fragment = newInstance(newFolderPath);

        fm.beginTransaction()
                .addToBackStack(null)
                .replace(R.id.file_picker_relative_l, fragment)
                .commit();

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_main, menu);

        menu.setGroupVisible(R.id.groupVsbl, FilePickerActivity.multiselect_mode);
        menu.add(0, FilePickerActivity.SORT_MENU_ITEM, 0, "sort")
                .setIcon(getNextIcon())
                .setShowAsAction(
                        MenuItem.SHOW_AS_ACTION_ALWAYS
                                | MenuItem.SHOW_AS_ACTION_WITH_TEXT);

        if (FilePickerActivity.multiselect_mode) {
            menu.add(1, FilePickerActivity.RETURN_PATHES_ITEM, 1, "confirm")
                    .setIcon(android.R.drawable.ic_menu_send)
                    .setShowAsAction(
                            MenuItem.SHOW_AS_ACTION_ALWAYS
                                    | MenuItem.SHOW_AS_ACTION_WITH_TEXT);
            menu.add(0, FilePickerActivity.CANCEL_ITEM, 0, "cancel")
                    .setIcon(android.R.drawable.ic_menu_close_clear_cancel)
                    .setShowAsAction(
                            MenuItem.SHOW_AS_ACTION_ALWAYS
                                    | MenuItem.SHOW_AS_ACTION_WITH_TEXT);
        } else {

        }
        super.onCreateOptionsMenu(menu, inflater);
    }

    private int getNextIcon() {
        switch (sortType) {
            case FilesListRecyclerAdapter.BY_NAME:
                return android.R.drawable.ic_menu_sort_alphabetically;
            case FilesListRecyclerAdapter.BY_SIZE:
                return R.drawable.ic_weight;
            case FilesListRecyclerAdapter.BY_DATE:
                return R.drawable.ic_date;
            default:
                return android.R.drawable.ic_menu_sort_alphabetically;

        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.i("ITEM", "-------" + item.getItemId() + " " + item.getTitle());
        switch (item.getItemId()) {
            case FilePickerActivity.SORT_MENU_ITEM:
                getNextSortType(100);
                changeSorting();
                break;
            case FilePickerActivity.RETURN_PATHES_ITEM:
                finishActivity(FilePickerActivity.addedFilesList);
                activity.setMultiSelectionMode(false);
                break;
            case FilePickerActivity.CANCEL_ITEM:
                activity.setMultiSelectionMode(false);
                FilePickerActivity.addedFilesList = new ArrayList<>();
                updateFragment(path);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void changeSorting() {
        sortType = getNextSortType(sortType);
        onFragmentChangeListener.onFragmentChange(DIR);
        onFileClickListener = new OnFileClickListener() {
            @Override
            public void onFileClick(File file, boolean b, View item) {
                newFolderPath = path;
                updateFragment(newFolderPath);
            }
        };
        adapter = new FilesListRecyclerAdapter(files, getActivity(), sortType, onFileClickListener);
        onFileClickListener.onFileClick(file, false, null);

    }

    private void updateFragment(String path) {
        Log.i("File picker", "Path" + path);
        FragmentManager fm = activity.getSupportFragmentManager();
        Fragment fragment = newInstance(path);

        fm.beginTransaction()
                .addToBackStack("one")
                .replace(R.id.file_picker_relative_l, fragment)
                .commit();
        fm.popBackStack();
    }

    private void finishActivity(ArrayList<File> files) {
        Intent intent = new Intent();
        ArrayList<String> filePathes = new ArrayList<>();
        for (File f : files) {
            filePathes.add(f.getAbsolutePath());
        }

        intent.putExtra("filePathes", filePathes);
        activity.setResult(getActivity().RESULT_OK, intent);
        activity.finish();

        FilePickerActivity.addedFilesList = new ArrayList<>();
    }

    private List<String> setFileNames() {
        Log.i("File picker", "Path" + path);
        file = new File(path);
        files = new ArrayList<File>(Arrays.asList(file.listFiles()));
        file_names = new ArrayList<>(files.size());
        for (File f : files) {
            file_names.add(f.getName());
        }
        return file_names;
    }

    private String getDirName(String path) {
        String[] arr = path.split("/");
        return arr[arr.length-1];
    }

    private int getNextSortType(int oldType) {
        switch (oldType) {
            case FilesListRecyclerAdapter.BY_NAME:
                return FilesListRecyclerAdapter.BY_SIZE;
            case FilesListRecyclerAdapter.BY_SIZE:
                return FilesListRecyclerAdapter.BY_DATE;
            case FilesListRecyclerAdapter.BY_DATE:
                return FilesListRecyclerAdapter.BY_NAME;
            default:
                return FilesListRecyclerAdapter.BY_NAME;

        }
    }

    public interface OnFragmentChangeListener {
        void onFragmentChange(String title);
    }
}
