package com.carcatalog.app.db;

import android.database.Cursor;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.room.EntityDeletionOrUpdateAdapter;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import java.lang.Class;
import java.lang.Exception;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;

@SuppressWarnings({"unchecked", "deprecation"})
public final class FavouriteCarDao_Impl implements FavouriteCarDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<FavouriteCar> __insertionAdapterOfFavouriteCar;

  private final EntityDeletionOrUpdateAdapter<FavouriteCar> __deletionAdapterOfFavouriteCar;

  public FavouriteCarDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfFavouriteCar = new EntityInsertionAdapter<FavouriteCar>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `favourites` (`id`,`brand`,`model`,`color`,`year`,`vin`,`price`) VALUES (?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          final FavouriteCar entity) {
        statement.bindLong(1, entity.id);
        if (entity.brand == null) {
          statement.bindNull(2);
        } else {
          statement.bindString(2, entity.brand);
        }
        if (entity.model == null) {
          statement.bindNull(3);
        } else {
          statement.bindString(3, entity.model);
        }
        if (entity.color == null) {
          statement.bindNull(4);
        } else {
          statement.bindString(4, entity.color);
        }
        statement.bindLong(5, entity.year);
        if (entity.vin == null) {
          statement.bindNull(6);
        } else {
          statement.bindString(6, entity.vin);
        }
        if (entity.price == null) {
          statement.bindNull(7);
        } else {
          statement.bindString(7, entity.price);
        }
      }
    };
    this.__deletionAdapterOfFavouriteCar = new EntityDeletionOrUpdateAdapter<FavouriteCar>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "DELETE FROM `favourites` WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          final FavouriteCar entity) {
        statement.bindLong(1, entity.id);
      }
    };
  }

  @Override
  public void insert(final FavouriteCar car) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __insertionAdapterOfFavouriteCar.insert(car);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void delete(final FavouriteCar car) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __deletionAdapterOfFavouriteCar.handle(car);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public LiveData<List<FavouriteCar>> getAll() {
    final String _sql = "SELECT * FROM favourites ORDER BY brand, model";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return __db.getInvalidationTracker().createLiveData(new String[] {"favourites"}, false, new Callable<List<FavouriteCar>>() {
      @Override
      @Nullable
      public List<FavouriteCar> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfBrand = CursorUtil.getColumnIndexOrThrow(_cursor, "brand");
          final int _cursorIndexOfModel = CursorUtil.getColumnIndexOrThrow(_cursor, "model");
          final int _cursorIndexOfColor = CursorUtil.getColumnIndexOrThrow(_cursor, "color");
          final int _cursorIndexOfYear = CursorUtil.getColumnIndexOrThrow(_cursor, "year");
          final int _cursorIndexOfVin = CursorUtil.getColumnIndexOrThrow(_cursor, "vin");
          final int _cursorIndexOfPrice = CursorUtil.getColumnIndexOrThrow(_cursor, "price");
          final List<FavouriteCar> _result = new ArrayList<FavouriteCar>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final FavouriteCar _item;
            _item = new FavouriteCar();
            _item.id = _cursor.getInt(_cursorIndexOfId);
            if (_cursor.isNull(_cursorIndexOfBrand)) {
              _item.brand = null;
            } else {
              _item.brand = _cursor.getString(_cursorIndexOfBrand);
            }
            if (_cursor.isNull(_cursorIndexOfModel)) {
              _item.model = null;
            } else {
              _item.model = _cursor.getString(_cursorIndexOfModel);
            }
            if (_cursor.isNull(_cursorIndexOfColor)) {
              _item.color = null;
            } else {
              _item.color = _cursor.getString(_cursorIndexOfColor);
            }
            _item.year = _cursor.getInt(_cursorIndexOfYear);
            if (_cursor.isNull(_cursorIndexOfVin)) {
              _item.vin = null;
            } else {
              _item.vin = _cursor.getString(_cursorIndexOfVin);
            }
            if (_cursor.isNull(_cursorIndexOfPrice)) {
              _item.price = null;
            } else {
              _item.price = _cursor.getString(_cursorIndexOfPrice);
            }
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public int isFavourite(final int carId) {
    final String _sql = "SELECT COUNT(*) FROM favourites WHERE id = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, carId);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _result;
      if (_cursor.moveToFirst()) {
        _result = _cursor.getInt(0);
      } else {
        _result = 0;
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}
