package co.vgetto.har.rxservices;

import android.content.Context;
import co.vgetto.har.db.DefaultConfigurationValues;
import co.vgetto.har.db.entities.ConfigurationValue;
import java.util.ArrayList;
import java.util.List;
import rx.Observable;

/**
 * Created by Kovje on 10.9.2015..
 */

/**
 * Helper class for working with schedules
 */
public final class RxConfigurationValuesService {
  private final RxDbService rxDbService;
  private final Context context;

  public RxConfigurationValuesService(Context context, RxDbService rxDbService) {
    this.context = context;
    this.rxDbService = rxDbService;
  }

  /**
   * returns an Observable that emits a list of schedules on every db update, as long as
   * subscribed
   */
  public Observable<List<List<ConfigurationValue>>> getConfigurationValuesQueryObservable() {
    return rxDbService.getQueryObservable(ConfigurationValue.class)
        .mapToList(ConfigurationValue.BRITE_MAPPER)
        .concatMap(configurationValues -> {
          List<List<ConfigurationValue>> valuesForAdapter =
              new ArrayList<List<ConfigurationValue>>(5);

          List<ConfigurationValue> durationList = new ArrayList<ConfigurationValue>();
          List<ConfigurationValue> delayList = new ArrayList<ConfigurationValue>();
          List<ConfigurationValue> numOfRecList = new ArrayList<ConfigurationValue>();
          List<ConfigurationValue> filePrefixList = new ArrayList<ConfigurationValue>();
          List<ConfigurationValue> folderNamelist = new ArrayList<ConfigurationValue>();

          for (ConfigurationValue entry : configurationValues) {
            switch (entry.type()) {
              case DefaultConfigurationValues.DURATION:
                durationList.add(entry);
                break;
              case DefaultConfigurationValues.DELAY:
                delayList.add(entry);
                break;
              case DefaultConfigurationValues.NUM_OF_RECORDINGS:
                numOfRecList.add(entry);
                break;
              case DefaultConfigurationValues.FILE_PREFIX:
                filePrefixList.add(entry);
                break;
              case DefaultConfigurationValues.FOLDER_NAME:
                folderNamelist.add(entry);
                break;
            }
          }
          valuesForAdapter.add(durationList);
          valuesForAdapter.add(delayList);
          valuesForAdapter.add(numOfRecList);
          valuesForAdapter.add(filePrefixList);
          valuesForAdapter.add(folderNamelist);
          return Observable.just(valuesForAdapter);
        });
  }
}
