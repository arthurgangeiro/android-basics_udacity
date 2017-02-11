package br.com.arthurgrangeiro.mybooklisting;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

/**
 * Created by Arthur on 12/01/2017.
 */

public class BookViewHolder extends RecyclerView.ViewHolder {
    final TextView mImageView;
    final TextView bookTitle;
    final TextView authorName;
    public Book currentBook;

    public BookViewHolder(final View itemView) {
        super(itemView);
        mImageView = (TextView) itemView.findViewById(R.id.book_image);
        bookTitle = (TextView) itemView.findViewById(R.id.book_title);
        authorName = (TextView) itemView.findViewById(R.id.book_author);

        //Set an OnClickListener with start a broweser intent using the Book attribute mInfoLink
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mURL = currentBook.getmInfoLink();
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(mURL));
                itemView.getContext().startActivity(browserIntent);
            }
        });
    }
}
