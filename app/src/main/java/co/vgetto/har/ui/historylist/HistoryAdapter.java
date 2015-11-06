package co.vgetto.har.ui.historylist;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;
import co.vgetto.har.R;
import co.vgetto.har.db.entities.History;
import co.vgetto.har.db.entities.Trigger;
import co.vgetto.har.ui.triggerlist.TriggerListLayout;
import java.text.DateFormat;
import java.util.List;
import java.util.TimeZone;
import timber.log.Timber;

/**
 * Created by Kovje on 12.9.2015..
 */
public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.ViewHolder> {
    private List<History> history;
    private HistoryListClicks fragmentCallback;
    private DateFormat df = DateFormat.getDateTimeInstance();

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @Bind(R.id.tvHistoryType)
        TextView tvHistoryType;

        @Bind(R.id.tvHistoryState)
        TextView tvHistoryState;

        @Bind(R.id.tvHistoryStartedDate)
        TextView tvHistoryStartedDate;

        @Bind(R.id.tvHistoryEndedDate)
        TextView tvHistoryEndedDate;

        @Bind(R.id.tvHistorySavedFiles)
        TextView tvHistorySavedFiles;

        public IMyViewHolderClicks mListener;

        public ViewHolder(View v, IMyViewHolderClicks listener) {
            super(v);
            ButterKnife.bind(this, v);
            this.mListener = listener;
            v.setOnClickListener(this);
        //    mTextView = (TextView)v.findViewById(R.id.tvSchedule);

            /*
            btnEdit.setTag(R.id.type_tag, "edit");
            btnEdit.setOnClickListener(this);
            btnDelete.setTag(R.id.type_tag, "delete");
            btnDelete.setOnClickListener(this);
            */
        }

        @Override
        public void onClick(View v) {
            /*
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
            */
            mListener.cardClicked(getAdapterPosition());
        }

        public  interface IMyViewHolderClicks {
            void cardClicked(int position);
        }
    }



    // Create new views (invoked by the layout manager)
    @Override
    public HistoryAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                   int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.history_item, parent, false);
        // set the view's size, margins, paddings and layout parameters

        HistoryAdapter.ViewHolder vh = new ViewHolder(v, new ViewHolder.IMyViewHolderClicks() {
            @Override public void cardClicked(int position) {
                fragmentCallback.cardClicked(history.get(position));
            }
        });

        return vh;
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public HistoryAdapter(HistoryListLayout layout, List<History> history) {
        try {
            this.fragmentCallback = ((HistoryListClicks) layout);
        } catch (ClassCastException e) {
            throw new ClassCastException("Fragment must implement ViewClickCallback.");
        }
        this.history = history;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        df.setTimeZone(TimeZone.getDefault());

        History h = history.get(position);

        holder.tvHistoryType.setText(Integer.toString(h.type()));
        holder.tvHistoryState.setText(Integer.toString(h.state()));
        holder.tvHistoryStartedDate.setText(df.format(h.startedRecordingDate()));
        holder.tvHistoryEndedDate.setText(df.format(h.endedRecordingDate()));
        holder.tvHistorySavedFiles.setText(h.savedFiles().toString());
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        if(history != null)
            return history.size();
        else
            return 0;
    }

    public static interface HistoryListClicks {
        void cardClicked(History h);
    }
}
