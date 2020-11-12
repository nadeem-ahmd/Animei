package nadeem.animei.Library.Library;

import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import nadeem.animei.Library.Item;
import nadeem.animei.Library.ItemAdapter;
import nadeem.animei.Model.Anime;
import nadeem.animei.R;

public class SavedFragment extends Fragment {

    private List<Item> savedLibraryItems = new ArrayList<>();
    private Set<String> saved;

    private SavedFragment savedFragment;
    private TextView message;

    private ItemAdapter adapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        savedFragment = this;

        Toolbar toolbar = this.getActivity().findViewById(R.id.main_toolbar);
        toolbar.setTitle("Library");
        toolbar.setSubtitle("Saved");

        init();
    }

    public void init() {
        SharedPreferences preferences = getActivity().getSharedPreferences("saved", Context.MODE_PRIVATE);
        saved = preferences.getStringSet("saved", null);
        if (saved == null) {
            saved = new HashSet<>();
        }
        new Source().execute();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View myView = inflater.inflate(R.layout.fragment_saved, container, false);
        message = myView.findViewById(R.id.fragment_saved_message);
        return myView;
    }

    @Override
    public void onResume() {
        init();
        super.onResume();
    }

    public void loadLibrary(ArrayList<Anime> animeList) {
        savedLibraryItems.clear();
        for (int i = 0; i < animeList.size(); i++) {
            Item libraryItem = new Item(animeList.get(i).getCover(), animeList.get(i).getName(), animeList.get(i).getLink());
            savedLibraryItems.add(libraryItem);
        }
        if (savedLibraryItems.size() != 0) {
            adapter = new ItemAdapter(getActivity(), savedLibraryItems);
            RecyclerView recyclerView = getActivity().findViewById(R.id.saved_card_recycler_view);
            recyclerView.setHasFixedSize(true);
            RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getActivity(), 2);
            recyclerView.setLayoutManager(layoutManager);
            recyclerView.setAdapter(adapter);
            message.setVisibility(View.INVISIBLE);
        } else {
            message.setVisibility(View.VISIBLE);
        }

        if (adapter != null)
            adapter.notifyDataSetChanged();
    }

    private class Source extends AsyncTask<String, Void, Boolean> {

        private ArrayList<Anime> discoveredAnime = new ArrayList<>();

        protected Boolean doInBackground(final String... args) {
            for (Object savedObject : saved) {
                try {
                    if (savedObject != null) {
                        Document document = Jsoup.connect(savedObject.toString()).get();

                        String cover = document.select("#details > div.cover > img").attr("src");
                        String alt = document.select("#head > div.content > div > div > p").text();

                        discoveredAnime.add(new Anime(savedObject.toString(), alt, cover));
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            savedFragment.loadLibrary(discoveredAnime);
        }
    }
}
