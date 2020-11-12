package nadeem.animei.Library;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.List;

import nadeem.animei.Anime.AnimeActivity;
import nadeem.animei.R;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ViewHolder> {

    private List<Item> libraryItems;
    private Context context;

    public ItemAdapter(Context context, List<Item> libraryItems) {
        this.context = context;
        this.libraryItems = libraryItems;
    }

    @Override
    public ItemAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.listview_library_item, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ItemAdapter.ViewHolder viewHolder, final int i) {
        Picasso.with(context).load(libraryItems.get(i).getCover()).fit().into(viewHolder.img_android);
        viewHolder.tv_android.setText(libraryItems.get(i).getTitle());
        viewHolder.img_android.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, AnimeActivity.class);
                intent.putExtra("link", libraryItems.get(i).getLink());
                intent.putExtra("title", libraryItems.get(i).getTitle());
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                context.startActivity(intent);
            }
        });
        viewHolder.img_android.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Toast.makeText(context, libraryItems.get(i).getTitle(), Toast.LENGTH_SHORT).show();
                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        return libraryItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tv_android;
        private ImageView img_android;

        public ViewHolder(View view) {
            super(view);

            tv_android = view.findViewById(R.id.tv_android);
            img_android = view.findViewById(R.id.img_android);
        }
    }

}