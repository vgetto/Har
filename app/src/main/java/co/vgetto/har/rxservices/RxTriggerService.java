package co.vgetto.har.rxservices;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import co.vgetto.har.audio.RecordAudioService;
import co.vgetto.har.db.Db;
import co.vgetto.har.db.entities.History;
import co.vgetto.har.db.entities.Trigger;
import co.vgetto.har.db.tables.TriggersTable;
import java.util.List;
import rx.Observable;

/**
 * Created by Kovje on 10.9.2015..
 */

/**
 * Helper class for working with triggers
 */

public final class RxTriggerService {
  private final RxDbService rxDbService;

  public RxTriggerService(RxDbService rxDbService) {
    this.rxDbService = rxDbService;
  }

  /**
   *  returns and observable that emit's a list of items every time trigger table is updated
   *  as long a subscription to this observable is alive
   */
  public final Observable<List<Trigger>> getTriggerQueryObservable() {
    return rxDbService.getQueryObservable(Trigger.class).mapToList(Trigger.BRITE_MAPPER);
  }

  /**
   *  returns one trigger object base on id
   * @param id
   * @return
   */
  public final Observable<Trigger> getTriggerById(long id) {
    return rxDbService.get(Trigger.class, Trigger.selectionById, Db.getSelectionArgsForId(id), null)
        .map(Trigger.SINGLE_MAPPER);
  }

  // no comments needed for these methods..

  public final Observable<Long> insertTrigger(ContentValues values) {
    return rxDbService.insert(Trigger.class, values);
  }

  public final Observable<Integer> editTrigger(ContentValues values) {
    return rxDbService.edit(Trigger.class, values, Trigger.selectionById,
        Db.getSelectionArgsForId(values.getAsLong(TriggersTable.ID)));
  }

  public final Observable<Integer> deleteTrigger(long id) {
    return rxDbService.delete(Trigger.class, Trigger.selectionById, Db.getSelectionArgsForId(id));
  }
  
  public final Observable<Trigger> findTriggerByTypeAndPhoneNumber(int type, String phoneNumber) {
    return rxDbService.get(Trigger.class, Trigger.selectionByTypeAndPhoneNumber, Db.getSelectionArgsForTypeAndPhoneNumber(type, phoneNumber), null).map(Trigger.SINGLE_MAPPER);
  }

}
