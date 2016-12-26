package com.example.hazem.events.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hazem.events.Activitys.MainActivity;
import com.example.hazem.events.Models.Event;
import com.example.hazem.events.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by Hazem on 12/25/2016.
 */

public class MyAdapter extends BaseAdapter{


    MainActivity mainActivity;
    private List<Event> mEvent;
    private Context mContext;

    public MyAdapter(List<Event> mEvent, Context mContext) {
        this.mEvent = mEvent;
        this.mContext = mContext;
    }

    @Override
    public int getCount() {
        return mEvent.size();
    }

    @Override
    public Object getItem(int position) {
        return mEvent.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        ViewHolder holder;

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater)
                    mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.event_item, parent,false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        }
        else {
            holder = (ViewHolder) convertView.getTag();
        }

        final Event event = mEvent.get(position);
        holder.event_date_txt.setText(String.valueOf(event.getEventDate()));
        holder.event_description_text.setText(event.getEvenDescription());
        holder.event_location_txt.setText(event.getEventLocation());
        holder.event_name_txt.setText(event.getEventName());
        holder.eventColor.setBackgroundColor(mContext.getResources().getColor(getColor(event.getColor())));
        holder.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popup = new PopupMenu(mContext, v);
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        int id = item.getItemId();
                        switch (id)
                        {
                            case R.id.delete_settings:
                                deleteFromDB(event.getId(),position);
                                break;
                        }
                        return true;                    }
                });
                MenuInflater inflater = popup.getMenuInflater();
                inflater.inflate(R.menu.menu, popup.getMenu());

                popup.show();
            }
        });

        return convertView;
    }

    private int getColor(String color) {
        if (color.equals("red")){
            return R.color.colorRed;
        }else if (color.equals("green")){
            return R.color.colorGreen;
        }else if (color.equals("yellow")){
            return R.color.colorYellow;
        }else
        return 0;
    }

    public void deleteFromDB(int id,int position){
        Realm realm = Realm.getDefaultInstance();
        final RealmResults<Event> result2 = realm.where(Event.class)
                .equalTo("id", id)
                .findAll();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                // Delete all matches
                result2.deleteAllFromRealm();
                Toast.makeText(mContext, "Deleted", Toast.LENGTH_SHORT).show();
            }
        });
        mEvent.remove(position);
        notifyDataSetChanged();

    }


    static class ViewHolder {
        @BindView(R.id.event_date_txt) TextView event_date_txt;
        @BindView(R.id.event_description_text) TextView event_description_text;
        @BindView(R.id.event_image) ImageView event_image;
        @BindView(R.id.event_location_txt) TextView event_location_txt;
        @BindView(R.id.event_name_txt) TextView event_name_txt;
        @BindView(R.id.overlay_bttn) ImageButton button;
        @BindView(R.id.event_color) View eventColor;
        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }



}
