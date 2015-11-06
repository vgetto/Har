package co.vgetto.har.ui.backstack;

import android.os.Bundle;
import android.app.Fragment;
import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class SavedBackstackFragment extends Fragment {
  private ArrayList<SavedBackstackItem> savedBackstackItems = new ArrayList<SavedBackstackItem>();

  public SavedBackstackFragment() {
    // Required empty public constructor
  }

  @Override public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setRetainInstance(true);
  }

  public void saveBackstackItems(ArrayList<SavedBackstackItem> savedBackstackItems) {
    this.savedBackstackItems = savedBackstackItems;
  }

  public ArrayList<SavedBackstackItem> getSavedBackstackItems() {
    return savedBackstackItems;
  }
}
