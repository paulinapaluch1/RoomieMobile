package com.pm.roomie.roomie.activities.queue;

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

import com.pm.roomie.roomie.R;
import com.pm.roomie.roomie.remote.ApiUtils;
import com.pm.roomie.roomie.remote.UserService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.pm.roomie.roomie.CurrentLoggedUser.getUser;
import static com.pm.roomie.roomie.R.layout.row;

public class QueueAdapter extends ArrayAdapter<String> {
    private UserService userService;
    private Context context;

    private String[] names;
    private String[] products;


    public QueueAdapter(@NonNull Context context,  String[] names,String[] products) {
        super(context, row,names);
        this.context = context;
        this.names = names;
        this.products = products;
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

        addHistoryListener(products[position], layoutInflater, row);
        addRegisterListener(products[position], layoutInflater, row);

        return row;
    }


    private void addRegisterListener(String productName, LayoutInflater layoutInflater, View row) {
        Button registerButton = row.findViewById(R.id.register);
        registerButton.setOnClickListener(v -> {
            Call<Boolean> call = userService.registerBuying(productName, getUser().getId());

            call.enqueue(new Callback<Boolean>() {
                @Override
                public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                    if (response.isSuccessful()) {
                        Boolean resObj = response.body();
                        if(resObj){
                            createToast("Zarejestrowano zakup", layoutInflater, row,context);
                        } else {
                            createToast("Rejestracja zakupu nie powiodła się", layoutInflater, row,context);
                        }}else{
                        createToast("Wystąpił błąd response unsuccessful", layoutInflater, row,context);
                    }
                }

                @Override
                public void onFailure(Call<Boolean> call, Throwable t) {
                    createToast("Wystąpił błąd. Spróbuj ponownie", layoutInflater, row,context);

                }

            });

        });

    }

    private void addHistoryListener(String product, LayoutInflater layoutInflater, View row) {
        Button archiveButton = (Button) row.findViewById(R.id.history);
        archiveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ProductHistoryActivity.class);
                intent.putExtra("name",product);
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
