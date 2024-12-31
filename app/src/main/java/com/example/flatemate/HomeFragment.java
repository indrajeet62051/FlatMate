package com.example.flatemate;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.text.TextRecognition;
import com.google.mlkit.vision.text.TextRecognizer;
import com.google.mlkit.vision.text.latin.TextRecognizerOptions;

import java.io.IOException;

public class HomeFragment extends Fragment {

    private static final int CAMERA_REQUEST_CODE = 1000;  // Camera request code
    private TextView resultTextView;
    private ImageView imageView;
    private ImageView scanimg;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        scanimg = view.findViewById(R.id.scanimg);
        resultTextView = view.findViewById(R.id.resultTextView);
        imageView = view.findViewById(R.id.imageView);

        scanimg.setOnClickListener(v -> {
            // Check for camera permission before launching the camera
            if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CAMERA}, 1);
            } else {
                openCamera();
            }
        });

        return view;
    }

    // Handle permission request result
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openCamera();
            } else {
                Toast.makeText(getContext(), "Camera permission is required", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void openCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
            startActivityForResult(intent, CAMERA_REQUEST_CODE);
        } else {
            Toast.makeText(getContext(), "No camera app available", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CAMERA_REQUEST_CODE && resultCode == getActivity().RESULT_OK) {
            if (data != null) {
                Bitmap bitmap = (Bitmap) data.getExtras().get("data");
                if (bitmap != null) {
                    imageView.setImageBitmap(bitmap);
                    InputImage image = InputImage.fromBitmap(bitmap, 0);
                    recognizeText(image);
                }
            }
        }
    }

    private void recognizeText(InputImage image) {
        TextRecognizer recognizer = TextRecognition.getClient(new TextRecognizerOptions.Builder().build());
        recognizer.process(image)
                .addOnSuccessListener(result -> {
                    String recognizedText = result.getText();
                    resultTextView.setText(recognizedText);
                    Log.d("TextRecognition", "Recognized text: " + recognizedText);
                })
                .addOnFailureListener(e -> {
                    resultTextView.setText("Error recognizing text.");
                    Log.e("TextRecognition", "Error: " + e.getMessage());
                });
    }
}
