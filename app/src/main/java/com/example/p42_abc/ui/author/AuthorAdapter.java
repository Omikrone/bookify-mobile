package com.example.p42_abc.ui.author;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.p42_abc.R;
import com.example.p42_abc.model.Author;

import java.util.List;

public class AuthorAdapter extends RecyclerView.Adapter<AuthorAdapter.AuthorViewHolder> {
    private final List<Author> _authorList;
    private final ItemClickListener _buttonListener;
    private final ItemClickListener _itemListener;

    public AuthorAdapter(List<Author> authorList, ItemClickListener btnListener, ItemClickListener itemListener) {
        _authorList = authorList;
        _buttonListener = btnListener;
        _itemListener = itemListener;
    }

    public interface ItemClickListener {
        void onAuthorClick(Author author);
        void onButtonClick(Author author);
    }

    @NonNull
    @Override
    public AuthorViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_author, parent, false);
        return new AuthorViewHolder(view.getContext(), view);
    }

    @Override
    public void onBindViewHolder(@NonNull AuthorAdapter.AuthorViewHolder holder, int position) {
        Author author = _authorList.get(position);
        holder.bind(author, _itemListener, _buttonListener);
    }

    @Override
    public int getItemCount() {
        return _authorList.size();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void updateList(List<Author> authors) {
        _authorList.clear();
        _authorList.addAll(authors);
        notifyDataSetChanged();
    }

    public static class AuthorViewHolder extends RecyclerView.ViewHolder {
        private final TextView _authorName;
        private final ImageButton _button;
        private final ImageView _authorImage;
        private final Context _context;

        public AuthorViewHolder(Context context, @NonNull View itemView) {
            super(itemView);
            _context = context;
            _authorName = itemView.findViewById(R.id.authorName);
            _button = itemView.findViewById(R.id.btn_delete);
            _authorImage = itemView.findViewById(R.id.authorImage);
        }

        @SuppressLint("SetTextI18n")
        public void bind(Author author, ItemClickListener itemListener, ItemClickListener buttonListener) {
            _authorName.setText(author.getFirstname() + " " + author.getLastname());
            String imageUrl = author.getImage();
            Log.d("AuthorAdapter", "Image URL: " + imageUrl);

            Glide.with(_context)
                    .load(imageUrl)
                    .circleCrop()
                    .placeholder(R.drawable.placeholder)
                    .error(R.drawable.error)
                    .into(_authorImage);
            itemView.setOnClickListener(v -> itemListener.onAuthorClick(author));
            _button.setOnClickListener(v -> buttonListener.onButtonClick(author));
        }
    }
}
