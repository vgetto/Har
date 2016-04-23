package co.vgetto.har.db.entities;

import android.content.ContentValues;
import android.database.Cursor;
import auto.parcel.AutoParcel;
import co.vgetto.har.db.Db;
import co.vgetto.har.db.tables.ConfigurationValuesTable;
import rx.functions.Func1;

/**
 * Created by Kovje on 11.9.2015..
 */
@AutoParcel public abstract class ConfigurationValue {
  public static final String selectionById = ConfigurationValuesTable.ID + " = ?";

  public abstract long id();

  public abstract int type();

  public abstract String value();

  public static ConfigurationValue create(long id, int type, String value) {
    return new AutoParcel_ConfigurationValue(id, type, value);
  }

  public static ConfigurationValue getConfigurationValueFromCursor(Cursor cursor) {
    long id = Db.getLong(cursor, ConfigurationValuesTable.ID);
    int type = Db.getInt(cursor, ConfigurationValuesTable.TYPE);
    String value = Db.getString(cursor, ConfigurationValuesTable.VALUE);

    return new AutoParcel_ConfigurationValue(id, type, value);
  }


  public static final Func1<Cursor, ConfigurationValue> BRITE_MAPPER =
      new Func1<Cursor, ConfigurationValue>() {
        @Override public ConfigurationValue call(Cursor cursor) {
          return getConfigurationValueFromCursor(cursor);
        }
      };

  public ContentValues getContentValues() {
    return new ContentValuesBuilder().id(id()).type(type()).value(value()).build();
  }

  public static final class ContentValuesBuilder {
    private final ContentValues values = new ContentValues();

    public ContentValuesBuilder id(long id) {
      values.put(ConfigurationValuesTable.ID, id);
      return this;
    }

    public ContentValuesBuilder type(int type) {
      values.put(ConfigurationValuesTable.TYPE, type);
      return this;
    }

    public ContentValuesBuilder value(String value) {
      values.put(ConfigurationValuesTable.VALUE, value);
      return this;
    }

    public ContentValues build() {
      return values; // TODO defensive copy?
    }
  }
}
