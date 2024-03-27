package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.ml.modeldownloader.CustomModel;
import com.google.firebase.ml.modeldownloader.CustomModelDownloadConditions;
import com.google.firebase.ml.modeldownloader.DownloadType;
import com.google.firebase.ml.modeldownloader.FirebaseModelDownloader;

import org.tensorflow.lite.Interpreter;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

public class MainActivity extends AppCompatActivity {

    // Define TAG constant for logging
    private static final String TAG = "MainActivity";

    // Declare Interpreter variable outside onCreate method
    private Interpreter interpreter;
    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imageView = findViewById(R.id.imageView);

        // Try to download the custom model
        CustomModelDownloadConditions conditions = new CustomModelDownloadConditions.Builder()
                .requireWifi()  // Also possible: .requireCharging() and .requireDeviceIdle()
                .build();
        FirebaseModelDownloader.getInstance()
                .getModel("best-fp16", DownloadType.LOCAL_MODEL_UPDATE_IN_BACKGROUND, conditions)
                .addOnSuccessListener(new OnSuccessListener<CustomModel>() {
                    @Override
                    public void onSuccess(CustomModel model) {
                        // Download successful, show a success message
                        Toast.makeText(MainActivity.this, "모델 다운로드 성공", Toast.LENGTH_SHORT).show();
                        // The CustomModel object contains the local path of the model file,
                        // which you can use to instantiate a TensorFlow Lite interpreter.
                        File modelFile = model.getFile();
                        if (modelFile != null) {
                            // Initialize the interpreter here instead of inside if condition
                            interpreter = new Interpreter(modelFile);

                            // Move the rest of the code that depends on the interpreter here
                            Bitmap inputBitmap = getYourInputImage();
                            ByteBuffer input = preprocessBitmap(inputBitmap);

                            int bufferSize = 1000 * java.lang.Float.SIZE / java.lang.Byte.SIZE;
                            ByteBuffer modelOutput = ByteBuffer.allocateDirect(bufferSize).order(ByteOrder.nativeOrder());
                            interpreter.run(input, modelOutput);

                            // Postprocess the model output to get the result image
                            Bitmap resultBitmap = postprocessModelOutput(modelOutput, inputBitmap);
                            // Display the result image in ImageView
                            imageView.setImageBitmap(resultBitmap);
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Download failed, show a failure message
                        Toast.makeText(MainActivity.this, "모델 다운로드 실패", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    // Preprocess input bitmap
    // Preprocess input bitmap
    private ByteBuffer preprocessBitmap(Bitmap bitmap) {
        // Resize the input bitmap to 224x224
        bitmap = Bitmap.createScaledBitmap(bitmap, 224, 224, true);

        ByteBuffer input = ByteBuffer.allocateDirect(224 * 224 * 3 * 4).order(ByteOrder.nativeOrder());
        for (int y = 0; y < 224; y++) {
            for (int x = 0; x < 224; x++) {
                int px = bitmap.getPixel(x, y);
                // Get channel values from the pixel value.
                int r = Color.red(px);
                int g = Color.green(px);
                int b = Color.blue(px);
                // Normalize channel values to [-1.0, 1.0].
                float rf = (r - 127) / 255.0f;
                float gf = (g - 127) / 255.0f;
                float bf = (b - 127) / 255.0f;
                input.putFloat(rf);
                input.putFloat(gf);
                input.putFloat(bf);
            }
        }
        return input;
    }


    // Postprocess model output
    private Bitmap postprocessModelOutput(ByteBuffer modelOutput, Bitmap inputBitmap) {
        modelOutput.rewind();
        FloatBuffer probabilities = modelOutput.asFloatBuffer();
        Bitmap resultBitmap = inputBitmap.copy(inputBitmap.getConfig(), true);

        // Draw red border around areas with probability > 0.5
        Canvas canvas = new Canvas(resultBitmap);
        Paint paint = new Paint();
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(Color.RED);
        paint.setStrokeWidth(5);

        for (int y = 0; y < 224; y++) {
            for (int x = 0; x < 224; x++) {
                int index = y * 224 + x;
                float probability = probabilities.get(index);
                if (probability > 0.5) {
                    canvas.drawRect(x, y, x + 1, y + 1, paint);
                }
            }
        }
        return resultBitmap;
    }

    // Load input image from assets


    // Load input image from assets
    private Bitmap getYourInputImage() {
        AssetManager assetManager = getAssets();
        try {
            InputStream inputStream = assetManager.open("image2.png");
            return BitmapFactory.decodeStream(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
