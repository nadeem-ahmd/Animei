package nadeem.animei;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;

import java.io.File;
import java.util.HashSet;
import java.util.Set;

import static android.content.Context.CLIPBOARD_SERVICE;

public class Utilities {

    public static int PAGES = 2;

    public static String BASE = "https://4anime.to";
    public static String PAGE = "/page/";
    public static String POPULAR = "/popular-this-week";
    public static String SEARCH = "/?s=";

    public static MaterialDialog dialog(final Activity activity) {
        MaterialDialog dialog = new MaterialDialog.Builder(activity)
                .progress(true, 0)
                .progressIndeterminateStyle(true)
                .cancelable(false)
                .canceledOnTouchOutside(false)
                .show();

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().setDimAmount(0);
        return dialog;
    }

    public static void clearSaved(Context context) {
        try {
            SharedPreferences preferences = context.getSharedPreferences("saved", Context.MODE_PRIVATE);
            Set<String> set = new HashSet<>();
            SharedPreferences.Editor editor = preferences.edit();
            editor.putStringSet("saved", set).apply();
            editor.commit();
            Toast.makeText(context, "Successfully cleared saved anime", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(context, "Error: Failed to clear saved anime", Toast.LENGTH_SHORT).show();
        }
    }

    public static void deleteCache(Context context) {
        try {
            File dir = context.getCacheDir();
            deleteDir(dir, context);
            Toast.makeText(context, "Successfully cleared cache", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(context, "Error: Failed to clear cache", Toast.LENGTH_SHORT).show();
        }
    }

    public static void contact(Context context) {
        ClipboardManager clipboard = (ClipboardManager) context.getSystemService(CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("Contact email", context.getString(R.string.contact));
        clipboard.setPrimaryClip(clip);
        Toast.makeText(context, "Copied contact email to clipboard", Toast.LENGTH_SHORT).show();
    }

    public static boolean deleteDir(File dir, Context context) {
        if (dir != null && dir.isDirectory()) {
            String[] children = dir.list();
            for (String child : children) {
                boolean success = deleteDir(new File(dir, child), context);
                if (!success) {
                    return false;
                }
            }
            return dir.delete();
        } else if (dir != null && dir.isFile()) {
            return dir.delete();
        } else {
            return false;
        }
    }

    public static void update(Context context) {
        Toast.makeText(context, "Running latest version", Toast.LENGTH_LONG).show();
    }
}
