package com.example.android.librarian;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by tonynguyen on 11/30/16.
 */

public class BookAdapter extends ArrayAdapter<Book> {

    public BookAdapter(Context context, ArrayList<Book> datas) {
        super(context,0,datas);
    }

    // ViewHolder
    static class ViewHolder{
        TextView bookTitle;
        TextView bookAuthor;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder;

        // Check if layout is being reuse, if not inflate new layout
        View itemListView = convertView;
        if (itemListView == null) {
            itemListView = LayoutInflater.from(getContext()).inflate(R.layout.list_items, parent, false);

            holder = new ViewHolder();
            holder.bookTitle = (TextView) itemListView.findViewById(R.id.book_title_listItem);
            holder.bookAuthor = (TextView) itemListView.findViewById(R.id.book_author_listItem);
            itemListView.setTag(holder);
        } else {
            holder = (ViewHolder) itemListView.getTag();
        }

        // Book position
        Book currentBook = getItem(position);

        // Book image url
        ImageView bookImageUrl = (ImageView) itemListView.findViewById(R.id.book_image);
        Picasso.with(itemListView.getContext()).load(currentBook.getBookImageUrl()).into(bookImageUrl);

        // Set book title to correct view
        holder.bookTitle.setText(currentBook.getBookTitle());

        // Set book author to correct view
        String author = currentBook.getBookAuthor();
        author = author.replace("[", "").replace("]", "").replace("\"", "");
        holder.bookAuthor.setText(author);

        return itemListView;
    }
}
