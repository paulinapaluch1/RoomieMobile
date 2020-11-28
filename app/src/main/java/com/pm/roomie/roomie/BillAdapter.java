package com.pm.roomie.roomie;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.pm.roomie.roomie.model.User;
import com.pm.roomie.roomie.remote.ApiUtils;
import com.pm.roomie.roomie.remote.UserService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.pm.roomie.roomie.R.layout.row_bills;

import java.util.Date;

public class BillAdapter extends ArrayAdapter<String> {

    private UserService userService;
    private Context context;
    private Integer[] ids;
    private String[] names;
    private String[] dates;
    private Double[] amounts;

    public BillAdapter(@NonNull Context context,  String[] names, String[] dates, Double[] amounts, Integer[] ids ) {
        super(context, R.layout.row_bills,names);
        this.context = context;
        this.names = names;
        this.dates = dates;
        this.amounts = amounts;
        this.ids = ids;
        this.userService = ApiUtils.getUserService();
    }
//
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View row = layoutInflater.inflate(R.layout.row_bills, parent, false);
        TextView name = row.findViewById(R.id.name);
        TextView date = row.findViewById(R.id.date);
        TextView amount = row.findViewById(R.id.amount);

        name.setText(names[position]);
        date.setText("Data: " + dates[position]);
        amount.setText("Kwota: " + amounts[position]+"zł");

        return row;
    }
}
