package co.vgetto.har.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;
import android.support.v4.util.Pair;
import co.vgetto.har.db.entities.ConfigurationValue;
import co.vgetto.har.db.tables.ConfigurationValuesTable;
import co.vgetto.har.db.tables.HistoryTable;
import co.vgetto.har.db.tables.SchedulesTable;
import co.vgetto.har.db.tables.TriggersTable;
import co.vgetto.har.db.tables.UserTable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Kovje on 25.8.2015..
 */
public class DbHelper extends SQLiteOpenHelper {
  public static final String DB_NAME = "har.db";
  public static final int DB_VERSION = 1;

  List<ConfigurationValue> values = new ArrayList<>();

  public DbHelper(Context context) {
    super(context, DB_NAME, null, DB_VERSION);
  }

  @Override public void onCreate(SQLiteDatabase db) {
    db.execSQL(UserTable.getUserTableQuery());
    db.execSQL(SchedulesTable.getScheduleLocalTableQuery());
    db.execSQL(TriggersTable.getTriggerLocalTableQuery());
    db.execSQL(HistoryTable.getLocalHistoryTableQuery());
    db.execSQL(ConfigurationValuesTable.getConfigurationValuesTableQuery());

    insertDefaultValues(db);
  }

  public void insertDefaultValues(SQLiteDatabase db) {
    String sql = "INSERT OR REPLACE INTO " + ConfigurationValuesTable.TABLE
        + " ( type, value ) VALUES ( ?, ? )";
        /*
         * According to the docs http://developer.android.com/reference/android/database/sqlite/SQLiteDatabase.html
         * Writers should use beginTransactionNonExclusive() or beginTransactionWithListenerNonExclusive(SQLiteTransactionListener)
         * to start a transaction. Non-exclusive mode allows database file to be in readable by other threads executing queries.
         */
    db.beginTransactionNonExclusive();

    SQLiteStatement statement = db.compileStatement(sql);

    for (Pair<Integer, String> entry : DefaultConfigurationValues.getDefaultValues()) {
      statement.bindLong(1, entry.first);
      statement.bindString(2, entry.second);

      statement.execute();
      statement.clearBindings();
    }

    db.setTransactionSuccessful();
    db.endTransaction();

    //TODO close or not to close ?
    //db.close();
  }

  @Override public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

  }
}
