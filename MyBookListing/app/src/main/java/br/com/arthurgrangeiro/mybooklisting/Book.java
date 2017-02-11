package br.com.arthurgrangeiro.mybooklisting;

import java.util.ArrayList;

/**
 * Created by Arthur on 12/01/2017.
 */

public class Book {
    private String mInfoLink;
    private String mTitle;
    private ArrayList mAuthors = new ArrayList();

    public Book(String mInfoLink, String mTitle, ArrayList mAuthors) {
        this.mInfoLink = mInfoLink;
        this.mTitle = mTitle;
        this.mAuthors = mAuthors;
    }

    public String getmInfoLink() {
        return mInfoLink;
    }

    public String getmTitle() {
        return mTitle;
    }

    /**
     * Reads the mAuthors ArrayList and put all names inside a String
     *
     * @return
     */
    public String getAuthorsName() {
        StringBuilder authorsBuilder = new StringBuilder();
        for (int i = 0; i < mAuthors.size(); i++) {
            authorsBuilder.append(mAuthors.get(i));
            if (i < (mAuthors.size() - 1)) {
                authorsBuilder.append(", ");
            }
        }
        String authors = String.valueOf(authorsBuilder);
        return authors;
    }

    @Override
    public String toString() {
        return "Book{" +
                ", mTitle='" + mTitle + '\'' +
                ", mAuthors=" + mAuthors +
                ", mInfoLink='" + mInfoLink + '\'' +
                '}';
    }
}
