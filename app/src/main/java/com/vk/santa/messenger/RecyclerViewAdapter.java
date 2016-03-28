package com.vk.santa.messenger;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by mixmax on 26.03.16.
 */
public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    public static final int BY_NAME = 0;
    public static final int BY_SIZE = 1;
    public static final int BY_DATE = 2;

    private OnFileClickListener listener;
    private List<File> files;
    private int sortingBy = 0;

    Date date = new Date();
    SimpleDateFormat df = new SimpleDateFormat("dd/mm/yy");

    public RecyclerViewAdapter(List<File> files, int sortBy, OnFileClickListener listener) {
        this.sortingBy = sortBy;
        this.listener = listener;
        this.files = sortFiles(files);


    }

    @Override
    public RecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.filelist_item, parent, false);
        final ViewHolder viewHolder = new ViewHolder(v);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerViewAdapter.ViewHolder holder, int position) {
        File file = files.get(position);
        holder.bind(file, listener);
    }

    @Override
    public int getItemCount() {
        return files.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        public OnFileClickListener listener;
        private TextView file_name;
        private TextView file_size;
        private TextView file_date;
        public ViewHolder(View itemView) {
            super(itemView);
            file_name = (TextView) itemView.findViewById(R.id.filename);
            file_size = (TextView) itemView.findViewById(R.id.filesize);
            file_date = (TextView) itemView.findViewById(R.id.filedate);
        }

        public void bind(final File file, final OnFileClickListener listener) {

            String dateText = df.format(file.lastModified());
            file_name.setText(file.getName());
            float filesize = file.length()/1024;
            file_size.setText(filesize + "kb");
            file_date.setText(dateText);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onFileClick(file);
                }
            });
        }


    }

    private List<File> sortFiles(List<File> files){
        List<File> sortedList = new ArrayList<>(files.size());
        List<File> dir = new ArrayList<>();
        List<File> notDir = new ArrayList<>();

        for(File f: files){
            if(f.isDirectory()){
                dir.add(f);
            }else{
                notDir.add(f);
            }
        }
        //folders always by name
        Collections.sort(dir, new Comparator<File>() {
            @Override
            public int compare(File lhs, File rhs){
                return lhs.getName().toLowerCase().compareTo(rhs.getName().toLowerCase());
            }
        });

        //then sort files
        if(sortingBy == BY_NAME){
        Collections.sort(notDir, new Comparator<File>() {
            @Override
            public int compare(File lhs, File rhs){
                return lhs.getName().toLowerCase().compareTo(rhs.getName().toLowerCase());
            }
        });
    } else if(sortingBy == BY_SIZE){
            Collections.sort(notDir, new Comparator<File>() {
                @Override
                public int compare(File lhs, File rhs) {
                    return Long.compare(lhs.length(), rhs.length());
                }});
        }  else if(sortingBy == BY_DATE){
            Collections.sort(notDir, new Comparator<File>() {
                @Override
                public int compare(File lhs, File rhs) {
                    return Long.compare(lhs.lastModified(), rhs.lastModified());
                }});
        }

        sortedList.addAll(dir);
        sortedList.addAll(notDir);

        return sortedList;
    }


    }



