package co.vgetto.har.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import co.vgetto.har.db.tables.HistoryTable;
import co.vgetto.har.db.tables.SchedulesTable;
import co.vgetto.har.db.tables.TriggersTable;

/**
 * Created by Kovje on 25.8.2015..
 */
public class DbHelper extends SQLiteOpenHelper {
  public static final String DB_NAME = "har.db";
  public static final int DB_VERSION = 1;

  public DbHelper(Context context) {
    super(context, DB_NAME, null, DB_VERSION);
  }

  @Override public void onCreate(SQLiteDatabase db) {
    db.execSQL(SchedulesTable.getScheduleLocalTableQuery());
    db.execSQL(TriggersTable.getTriggerLocalTableQuery());
    db.execSQL(HistoryTable.getLocalHistoryTableQuery());
  }

  @Override public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

  }
}
