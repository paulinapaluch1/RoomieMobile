package com.pm.roomie.roomie;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class FlatmateAdapter extends ArrayAdapter<String> {

    Context context;
    String[] names;
    String[] debits;
    String[] phones;

    public FlatmateAdapter(@NonNull Context context,  String[] names,String[] debits,String[] phones ) {
        super(context, R.layout.row,names);
        this.context = context;
        this.names = names;
        this.debits = debits;
        this.phones = phones;
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
        hour.setText("Saldo: "+debits[position]);
        type.setText("Telefon: "+phones[position]);

        return row;
    }
}
