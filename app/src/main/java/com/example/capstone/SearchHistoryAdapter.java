package com.example.capstone;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.capstone.WarningLight;

import java.util.ArrayList;

public class SearchHistoryAdapter extends RecyclerView.Adapter<SearchHistoryAdapter.ViewHolder> {
    private Context mContext;
    private ArrayList<WarningLight> mSearchHistory;

    public SearchHistoryAdapter(Context context, ArrayList<WarningLight> searchHistory) {
        mContext = context;
        mSearchHistory = searchHistory;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.activity_search_history, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        WarningLight warningLight = mSearchHistory.get(position);
        holder.titleTextView.setText(warningLight.getTitle());
        holder.descriptionTextView.setText(warningLight.getDescription());

    }

    @Override
    public int getItemCount() {
        return mSearchHistory.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView albumImageView;
        TextView titleTextView;
        TextView descriptionTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            albumImageView = itemView.findViewById(R.id.album_iv2);
            titleTextView = itemView.findViewById(R.id.title_tv2);
            descriptionTextView = itemView.findViewById(R.id.description_tv2);
        }
    }
}
