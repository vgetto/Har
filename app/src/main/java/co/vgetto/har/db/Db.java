package co.vgetto.har.db;

import android.database.Cursor;
import android.support.annotation.NonNull;
import co.vgetto.har.db.entities.SavedFile;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Jake Wharton
 */
public final class Db {
  private static String ITEM_SEPARATOR = "__,__";
  private static String OBJECT_SEPARATOR = "__-__";

  public static final int BOOLEAN_FALSE = 0;
  public static final int BOOLEAN_TRUE = 1;

  public static final String[] getSelectionArgsForId(long id) {
    String[] args = new String[1];
    args[0] = Long.toString(id);
    return args;
  }

  public static final String[] getSelectionArgsForForeignIdAndType(long foreignId, int type) {
    String[] args = new String[2];
    args[0] = Long.toString(foreignId);
    args[1] = Integer.toString(type);
    return args;
  }

  public static final String[] getSelectionArgsForTypeAndPhoneNumber(int type, String phoneNumber) {
    String[] args = new String[2];
    args[0] = Integer.toString(type);
    args[1] = phoneNumber;
    return args;
  }

  public static String getString(Cursor cursor, String columnName) {
    return cursor.getString(cursor.getColumnIndexOrThrow(columnName));
  }

  public static boolean getBoolean(Cursor cursor, String columnName) {
    return getInt(cursor, columnName) == BOOLEAN_TRUE;
  }

  public static long getLong(Cursor cursor, String columnName) {
    return cursor.getLong(cursor.getColumnIndexOrThrow(columnName));
  }

  public static int getInt(Cursor cursor, String columnName) {
    return cursor.getInt(cursor.getColumnIndexOrThrow(columnName));
  }

  public static String convertSavedFilesListToString(List<SavedFile> savedFileList) {
    if ((savedFileList != null) &&  (savedFileList.size() > 0)) {
      StringBuffer stringBuffer = new StringBuffer();

      for (SavedFile savedFile : savedFileList) {
        stringBuffer.append(savedFile.filePath())
            .append(OBJECT_SEPARATOR)
            .append(Long.toString(savedFile.recordingEndedTime()))
            .append(OBJECT_SEPARATOR)
            .append(Boolean.toString(savedFile.synced()))
            .append(ITEM_SEPARATOR);
      }

      int lastIndex = stringBuffer.lastIndexOf(ITEM_SEPARATOR);
      stringBuffer.delete(lastIndex, lastIndex+ ITEM_SEPARATOR.length() + 1);

      return stringBuffer.toString();
    } else {
      return "";
    }
  }

  public static List<SavedFile> convertStringToSavedFilesList(@NonNull String str) {
    List<SavedFile> savedFiles = new ArrayList<>();
    // if there are some files already, return them as list of SavedFile objects
    if (!str.equals("")) {
      List<String> objectStringList = Arrays.asList(str.split(ITEM_SEPARATOR));
      List<String> i;

      for (String objectString : objectStringList) {
        // TODO CHECK
        i = Arrays.asList(objectString.split(OBJECT_SEPARATOR));
        savedFiles.add(SavedFile.create(i.get(0), Long.valueOf(i.get(1)), Boolean.valueOf(i.get(2))));
      }
    }
    // if input string == "" , return empty list
    return savedFiles;
  }

  private Db() {
    throw new AssertionError("No instances.");
  }
}
