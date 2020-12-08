package com.pm.roomie.roomie;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.pm.roomie.roomie.remote.ApiUtils;
import com.pm.roomie.roomie.remote.UserService;

import static com.pm.roomie.roomie.R.layout.row;

public class QueueAdapter extends ArrayAdapter<String> {
    private UserService userService;
    private Context context;

    private Integer[] ids;
    private String[] names;
    private String[] products;


    public QueueAdapter(@NonNull Context context,  String[] names,String[] products, Integer[] ids) {
        super(context, row,names);
        this.context = context;
        this.names = names;
        this.products = products;
        this.ids = ids;
        this.userService =  ApiUtils.getUserService();
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View row = layoutInflater.inflate(R.layout.queue_row, parent, false);
        TextView name = row.findViewById(R.id.name);
        TextView product = row.findViewById(R.id.product);
        name.setText(names[position]+" - twoja kolej!");
        product.setText(products[position]);

        addHistoryListener(ids[position], layoutInflater, row);
        addRegisterListener(ids[position], row);

        return row;
    }

    private void addRegisterListener(Integer id, View row) {
        Button editButton = (Button) row.findViewById(R.id.add);
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(context, ReqisterProductActivity.class);
//                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                context.startActivity(intent);
            }
        });
    }

    private void addHistoryListener(Integer id, LayoutInflater layoutInflater, View row) {
        Button archiveButton = (Button) row.findViewById(R.id.history);
        archiveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ProductHistoryActivity.class);
                intent.putExtra("id",id);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);

            }
        });
    }

    private void createToast(String toastText, LayoutInflater inflater, View row, Context comtext) {
        View layout = inflater.inflate(R.layout.custom_toast,
                (ViewGroup) row.findViewById(R.id.custom_toast_container));
        TextView text = (TextView) layout.findViewById(R.id.text);
        text.setText(toastText);
        Toast toast = new Toast(context.getApplicationContext());
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setView(layout);
        toast.show();
    }

}
