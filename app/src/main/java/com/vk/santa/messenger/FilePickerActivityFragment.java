package com.vk.santa.messenger;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class FilePickerActivityFragment extends Fragment implements AdapterView.OnItemClickListener{

    List<String> file_names;
    private String path = Environment.getExternalStorageDirectory().toString();
    private ListView files_lv;
    private List<File> files;
    private ArrayAdapter<String> adatper;

    public FilePickerActivityFragment(){}

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

        if(file_names.size()>1){
            adatper = new ArrayAdapter<String>(getActivity(), com.vk.santa.Messenger.R.layout.filelist_item, file_names);
             }else{
            file_names.add("No files");
            file_names.add("this part");
            file_names.add("must be");
            file_names.add("fixed");
            //show text field maybe? otherwise will be FileNotFound
            adatper = new ArrayAdapter<String>(getActivity(), com.vk.santa.Messenger.R.layout.filelist_item, file_names);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    View view = inflater.inflate(com.vk.santa.Messenger.R.layout.fragment_file_picker, container, false);

    files_lv = (ListView) view.findViewById(com.vk.santa.Messenger.R.id.files_listView);
    files_lv.setAdapter(adatper);
    files_lv.setOnItemClickListener(this);
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

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        String file_name = this.file_names.get(position);
        File file = files.get(position);
        if(!file.isDirectory()){
            Intent intent = new Intent();
            intent.putExtra("filePath", file.getAbsolutePath());
            getActivity().setResult(getActivity().RESULT_OK, intent);
            getActivity().finish();
        }else{
            String newFolderPath = file.getAbsolutePath();
            Log.i("File picker", "Path" + newFolderPath);
            FragmentManager fm = getActivity().getSupportFragmentManager();
            Fragment fragment = newInstance(newFolderPath);

            fm.beginTransaction().addToBackStack(null).replace(com.vk.santa.Messenger.R.id.files_frag_placeholder, fragment).commit();


        }

    }
}
