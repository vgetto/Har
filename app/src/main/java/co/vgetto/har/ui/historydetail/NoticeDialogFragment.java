package co.vgetto.har.ui.historydetail;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import timber.log.Timber;

/**
 * Created by Kovje on 30.11.2015..
 */
public class NoticeDialogFragment extends DialogFragment {

  @Override public void onAttach(Context context) {
    super.onAttach(context);
    Timber.i("Context in dialogfragment -> " + context.toString());
  }

  @Override
  public Dialog onCreateDialog(Bundle savedInstanceState) {
    // Use the Builder class for convenient dialog construction
    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
    builder.setMessage("Title man")
        .setPositiveButton("jea", new DialogInterface.OnClickListener() {
          public void onClick(DialogInterface dialog, int id) {
            // FIRE ZE MISSILES!
          }
        })
        .setNegativeButton("nou", new DialogInterface.OnClickListener() {
          public void onClick(DialogInterface dialog, int id) {
            // User cancelled the dialog
          }
        });
    // Create the AlertDialog object and return it
    return builder.create();
  }
}
