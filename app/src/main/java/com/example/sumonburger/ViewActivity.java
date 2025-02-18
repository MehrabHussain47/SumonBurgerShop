package com.example.sumonburger;

import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.sumonburgershop.R;

public class ViewActivity extends AppCompatActivity {

    private LinearLayout linearLayoutProducts;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view);

        dbHelper = new DatabaseHelper(this);
        linearLayoutProducts = findViewById(R.id.linearLayoutProducts);

        displayProducts();
    }

    private void displayProducts() {
        Cursor cursor = dbHelper.getAllProducts();

        if (cursor != null && cursor.moveToFirst()) {
            do {
                String name = cursor.getString(cursor.getColumnIndexOrThrow("NAME"));
                String price = cursor.getString(cursor.getColumnIndexOrThrow("PRICE"));
                final int quantity = cursor.getInt(cursor.getColumnIndexOrThrow("QUANTITY"));
                String encodedImage = cursor.getString(cursor.getColumnIndexOrThrow("PHOTO"));

                View productCard = LayoutInflater.from(this).inflate(R.layout.product_card, linearLayoutProducts, false);

                ImageView imageViewPhoto = productCard.findViewById(R.id.imageViewPhoto);
                TextView textViewName = productCard.findViewById(R.id.textViewName);
                TextView textViewPrice = productCard.findViewById(R.id.textViewPrice);
                TextView textViewQuantity = productCard.findViewById(R.id.textViewQuantity);
                Button buttonOrder = productCard.findViewById(R.id.buttonOrder);

                textViewName.setText(name);
                textViewPrice.setText(price);
                textViewQuantity.setText("Quantity: " + quantity);
                if (encodedImage != null) {
                    byte[] bytes = Base64.decode(encodedImage, Base64.DEFAULT);
                    Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                    imageViewPhoto.setImageBitmap(bitmap);
                }

                buttonOrder.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (quantity > 0) {
                            Toast.makeText(ViewActivity.this, "You can order now.", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(ViewActivity.this, "You can't order now. Product is unavailable.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                linearLayoutProducts.addView(productCard);

            } while (cursor.moveToNext());
        } else {
            Toast.makeText(this, "No products found", Toast.LENGTH_SHORT).show();
        }
        if (cursor != null) {
            cursor.close();
        }
    }
}
