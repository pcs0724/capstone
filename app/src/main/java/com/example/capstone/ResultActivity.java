package com.example.capstone;

import static android.content.ContentValues.TAG;

import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.net.URL;
import java.util.ArrayList;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.ml.common.FirebaseMLException;
import com.google.firebase.ml.custom.FirebaseCustomLocalModel;
import com.google.firebase.ml.custom.FirebaseModelDataType;
import com.google.firebase.ml.custom.FirebaseModelInputOutputOptions;
import com.google.firebase.ml.custom.FirebaseModelInputs;
import com.google.firebase.ml.custom.FirebaseModelInterpreter;
import com.google.firebase.ml.custom.FirebaseModelInterpreterOptions;
import com.google.firebase.ml.custom.FirebaseModelOutputs;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.List;

public class ResultActivity extends AppCompatActivity {//결과창
    RecyclerView list;
    ListAdapter adapter;
    private ImageView imageView;
    private CardView cardView;
    Bitmap mBitmap;
    private FirebaseCustomLocalModel localModel;
    private FirebaseModelInterpreter interpreter;
    FirebaseModelInterpreterOptions options;
    FirebaseModelInputOutputOptions inputOutputOptions;
    WarningLightManager manager;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.result);

        list=findViewById(R.id.list);
        LinearLayoutManager layoutManager=new LinearLayoutManager(getApplicationContext(),LinearLayoutManager.VERTICAL,false);
        list.setLayoutManager(layoutManager);

        adapter=new ListAdapter(this); // "this"는 현재 액티비티를 가리킴
        list.setAdapter(adapter);

        manager = WarningLightManager.getInstance(this);


        localModel = new FirebaseCustomLocalModel.Builder()
                .setAssetFilePath("best_final.tflite")
                .build();

        if (localModel != null) {
            Log.i(TAG, "모델 파일 불러오기 성공");
            try {
                options =
                        new FirebaseModelInterpreterOptions.Builder(localModel).build();
                interpreter = FirebaseModelInterpreter.getInstance(options);
                inputOutputOptions =
                        new FirebaseModelInputOutputOptions.Builder()
                                .setInputFormat(0, FirebaseModelDataType.FLOAT32, new int[]{1, 640, 640, 3})
                                .setOutputFormat(0, FirebaseModelDataType.FLOAT32, new int[]{1, 25200, 32})
                                .build();

            } catch (FirebaseMLException e) {
                Log.i(TAG, "인터프리터 생성 실패");
            }
        } else {
            Log.i(TAG, "모델 파일 불러오기 실패");
        }

        // ImageView 초기화
        imageView = findViewById(R.id.imageView);
        cardView = findViewById(R.id.imageCardView);
        // MainActivity에서 전달된 이미지 URI 가져오기
        Uri imageUri = getIntent().getParcelableExtra("image_uri");

        if (imageUri != null) {
            // URI에서 Bitmap 이미지로 변환하여 ImageView에 설정
            Bitmap bitmap = getBitmapFromUri(imageUri);
            if (bitmap != null) {
                imageView.setImageBitmap(bitmap);
                mBitmap=bitmap;
            }
        }

        ViewTreeObserver viewTreeObserver = imageView.getViewTreeObserver();
        viewTreeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {

//                imageView.setLayoutParams(layoutParams);
//                imageView.getViewTreeObserver().removeOnGlobalLayoutListener(this);

                ViewGroup.LayoutParams imageLayoutParams = imageView.getLayoutParams();
                imageLayoutParams.width = mBitmap.getWidth();
                imageLayoutParams.height = mBitmap.getHeight();

                ViewGroup.LayoutParams cardLayoutParams = cardView.getLayoutParams();
                cardLayoutParams.width = imageLayoutParams.width + 20;  // 10dp padding on each side
                cardView.setLayoutParams(cardLayoutParams);
                imageView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        });

        new ModelExecutionTask().execute();


    }


    private void updateUI(List<Prediction> predictions) {
//        if (predictions.isEmpty()) {
//            // 검출된 경고등이 없을 때 MainActivity로 돌아가기
//            Intent intent = new Intent(ResultActivity.this, MainActivity.class);
//            startActivity(intent);
//            finish(); // 현재 액티비티 종료
//            Log.d(TAG, "검출된 경고등이 없습니다." );
//            return;
//        }

        if(!predictions.isEmpty()){
            for (Prediction prediction : predictions) {
                ClassPrediction classPrediction = prediction.getMax();
                WarningLight light = manager.getWarningLight(getLabelFromAssets(classPrediction.getClassIndex()));

                Log.d(TAG, "WarningLight: " + getLabelFromAssets(classPrediction.getClassIndex()));
                Log.d(TAG, "x: " + prediction.x);
                Log.d(TAG, "y: " + prediction.y);
                Log.d(TAG, "w: " + prediction.w);
                Log.d(TAG, "h: " + prediction.h);

                adapter.addItem(new listItem(light.getImageName(), light.getTitle(), light.getDescription(), light.getDescription2(), light.getDescription3()));
            }
            adapter.notifyDataSetChanged();
        }

    }


    private Bitmap getBitmapFromUri(Uri uri) {
        try {
            // URI를 통해 이미지 가져오기
            return MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public ByteBuffer preprocessBitmap(Bitmap bitmap) {
        int width = 640;
        int height = 640;

        // 이미지 크기 조정
        Bitmap resizedBitmap = Bitmap.createScaledBitmap(bitmap, width, height, true);

        // ByteBuffer를 이용하여 모델 입력 데이터 준비
        ByteBuffer input = ByteBuffer.allocateDirect(4 * width * height * 3).order(ByteOrder.nativeOrder());

        int[] intValues = new int[width * height];
        resizedBitmap.getPixels(intValues, 0, width, 0, 0, width, height);
        for (int value : intValues) {
            // 색상값 정규화 및 ByteBuffer에 저장
            input.putFloat((float) (Color.red(value) - 127) / 255.0f);
            input.putFloat((float) (Color.green(value) - 127) / 255.0f);
            input.putFloat((float) (Color.blue(value) - 127) / 255.0f);
        }
        input.rewind(); // ByteBuffer를 읽기 전에 position을 0으로 리셋
        return input;
    }
    private List<Prediction>  runModel() throws FirebaseMLException {

        mBitmap = Bitmap.createScaledBitmap(mBitmap, 640, 640, true);

        ByteBuffer input = preprocessBitmap(mBitmap);

        FirebaseModelInputs inputs = new FirebaseModelInputs.Builder()
                .add(input)
                .build();

        List<Prediction> predictions = new ArrayList<>();

        interpreter.run(inputs, inputOutputOptions)
                .continueWith(new Continuation<FirebaseModelOutputs, List<Prediction>>() {
                    @Override
                    public List<Prediction> then(@NonNull Task<FirebaseModelOutputs> task) throws Exception {
                        FirebaseModelOutputs result = task.getResult(); // 모델 실행 결과 가져오기

                        float[][][] outputData = result.getOutput(0); // 출력 데이터 가져오기

                        List<Prediction> predictions = new ArrayList<>();

                        for (float[][] array2D : outputData) {
                            for (float[] array1D : array2D) {
                                // 결과 데이터를 예측 객체로 변환하여 목록에 추가
                                Prediction prediction = parsePrediction(array1D);
                                boolean isDuplicate = false;
                                // 중복 여부 확인
                                for (Prediction p : predictions) {
                                    if(predictions.isEmpty()){
                                        break;
                                    }
                                    else if (p.getMax().getClassIndex() == prediction.getMax().getClassIndex()) {
                                        isDuplicate = true;
                                        break;
                                    }
                                }
                                // 중복이 아니고 특정 정확도 이상인 경우에만 저장
                                if (!isDuplicate && (prediction.conf>0.2)) {
                                    predictions.add(prediction);
                                }
                            }
                        }


                        return predictions;
                    }
                })
                .addOnCompleteListener(new OnCompleteListener<List<Prediction>>() {
                    @Override
                    public void onComplete(@NonNull Task<List<Prediction>> task) {
                        if (task.isSuccessful()) {
                            List<Prediction> predictions = task.getResult();
                            updateUI(predictions); // UI 업데이트
                        } else {
                            // 예외 처리
                            Exception e = task.getException();
                            if (e != null) {
                                e.printStackTrace();
                            }
                        }
                    }
                });

        return predictions;
    }
    private Prediction parsePrediction(float[] array1D) {
        // x, y, w, h, conf 값을 추출
        float x = array1D[0];
        float y = array1D[1];
        float w = array1D[2];
        float h = array1D[3];
        float conf = array1D[4];

        // 클래스 및 해당 확률 추출
        List<ClassPrediction> classPredictions = new ArrayList<>();
        for (int i = 5; i < array1D.length; i++) {
            classPredictions.add(new ClassPrediction(i - 5, array1D[i]));
        }

        // Prediction 객체 생성 및 반환
        return new Prediction(x, y, w, h, conf, classPredictions);
    }


    private String getLabelFromAssets(int index) {
        try {
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(getAssets().open("classes_indicators.txt")));
            for (int i = 0; i < index; i++) {
                reader.readLine();
            }
            return reader.readLine();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private class ModelExecutionTask extends AsyncTask<Void, Void, List<Prediction>> {
        @Override
        protected List<Prediction> doInBackground(Void... voids) {
            List<Prediction> predictions = null;
            try {
                predictions = runModel(); // 모델 실행 및 예측 결과 반환
            } catch (FirebaseMLException e) {
                e.printStackTrace();
            }
            return predictions;
        }
        @Override
        protected void onPostExecute(List<Prediction> predictions) {
            super.onPostExecute(predictions);
            // 모델 실행이 완료된 후 UI를 업데이트합니다.
            updateUI(predictions);
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







