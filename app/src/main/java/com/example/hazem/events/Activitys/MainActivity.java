package com.example.hazem.events.Activitys;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.hazem.events.Adapters.MyAdapter;
import com.example.hazem.events.Broadcast.AlarmReceiver;
import com.example.hazem.events.Models.Event;
import com.example.hazem.events.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;

public class MainActivity extends AppCompatActivity {

    private Realm realm;
    private RealmConfiguration config;
    int newyear =0;
    int newmonth = 0;
    int newday = 0;
    int newhour = 0;
    int newminute = 0;
    String color="";
    MyAdapter myAdapter;
    List<Event> mEvents;
    final static int RQS_1 = 1;
    @BindView(R.id.toolBar) Toolbar toolbar;
    @BindView(R.id.events_list_view) ListView events_list_view;
    @BindView(R.id.add_BTN) Button addBtn;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);

        //initializing realm database
        Realm.init(this);
        config = new RealmConfiguration.Builder().build();
        Realm.setDefaultConfiguration(config);
        realm = Realm.getDefaultInstance();

        mEvents = new ArrayList<Event>();

        myAdapter = new MyAdapter(mEvents,this);
        events_list_view.setAdapter(myAdapter);

    }

    @Override
    protected void onStart() {
        super.onStart();
        RealmResults<Event> savedEvents = realm.where(Event.class).findAll();
        for (Event event : savedEvents) {
            Event event1 = new Event(event.getId(),event.getEventName()
                    ,event.getEvenDescription(),event.getEventLocation()
                    ,event.getEventDate(),event.getColor());
            mEvents.add(event1);
            events_list_view.setAdapter(myAdapter);
        }

    }



    @OnClick(R.id.add_BTN)
    public void add(View v){
        final Calendar calendar = Calendar.getInstance();
        newyear = calendar.get(Calendar.YEAR);
        newmonth = calendar.get(Calendar.MONTH)+1;
        newday = calendar.get(Calendar.DAY_OF_MONTH);
        newhour = calendar.get(Calendar.HOUR_OF_DAY);
        newminute = calendar.get(Calendar.MINUTE);

        LayoutInflater li = LayoutInflater.from(this);
        View promptsView = li.inflate(R.layout.aad_prompts, null);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

        alertDialogBuilder.setView(promptsView);

        final RadioGroup radio_group = (RadioGroup)promptsView.findViewById (R.id.radio_group)  ;
        final EditText title_ET = (EditText)promptsView.findViewById (R.id.title_ET)  ;
        final  EditText description_ET = (EditText)promptsView.findViewById (R.id.description_ET)  ;
        final  EditText locatio_ET = (EditText)promptsView.findViewById (R.id.locatio_ET)  ;
        final   Button date_ET = (Button)promptsView.findViewById (R.id.date_ET)  ;
        final   Button time_ET = (Button)promptsView.findViewById (R.id.time_ET)  ;
        final RadioButton radioButton;

        radioButton = (RadioButton)promptsView.findViewById(R.id.yellow);
        radioButton.setChecked(true);
        color = "yellow";
        radio_group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            switch (checkedId)
            {
                case R.id.red:
                    color = "red";
                    Toast.makeText(getApplicationContext(),"Hello from view" + "red",Toast.LENGTH_SHORT).show();
                    break;
                case R.id.green:
                    color = "green";
                    Toast.makeText(getApplicationContext(),"Hello from view"+ "green",Toast.LENGTH_SHORT).show();
                    break;
                case R.id.yellow:
                    color = "yellow";
                    Toast.makeText(getApplicationContext(),"Hello from view"+ "yellow",Toast.LENGTH_SHORT).show();
                    break;
                default:
                    color = "default";
            }
        }
    });

        date_ET.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Calendar mcurrentDate = Calendar.getInstance();
                final int year = mcurrentDate.get(Calendar.YEAR);
                final int month = mcurrentDate.get(Calendar.MONTH);
                final int day = mcurrentDate.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datePickerDialog = new DatePickerDialog(MainActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                newyear = year; newmonth = monthOfYear + 1; newday = dayOfMonth;
                            }
                        }, year, month, day);
                datePickerDialog.show();   }
        });

        time_ET.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(MainActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        newhour = selectedHour; newminute = selectedMinute;

                    }
                }, hour, minute, false);//Yes 24 hour time
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();            }
        });

              alertDialogBuilder
                .setCancelable(true)
                .setPositiveButton("SAVE",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                if (title_ET.getText().toString().isEmpty()){
                                    Toast.makeText(getApplicationContext(),"Empty Name Not Allowed",Toast.LENGTH_SHORT).show();
                                    return;
                                }
                                // get user input and set it to event
                                Event event = new Event(mEvents.size(),title_ET.getText().toString()
                                        ,description_ET.getText().toString()
                                        , locatio_ET.getText().toString()
                                        ,newyear+"-"+newmonth+"-"+newday
                                        +" "+newhour+":"+newminute+"",color);
                                mEvents.add(event);
                                saveToDB(event);
                                events_list_view.setAdapter(myAdapter);
                                if(milliseconds(newday+"-"+newmonth+"-"+newyear+" "+newhour+":"+newminute) <= calendar.getTimeInMillis()){
                                    Toast.makeText(getApplicationContext(),"Wrong Time ",Toast.LENGTH_SHORT).show();
                                }else {
                                setAlarm(milliseconds(newday+"-"+newmonth+"-"+newyear+" "+newhour+":"+newminute),title_ET.getText().toString());
                                }

                            }
                        })
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                dialog.cancel();
                            }
                        });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();

    }


    private void setAlarm(long targetCal,String eventName){

        Intent intent = new Intent(getBaseContext(), AlarmReceiver.class);
        intent.putExtra("name",eventName);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getBaseContext(), RQS_1, intent, 0);
        AlarmManager alarmManager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP, targetCal, pendingIntent);
    }

    public long milliseconds(String date)
    {
        //String date_ = date;
        SimpleDateFormat sdf = new SimpleDateFormat("d-M-yyyy hh:mm");
        try
        {
            Date mDate = sdf.parse(date);
            long timeInMilliseconds = mDate.getTime();
            Toast.makeText(this,"Date in milli :: " + timeInMilliseconds,Toast.LENGTH_SHORT).show();
            return timeInMilliseconds;
        }
        catch (ParseException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return 0;
    }


    public void saveToDB(Event event){
        final Event event1 = event;
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                // This will update an existing object with the same primary key
                // or create a new object if an object with no primary key
                realm.copyToRealmOrUpdate(event1);
            }
        });

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        realm.close();
    }


}




