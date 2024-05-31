package com.example.capstone;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class ListActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private WarningLightAdapter adapter;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        EditText searchEditText = findViewById(R.id.search_edit_text);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        List<WarningLight> warningLights = WarningLightManager.getInstance(this).getAllWarningLights();
        adapter = new WarningLightAdapter(this, warningLights, new WarningLightAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(WarningLight warningLight) {
                Intent intent = new Intent(ListActivity.this, DetailActivity2.class);
                intent.putExtra("WarningLight", warningLight);
                startActivity(intent);
            }
        });
        recyclerView.setAdapter(adapter);

        // 검색 기능 연결
        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                adapter.getFilter().filter(s);
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }
}
class WarningLightAdapter extends RecyclerView.Adapter<WarningLightAdapter.ViewHolder> {
    private List<WarningLight> warningLights;
    private LayoutInflater inflater;
    private OnItemClickListener listener;
    private List<WarningLight> warningLightsFiltered;

    interface OnItemClickListener {
        void onItemClick(WarningLight warningLight);
    }

    WarningLightAdapter(Context context, List<WarningLight> warningLights, OnItemClickListener listener) {
        this.warningLights = warningLights;
        this.inflater = LayoutInflater.from(context);
        this.warningLightsFiltered = new ArrayList<>(warningLights);
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(warningLightsFiltered.get(position)); // 필터링된 리스트 사용
    }

    @Override
    public int getItemCount() {
        return warningLightsFiltered.size(); // 필터링된 리스트의 크기 반환
    }
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                List<WarningLight> filteredList = new ArrayList<>();
                if (constraint == null || constraint.length() == 0) {
                    filteredList.addAll(warningLights);
                } else {
                    String filterPattern = constraint.toString().toLowerCase().trim();
                    for (WarningLight item : warningLights) {
                        if (item.getTitle().toLowerCase().contains(filterPattern)) {
                            filteredList.add(item);
                        }
                    }
                }
                FilterResults results = new FilterResults();
                results.values = filteredList;
                return results;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                warningLightsFiltered.clear();
                warningLightsFiltered.addAll((List) results.values);
                notifyDataSetChanged();
            }
        };
    }
    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView textView;

        ViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView);
            textView = itemView.findViewById(R.id.textView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(warningLightsFiltered.get(getAdapterPosition()));
                }
            });
        }

        void bind(WarningLight warningLight) {
            textView.setText(warningLight.getTitle());
            Bitmap imageBitmap = AssetUtils.loadImageFromAssets(itemView.getContext(), warningLight.getImageName());
            imageView.setImageBitmap(imageBitmap);
        }
    }
}