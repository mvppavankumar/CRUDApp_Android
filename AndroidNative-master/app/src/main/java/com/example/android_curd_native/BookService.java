package com.example.android_curd_native;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface BookService {
    @GET("books")
    Call<List<Book>> getBooks();

    @GET("books/{id}")
    Call<Book> getBookById(@Path("id") String bookId);


    @PUT("books/{id}")
    Call<Void> updateBook(
            @Path("id") String bookId,
            @Body Book updatedBook
    );
    @DELETE("books/{id}")
    Call<Void> deleteBook(@Path("id") String bookId);
}
