package com.example.living.myapplication;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TimePicker;

public class SitePickerFragment extends DialogFragment {

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.activity_site_history_picker, null);
        final TimePicker timePicker = (TimePicker) view.findViewById(R.id.site_history_timepicker);
        final DatePicker datePicker = (DatePicker) view.findViewById(R.id.site_history_datapicker);
        datePicker.setCalendarViewShown(false);
        datePicker.setSpinnersShown(true);

        timePicker.setIs24HourView(true);
        builder.setView(view);
//                .setPositiveButton(android.R.string.ok,
//                        new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int id) {
//                                int arrive_hour = timePicker.getCurrentHour();
//                                int arrive_min = timePicker.getCurrentMinute();
////                                String timeStr = formatTime(arrive_hour, arrive_min);
//                                int arrive_year = datePicker.getYear();
//                                int arrive_month = datePicker.getMonth();
//                                int arrive_day = datePicker.getDayOfMonth();
//                                Calendar calendar = Calendar.getInstance();
//                                calendar.set(arrive_year, arrive_month, arrive_day, arrive_hour, arrive_min, 0);
//                                long l = calendar.getTimeInMillis();
////                                String dateStr = formatDate(arrive_year, arrive_month, arrive_day);
////                                dateStr += " " + timeStr;
//                            }
//                        });
        return builder.create();
    }

//    private String formatDate(int arrive_year, int arrive_month, int arrive_day) {
//        String result = "" + arrive_year + "-";
//        if (arrive_month < 10) {
//            result += "0" + arrive_month;
//        } else {
//            result += arrive_month;
//        }
//        result += "-";
//        if (arrive_day < 10) {
//            result += "0" + arrive_day;
//        } else {
//            result += arrive_day;
//        }
//        return result;
//    }

//    private String formatTime(int h, int m) {
//        String result = "";
//        if (h < 10) {
//            result += "0" + h;
//        } else {
//            result += "" + h;
//        }
//        result += ":";
//        if (m < 10) {
//            result += "0" + m;
//        } else {
//            result += "" + m;
//        }
//        return result;
//    }
}
