package com.example.newnormal.ui.fragments;

import androidx.fragment.app.Fragment;

import com.example.newnormal.data.models.News;
import com.example.newnormal.ui.activities.MainActivity;
import com.google.cloud.language.v1.Sentiment;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class BaseNewsFragment extends Fragment {
    protected String mergeAllNewsTitles(List<News> newsList) {
        List<String> newsTitlesList = new ArrayList<>();
        for (News news : newsList) {
            newsTitlesList.add(news.getTitle());
        }

        StringBuilder newsTitlesSb = new StringBuilder();
        ListIterator<String> newsTitlesIterator = newsTitlesList.listIterator();
        while (newsTitlesIterator.hasNext()) {
            int index = newsTitlesIterator.nextIndex();
            String newsTitle = newsTitlesIterator.next();

            if (Character.isDigit(newsTitle.charAt(0))) { // Remove articles starting with number as they case invalid sentiment sentence reads
                newsList.remove(index);
                newsTitlesIterator.remove();
            }
            else { // Adjust these signs in news articles, as they case invalid sentiment sentence reads
                if (newsTitle.contains(".")) {
                    newsTitle = newsTitle.replace(".", "");
                }
                if (newsTitle.contains("!")) {
                    newsTitle = newsTitle.replace("!", "");
                }
                if (newsTitle.contains("?")) {
                    newsTitle = newsTitle.replace("?", "");
                }
                if (newsTitle.contains("$")) {
                    newsTitle = newsTitle.replace("$", " dollars");
                }
                if (newsTitle.contains("€")) {
                    newsTitle = newsTitle.replace("€", " euros");
                }
                if (newsTitle.contains("£")) {
                    newsTitle = newsTitle.replace("£", " pounds");
                }

                newsTitlesList.set(index, newsTitle);
                newsTitlesSb.append(newsTitle).append(". ");
            }
        }
        return newsTitlesSb.toString().trim();
    }

    protected void filterPositiveNewsTitles(MainActivity activity, List<News> newsList, String newsTitlesString) {
        // TODO: move this sentiment analysis process somewhere where will be done only once (e.g. MainActivity or NewsViewModel)
        List<Sentiment> sentiments = activity.performSentimentAnalysisClient(newsTitlesString);
        if (sentiments.size() == newsList.size()) { // Check if number of sentiments match number of news articles (sentiment analysis done correctly)
            Iterator<News> newsIterator = newsList.iterator();
            int index = 0;
            while (newsIterator.hasNext()) {
                newsIterator.next();
                Sentiment sentiment = sentiments.get(index);
                float sentimentScore = 0;
                if (sentiment != null) {
                    sentimentScore = sentiment.getScore();
                }
                if (sentimentScore <= 0) {
                    newsIterator.remove();
                }
                index++;
            }
        }
        else {
//            Toast.makeText(getActivity(),"Sentiment analysis failed, displaying all news articles!",Toast.LENGTH_LONG).show();
        }
    }
}
