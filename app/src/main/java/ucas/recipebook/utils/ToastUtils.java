package ucas.recipebook.utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import ucas.recipebook.R;

public class ToastUtils {

    public static void show(Context context, String message, int duration) {
        Toast toast = new Toast(context.getApplicationContext());
        View view = LayoutInflater.from(context).inflate(R.layout.layout_toast, null);
        TextView tv = view.findViewById(R.id.tvToastMessage);
        tv.setText(message);
        toast.setView(view);
        toast.setDuration(duration);
        toast.show();
    }

    public static void showShort(Context context, String message) {
        show(context, message, Toast.LENGTH_SHORT);
    }

    public static void showLong(Context context, String message) {
        show(context, message, Toast.LENGTH_LONG);
    }
}

