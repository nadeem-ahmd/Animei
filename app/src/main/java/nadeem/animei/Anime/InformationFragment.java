package nadeem.animei.Anime;

import android.app.Dialog;
import android.app.Fragment;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;

import nadeem.animei.Model.Anime;
import nadeem.animei.R;
import nadeem.animei.Utilities;

public class InformationFragment extends Fragment {

    private ImageView cover;
    private TextView description;

    private String link;
    private String title;

    private InformationFragment informationFragment;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        this.informationFragment = this;

        Intent intent = getActivity().getIntent();
        link = intent.getStringExtra("link");
        title = intent.getStringExtra("title");

        getActivity().setTitle(title);

        description = getView().findViewById(R.id.anime_description);
        cover = getView().findViewById(R.id.anime_cover);

        new Source().execute();
    }

    public void loadInformation(Anime anime) {
        if (anime != null) {
            description.setText(anime.getDescription());
            Picasso.with(this.getActivity()).load(anime.getCover()).fit().into(cover);
            cover.setVisibility(View.VISIBLE);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_information, container, false);
    }

    private class Source extends AsyncTask<Void, Void, Void> {

        private Anime anime;
        private Dialog dialog;

        protected void onPreExecute() {
            dialog = Utilities.dialog(informationFragment.getActivity());
        }

        public Void doInBackground(Void... params) {
            try {
                Document document = Jsoup.connect(link).get();

                String name = document.select("#head > div.content > div > div > p").text();
                String cover = document.select("#details > div.cover > img").attr("src");
                String description = document.select("#description-mob > p:nth-child(2)").first().text();

                anime = new Anime(link, name, cover, description);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            if (dialog.isShowing()) {
                dialog.dismiss();
            }
            informationFragment.loadInformation(anime);
        }
    }
}
