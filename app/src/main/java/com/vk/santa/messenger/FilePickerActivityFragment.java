package com.vk.santa.messenger;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class FilePickerActivityFragment extends Fragment  {
    public static int sortType = RecyclerViewAdapter.BY_NAME;
    public static String newFolderPath;
    File file;
    Activity activity;
    private OnFragmentChangeListener mListener;
    private List<String> file_names;
    private String path = Environment.getExternalStorageDirectory().toString();
    private String DIR;
    private RecyclerView files_recycler_view;
    private List<File> files;
    private RecyclerViewAdapter adapter;
    private OnFileClickListener ofc;
    private Button button;

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
        if(getArguments() != null && getArguments().getString("newPath", path) != null) {
            path = getArguments().getString("newPath", path);
            Log.i("FP onCreate", "getArgs" + "not null");
        }
        DIR = getDirName(path);
        activity = getActivity();
        if (mListener == null) mListener = (OnFragmentChangeListener) activity;
        setFileNames();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_file_picker, container, false);

        button = (Button) getActivity().findViewById(R.id.sort_button);

        if (mListener != null) mListener.onFragmentChange(DIR);

        files_recycler_view = (RecyclerView) view.findViewById(R.id.files_recycler);
        this.adapter = new RecyclerViewAdapter(files, getActivity(), sortType, new OnFileClickListener() {
            @Override
            public void onFileClick(File file) {
                if (!file.isDirectory()) {
                    Intent intent = new Intent();
                    intent.putExtra("filePath", file.getAbsolutePath());
                    getActivity().setResult(getActivity().RESULT_OK, intent);
                    getActivity().finish();
                } else {
                    mListener.onFragmentChange(DIR);
                    newFolderPath = file.getAbsolutePath();
                    Log.i("File picker", "Path" + newFolderPath);
                    FragmentManager fm = getActivity().getSupportFragmentManager();
                    Fragment fragment = newInstance(newFolderPath);

                    fm.beginTransaction()
                            .addToBackStack("zero")
                            .replace(R.id.file_picker_relative_l, fragment)
                            .commit();

                }
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sortType = changeButtonState(sortType);
                mListener.onFragmentChange(DIR);
                Log.i("TAG", "SWAG-----");
                ofc = new OnFileClickListener() {
                    @Override
                    public void onFileClick(File item) {

                                Log.i("TAG", "SWAG-----");

                                newFolderPath = path;
                                Log.i("File picker", "Path" + newFolderPath);
                                FragmentManager fm = getActivity().getSupportFragmentManager();
                                Fragment fragment = newInstance(newFolderPath);
                                fm.popBackStack();
                                fm.beginTransaction()
                                        .addToBackStack("one")

                                        .replace(R.id.file_picker_relative_l, fragment)
                                        .commit();

                            }
                        };


                adapter = new RecyclerViewAdapter(files, getActivity(), sortType, ofc);



               ofc.onFileClick(file);

            }
        });



        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());

        files_recycler_view.setLayoutManager(layoutManager);
        files_recycler_view.setAdapter(adapter);
        return  view;
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

    private int changeButtonState(int oldState){
        switch(oldState){
            case RecyclerViewAdapter.BY_NAME: button.setText("size");
                return RecyclerViewAdapter.BY_SIZE;
            case RecyclerViewAdapter.BY_SIZE: button.setText("date");
                return RecyclerViewAdapter.BY_DATE;
            case RecyclerViewAdapter.BY_DATE: button.setText("name");
                return RecyclerViewAdapter.BY_NAME;
            default:                          button.setText("name");
                return RecyclerViewAdapter.BY_NAME;

        }
    }

    public interface OnFragmentChangeListener {
        void onFragmentChange(String title);
    }
}
