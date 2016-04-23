package co.vgetto.har.db;

import android.support.v4.util.Pair;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Kovje on 2.12.2015..
 */
public class DefaultConfigurationValues {
  public static final int DURATION = 0;
  public static final int DELAY = 1;
  public static final int NUM_OF_RECORDINGS = 2;
  public static final int FILE_PREFIX = 3;
  public static final int FOLDER_NAME = 4;


  public static List<Pair<Integer, String>> getDefaultValues() {
    List<Pair<Integer, String>> list = new ArrayList<>(25);

    list.add(new Pair<>(DURATION, "100s"));
    list.add(new Pair<>(DURATION, "200s"));
    list.add(new Pair<>(DURATION, "300s"));
    list.add(new Pair<>(DURATION, "400s"));
    list.add(new Pair<>(DURATION, "500s"));
    list.add(new Pair<>(DURATION, "500s"));
    list.add(new Pair<>(DURATION, "500s"));

    list.add(new Pair<>(DELAY, "10"));
    list.add(new Pair<>(DELAY, "20"));
    list.add(new Pair<>(DELAY, "30"));
    list.add(new Pair<>(DELAY, "40"));
    list.add(new Pair<>(DELAY, "50"));
    list.add(new Pair<>(DELAY, "50"));
    list.add(new Pair<>(DELAY, "50"));

    list.add(new Pair<>(NUM_OF_RECORDINGS, "1"));
    list.add(new Pair<>(NUM_OF_RECORDINGS, "2"));
    list.add(new Pair<>(NUM_OF_RECORDINGS, "3"));
    list.add(new Pair<>(NUM_OF_RECORDINGS, "4"));
    list.add(new Pair<>(NUM_OF_RECORDINGS, "5"));
    list.add(new Pair<>(NUM_OF_RECORDINGS, "5"));
    list.add(new Pair<>(NUM_OF_RECORDINGS, "5"));
    list.add(new Pair<>(NUM_OF_RECORDINGS, "5"));
    list.add(new Pair<>(NUM_OF_RECORDINGS, "5"));

    list.add(new Pair<>(FILE_PREFIX, "audio"));
    list.add(new Pair<>(FILE_PREFIX, "rec"));
    list.add(new Pair<>(FILE_PREFIX, "a"));
    list.add(new Pair<>(FILE_PREFIX, "r"));
    list.add(new Pair<>(FILE_PREFIX, "har_rec"));
    list.add(new Pair<>(FILE_PREFIX, "har_rec"));
    list.add(new Pair<>(FILE_PREFIX, "har_rec"));
    list.add(new Pair<>(FILE_PREFIX, "har_rec"));

    list.add(new Pair<>(FOLDER_NAME, "har"));
    list.add(new Pair<>(FOLDER_NAME, "harRecordings"));
    list.add(new Pair<>(FOLDER_NAME, "audiorec"));
    list.add(new Pair<>(FOLDER_NAME, "hiddenrec"));
    list.add(new Pair<>(FOLDER_NAME, "harrec"));

    return list;
  }
}
