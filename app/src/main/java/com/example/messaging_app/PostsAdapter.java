package com.example.messaging_app;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class PostsAdapter extends RecyclerView.Adapter<PostsAdapter.PostViewHolder> {

    private List<Post> postsList;
    private OnPostClickListener listener;

    public interface OnPostClickListener {
        void onLikeClick(Post post, int position);

        void onCommentClick(Post post);
    }

    public PostsAdapter(List<Post> postsList, OnPostClickListener listener) {
        this.postsList = postsList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_post, parent, false);
        return new PostViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PostViewHolder holder, int position) {
        Post post = postsList.get(position);
        holder.bind(post);
    }

    @Override
    public int getItemCount() {
        return postsList.size();
    }

    public class PostViewHolder extends RecyclerView.ViewHolder {
        private TextView userAvatar;
        private TextView usernameText;
        private TextView timestampText;
        private TextView postContent;
        private ImageView postImage;
        private LinearLayout likeButton;
        private ImageView likeIcon;
        private TextView likesCount;
        private LinearLayout commentButton;

        public PostViewHolder(@NonNull View itemView) {
            super(itemView);
            userAvatar = itemView.findViewById(R.id.user_avatar);
            usernameText = itemView.findViewById(R.id.username_text);
            timestampText = itemView.findViewById(R.id.timestamp_text);
            postContent = itemView.findViewById(R.id.post_content);
            postImage = itemView.findViewById(R.id.post_image);
            likeButton = itemView.findViewById(R.id.like_button);
            likeIcon = itemView.findViewById(R.id.like_icon);
            likesCount = itemView.findViewById(R.id.likes_count);
            commentButton = itemView.findViewById(R.id.comment_button);
        }

        public void bind(Post post) {
            // Set user info
            userAvatar.setText(post.getUserInitial());
            usernameText.setText(post.getUsername());
            timestampText.setText(post.getTimestamp());

            // Set post content
            postContent.setText(post.getContent());

            // Handle post image
            if (post.getImageUrl() != null && !post.getImageUrl().isEmpty()) {
                postImage.setVisibility(View.VISIBLE);
                // TODO: Load image using Glide or Picasso
                // For now, we'll use a placeholder
                postImage.setImageResource(R.drawable.ic_image_placeholder);
            } else {
                postImage.setVisibility(View.GONE);
            }

            // Set likes
            updateLikeButton(post);

            // Set click listeners
            likeButton.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onLikeClick(post, getAdapterPosition());
                }
            });

            commentButton.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onCommentClick(post);
                }
            });
        }

        private void updateLikeButton(Post post) {
            likesCount.setText(String.valueOf(post.getLikesCount()));

            if (post.isLiked()) {
                likeIcon.setImageResource(R.drawable.ic_heart_filled);
                likeIcon.setColorFilter(itemView.getContext().getResources().getColor(R.color.red));
                likesCount.setTextColor(itemView.getContext().getResources().getColor(R.color.red));
            } else {
                likeIcon.setImageResource(R.drawable.ic_heart_outline);
                likeIcon.setColorFilter(itemView.getContext().getResources().getColor(R.color.muted_foreground_dark));
                likesCount.setTextColor(itemView.getContext().getResources().getColor(R.color.muted_foreground_dark));
            }
        }
    }
}
