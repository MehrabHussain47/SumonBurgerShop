package com.example.sumonburger;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.sumonburgershop.R;

public class DeleteActivity extends AppCompatActivity {

    private EditText editTextSearchName, editTextName, editTextPrice, editTextQuantity;
    private ImageView imageViewPhoto;
    private Button buttonSearch, buttonDelete;
    private DatabaseHelper dbHelper;
    private String encodedImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete);

        dbHelper = new DatabaseHelper(this);

        // Initializing UI components
        editTextSearchName = findViewById(R.id.editTextSearchName);
        editTextName = findViewById(R.id.editTextName);
        editTextPrice = findViewById(R.id.editTextPrice);
        editTextQuantity = findViewById(R.id.editTextQuantity);
        imageViewPhoto = findViewById(R.id.imageViewPhoto);
        buttonSearch = findViewById(R.id.buttonSearch);
        buttonDelete = findViewById(R.id.buttonDelete);

        buttonSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchProduct();
            }
        });

        buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteProduct();
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
            String name = cursor.getString(cursor.getColumnIndex("NAME"));
            String price = cursor.getString(cursor.getColumnIndex("PRICE"));
            String quantity = cursor.getString(cursor.getColumnIndex("QUANTITY"));
            encodedImage = cursor.getString(cursor.getColumnIndex("PHOTO"));

            editTextName.setText(name);
            editTextPrice.setText(price);
            editTextQuantity.setText(quantity);
            if (encodedImage != null) {
                byte[] bytes = Base64.decode(encodedImage, Base64.DEFAULT);
                Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                imageViewPhoto.setImageBitmap(bitmap);
            }

            editTextName.setEnabled(false);
            editTextPrice.setEnabled(false);
            editTextQuantity.setEnabled(false);
            buttonDelete.setEnabled(true);
        } else {
            Toast.makeText(this, "Product not found", Toast.LENGTH_SHORT).show();
        }
        cursor.close();
    }

    private void deleteProduct() {
        String name = editTextSearchName.getText().toString();
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int result = db.delete(DatabaseHelper.ITEMS_TABLE, "NAME=?", new String[]{name});

        if (result > 0) {
            Toast.makeText(this, "Product deleted successfully", Toast.LENGTH_SHORT).show();
            clearFields();
        } else {
            Toast.makeText(this, "Failed to delete product", Toast.LENGTH_SHORT).show();
        }
    }

    private void clearFields() {
        editTextSearchName.setText("");
        editTextName.setText("");
        editTextPrice.setText("");
        editTextQuantity.setText("");
        imageViewPhoto.setImageBitmap(null);
        buttonDelete.setEnabled(false);
    }
}
