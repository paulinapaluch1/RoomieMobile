package com.pm.roomie.roomie;

    import android.content.Intent;
    import android.os.Build;
    import android.os.Bundle;
    import android.view.LayoutInflater;
    import android.view.View;
    import android.view.ViewGroup;
    import android.widget.Button;
    import android.widget.CheckBox;
    import android.widget.ListView;
    import android.widget.TextView;
    import android.widget.Toast;

    import androidx.annotation.RequiresApi;
    import androidx.appcompat.app.AppCompatActivity;

    import com.pm.roomie.roomie.model.Bill;
    import com.pm.roomie.roomie.model.MembersBill;
    import com.pm.roomie.roomie.model.User;
    import com.pm.roomie.roomie.remote.ApiUtils;
    import com.pm.roomie.roomie.remote.UserService;

    import java.util.ArrayList;
    import java.util.Date;
    import java.util.stream.Collectors;

    import retrofit2.Call;
    import retrofit2.Callback;
    import retrofit2.Response;
    import java.text.SimpleDateFormat;
    import java.text.ParseException;


public class MembersBillsActivity extends AppCompatActivity {
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_bills);

    private UserService userService;

    ListView listView;
    String[] names = {"Internet", "Prąd"};
    String[] dates={"03-11-2020", "21-11-2020"};
    Double[] dividedamounts = {15.0, 25.5};
    boolean[] paid = {false, true};
    CheckBox pay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bills);
        userService = ApiUtils.getUserService();
        Bundle extras = getIntent().getExtras();
        int currentUserId=0;
        if (extras != null) {
            currentUserId= (int)extras.get("userId");
        }

        Call<ArrayList<MembersBill>> call = userService.getUserBills(currentUserId);

        call.enqueue(new Callback<ArrayList<MembersBill>>() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onResponse(Call<ArrayList<MembersBill>> call, Response<ArrayList<MembersBill>> response) {
                if (response.isSuccessful()) {
                    ArrayList<MembersBill> membersBillsList = response.body();
                    if((membersBillsList!=null)){
                        createToast("ok");
                        int size = membersBillsList.size();
//                        ArrayList<String> namesList=(ArrayList) billsList.stream().map(f->f.getBillType().getType()).collect(Collectors.toList());
//                        ArrayList<String> namesList=(ArrayList) memberBillsList.stream().map(f->f.concat(" ")).collect(Collectors.toList());
//                        names = getStringArray(namesList);
//                        ArrayList<String> datesList=(ArrayList) memberBillsList.stream().map(f->f.getDate()).collect(Collectors.toList());
//                        dates = getDateArray(datesList);
//                        ArrayList<Double> dividedAmountsList=(ArrayList) membersBillsList.stream().map(f->f.getDividedAmount()).collect(Collectors.toList());
//                        dividedamounts = getDoubleArray(dividedAmountsList);
                        ArrayList<Integer> idsList=(ArrayList) membersBillsList.stream().map(f->f.getId()).collect(Collectors.toList());
                        Integer[] ids = getIntegerArray(idsList);
//
                        listView = findViewById(R.id.listViewBills);
                        MembersBillAdapter billAdapter = new MembersBillAdapter(getApplicationContext(),names,dates,dividedamounts, ids);
                        listView.setAdapter(billAdapter);
                    } else {
                        createToast("Lista rachunków jest pusta");
                    }}else{
                    createToast("Wystąpił błąd. Spróbuj ponownie");
                }
            }

            //
            @Override
            public void onFailure(Call<ArrayList<MembersBill>> call, Throwable t) {
                Toast.makeText(com.pm.roomie.roomie.MembersBillsActivity.this,t.getMessage(),Toast.LENGTH_LONG).show();
            }
        });

    }

//    public void mark_payment(View v) {
//        if (((CheckBox) v).isChecked()) {
//            Toast.makeText(MembersBillsActivity.this,"Zapłacone", Toast.LENGTH_LONG).show());
//        }
//    }

    private void createToast(String toastText) {
        LayoutInflater inflater = getLayoutInflater();
        View layout = inflater.inflate(R.layout.custom_toast,
                (ViewGroup) findViewById(R.id.custom_toast_container));
        TextView text = (TextView) layout.findViewById(R.id.text);
        text.setText(toastText);
        Toast toast = new Toast(getApplicationContext());
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setView(layout);
        toast.show();
    }

    public static String[] getStringArray(ArrayList<String> arr)
    {
        String str[] = new String[arr.size()];
        for (int j = 0; j < arr.size(); j++) {
            str[j] = arr.get(j);
        }
        return str;
    }

    public static String[] getDateArray(ArrayList<String> arr)
    {
        String str[] = new String[arr.size()];
        for (int j = 0; j < arr.size(); j++) {
            str[j] = arr.get(j);
            str[j] = str[j].substring(0,10);
        }
        return str;
    }

    public static Double[] getDoubleArray(ArrayList<Double> arr)
    {
        Double str[] = new Double[arr.size()];
        for (int j = 0; j < arr.size(); j++) {
            str[j] = arr.get(j);
        }
        return str;
    }


    public static Integer[] getIntegerArray(ArrayList<Integer> arr)
    {
        Integer str[] = new Integer[arr.size()];
        for (int j = 0; j < arr.size(); j++) {
            str[j] = arr.get(j);
        }
        return str;
    }
}