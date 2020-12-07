package com.pm.roomie.roomie;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.pm.roomie.roomie.remote.ApiUtils;
import com.pm.roomie.roomie.remote.UserService;

public class MembersBillAdapter extends ArrayAdapter<String> {

    private UserService userService;
    private Context context;
    private Integer[] ids;
    private String[] names;
    private String[] dates;
    Double[] dividedamounts;
    boolean[] paid;

    public MembersBillAdapter(@NonNull Context context, String[] names, String[] dates, Double[] dividedamounts, Integer[] ids ) {
        super(context, R.layout.row_bills_user,names);
        this.context = context;
        this.names = names;
        this.dates = dates;
        this.dividedamounts = dividedamounts;
        this.ids = ids;
        this.userService = ApiUtils.getUserService();
    }
    //
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View row = layoutInflater.inflate(R.layout.row_bills_user, parent, false);
        TextView name = row.findViewById(R.id.name);
        TextView date = row.findViewById(R.id.date);
        TextView amount = row.findViewById(R.id.amount);
        CheckBox pay = row.findViewById(R.id.checkbox_bill);

        name.setText(names[position]);
        date.setText("Data: " + dates[position]);
        amount.setText("Kwota: " + dividedamounts[position]+" zł");

        addDetailsListener(ids[position], row);
        addPaidListener(ids[position], row);

        return row;
    }

    private void addDetailsListener(Integer id, View row) {

        Button detailsButton = (Button) row.findViewById(R.id.details);
        detailsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ShowBillsDetailsUserActivity.class);
                intent.putExtra("id", id);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });
    }

    private void addPaidListener(Integer id, View row) {

        CheckBox paidButton = (CheckBox) row.findViewById(R.id.checkbox_bill);
        paidButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (paidButton.isChecked()) {
                    System.out.println("Zaznaczone");
                } else {
                    System.out.println("Nie zaznaczone");
                }
            }
        });
    }

//    public void mark_payment(View v) {
//        if (((CheckBox) v).isChecked()) {
//            Toast.makeText(MembersBillsActivity.this,"Zapłacone", Toast.LENGTH_LONG).show());
//        }
//    }

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