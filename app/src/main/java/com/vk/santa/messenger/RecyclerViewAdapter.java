package com.vk.santa.messenger;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

/**
 * Created by mixmax on 26.03.16.
 */
public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    public static final int BY_NAME = 0;
    public static final int BY_SIZE = 1;
    public static final int BY_DATE = 2;
    private final Context context;
    Date date = new Date();
    SimpleDateFormat df = new SimpleDateFormat("dd/mm/yy");
    private OnFileClickListener listener;
    private List<File> files;
    private int sortingBy = 0;

    public RecyclerViewAdapter(List<File> files, Context c, int sortBy, OnFileClickListener listener) {
        this.sortingBy = sortBy;
        this.listener = listener;
        this.files = sortFiles(files);
        this.context = c;


    }

    public static int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) > reqHeight
                    && (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
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

    private List<File> sortFiles(List<File> files) {
        List<File> sortedList = new ArrayList<>(files.size());
        List<File> dir = new ArrayList<>();
        List<File> notDir = new ArrayList<>();

        for (File f : files) {
            if (f.isDirectory()) {
                dir.add(f);
            } else {
                notDir.add(f);
            }
        }
        //folders always by name
        Collections.sort(dir, new Comparator<File>() {
            @Override
            public int compare(File lhs, File rhs) {
                return lhs.getName().toLowerCase().compareTo(rhs.getName().toLowerCase());
            }
        });

        //then sort files
        if (sortingBy == BY_NAME) {
            Collections.sort(notDir, new Comparator<File>() {
                @Override
                public int compare(File lhs, File rhs) {
                    return lhs.getName().toLowerCase().compareTo(rhs.getName().toLowerCase());
                }
            });
        } else if (sortingBy == BY_SIZE) {
            Collections.sort(notDir, new Comparator<File>() {
                @Override
                public int compare(File lhs, File rhs) {
                    return Long.compare(lhs.length(), rhs.length());
                }
            });
        } else if (sortingBy == BY_DATE) {
            Collections.sort(notDir, new Comparator<File>() {
                @Override
                public int compare(File lhs, File rhs) {
                    return Long.compare(lhs.lastModified(), rhs.lastModified());
                }
            });
        }

        sortedList.addAll(dir);
        sortedList.addAll(notDir);

        return sortedList;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private final String FOLDER = "FOLER";
        private final String SYSTEM = "SYSTEM";
        private final String IMAGE = "IMAGE";
        private final String FILE = "FILE";
        public OnFileClickListener listener;
        private TextView file_name;
        private TextView file_size;
        private TextView file_date;
        private ImageView preview;

        public ViewHolder(View itemView) {
            super(itemView);
            file_name = (TextView) itemView.findViewById(R.id.filename);
            file_size = (TextView) itemView.findViewById(R.id.filesize);
            file_date = (TextView) itemView.findViewById(R.id.filedate);
            preview = (ImageView) itemView.findViewById(R.id.file_preview_iv);
        }

        public void bind(final File file, final OnFileClickListener listener) {
            String dateText = df.format(file.lastModified());


            file_name.setText(getCleanName(file));

            file_size.setText(getFileSize(file));
            file_date.setText(dateText);
            preview.setImageDrawable(getIcon(file));


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onFileClick(file);
                }
            });


        }

        private Drawable getIcon(File file) {
            String file_type = getType(file);
            if (file_type.equals(FOLDER)) {
                return ResourcesCompat.getDrawable(context.getResources(), R.drawable.folder_icon, context.getTheme());
            } else if (file_type.equals(SYSTEM)) {
                return ResourcesCompat.getDrawable(context.getResources(), R.drawable.attention_icon, context.getTheme());
            } else if (file_type.equals(IMAGE)) {
                return getImageIcon(file, preview);
            } else {

                TextDrawable drawable = TextDrawable.builder()
                        .beginConfig()
                        .bold()
                        .withBorder(4) /* thickness in px */
                        .endConfig()
                        .buildRound(file_type, Color.BLUE);
                return drawable;
            }
        }


        private String getType(File file) {
            String type;
            boolean isDir = file.isDirectory();

            if (isDir) {
                return FOLDER;
            } else if (file.isHidden()) {
                return SYSTEM;
            } else {
                String fileName = file.getName();
                if (fileName.lastIndexOf(".") == 0) {
                    return SYSTEM;
                } else {
                    type = fileName.substring(fileName.lastIndexOf(".") + 1, fileName.length());
                    if (type.equals("png") || type.equals("jpg") || type.equals("gif")) {
                        return IMAGE;
                    }
                    return type;
                }

            }
        }

        private String getCleanName(File file) {
            if (file.isDirectory() || file.getName().charAt(0) == '.') return file.getName();
            return file.getName().substring(0, file.getName().lastIndexOf("."));
        }

        private String getFileSize(File f) {
            float filesize = f.length() / 1024;
            if (filesize > 999) return filesize / 1024 + " mb";
            return filesize + " kb";
        }

        private BitmapDrawable getImageIcon(File file, ImageView imageView) {
            int reqWidth = imageView.getWidth();
            int reqHeight = imageView.getHeight();
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(file.getAbsolutePath(), options);
            options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);


            options.inJustDecodeBounds = false;
            return new BitmapDrawable(context.getResources(), BitmapFactory.decodeFile(file.getAbsolutePath(), options));

        }
    }


    }



