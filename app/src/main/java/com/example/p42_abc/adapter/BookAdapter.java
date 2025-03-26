package com.example.p42_abc.adapter;

import android.annotation.SuppressLint;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.p42_abc.model.Author;
import com.example.p42_abc.model.Book;

import java.util.List;

public class BookAdapter extends RecyclerView.Adapter<BookAdapter.BookViewHolder> {
    private List<Book> _bookList;

    public BookAdapter(List<Book> bookList) {
        _bookList = bookList;
    }

    @NonNull
    @Override
    public BookViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LinearLayout linearLayout = new LinearLayout(parent.getContext());
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        linearLayout.setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        TextView authorName = new TextView(parent.getContext());
        authorName.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        authorName.setTextSize(16);
        authorName.setPadding(16, 16, 16, 16);

        linearLayout.addView(authorName);

        return new BookViewHolder(linearLayout, authorName);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull BookAdapter.BookViewHolder holder, int position) {
        Book author = _bookList.get(position);
        holder._authorName.setText(author.getTitle() + ": " + author.getDescription());

    }

    @Override
    public int getItemCount() {
        return _bookList.size();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void updateList(List<Book> books) {
        _bookList = books;
        notifyDataSetChanged();
    }

    public static class BookViewHolder extends RecyclerView.ViewHolder {
        TextView _authorName;

        public BookViewHolder(View itemView, TextView authorName) {
            super(itemView);
            _authorName = authorName;
        }
    }
}
