package com.example.newnormal.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.newnormal.R;
import com.example.newnormal.data.models.News;
import com.example.newnormal.ui.activities.MainActivity;
import com.example.newnormal.ui.adapters.NewsAdapter;

import java.util.List;

public class NewsFragment extends Fragment {
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_news, container, false);

        RecyclerView rvNewsArticles = root.findViewById(R.id.rv_news_articles);
        rvNewsArticles.setLayoutManager(new LinearLayoutManager(getContext()));
        rvNewsArticles.setHasFixedSize(true);

        final NewsAdapter newsAdapter = new NewsAdapter();
        rvNewsArticles.setAdapter(newsAdapter);

        // TODO: replace this with more loosely coupled solution (search on Google: "send data from activity to fragment")
        MainActivity activity = (MainActivity) getActivity();
        LiveData<List<News>> newsList = activity.getNewsList();

        newsList.observe(getViewLifecycleOwner(), new Observer<List<News>>() {
            @Override
            public void onChanged(@Nullable List<News> newsList) {
                newsAdapter.setNews(newsList);
            }
        });


//        newsAdapter.setOnItemClickListener(new OfferAdapter.OnItemClickListener() {
//            @Override
//            public void onItemClick(Offer offer) {
//                Intent i = new Intent(getActivity(), OfferDetailsActivity.class);
//                i.putExtra(OfferDetailsActivity.EXTRA_OFFER_ID, offer.getOfferId());
//                i.putExtra(OfferDetailsActivity.EXTRA_USER_ID, offer.getUserId());
//                i.putExtra(OfferDetailsActivity.EXTRA_OFFER_NAME, offer.getName());
//                i.putExtra(OfferDetailsActivity.EXTRA_OFFER_ADDRESS, offer.getAddress());
//                i.putExtra(OfferDetailsActivity.EXTRA_OFFER_CITY, offer.getCity());
//                i.putExtra(OfferDetailsActivity.EXTRA_OFFER_POST_CODE, offer.getPostCode());
//                i.putExtra(OfferDetailsActivity.EXTRA_OFFER_DESCRIPTION, offer.getDescription());
//                i.putExtra(OfferDetailsActivity.EXTRA_PRICE, offer.getPrice());
//                startActivityForResult(i, OFFER_DETAILS_REQUEST);
//            }
//        });

        return root;
    }
}
