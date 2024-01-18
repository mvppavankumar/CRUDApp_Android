package com.example.android_curd_native;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import java.util.HashMap;
import java.util.Map;

public class AddBook extends AppCompatActivity {
    private EditText editTextBookName, editTextAuthorName, editTextEmail, editTextAddedBy;
    private String ipAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_book);

        editTextBookName = findViewById(R.id.editTextBookName);
        editTextAuthorName = findViewById(R.id.editTextAuthorName);
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextAddedBy = findViewById(R.id.editTextAddedBy);
        Button cancelButton = findViewById(R.id.cancelButton); // Replace with your actual button ID
        ipAddress = getResources().getString(R.string.ipaddress);

        Button addButton = findViewById(R.id.addButton);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addBook();
            }
        });
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void addBook() {
        String bookName = editTextBookName.getText().toString();
        String authorName = editTextAuthorName.getText().toString();
        String email = editTextEmail.getText().toString();
        String addedBy = editTextAddedBy.getText().toString();


        // Validate inputs if needed
        Map<String, Object> params = new HashMap<>();
        params.put("bookName", bookName);
        params.put("bookAuthor", authorName);
        params.put("email", email);
        params.put("addedBy", addedBy);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://" + ipAddress + ":3301/") // Replace with your server URL
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiService apiService = retrofit.create(ApiService.class);

        Call<JSONObject> call = apiService.addBook(params);
        call.enqueue(new Callback<JSONObject>() {
            @Override
            public void onResponse(Call<JSONObject> call, Response<JSONObject> response) {
                if (response.isSuccessful() && response.body() != null) {
                    try {
                        // Retrieve the raw response string
                        String rawResponse = response.body().toString();

                        // Create a JSONObject from the raw response string
                        JSONObject jsonObject = new JSONObject(rawResponse);

                        // Extract the "message" or "success" key based on your server response
                        String message = jsonObject.getString("message");
                        // Or, if using "success" key
                        // boolean success = jsonObject.getBoolean("success");

                        if ("success".equals(message)) {
                            Toast.makeText(AddBook.this, "Book added successfully", Toast.LENGTH_SHORT).show();
                            setResult(RESULT_OK);
                            finish(); // Close the EditBook activity after a successful update
                        } else {
                            Toast.makeText(AddBook.this, "Failed to add book", Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.e("JSONParsingError", "Error parsing JSON response");
                        Toast.makeText(AddBook.this, "Book added successfully", Toast.LENGTH_SHORT).show();
                        setResult(RESULT_OK);
                        finish(); // Close the EditBook activity after a successful update
                    }
                } else {
                    // Handle errors
                    Log.e("ServerError", "Error in server response");
                    Toast.makeText(AddBook.this, "Error in server response", Toast.LENGTH_SHORT).show();
                }
            }



            @Override
            public void onFailure(Call<JSONObject> call, Throwable t) {
                Log.e("NetworkError", "Network error", t);
                Toast.makeText(AddBook.this, "Network error", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
