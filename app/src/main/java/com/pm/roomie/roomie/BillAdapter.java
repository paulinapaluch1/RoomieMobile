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

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.pm.roomie.roomie.CurrentLoggedUser.getBill;

public class BillAdapter extends ArrayAdapter<String> {

    private UserService userService;
    private Context context;
    private Integer[] ids;
    private String[] names;
    private String[] dates;
    private Double[] amounts;

    public BillAdapter(@NonNull Context context,  String[] names, String[] dates, Double[] amounts, Integer[] ids ) {
        super(context, R.layout.row_bills_admin,names);
        this.context = context;
        this.names = names;
        this.dates = dates;
        this.amounts = amounts;
        this.ids = ids;
        this.userService = ApiUtils.getUserService();
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View row = layoutInflater.inflate(R.layout.row_bills_admin, parent, false);
        TextView name = row.findViewById(R.id.nameAdmin);
        TextView date = row.findViewById(R.id.dateAdmin);
        TextView amount = row.findViewById(R.id.amountAdmin);

        name.setText(names[position]);
        date.setText("Data: " + dates[position]);
        amount.setText("Kwota: " + amounts[position]+" zł");

        addDetailsListener(ids[position], row);
        editBillListener(ids[position], row);

        return row;
    }

    private void addDetailsListener(Integer id, View row) {

        Button editButton = (Button) row.findViewById(R.id.detailsAdmin);
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ShowBillsDetailsActivity.class);
                intent.putExtra("id", id);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });
    }

    private void editBillListener(Integer id, View row) {

        Button detailsButton = (Button) row.findViewById(R.id.editAdmin);
        detailsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, EditBillFormActivity.class);
//                intent.putExtra("billId",  getBill().getId());
                intent.putExtra("id", id);
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