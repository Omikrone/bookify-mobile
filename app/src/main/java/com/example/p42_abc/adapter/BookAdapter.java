package com.example.p42_abc.adapter;

import android.annotation.SuppressLint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.p42_abc.R;
import com.example.p42_abc.model.Author;
import com.example.p42_abc.model.Book;

import java.util.List;

public class BookAdapter extends RecyclerView.Adapter<BookAdapter.BookViewHolder> {
    private List<Book> _bookList;
    private final OnBookClickListener _listener;

    public BookAdapter(List<Book> bookList, OnBookClickListener listener) {
        _bookList = bookList;
        _listener = listener;
    }

    public interface OnBookClickListener {
        void onBookClick(Book book);
    }

    @NonNull
    @Override
    public BookViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_book, parent, false);
        return new BookViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BookAdapter.BookViewHolder holder, int position) {
        Book book = _bookList.get(position);
        holder.bind(book, _listener);
    }

    @Override
    public int getItemCount() {
        return _bookList.size();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void updateList(List<Book> books) {
        _bookList.clear();
        _bookList.addAll(books);
        notifyDataSetChanged(); // TODO: Remplacer par DiffUtil pour plus d’efficacité
    }

    public static class BookViewHolder extends RecyclerView.ViewHolder {
        private final TextView _title;

        public BookViewHolder(@NonNull View itemView) {
            super(itemView);
            _title = itemView.findViewById(R.id.bookTitle);
        }

        @SuppressLint("SetTextI18n")
        public void bind(Book book, OnBookClickListener listener) {
            Log.d("BookAdapter", "Binding book: " + book.getTitle());
            _title.setText(book.getTitle());
            itemView.setOnClickListener(v -> listener.onBookClick(book));
        }
    }
}
