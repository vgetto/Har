package co.vgetto.har.ui.historydetail;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;
import co.vgetto.har.R;
import co.vgetto.har.db.entities.History;
import co.vgetto.har.db.entities.SavedFile;
import co.vgetto.har.ui.historylist.HistoryListLayout;
import com.jakewharton.rxbinding.view.RxView;
import java.text.DateFormat;
import java.util.List;
import java.util.TimeZone;

/**
 * Created by Kovje on 12.9.2015..
 */
public class HistoryDetailAdapter extends RecyclerView.Adapter<HistoryDetailAdapter.ViewHolder> {
  private List<SavedFile> history;
  private FileListClicks fragmentCallback;
  private DateFormat df = DateFormat.getDateTimeInstance();

  public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    @Bind(R.id.tvFilePath) TextView tvFilePath;
    @Bind(R.id.tvRecordedAt) TextView tvRecordedAt;
    @Bind(R.id.tvSynced) TextView tvSynced;

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

    @Override public void onClick(View v) {
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

    public interface IMyViewHolderClicks {
      void cardClicked(int position);
    }
  }

  // Create new views (invoked by the layout manager)
  @Override public HistoryDetailAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
      int viewType) {
    // create a new view
    View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.history_detail, parent, false);
    // set the view's size, margins, paddings and layout parameters

    HistoryDetailAdapter.ViewHolder vh = new ViewHolder(v, new ViewHolder.IMyViewHolderClicks() {
      @Override public void cardClicked(int position) {
        fragmentCallback.cardClicked(history.get(position));
      }
    });

    return vh;
  }

  // Provide a suitable constructor (depends on the kind of dataset)
  public HistoryDetailAdapter(HistoryDetailLayout layout, List<SavedFile> files) {
    try {
      this.fragmentCallback = ((FileListClicks) layout);
    } catch (ClassCastException e) {
      throw new ClassCastException("Fragment must implement ViewClickCallback.");
    }
    this.history = files;
  }

  // Replace the contents of a view (invoked by the layout manager)
  @Override public void onBindViewHolder(ViewHolder holder, int position) {
    // - get element from your dataset at this position
    // - replace the contents of the view with that element
    df.setTimeZone(TimeZone.getDefault());

    SavedFile file = history.get(position);

    holder.tvFilePath.setText(file.filePath());
    holder.tvRecordedAt.setText(df.format(file.recordingEndedTime()));
    if (file.synced()) {
      holder.tvSynced.setText("File synced with dropbox");
    } else {
      holder.tvSynced.setText("Not synced");
    }
  }

  // Return the size of your dataset (invoked by the layout manager)
  @Override public int getItemCount() {
    if (history != null) {
      return history.size();
    } else {
      return 0;
    }
  }

  public static interface FileListClicks {
    void cardClicked(SavedFile file);
  }
}
