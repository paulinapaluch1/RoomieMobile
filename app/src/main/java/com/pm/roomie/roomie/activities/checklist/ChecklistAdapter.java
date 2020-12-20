package com.pm.roomie.roomie.activities.checklist;

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

public class ChecklistAdapter extends ArrayAdapter<String> {
    private UserService userService;
    private Context context;
    private Integer[] ids;
    private String[] items;


    public ChecklistAdapter(@NonNull Context context, String[] items, Integer[] ids) {
        super(context, row,items);
        this.context = context;
        this.items = items;
        this.ids = ids;
        this.userService =  ApiUtils.getUserService();
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View row = layoutInflater.inflate(R.layout.checklist_row, parent, false);
        TextView name = row.findViewById(R.id.item);

        name.setText(items[position]);
        addDeleteItemListener(position, layoutInflater, row);

        return row;
    }
    private void addDeleteItemListener(Integer position, LayoutInflater layoutInflater, View row) {
        Button deleteButton = (Button) row.findViewById(R.id.delete);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Call<Boolean> call = userService.deleteItem(items[position],getUser().getId());

                call.enqueue(new Callback<Boolean>() {
                    @Override
                    public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                        if (response.isSuccessful()) {
                            Boolean resObj = response.body();
                            if (resObj) {
                                removeTheElement(items,position);
                                Intent intent = new Intent(context, ChecklistActivity.class);

                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                context.startActivity(intent);

                                createToast("Usunięto element", layoutInflater, row, context);
                            } else {
                                createToast("Usunięcie nie powiodło się", layoutInflater, row, context);
                            }
                        } else {
                            createToast("Wystąpił błąd response unsuccessful", layoutInflater, row, context);
                        }
                    }

                    @Override
                    public void onFailure(Call<Boolean> call, Throwable t) {
                        createToast("Wystąpił błąd. Spróbuj ponownie", layoutInflater, row, context);

                    }

                });

            }
        });
    }


    public static String[] removeTheElement(String[] arr,
                                         int index)
    {

        if (arr == null
                || index < 0
                || index >= arr.length) {

            return arr;
        }

        // Create another array of size one less
        String[] anotherArray = new String[arr.length - 1];

        // Copy the elements except the index
        // from original array to the other array
        for (int i = 0, k = 0; i < arr.length; i++) {

            // if the index is
            // the removal element index
            if (i == index) {
                continue;
            }

            // if the index is not
            // the removal element index
            anotherArray[k++] = arr[i];
        }

        // return the resultant array
        return anotherArray;
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
