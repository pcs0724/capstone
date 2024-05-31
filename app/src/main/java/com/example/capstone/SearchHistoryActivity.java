package com.example.capstone;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class SearchHistoryActivity extends AppCompatActivity {

    SearchHistoryAdapter adapter;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_history);

        // Retrieve search history from intent extras
        ArrayList<WarningLight> searchHistory = (ArrayList<WarningLight>) getIntent().getSerializableExtra("search_history");

        // Set up adapter
        adapter = new SearchHistoryAdapter(this, searchHistory);
    }
}
