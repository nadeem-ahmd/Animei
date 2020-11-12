package nadeem.animei.Anime;

import android.app.Fragment;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.commons.lang3.ArrayUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;

import nadeem.animei.Model.Episode;
import nadeem.animei.R;

public class EpisodesFragment extends Fragment {

    private EpisodesFragment episodesFragment;

    private ArrayList<Episode> episodeList;

    private ListView episodesList;
    private TextView message;

    private ProgressBar progressBar;

    private String link;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        episodesFragment = this;

        episodesList = getView().findViewById(R.id.anime_episodes_list);
        message = getView().findViewById(R.id.fragment_episodes_message);
        progressBar = getView().findViewById(R.id.episodes_progress);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View myView = inflater.inflate(R.layout.fragment_episodes, container, false);

        Intent intent = getActivity().getIntent();
        link = intent.getStringExtra("link");

        new Episodes().execute();

        return myView;
    }

    public void loadEpisodes(ArrayList<Episode> episodeList) {
        this.episodeList = episodeList;
        if (!this.isAdded())
            return;

        episodesList.setBackgroundColor(getResources().getColor(R.color.colorPrimaryLight));

        String[] episodes = new String[episodeList.size()];
        for (Episode e : episodeList) {
            if (ArrayUtils.contains(episodes, e.getEpisode()))
                break;
            episodes[episodeList.indexOf(e)] = e.getEpisodeString();
        }
        if (episodeList.size() == 0) {
            message.setVisibility(View.VISIBLE);
        } else {
            if (getActivity() != null) {
                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(getActivity(), R.layout.listview_episodes_item, episodes);
                episodesList.setAdapter(arrayAdapter);
                episodesList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Episode episode = episodesFragment.episodeList.get(position);
                        new Links(episode.getLink()).execute();
                    }
                });
            }
        }
        progressBar.setVisibility(View.INVISIBLE);
    }

    public void streamVideo(String url) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(episodesFragment.getActivity());
        if (preferences.getBoolean("internal_player", false)) {
            Intent internal = new Intent(episodesFragment.getActivity(), VideoActivity.class);
            internal.putExtra("link", url);
            startActivity(internal);
        } else {
            Intent external = new Intent(Intent.ACTION_VIEW);
            external.setDataAndType(Uri.parse(url), "video/*");
            if (external.resolveActivity(getActivity().getPackageManager()) != null)
                getActivity().startActivity(external);
            else
                Toast.makeText(getActivity(), "Error: No external app to stream video", Toast.LENGTH_SHORT).show();
        }
    }

    private class Episodes extends AsyncTask<Void, Void, Void> {

        private ArrayList<Episode> episodes = new ArrayList<>();

        public Void doInBackground(Void... params) {
            try {
                Document document = Jsoup.connect(link).get();
                Elements element = document.select("#servers > div > div > ul >li");

                for (int i = 0; i < element.size(); i++) {
                    String href = element.get(i).select("a").attr("href");
                    String title = element.get(i).select("a").text();

                    episodes.add(new Episode(href, Integer.parseInt(title)));
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            episodesFragment.loadEpisodes(episodes);
        }
    }

    private class Links extends AsyncTask<Void, Void, Void> {

        private String source;
        private String src;

        Links(String source) {
            this.source = source;
        }

        public Void doInBackground(Void... params) {
            try {
                Document document = Jsoup.connect(source).get();
                Elements element = document.select("#focuselement > div.videojs-desktop > div > video > source");

                src = element.attr("src");
            } catch (IOException | ArrayIndexOutOfBoundsException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            episodesFragment.streamVideo(src);
        }
    }
}
