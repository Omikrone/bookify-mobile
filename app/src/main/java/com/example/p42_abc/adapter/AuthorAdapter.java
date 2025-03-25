package com.example.p42_abc.adapter;

import android.annotation.SuppressLint;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.p42_abc.model.Author;

import java.util.List;

public class AuthorAdapter extends RecyclerView.Adapter<AuthorAdapter.AuthorViewHolder> {
    private List<Author> _authorList;

    public AuthorAdapter(List<Author> authorList) {
        _authorList = authorList;
    }

    @NonNull
    @Override
    public AuthorViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
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

        return new AuthorViewHolder(linearLayout, authorName);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull AuthorAdapter.AuthorViewHolder holder, int position) {
        Author author = _authorList.get(position);
        holder._authorName.setText(author.getFirstname() + " " + author.getLastname());

    }

    @Override
    public int getItemCount() {
        return _authorList.size();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void updateList(List<Author> authors) {
        _authorList = authors;
        notifyDataSetChanged();
    }

    public static class AuthorViewHolder extends RecyclerView.ViewHolder {
        TextView _authorName;

        public AuthorViewHolder(View itemView, TextView authorName) {
            super(itemView);
            _authorName = authorName;
        }
    }
}
