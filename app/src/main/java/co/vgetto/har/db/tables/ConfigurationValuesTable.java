package co.vgetto.har.db.tables;

/**
 * Created by Kovje on 6.9.2015..
 */
public class ConfigurationValuesTable {
  public static final String TABLE = "configuration_values";

  public static final String ID = "_id";
  public static final String TYPE = "type";
  public static final String VALUE = "value";

  private static final String configurationValuesColumns = " (" +
      ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
      TYPE + " INTEGER," +
      VALUE + " TEXT" +
      ")";

  public static String getConfigurationValuesTableQuery() {
    return "CREATE TABLE " + TABLE + configurationValuesColumns;
  }

  public static final String[] projection = {
      ID, TYPE, VALUE
  };
}

