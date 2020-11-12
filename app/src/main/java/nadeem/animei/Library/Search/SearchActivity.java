package nadeem.animei.Library.Search;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

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

public class SearchActivity extends AppCompatActivity {

    private List<Item> resultsLibraryItem = new ArrayList<>();
    private SearchActivity searchActivity;

    private TextView message;
    private SearchView searchView;

    private RecyclerView recyclerView;
    private ItemAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        searchActivity = this;

        setContentView(R.layout.activity_search);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Search");

        message = findViewById(R.id.search_activity_message);

        recyclerView = findViewById(R.id.search_card_recycler_view);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this, 2);
        adapter = new ItemAdapter(this, resultsLibraryItem);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
    }

    public void loadResults(ArrayList<Anime> resultsList) {
        resultsLibraryItem.clear();
        for (int i = 0; i < resultsList.size(); i++) {
            Item libraryItem = new Item(resultsList.get(i).getCover(), resultsList.get(i).getName(), resultsList.get(i).getLink());
            resultsLibraryItem.add(libraryItem);
        }
        message.setVisibility(View.INVISIBLE);
        recyclerView.setVisibility(View.VISIBLE);
        adapter.notifyDataSetChanged();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search_menu, menu);
        searchView = (SearchView) MenuItemCompat.getActionView(menu.findItem(R.id.action_search));
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Source source = new Source();
                source.execute(query);
                searchView.clearFocus();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }

        });
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem menuItem = menu.findItem(R.id.action_search);
        menuItem.expandActionView();
        MenuItemCompat.setOnActionExpandListener(menuItem, new MenuItemCompat.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {
                return true;
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
                searchActivity.finish();
                return false;
            }
        });
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    private class Source extends AsyncTask<String, Void, Boolean> {

        private ArrayList<Anime> arrayList = new ArrayList<>();

        private MaterialDialog dialog;

        protected void onPreExecute() {
            dialog = Utilities.dialog(searchActivity);
        }

        protected Boolean doInBackground(final String... args) {
            try {
                String query = args[0].replace(" ", "+");
                String url = Utilities.BASE + Utilities.SEARCH + query;

                Document document = Jsoup.connect(url).get();
                Elements series = document.select("#headerDIV_2");

                int length = series.size();
                for (int i = 1; i < length; i++) {
                    String href = series.get(i).select("a").attr("href");
                    String alt = series.get(i).select("#headerDIV_95 > a > div").text();
                    String img = series.get(i).select("img").attr("src");

                    arrayList.add(new Anime(href, alt, img));
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
            searchActivity.loadResults(arrayList);
        }
    }
}