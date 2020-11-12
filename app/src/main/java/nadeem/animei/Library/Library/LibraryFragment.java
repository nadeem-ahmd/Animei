package nadeem.animei.Library.Library;

import android.app.Fragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.afollestad.materialdialogs.MaterialDialog;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import nadeem.animei.Library.Item;
import nadeem.animei.Library.ItemAdapter;
import nadeem.animei.Model.Anime;
import nadeem.animei.R;
import nadeem.animei.Utilities;

public class LibraryFragment extends Fragment {

    private static String link;

    private static List<Item> libraryItems = new ArrayList<>();

    private LibraryFragment libraryFragment;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        libraryFragment = this;

        Bundle args = getArguments();
        link = args.getString("link", Utilities.BASE + Utilities.POPULAR + Utilities.PAGE);

        Toolbar toolbar = this.getActivity().findViewById(R.id.main_toolbar);
        String library = args.getString("library", "Updated");
        toolbar.setTitle("Library");
        toolbar.setSubtitle(library);

        new Source().execute();
    }

    public void loadLibrary(ArrayList<Anime> arrayList) {
        libraryItems.clear();
        for (int i = 0; i < arrayList.size(); i++) {
            Item libraryItem = new Item(arrayList.get(i).getCover(), arrayList.get(i).getName(), arrayList.get(i).getLink());
            libraryItems.add(libraryItem);
        }
        ItemAdapter adapter = new ItemAdapter(getActivity(), libraryItems);

        RecyclerView recyclerView = getActivity().findViewById(R.id.library_card_recycler_view);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getActivity(), 2);
        recyclerView.setLayoutManager(layoutManager);

        recyclerView.setAdapter(adapter);
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.library_layout, container, false);
    }

    private class Source extends AsyncTask<String, Void, Boolean> {

        private ArrayList<Anime> discoveredAnime = new ArrayList<>();
        private MaterialDialog dialog;

        protected void onPreExecute() {
            dialog = Utilities.dialog(libraryFragment.getActivity());
        }

        protected Boolean doInBackground(final String... args) {
            try {
                for (int x = 1; x <= Utilities.PAGES; x++) {
                    Document document = Jsoup.connect(link + Utilities.PAGE + x).get();
                    Elements series = document.select("#headerDIV_3");

                    int length = series.size();
                    for (int i = 0; i < length; i++) {
                        String href = series.get(i).select("a").attr("href");
                        String alt = series.get(i).select("img").attr("title");
                        String img = series.get(i).select("img").attr("src");

                        discoveredAnime.add(new Anime(href, alt, img));
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            if (dialog.isShowing()) {
                dialog.dismiss();
            }
            libraryFragment.loadLibrary(discoveredAnime);
        }
    }
}
