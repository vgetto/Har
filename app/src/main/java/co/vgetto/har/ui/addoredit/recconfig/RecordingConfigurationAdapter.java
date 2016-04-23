package co.vgetto.har.ui.addoredit.recconfig;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;
import co.vgetto.har.R;
import co.vgetto.har.db.entities.ConfigurationValue;
import java.text.DateFormat;
import java.util.List;
import java.util.TimeZone;
import timber.log.Timber;

/**
 * Created by Kovje on 12.9.2015..
 */
public class RecordingConfigurationAdapter
    extends RecyclerView.Adapter<RecordingConfigurationAdapter.ViewHolder> {
  private List<List<ConfigurationValue>> configurationValuesList;
  private RecordingConfigurationListClicks fragmentCallback;
  private DateFormat df = DateFormat.getDateTimeInstance();

  // Provide a reference to the views for each data item
  // Complex data items may need more than one view per item, and
  // you provide access to all the views for a data item in a view holder
  public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    // each data item is just a string in this case
    @Bind(R.id.tvCurrentSelection) TextView tvCurrentSelection;

    @Bind(R.id.btnOne) Button btnOne;

    @Bind(R.id.btnTwo) Button btnTwo;

    @Bind(R.id.btnThree) Button btnThree;

    @Bind(R.id.btnFour) Button btnFour;

    @Bind(R.id.btnFive) Button btnFive;

    @Bind(R.id.btnAdd) Button btnAdd;

    public IMyViewHolderClicks mListener;

    public ViewHolder(View v, IMyViewHolderClicks listener) {
      super(v);
      ButterKnife.bind(this, v);
      this.mListener = listener;
      //    mTextView = (TextView)v.findViewById(R.id.tvSchedule);

      btnOne.setTag(R.id.type_tag, "btnOne");
      btnOne.setOnClickListener(this);

      btnTwo.setTag(R.id.type_tag, "btnTwo");
      btnTwo.setOnClickListener(this);

      btnThree.setTag(R.id.type_tag, "btnThree");
      btnThree.setOnClickListener(this);

      btnFour.setTag(R.id.type_tag, "btnFour");
      btnFour.setOnClickListener(this);

      btnFive.setTag(R.id.type_tag, "btnFive");
      btnFive.setOnClickListener(this);

      btnAdd.setTag(R.id.type_tag, "btnAdd");
      btnAdd.setOnClickListener(this);
    }

    @Override public void onClick(View v) {
      if (v instanceof TextView) {
        int position = getAdapterPosition();
        String tag = (String) v.getTag(R.id.type_tag);
        switch (tag) {
          case "btnAdd":
            mListener.onAdd(position);
            break;
          case "btnOne":
            mListener.onValue(position, 0);
            break;
          case "btnTwo":
            mListener.onValue(position, 1);
            break;
          case "btnThree":
            mListener.onValue(position, 2);
            break;
          case "btnFour":
            mListener.onValue(position, 3);
            break;
          case "btnFive":
            mListener.onValue(position, 4);
            break;
        }
      }
    }

    public interface IMyViewHolderClicks {
      void onValue(int adapterPosition, int listPositon);

      void onAdd(int position);
    }
  }

  // Create new views (invoked by the layout manager)
  @Override public RecordingConfigurationAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
      int viewType) {
    // create a new view
    View v = LayoutInflater.from(parent.getContext())
        .inflate(R.layout.recording_configuration_item, parent, false);
    // set the view's size, margins, paddings and layout parameters

    RecordingConfigurationAdapter.ViewHolder vh =
        new ViewHolder(v, new RecordingConfigurationAdapter.ViewHolder.IMyViewHolderClicks() {
          @Override public void onValue(int adapterPosition, int listPositon) {
            fragmentCallback.valueClicked(configurationValuesList.get(adapterPosition).get(listPositon));
          }

          @Override public void onAdd(int position) {
            Timber.i("Add clicked , position -> " + Integer.toString(position));
          }
        });
    return vh;
  }

  // Provide a suitable constructor (depends on the kind of dataset)
  public RecordingConfigurationAdapter(RecordingConfigurationLayout layout, List<List<ConfigurationValue>> configurationValuesList) {
    try {
      this.fragmentCallback = ((RecordingConfigurationListClicks) layout);
    } catch (ClassCastException e) {
      throw new ClassCastException("Fragment must implement ViewClickCallback.");
    }
    this.configurationValuesList = configurationValuesList;
  }

  // Replace the contents of a view (invoked by the layout manager)
  @Override public void onBindViewHolder(ViewHolder holder, int position) {
    // - get element from your dataset at this position
    // - replace the contents of the view with that element
    df.setTimeZone(TimeZone.getDefault());
    List<ConfigurationValue> list = configurationValuesList.get(position);

    holder.btnOne.setText(list.get(0).value());
    holder.btnTwo.setText(list.get(1).value());
    holder.btnThree.setText(list.get(2).value());
    holder.btnFour.setText(list.get(3).value());
    holder.btnFive.setText(list.get(4).value());
    holder.btnAdd.setText("+");
  }

  // Return the size of your dataset (invoked by the layout manager)
  @Override public int getItemCount() {
    if (configurationValuesList != null) {
      return 5;
    } else {
      return 0;
    }
  }

  public static interface RecordingConfigurationListClicks {
    void valueClicked(ConfigurationValue value);

    void addClicked(ConfigurationValue value);
  }
}
