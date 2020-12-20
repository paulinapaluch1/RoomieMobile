package com.pm.roomie.roomie;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.pm.roomie.roomie.remote.ApiUtils;
import com.pm.roomie.roomie.remote.UserService;

public class TimetableAdapter extends ArrayAdapter<String>  {
    private UserService userService;
    private Context context;
    private Integer[] ids;
    private String[] names;
    private String[] dates;

    public TimetableAdapter(@NonNull Context context, String[] names, String[] dates, Integer[] ids ) {
        super(context, R.layout.row_timetable,names);
        this.context = context;
        this.names = names;
        this.dates = dates;
        this.ids = ids;
        this.userService = ApiUtils.getUserService();
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View row = layoutInflater.inflate(R.layout.row_timetable, parent, false);
        TextView name = row.findViewById(R.id.nameTimetable);
        TextView date = row.findViewById(R.id.dateTimetable);

        name.setText(names[position]);
        date.setText(dates[position]);

        return row;
    }

}
