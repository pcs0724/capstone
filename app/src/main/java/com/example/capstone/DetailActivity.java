package com.example.capstone;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

@SuppressLint("MissingInflatedId")
public class DetailActivity extends AppCompatActivity {//결과 창-> 세부 내용 보기 창

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail);

        // 상세 정보를 표시할 뷰들을 찾음
        ImageView albumImageView = findViewById(R.id.album_imageView);
        TextView titleTextView = findViewById(R.id.title_textView);
        TextView descriptionView = findViewById(R.id.description_textView);
        TextView descriptionView2 = findViewById(R.id.description_textView2);
        TextView descriptionView3 = findViewById(R.id.description_textView3);


        // 인텐트에서 아이템 데이터 가져오기
        Intent intent = getIntent();
        listItem clickedItem = intent.getParcelableExtra("clickedItem");

        if (intent != null) {
            String imageName = clickedItem.imageName;
            String title = clickedItem.title;
            String description = clickedItem.description;
            String description2 = clickedItem.description2;
            String description3 = clickedItem.description3;

            // 가져온 데이터를 활용하여 UI 업데이트 등의 작업 수행
            albumImageView.setImageBitmap(loadImageFromAssets(imageName));
            titleTextView.setText(title);
            descriptionView.setText(description);
            descriptionView2.setText(description2);
            descriptionView3.setText(description3);

        }
    }

    private Bitmap loadImageFromAssets(String fileName) {
        AssetManager assetManager = getAssets();
        InputStream inputStream = null;
        try {
            inputStream = assetManager.open(fileName);
            return BitmapFactory.decodeStream(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
