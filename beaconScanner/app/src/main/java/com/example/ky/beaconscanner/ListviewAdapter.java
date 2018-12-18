package com.example.ky.beaconscanner;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import static android.support.v4.content.ContextCompat.startActivity;

class ListviewAdapter extends BaseAdapter {

    private LayoutInflater inflater;
    private ArrayList<Listviewitem> data;
    private int layout;
    private Context context;

    public ListviewAdapter(Context context, int layout, ArrayList<Listviewitem> data){
        this.context=context;
        this.inflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.data=data;
        this.layout=layout;
    }
    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }
    public boolean getFlag(int position){
        return data.get(position).getFlag();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView==null){
            convertView=inflater.inflate(layout,parent,false);
        }
        Listviewitem listviewitem=data.get(position);
        TextView name=(TextView)convertView.findViewById(R.id.name);
        TextView phone=(TextView)convertView.findViewById(R.id.phone);
        TextView company=(TextView)convertView.findViewById(R.id.company);
        TextView product=(TextView)convertView.findViewById(R.id.product);
        TextView meter=(TextView)convertView.findViewById(R.id.meter);

        name.setText(listviewitem.getName());
        phone.setText(listviewitem.getPhone());
        company.setText(listviewitem.getCompany());
        product.setText(listviewitem.getProduct());
        meter.setText(listviewitem.getMeter());

        if(getFlag(position)){
            convertView.setBackgroundColor(Color.parseColor("#FFC0CB"));
//            convertView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse( "http://cclab.cbnu.ac.kr/node/profile/"+));
//                    context.startActivity(intent);
//                }
//            });
        }

        return convertView;
    }
}
