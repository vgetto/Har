package co.vgetto.har.db.tables;

/**
 * Created by Kovje on 6.9.2015..
 */
public class UserTable {
  public static final String TABLE = "user";

  public static final String ID = "_id";
  public static final String TOKEN = "token";
  public static final String EMAIL = "email";

  private static final String userColumns = " (" +
      ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
      TOKEN + " TEXT," +
      EMAIL + " EMAIL" +
      ")";

  public static String getUserTableQuery() {
    return "CREATE TABLE " + TABLE + userColumns;
  }

  public static final String[] projection = {
      ID, TOKEN, EMAIL
  };
}

