package com.vk.santa.messenger;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.io.File;
import java.util.List;

/**
 * Created by mixmax on 26.03.16.
 */
public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    OnItemClickListener listener;
    private List<File> files;

    public RecyclerViewAdapter(List<File> files, OnItemClickListener listener) {
        this.files = files;
        this.listener = listener;
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
        holder.bind(file, listener, position);
    }

    @Override
    public int getItemCount() {
        return files.size();
    }

    public interface OnItemClickListener {
        void onItemClick(File item, int position);
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        public FileItemClickListener listener;
        private TextView file_name;

        public ViewHolder(View itemView) {
            super(itemView);
            file_name = (TextView) itemView.findViewById(R.id.filename);
        }

        public void bind(final File file, final OnItemClickListener listener, final int position) {
            file_name.setText(file.getName());
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(file, position);
                }
            });
        }


    }


}


