package com.example.p42_abc.ui.book;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.recyclerview.widget.RecyclerView;
import com.example.p42_abc.R;
import com.example.p42_abc.model.Book;

import com.bumptech.glide.Glide;
import com.example.p42_abc.ui.author.AuthorAdapter;
import com.google.android.material.button.MaterialButton;

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
        private final Context _context;
        private final AppCompatImageButton _button;
        private final ImageView _bookCover;

        public BookViewHolder(@NonNull View itemView) {
            super(itemView);
            _context = itemView.getContext();
            if (itemView.getContext() == null) {
                Log.e("BookAdapter", "Context is null in BookViewHolder");
            }
            _title = itemView.findViewById(R.id.bookTitle);
            _button = itemView.findViewById(R.id.btn_delete);
            _bookCover = itemView.findViewById(R.id.bookCover);
        }

        @SuppressLint("SetTextI18n")
        public void bind(Book book, OnBookClickListener listener) {
            _title.setText(book.getTitle());

            String imageUrl = book.getCover() + "?default=false";
            imageUrl = imageUrl.trim().replaceAll("\\p{C}", "");

            Glide.with(_context)
                    .load(imageUrl)
                    .placeholder(R.drawable.placeholder)
                    .error(R.drawable.error)
                    .into(_bookCover);

            itemView.setOnClickListener(v -> listener.onBookClick(book));
        }
    }
}
