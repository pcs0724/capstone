package com.example.capstone;


import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class ListAdapter extends RecyclerView.Adapter<ListAdapter.ViewHolder> {//결과창 어뎁터
    private Context mContext;
    private ArrayList<listItem> items = new ArrayList<>();
    private OnItemClickListener mListener;

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

    public ListAdapter(Context context) {
        this.mContext = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View itemView = inflater.inflate(R.layout.recyclerview_list, parent, false);
        return new ViewHolder(itemView, mContext);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final listItem item = items.get(position);
        try {
            holder.setItem(item);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position = holder.getAdapterPosition(); // 변경된 부분
                if (position != RecyclerView.NO_POSITION && mListener != null) {
                    mListener.onItemClick(position);
                }
                Intent intent = new Intent(mContext, DetailActivity.class);
                intent.putExtra("clickedItem", item);
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void addItem(listItem item) {
        items.add(item);
    }

    public listItem getItem(int position) {
        return items.get(position);
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView album_iv;
        TextView title;
        TextView description_iv;

        public ViewHolder(@NonNull View itemView, Context context) {
            super(itemView);
            album_iv = itemView.findViewById(R.id.album_iv);
            title = itemView.findViewById(R.id.title_tv);
            //description_iv = itemView.findViewById(R.id.description_tv);
        }

        public void setItem(listItem item) throws IOException {
            InputStream is = itemView.getContext().getAssets().open(item.imageName);
            Drawable drawable = Drawable.createFromStream(is, null);
            album_iv.setImageDrawable(drawable);
            title.setText(item.title);
            //description_iv.setText(item.description);
        }
    }

}

