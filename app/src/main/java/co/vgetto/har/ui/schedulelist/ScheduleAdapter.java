package co.vgetto.har.ui.schedulelist;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;
import co.vgetto.har.R;
import co.vgetto.har.db.entities.Schedule;
import java.text.DateFormat;
import java.util.List;
import java.util.TimeZone;
import timber.log.Timber;

/**
 * Created by Kovje on 12.9.2015..
 */
public class ScheduleAdapter extends RecyclerView.Adapter<ScheduleAdapter.ViewHolder> {
    private List<Schedule> schedules;
    private ScheduleListClicks fragmentCallback;
    private DateFormat df = DateFormat.getDateTimeInstance();

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        // each data item is just a string in this case
        @Bind(R.id.tvStartTime)
        TextView tvStartTime;

        @Bind(R.id.tvStatus)
        TextView tvStatus;

        @Bind(R.id.tvSynced)
        TextView tvSynced;

        @Bind(R.id.btnEdit)
        Button btnEdit;

        @Bind(R.id.btnDelete)
        Button btnDelete;

        public IMyViewHolderClicks mListener;

        public ViewHolder(View v, IMyViewHolderClicks listener) {
            super(v);
            ButterKnife.bind(this, v);
            this.mListener = listener;
        //    mTextView = (TextView)v.findViewById(R.id.tvSchedule);

            btnEdit.setTag(R.id.type_tag, "edit");
            btnEdit.setOnClickListener(this);
            btnDelete.setTag(R.id.type_tag, "delete");
            btnDelete.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (v instanceof TextView) {
                int[] location = new int[2];
                v.getLocationOnScreen(location);
                Timber.i("Location of clicked button : " + Integer.toString(location[0]) + " " + Integer.toString(location[1]));
                int position = getAdapterPosition();
                String tag = (String) v.getTag(R.id.type_tag);
                if (tag.equals("edit")) {
                    mListener.onEdit(position);
                } else {
                    mListener.onDelete(position);
                }
            }
        }

        public  interface IMyViewHolderClicks {
            void onEdit(int position);
            void onDelete(int position);
        }
    }



    // Create new views (invoked by the layout manager)
    @Override
    public ScheduleAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                   int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.schedule_item, parent, false);
        // set the view's size, margins, paddings and layout parameters

        ScheduleAdapter.ViewHolder vh = new ViewHolder(v, new ScheduleAdapter.ViewHolder.IMyViewHolderClicks() {
            @Override
            public void onEdit(int position) {
                fragmentCallback.editClicked(schedules.get(position));
            }

            @Override
            public void onDelete(int position) {
                fragmentCallback.deleteClicked(schedules.get(position));
            }
        });
        return vh;
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public ScheduleAdapter(ScheduleListLayout layout, List<Schedule> schedules) {
        try {
            this.fragmentCallback = ((ScheduleListClicks) layout);
        } catch (ClassCastException e) {
            throw new ClassCastException("Fragment must implement ViewClickCallback.");
        }
        this.schedules = schedules;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        df.setTimeZone(TimeZone.getDefault());

        Schedule s = schedules.get(position);

        holder.tvStartTime.setText(df.format(s.scheduleConfiguration().startTime()));

        holder.tvStatus.setText(s.toString());
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        if(schedules != null)
            return schedules.size();
        else
            return 0;
    }

    public static interface ScheduleListClicks {
        void editClicked(Schedule s);
        void deleteClicked(Schedule s);
    }
}
