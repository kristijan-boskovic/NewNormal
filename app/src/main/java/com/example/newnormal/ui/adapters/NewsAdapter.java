package com.example.newnormal.ui.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.newnormal.R;
import com.example.newnormal.data.models.News;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.NewsHolder> implements Filterable {
    private List<News> newsList;
    private List<News> newsListAll;
    private OnItemClickListener listener;

    @NonNull
    @Override
    public NewsHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_news_article, parent, false);
        return new NewsHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull NewsHolder holder, int position) {
        News currentNews = newsList.get(position);
        holder.tvNewsTitle.setText(currentNews.getTitle());
        holder.tvNewsDescription.setText(currentNews.getDescription());
        holder.tvNewsSource.setText(currentNews.getSource());
        holder.tvNewsPublishingDate.setText(currentNews.getPublishingDate());
        Picasso.get()
                .load(currentNews.getImageUrl())
                .placeholder(R.drawable.ic_menu_gallery)
                .error(R.drawable.ic_menu_gallery)
                .fit()
                .centerCrop()
                .into(holder.ivNewsImage);
    }

    @Override
    public int getItemCount() {
        return newsList.size();
    }

    public void setNews(List<News> newsList) {
        this.newsList = newsList;
        this.newsListAll = new ArrayList<>(newsList);
        notifyDataSetChanged();
    }

    public News getNewsAt(int position) {
        return newsList.get(position);
    }

    class NewsHolder extends RecyclerView.ViewHolder {
        private final TextView tvNewsTitle;
        private final TextView tvNewsDescription;
        private final TextView tvNewsSource;
        private final TextView tvNewsPublishingDate;
        private final ImageView ivNewsImage;

        public NewsHolder(@NonNull View itemView) {
            super(itemView);
            tvNewsTitle = itemView.findViewById(R.id.tv_news_title);
            tvNewsDescription = itemView.findViewById(R.id.tv_news_description);
            tvNewsSource = itemView.findViewById(R.id.tv_news_source);
            tvNewsPublishingDate = itemView.findViewById(R.id.tv_news_publishing_date);
            ivNewsImage = itemView.findViewById(R.id.iv_news_image);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (listener != null && position != RecyclerView.NO_POSITION) {
                        listener.onItemClick(newsList.get(position));
                    }
                }
            });
        }
    }

    public interface OnItemClickListener {
        void onItemClick(News news);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    Filter filter = new Filter() {
        // Runs on background thread
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<News> filteredList = new ArrayList<>();

            if (constraint.toString().isEmpty()) {
                filteredList.addAll(newsListAll);
            }
            else {
                for (News news : newsListAll) {
                    if (news.getTitle().toLowerCase().contains(constraint.toString().toLowerCase())) {
                        filteredList.add(news);
                    }
                }
            }
            FilterResults filterResults = new FilterResults();
            filterResults.values = filteredList;

            return filterResults;
        }

        // Runs on UI thread
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            newsList.clear();
            newsList.addAll((Collection<? extends News>) results.values);
            notifyDataSetChanged();
        }
    };

    @Override
    public Filter getFilter() {
        return filter;
    }
}
