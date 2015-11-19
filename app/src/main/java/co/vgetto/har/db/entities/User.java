package co.vgetto.har.db.entities;

import android.content.ContentValues;
import android.database.Cursor;
import auto.parcel.AutoParcel;
import co.vgetto.har.db.Db;
import co.vgetto.har.db.entities.configurations.RecordingConfiguration;
import co.vgetto.har.db.entities.configurations.TriggerConfiguration;
import co.vgetto.har.db.entities.configurations.UploadConfiguration;
import co.vgetto.har.db.tables.TriggersTable;
import co.vgetto.har.db.tables.UserTable;
import rx.functions.Func1;

/**
 * Created by Kovje on 11.9.2015..
 */
@AutoParcel public abstract class User {
  public static final String selectionById = UserTable.ID + " = ?";

  public abstract long id();

  public abstract String token();

  public abstract String email();

  public static User create(long id, String token, String email) {
    return new AutoParcel_User(id, token, email);
  }

  public static User getUserFromCursor(Cursor cursor) {
    long id = Db.getLong(cursor, UserTable.ID);
    String token = Db.getString(cursor, UserTable.TOKEN);
    String email = Db.getString(cursor, UserTable.EMAIL);

    return new AutoParcel_User(id, token, email);
  }

  public static final Func1<Cursor, User> SINGLE_MAPPER = new Func1<Cursor, User>() {
    @Override public User call(Cursor cursor) {
      if (cursor.getCount() > 0) {
        cursor.moveToFirst();
        User u = getUserFromCursor(cursor);
        cursor.close();
        return u;
      }
      cursor.close();
      return null;
    }
  };

  public static final Func1<Cursor, User> BRITE_MAPPER = new Func1<Cursor, User>() {
    @Override public User call(Cursor cursor) {
      return getUserFromCursor(cursor);
    }
  };

  public ContentValues getContentValues() {
    return new ContentValuesBuilder().id(id()).token(token()).email(email()).build();
  }

  public static final class ContentValuesBuilder {
    private final ContentValues values = new ContentValues();

    public ContentValuesBuilder id(long id) {
      values.put(TriggersTable.ID, id);
      return this;
    }

    public ContentValuesBuilder token(String token) {
      values.put(UserTable.TOKEN, token);
      return this;
    }

    public ContentValuesBuilder email(String email) {
      values.put(UserTable.EMAIL, email);
      return this;
    }

    public ContentValues build() {
      return values; // TODO defensive copy?
    }
  }
}
