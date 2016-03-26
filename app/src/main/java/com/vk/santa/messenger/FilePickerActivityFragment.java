package com.vk.santa.messenger;

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

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class FilePickerActivityFragment extends Fragment {

    private List<String> file_names;
    private String path = Environment.getExternalStorageDirectory().toString();
    private RecyclerView files_recycler_view;
    private List<File> files;
    private RecyclerViewAdapter adapter;

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
         Log.i("FP onCreate", "newPath" + path);
         List<String> file_names = getFileNames();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_file_picker, container, false);
        files_recycler_view = (RecyclerView) view.findViewById(R.id.files_recycler);
        this.adapter = new RecyclerViewAdapter(files, new RecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(File item, int position) {
                File file = files.get(position);
                if (!file.isDirectory()) {
                    Intent intent = new Intent();
                    intent.putExtra("filePath", file.getAbsolutePath());
                    getActivity().setResult(getActivity().RESULT_OK, intent);
                    getActivity().finish();
                } else {
                    String newFolderPath = file.getAbsolutePath();
                    Log.i("File picker", "Path" + newFolderPath);
                    FragmentManager fm = getActivity().getSupportFragmentManager();
                    Fragment fragment = newInstance(newFolderPath);
                    fm.beginTransaction().addToBackStack(null).replace(R.id.files_frag_placeholder, fragment).commit();
                }
            }
        });
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());

        files_recycler_view.setLayoutManager(layoutManager);
        files_recycler_view.setAdapter(adapter);
    return  view;
    }

    private List<String> getFileNames(){
        Log.i("File picker", "Path" + path);
        File f = new File(path);
        files = new ArrayList<File>(Arrays.asList(f.listFiles()));
        file_names = new ArrayList<>(files.size());
        for(File file: files){
            file_names.add(file.getName());
        }
        return file_names;
    }
}
