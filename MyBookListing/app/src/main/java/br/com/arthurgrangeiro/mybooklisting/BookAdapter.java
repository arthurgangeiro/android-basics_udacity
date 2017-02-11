package br.com.arthurgrangeiro.mybooklisting;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

/**
 * Created by Arthur on 12/01/2017.
 */

public class BookAdapter extends RecyclerView.Adapter {
    private ArrayList<Book> mBooks;
    private Context context;


    public BookAdapter(ArrayList<Book> books) {
        mBooks = books;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        this.context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.book_list_item, parent, false);
        BookViewHolder holder = new BookViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        BookViewHolder bookViewHolder = (BookViewHolder) holder;
        Book book = mBooks.get(position);

        //Get the first book title first letter to use on the user interface like an image
        String image = String.valueOf(book.getmTitle().charAt(0));

        //Set the values to create the each item view
        bookViewHolder.mImageView.setText(image);
        bookViewHolder.currentBook = book;
        bookViewHolder.bookTitle.setText(book.getmTitle());
        bookViewHolder.authorName.setText(book.getAuthorsName());
    }

    @Override
    public int getItemCount() {
        return mBooks.size();
    }
}
