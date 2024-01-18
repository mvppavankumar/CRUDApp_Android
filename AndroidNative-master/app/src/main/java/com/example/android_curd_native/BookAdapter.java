package com.example.android_curd_native;

import android.app.AlertDialog;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import android.content.Context;

public class BookAdapter extends RecyclerView.Adapter<BookAdapter.ViewHolder> {

    private List<Book> bookList;
    private int editBookRequestCode;
    private Context context; // Add this variable
    public BookAdapter(List<Book> bookList, int editBookRequestCode, Context context) {
        this.bookList = bookList;
        this.editBookRequestCode = editBookRequestCode;
        this.context = context; // Initialize the context
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.book_list_item, parent, false);
        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Book book = bookList.get(position);

        // Set book title
        holder.bookTitleTextView.setText(book.getName());
        holder.bookAuthorTextView.setText(book.getAuthor());
        // Set click listeners for edit and delete buttons
        holder.editButton.setOnClickListener(v -> {
            int bookId = book.getId();

            Intent intent = new Intent(v.getContext(), EditBook.class);
            intent.putExtra("BOOK_ID", bookId);

            // Start EditBook activity with startActivityForResult
            ((MainActivity) v.getContext()).startActivityForResult(intent, editBookRequestCode);
        });


        holder.deleteButton.setOnClickListener(v -> {
            // Show a confirmation dialog
            AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
            builder.setMessage("Are you sure you want to delete this book?");
            builder.setPositiveButton("Yes", (dialog, which) -> {
                // User confirmed the deletion, send delete request
                int bookId = book.getId();  // Assuming Book class has a getId() method
                deleteBook(bookId, holder.getAdapterPosition());
            });
            builder.setNegativeButton("No", (dialog, which) -> {
                // User canceled the deletion, do nothing
            });
            builder.show();
        });

    }

    @Override
    public int getItemCount() {
        return bookList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView bookTitleTextView;
        public TextView bookAuthorTextView;
//        public Button editButton;
//        public Button deleteButton;
public ImageButton editButton;
        public ImageButton deleteButton;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            bookTitleTextView = itemView.findViewById(R.id.bookTitleTextView);
            bookAuthorTextView = itemView.findViewById(R.id.bookAuthorTextView);
            editButton = itemView.findViewById(R.id.editButton);
            deleteButton = itemView.findViewById(R.id.deleteButton);
        }
    }
    private void deleteBook(int bookId, int adapterPosition) {
        // Use the 'context' variable here
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://192.168.100.122:3301/") // Replace with your server URL
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        BookService bookService = retrofit.create(BookService.class);

        Call<Void> call = bookService.deleteBook(String.valueOf(bookId));

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    // Deletion successful, update UI
                    bookList.remove(adapterPosition);
                    notifyItemRemoved(adapterPosition);
                    Toast.makeText(context, "Book deleted successfully", Toast.LENGTH_SHORT).show();
                } else {
                    // Handle unsuccessful response
                    Toast.makeText(context, "Failed to delete book", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                // Handle failure (network error, etc.)
                Toast.makeText(context, "Network error", Toast.LENGTH_SHORT).show();
            }
        });
    }

}

