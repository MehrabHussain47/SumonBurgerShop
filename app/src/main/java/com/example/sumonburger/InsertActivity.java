package com.example.sumonburger;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.sumonburgershop.R;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class InsertActivity extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;
    private EditText editTextName, editTextPrice, editTextQuantity;
    private ImageView imageViewPhoto;
    private Button buttonSelectPhoto, buttonInsertData;
    private String encodedImage;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert);

        dbHelper = new DatabaseHelper(this);

        // Initializing UI components
        editTextName = findViewById(R.id.editTextName);
        editTextPrice = findViewById(R.id.editTextPrice);
        editTextQuantity = findViewById(R.id.editTextQuantity);
        imageViewPhoto = findViewById(R.id.imageViewPhoto);
        buttonSelectPhoto = findViewById(R.id.buttonSelectPhoto);
        buttonInsertData = findViewById(R.id.buttonInsertData);

        buttonSelectPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openImageChooser();
            }
        });

        buttonInsertData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                insertData();
            }
        });
    }

    private void openImageChooser() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri imageUri = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
                imageViewPhoto.setImageBitmap(bitmap);
                encodeBitmapImage(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void encodeBitmapImage(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        byte[] bytes = byteArrayOutputStream.toByteArray();
        encodedImage = Base64.encodeToString(bytes, Base64.DEFAULT);
    }

    private void insertData() {
        String name = editTextName.getText().toString();
        String price = editTextPrice.getText().toString();
        String quantity = editTextQuantity.getText().toString();

        if (name.isEmpty() || price.isEmpty() || quantity.isEmpty() || encodedImage == null) {
            Toast.makeText(this, "Please fill all fields and select an image", Toast.LENGTH_SHORT).show();
            return;
        }

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("NAME", name);
        contentValues.put("PRICE", price);
        contentValues.put("QUANTITY", quantity);
        contentValues.put("PHOTO", encodedImage);

        long result = db.insert("ITEMS_TABLE", null, contentValues);
        if (result == -1) {
            Toast.makeText(this, "Failed to insert data", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Data inserted successfully", Toast.LENGTH_SHORT).show();
            clearFields();
        }
    }

    private void clearFields() {
        editTextName.setText("");
        editTextPrice.setText("");
        editTextQuantity.setText("");
        imageViewPhoto.setImageBitmap(null);
    }
}
