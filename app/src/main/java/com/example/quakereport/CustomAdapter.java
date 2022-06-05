package com.example.quakereport;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.graphics.drawable.GradientDrawable;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class CustomAdapter extends ArrayAdapter<Earthquake> {

    public CustomAdapter(@NonNull Context context, @NonNull List<Earthquake> objects) {
        super(context, 0, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listview= convertView;

        if(listview==null){
            listview= LayoutInflater.from(getContext()).inflate(R.layout.list_item,parent,false);
        }

        Earthquake currTab= getItem(position);

        TextView mag= listview.findViewById(R.id.magText);
        TextView place= listview.findViewById(R.id.placeText);
        TextView date= listview.findViewById(R.id.dateText);
        TextView time= listview.findViewById(R.id.timeText);
        TextView place2= listview.findViewById(R.id.placeText2);

        DecimalFormat decimalFormat= new DecimalFormat("#.#");
        String finalMag= decimalFormat.format(currTab.getMagnitude());
        mag.setText(finalMag);
        String initial= currTab.getCityname();
        int found= initial.indexOf("of");

        if(found==-1){
            place.setText("Near the");
            place2.setText(initial);
        }
        else {
            String[] seperatedCity = initial.split("of ", 2);
            seperatedCity[0] += "of";
            place.setText(seperatedCity[0]);
            place2.setText(seperatedCity[1]);
        }

        Date date1= new Date(currTab.getDate());
        String formattedDate= formatDate(date1);
        date.setText(formattedDate);

        String formattedTime= formatTime(date1);
        time.setText(formattedTime);

        GradientDrawable magnitudeCircle = (GradientDrawable) mag.getBackground();

        // Get the appropriate background color based on the current earthquake magnitude
        int magnitudeColor = getMagnitudeColor(currTab.getMagnitude());

        // Set the color on the magnitude circle
        magnitudeCircle.setColor(magnitudeColor);

        return listview;
    }

    private String formatTime(Date date1) {
        SimpleDateFormat timeFormat= new SimpleDateFormat("h:mm a");
        return timeFormat.format(date1);
    }

    private String formatDate(Date date) {
        SimpleDateFormat dateFormatter= new SimpleDateFormat("LLL dd, yyyy");
        return dateFormatter.format(date);

    }

    private int getMagnitudeColor(double magnitude){
        int magnitudeColorResource;
        int magnitudefloor= (int) Math.floor(magnitude);
        switch (magnitudefloor){
            case 0:
            case 1:
                magnitudeColorResource= R.color.magnitude1;
                break;
            case 2:
                magnitudeColorResource=R.color.magnitude2;
                break;
            case 3:
                magnitudeColorResource=R.color.magnitude3;
                break;
            case 4:
                magnitudeColorResource=R.color.magnitude4;
                break;
            case 5:
                magnitudeColorResource=R.color.magnitude5;
                break;
            case 6:
                magnitudeColorResource=R.color.magnitude6;
                break;
            case 7:
                magnitudeColorResource=R.color.magnitude7;
                break;
            case 8:
                magnitudeColorResource=R.color.magnitude8;
                break;
            case 9:
                magnitudeColorResource=R.color.magnitude9;
                break;
            default:
                magnitudeColorResource=R.color.magnitude10plus;

        }

        return ContextCompat.getColor(getContext(),magnitudeColorResource);
    }
}
