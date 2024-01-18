package com.example.android_curd_native;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class EditBook extends AppCompatActivity {

    private Button updateButton;
    private TextInputEditText editTextBookName, editTextAuthorName, editTextEmail, editTextAddedBy;

    private String bookId; // Add a variable to store the book ID
    private String ipAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_book);

        updateButton = findViewById(R.id.addButton);
        editTextBookName = findViewById(R.id.editTextBookName);
        editTextAuthorName = findViewById(R.id.editTextAuthorName);
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextAddedBy = findViewById(R.id.editTextAddedBy);
        Button cancelButton = findViewById(R.id.cancelButton); // Replace with your actual button ID
        ipAddress = getResources().getString(R.string.ipaddress);

        // Get the book ID from the Intent
        bookId = String.valueOf(getIntent().getIntExtra("BOOK_ID", 0));

        // Fetch book details from the Node.js backend based on the book ID
        fetchBookDetails();

        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Extract values from the input fields
                String bookName = editTextBookName.getText().toString();
                String authorName = editTextAuthorName.getText().toString();
                String email = editTextEmail.getText().toString();
                String addedBy = editTextAddedBy.getText().toString();

                // Make Retrofit API call to update the book
                updateBook(bookName, authorName, email, addedBy);
            }
        });
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void fetchBookDetails() {
        // Implement Retrofit API call to fetch book details from the Node.js backend
        // Use the Retrofit instance you've created in your MainActivity

        // Sample Retrofit instance creation (replace with your actual implementation)
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://" + ipAddress + ":3301/") // Replace with your server URL
                .addConverterFactory(GsonConverterFactory.create())
                .build();


        // Sample API service interface (replace with your actual API service)
        BookService bookService = retrofit.create(BookService.class);

        // Sample API call (replace with your actual API call)
        Call<Book> call = bookService.getBookById(bookId);

        call.enqueue(new Callback<Book>() {
            @Override
            public void onResponse(Call<Book> call, Response<Book> response) {
                if (response.isSuccessful() && response.body() != null) {
                    // Populate the TextInputEditText fields with retrieved values
                    Book book = response.body();
                    if (book != null) {
                        editTextBookName.setText(book.getName());
                        editTextAuthorName.setText(book.getAuthor());
                        editTextEmail.setText(book.getEmail());
                        editTextAddedBy.setText(book.getAddedBy());
                    }
                } else {
                    // Handle unsuccessful response
                    Log.e("EditBook", "Failed to fetch book details. Response code: " + response.code());
                    try {
                        Log.e("EditBook", "Error body: " + response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Toast.makeText(EditBook.this, "Failed to fetch book details", Toast.LENGTH_SHORT).show();
                }
            }



            @Override
            public void onFailure(Call<Book> call, Throwable t) {
                // Print the exception
                t.printStackTrace();
                Toast.makeText(EditBook.this, "Network error", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateBook(String bookName, String authorName, String email, String addedBy) {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://" + ipAddress + ":3301/") // Replace with your server URL
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        BookService bookService = retrofit.create(BookService.class);
        Call<Void> call = bookService.updateBook(bookId, new Book(0, bookName, authorName, email, addedBy));
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    // Handle successful response (book updated)
                    Toast.makeText(EditBook.this, "Book updated successfully", Toast.LENGTH_SHORT).show();
                    setResult(RESULT_OK);
                    finish(); // Close the EditBook activity after a successful update
                } else {
                    // Handle unsuccessful response
                    Toast.makeText(EditBook.this, "Failed to update book", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                // Handle failure (network error, etc.)
                Toast.makeText(EditBook.this, "Network error", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
