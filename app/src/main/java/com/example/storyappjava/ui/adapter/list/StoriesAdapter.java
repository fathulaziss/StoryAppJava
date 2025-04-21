package com.example.storyappjava.ui.adapter.list;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.storyappjava.R;
import com.example.storyappjava.data.remote.dto.StoryDto;

import java.util.List;

public class StoriesAdapter extends RecyclerView.Adapter<StoriesAdapter.StoriesViewHolder> {

    private final Context context;
    private final List<StoryDto> stories;

    public StoriesAdapter(Context context, List<StoryDto> stories) {
        this.context = context;
        this.stories = stories;
    }

    @NonNull
    @Override
    public StoriesAdapter.StoriesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.story_item, parent, false);
        return new StoriesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StoriesAdapter.StoriesViewHolder holder, int position) {
        StoryDto story = stories.get(position);
        String[] names = story.getName().split(" ");
        StringBuilder nameCapitalized = new StringBuilder();

        for (String name : names) {
            if (!name.isEmpty()) {
                nameCapitalized.append(Character.toUpperCase(name.charAt(0)))
                        .append(name.substring(1).toLowerCase())
                        .append(" ");
            }
        }

        Glide.with(context).load(story.getPhotoUrl()).into(holder.ivPhoto);
        holder.tvName.setText(nameCapitalized);
        holder.tvDesc.setText(story.getDescription());
        holder.cvStory.setOnClickListener(view -> {});
    }

    @Override
    public int getItemCount() {
        return this.stories.size();
    }

    public static class StoriesViewHolder extends RecyclerView.ViewHolder {
        final TextView tvName, tvDesc;
        final CardView cvStory;
        final ImageView ivPhoto;

        public StoriesViewHolder(View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tv_name);
            tvDesc = itemView.findViewById(R.id.tv_desc);
            cvStory = itemView.findViewById(R.id.cv_story);
            ivPhoto = itemView.findViewById(R.id.iv_photo);
        }
    }
}
