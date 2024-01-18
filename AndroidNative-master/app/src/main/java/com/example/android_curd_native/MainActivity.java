package com.example.android_curd_native;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
public class MainActivity extends AppCompatActivity {
    private static final int EDIT_BOOK_REQUEST_CODE = 1;

    private List<Book> bookList;
    private BookAdapter bookAdapter;
    private String ipAddress;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_book);

        Button buttonAddBook = findViewById(R.id.buttonAddBook);
        // In a Java file
         ipAddress = getResources().getString(R.string.ipaddress);


        buttonAddBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, AddBook.class);
                startActivityForResult(intent, EDIT_BOOK_REQUEST_CODE);            }
        });

        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        bookList = new ArrayList<>();
        bookAdapter = new BookAdapter(bookList, EDIT_BOOK_REQUEST_CODE, MainActivity.this);
        recyclerView.setAdapter(bookAdapter);
        fetchBooks();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == EDIT_BOOK_REQUEST_CODE && resultCode == RESULT_OK) {
            fetchBooks();
        }
    }
    private void fetchBooks() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://" + ipAddress + ":3301/") // Replace with your server URL
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        BookService bookService = retrofit.create(BookService.class);

        Call<List<Book>> call = bookService.getBooks();

        call.enqueue(new Callback<List<Book>>() {
            @Override
            public void onResponse(Call<List<Book>> call, Response<List<Book>> response) {
                if (response.isSuccessful()) {
                    List<Book> receivedBooks = response.body();
                    Log.d("MainActivity", "Received Books: " + receivedBooks);

                    // Update bookList and notify the adapter
                    bookList.clear();
                    bookList.addAll(receivedBooks);
                    bookAdapter.notifyDataSetChanged();
                } else {
                    // Handle error
                    Log.e("MainActivity", "Error fetching books. Code: " + response.code());
                    Toast.makeText(MainActivity.this, "Error fetching books", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<List<Book>> call, Throwable t) {
                // Handle failure
                Log.e("MainActivity", "Failed to fetch books", t);
                Toast.makeText(MainActivity.this, "Failed to fetch books", Toast.LENGTH_SHORT).show();
            }
        });

    }
}