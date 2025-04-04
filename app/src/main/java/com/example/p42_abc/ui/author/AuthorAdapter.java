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
    // Liste des auteurs à afficher
    private final List<Author> _authorList;
    // Listeners pour les clicks sur item et bouton
    private final ItemClickListener _buttonListener;
    private final ItemClickListener _itemListener;

    /**
     * Constructeur de l'adapter
     * @param authorList Liste des auteurs à afficher
     * @param btnListener Listener pour les clicks sur le bouton de suppression
     * @param itemListener Listener pour les clicks sur l'item entier
     */
    public AuthorAdapter(List<Author> authorList, ItemClickListener btnListener, ItemClickListener itemListener) {
        _authorList = authorList;
        _buttonListener = btnListener;
        _itemListener = itemListener;
    }

    /**
     * Interface pour gérer les interactions utilisateur
     */
    public interface ItemClickListener {
        void onAuthorClick(Author author);
        void onButtonClick(Author author);
    }

    @NonNull
    @Override
    public AuthorViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Crée une nouvelle vue pour chaque item
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_author, parent, false);
        return new AuthorViewHolder(view.getContext(), view);
    }

    @Override
    public void onBindViewHolder(@NonNull AuthorAdapter.AuthorViewHolder holder, int position) {
        // Lie les données à la vue
        Author author = _authorList.get(position);
        holder.bind(author, _itemListener, _buttonListener);
    }

    @Override
    public int getItemCount() {
        return _authorList.size();
    }

    /**
     * Met à jour la liste des auteurs et rafraîchit l'affichage
     * @param authors Nouvelle liste d'auteurs
     */
    @SuppressLint("NotifyDataSetChanged")
    public void updateList(List<Author> authors) {
        _authorList.clear();
        _authorList.addAll(authors);
        notifyDataSetChanged(); // Note: Pour de grandes listes, préférer DiffUtil
    }

    /**
     * ViewHolder qui représente un item auteur
     */
    public static class AuthorViewHolder extends RecyclerView.ViewHolder {
        private final TextView _authorName;
        private final ImageButton _button;
        private final ImageView _authorImage;
        private final Context _context;

        public AuthorViewHolder(Context context, @NonNull View itemView) {
            super(itemView);
            _context = context;
            // Initialisation des vues
            _authorName = itemView.findViewById(R.id.authorName);
            _button = itemView.findViewById(R.id.btn_delete);
            _authorImage = itemView.findViewById(R.id.authorImage);
        }

        /**
         * Lie les données de l'auteur aux vues
         * @param author Auteur à afficher
         * @param itemListener Listener pour le click sur l'item
         * @param buttonListener Listener pour le click sur le bouton
         */
        @SuppressLint("SetTextI18n")
        public void bind(Author author, ItemClickListener itemListener, ItemClickListener buttonListener) {
            _authorName.setText(author.getFirstname() + " " + author.getLastname());
            String imageUrl = author.getImage();
            Log.d("AuthorAdapter", "Image URL: " + imageUrl); // Debug utile

            // Chargement de l'image avec Glide
            Glide.with(_context)
                    .load(imageUrl)
                    .circleCrop()
                    .placeholder(R.drawable.placeholder)
                    .error(R.drawable.error)
                    .into(_authorImage);

            // Gestion des clicks
            itemView.setOnClickListener(v -> itemListener.onAuthorClick(author));
            _button.setOnClickListener(v -> buttonListener.onButtonClick(author));
        }
    }
}