package com.pm.roomie.roomie;

import android.content.Context;
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

import static com.pm.roomie.roomie.R.layout.row;

public class FlatmateAdapter extends ArrayAdapter<String> {
    private UserService userService;
    private Context context;
    private Integer[] ids;
    private String[] names;
    private String[] debits;
    private String[] phones;

    public FlatmateAdapter(@NonNull Context context,  String[] names,String[] debits,String[] phones, Integer[] ids) {
        super(context, row,names);
        this.context = context;
        this.names = names;
        this.debits = debits;
        this.phones = phones;
        this.ids = ids;
        this.userService =  ApiUtils.getUserService();
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View row = layoutInflater.inflate(R.layout.row, parent, false);
        TextView name = row.findViewById(R.id.name);
        TextView hour = row.findViewById(R.id.debit);
        TextView type = row.findViewById(R.id.phone);

        name.setText(names[position]);
        hour.setText("Saldo: " + debits[position]+"zł");
        type.setText("Telefon: " + phones[position]);

        Button archiveButton = (Button) row.findViewById(R.id.archive);
        archiveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Call<Boolean> call = userService.archiveUser(ids[position]);

                call.enqueue(new Callback<Boolean>() {
                    @Override
                    public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                        if (response.isSuccessful()) {
                            Boolean resObj = response.body();
                            if(resObj){
                                createToast("Zarchiwizowano użytkownika", layoutInflater, row,context);


                            } else {
                                createToast("Archiwizacja nie powiodla się", layoutInflater, row,context);
                            }}else{
                            createToast("Wystąpił błąd spróbuj ponownie", layoutInflater, row,context);
                        }
                    }

                    @Override
                    public void onFailure(Call<Boolean> call, Throwable t) {
                        createToast("Wystąpił błąd spróbuj ponownie", layoutInflater, row,context);

                    }

                });

            }
        });





        return row;
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
