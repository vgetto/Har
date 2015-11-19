package co.vgetto.har.db;

import android.content.ContentResolver;
import android.content.Context;
import android.database.sqlite.SQLiteOpenHelper;
import co.vgetto.har.di.scopes.ApplicationScope;
import com.squareup.sqlbrite.BriteContentResolver;
import com.squareup.sqlbrite.BriteDatabase;
import com.squareup.sqlbrite.SqlBrite;
import dagger.Module;
import dagger.Provides;

/**
 * Created by Kovje on 6.9.2015..
 */
@Module public class DbModule {
  @Provides @ApplicationScope BriteContentResolver providesBriteContentResolver(
      ContentResolver resolver) {
    SqlBrite sqlBrite = SqlBrite.create();
    return sqlBrite.wrapContentProvider(resolver);
  }

  @Provides @ApplicationScope public SQLiteOpenHelper provideSQLiteOpenHelper(Context context) {
    return new DbHelper(context);
  }
}
