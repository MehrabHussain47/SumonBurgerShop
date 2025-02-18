package com.example.sumonburger;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import com.example.sumonburgershop.R;

public class UpdateActivity extends AppCompatActivity {

    private EditText editTextSearchName, editTextName, editTextPrice, editTextQuantity;
    private ImageView imageViewPhoto;
    private Button buttonSearch, buttonUpdate, buttonSelectPhoto;
    private DatabaseHelper dbHelper;
    private String encodedImage;
    private static final int PICK_IMAGE_REQUEST = 1;
    private String originalName;  // To store the original name

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);

        dbHelper = new DatabaseHelper(this);

        // Initializing UI components
        editTextSearchName = findViewById(R.id.editTextSearchName);
        editTextName = findViewById(R.id.editTextName);
        editTextPrice = findViewById(R.id.editTextPrice);
        editTextQuantity = findViewById(R.id.editTextQuantity);
        imageViewPhoto = findViewById(R.id.imageViewPhoto);
        buttonSearch = findViewById(R.id.buttonSearch);
        buttonUpdate = findViewById(R.id.buttonUpdate);
        buttonSelectPhoto = findViewById(R.id.buttonSelectPhoto);

        buttonSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchProduct();
            }
        });

        buttonUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateProduct();
            }
        });

        buttonSelectPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectPhoto();
            }
        });
    }

    private void searchProduct() {
        String searchName = editTextSearchName.getText().toString();
        if (searchName.isEmpty()) {
            Toast.makeText(this, "Please enter a name to search", Toast.LENGTH_SHORT).show();
            return;
        }

        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + DatabaseHelper.ITEMS_TABLE + " WHERE NAME=?", new String[]{searchName});

        if (cursor.moveToFirst()) {
            originalName = cursor.getString(cursor.getColumnIndexOrThrow("NAME"));
            String name = cursor.getString(cursor.getColumnIndexOrThrow("NAME"));
            String price = cursor.getString(cursor.getColumnIndexOrThrow("PRICE"));
            String quantity = cursor.getString(cursor.getColumnIndexOrThrow("QUANTITY"));
            encodedImage = cursor.getString(cursor.getColumnIndexOrThrow("PHOTO"));

            editTextName.setText(name);
            editTextPrice.setText(price);
            editTextQuantity.setText(quantity);
            if (encodedImage != null) {
                byte[] bytes = Base64.decode(encodedImage, Base64.DEFAULT);
                Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                imageViewPhoto.setImageBitmap(bitmap);
            }
        } else {
            Toast.makeText(this, "Product not found", Toast.LENGTH_SHORT).show();
        }
        cursor.close();
    }

    private void updateProduct() {
        String name = editTextName.getText().toString();
        String price = editTextPrice.getText().toString();
        String quantity = editTextQuantity.getText().toString();

        if (name.isEmpty() || price.isEmpty() || quantity.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(DatabaseHelper.ITEMS_COL_2, name);
        contentValues.put(DatabaseHelper.ITEMS_COL_3, price);
        contentValues.put(DatabaseHelper.ITEMS_COL_4, quantity);
        if (encodedImage != null) {
            contentValues.put(DatabaseHelper.ITEMS_COL_5, encodedImage);
        }

        int result = db.update(DatabaseHelper.ITEMS_TABLE, contentValues, "NAME=?", new String[]{originalName});
        if (result > 0) {
            Toast.makeText(this, "Product updated successfully", Toast.LENGTH_SHORT).show();
            clearFields();
        } else {
            Toast.makeText(this, "Failed to update product", Toast.LENGTH_SHORT).show();
        }
    }

    private void selectPhoto() {
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

                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                byte[] imageBytes = baos.toByteArray();
                encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void clearFields() {
        editTextSearchName.setText("");
        editTextName.setText("");
        editTextPrice.setText("");
        editTextQuantity.setText("");
        imageViewPhoto.setImageBitmap(null);
    }
}
