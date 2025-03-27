package com.example.p42_abc.adapter;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.p42_abc.R;
import com.example.p42_abc.model.Author;
import java.util.ArrayList;
import java.util.List;

public class AuthorAdapter extends RecyclerView.Adapter<AuthorAdapter.AuthorViewHolder> {
    private List<Author> _authorList = new ArrayList<>();
    private final OnAuthorClickListener _listener;

    public AuthorAdapter(List<Author> authorList, OnAuthorClickListener listener) {
        _authorList = authorList;
        _listener = listener;
    }

    public interface OnAuthorClickListener {
        void onAuthorClick(Author author);
    }

    @NonNull
    @Override
    public AuthorViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_author, parent, false);
        return new AuthorViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AuthorAdapter.AuthorViewHolder holder, int position) {
        Author author = _authorList.get(position);
        holder.bind(author, _listener);
    }

    @Override
    public int getItemCount() {
        return _authorList.size();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void updateList(List<Author> authors) {
        _authorList.clear();
        _authorList.addAll(authors);
        notifyDataSetChanged(); // TODO: Remplacer par DiffUtil pour plus d’efficacité
    }

    public static class AuthorViewHolder extends RecyclerView.ViewHolder {
        private final TextView _authorName;

        public AuthorViewHolder(@NonNull View itemView) {
            super(itemView);
            _authorName = itemView.findViewById(R.id.authorName);
        }

        @SuppressLint("SetTextI18n")
        public void bind(Author author, OnAuthorClickListener listener) {
            _authorName.setText(author.getLastname() + " " + author.getFirstname());
            itemView.setOnClickListener(v -> listener.onAuthorClick(author));
        }
    }
}
