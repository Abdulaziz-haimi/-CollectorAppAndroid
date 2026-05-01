package com.watercollector.app.data.local.dao;

import android.database.Cursor;
import android.os.CancellationSignal;
import androidx.annotation.NonNull;
import androidx.room.CoroutinesRoom;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.SharedSQLiteStatement;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.watercollector.app.data.local.entities.LocalSubscriberMeterEntity;
import java.lang.Class;
import java.lang.Exception;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import javax.annotation.processing.Generated;
import kotlin.Unit;
import kotlin.coroutines.Continuation;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class MeterDao_Impl implements MeterDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<LocalSubscriberMeterEntity> __insertionAdapterOfLocalSubscriberMeterEntity;

  private final SharedSQLiteStatement __preparedStmtOfClearAll;

  public MeterDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfLocalSubscriberMeterEntity = new EntityInsertionAdapter<LocalSubscriberMeterEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `local_subscriber_meters` (`subscriberMeterId`,`subscriberId`,`meterId`,`meterNumber`,`meterType`,`location`,`isActive`,`isPrimary`,`linkedAt`) VALUES (?,?,?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final LocalSubscriberMeterEntity entity) {
        statement.bindLong(1, entity.getSubscriberMeterId());
        statement.bindLong(2, entity.getSubscriberId());
        statement.bindLong(3, entity.getMeterId());
        statement.bindString(4, entity.getMeterNumber());
        if (entity.getMeterType() == null) {
          statement.bindNull(5);
        } else {
          statement.bindString(5, entity.getMeterType());
        }
        if (entity.getLocation() == null) {
          statement.bindNull(6);
        } else {
          statement.bindString(6, entity.getLocation());
        }
        final int _tmp = entity.isActive() ? 1 : 0;
        statement.bindLong(7, _tmp);
        final int _tmp_1 = entity.isPrimary() ? 1 : 0;
        statement.bindLong(8, _tmp_1);
        if (entity.getLinkedAt() == null) {
          statement.bindNull(9);
        } else {
          statement.bindString(9, entity.getLinkedAt());
        }
      }
    };
    this.__preparedStmtOfClearAll = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM local_subscriber_meters";
        return _query;
      }
    };
  }

  @Override
  public Object insertAll(final List<LocalSubscriberMeterEntity> items,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfLocalSubscriberMeterEntity.insert(items);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object clearAll(final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfClearAll.acquire();
        try {
          __db.beginTransaction();
          try {
            _stmt.executeUpdateDelete();
            __db.setTransactionSuccessful();
            return Unit.INSTANCE;
          } finally {
            __db.endTransaction();
          }
        } finally {
          __preparedStmtOfClearAll.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object getBySubscriberId(final int subscriberId,
      final Continuation<? super List<LocalSubscriberMeterEntity>> $completion) {
    final String _sql = "SELECT * FROM local_subscriber_meters WHERE subscriberId = ? ORDER BY isPrimary DESC, meterNumber";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, subscriberId);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<LocalSubscriberMeterEntity>>() {
      @Override
      @NonNull
      public List<LocalSubscriberMeterEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfSubscriberMeterId = CursorUtil.getColumnIndexOrThrow(_cursor, "subscriberMeterId");
          final int _cursorIndexOfSubscriberId = CursorUtil.getColumnIndexOrThrow(_cursor, "subscriberId");
          final int _cursorIndexOfMeterId = CursorUtil.getColumnIndexOrThrow(_cursor, "meterId");
          final int _cursorIndexOfMeterNumber = CursorUtil.getColumnIndexOrThrow(_cursor, "meterNumber");
          final int _cursorIndexOfMeterType = CursorUtil.getColumnIndexOrThrow(_cursor, "meterType");
          final int _cursorIndexOfLocation = CursorUtil.getColumnIndexOrThrow(_cursor, "location");
          final int _cursorIndexOfIsActive = CursorUtil.getColumnIndexOrThrow(_cursor, "isActive");
          final int _cursorIndexOfIsPrimary = CursorUtil.getColumnIndexOrThrow(_cursor, "isPrimary");
          final int _cursorIndexOfLinkedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "linkedAt");
          final List<LocalSubscriberMeterEntity> _result = new ArrayList<LocalSubscriberMeterEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final LocalSubscriberMeterEntity _item;
            final int _tmpSubscriberMeterId;
            _tmpSubscriberMeterId = _cursor.getInt(_cursorIndexOfSubscriberMeterId);
            final int _tmpSubscriberId;
            _tmpSubscriberId = _cursor.getInt(_cursorIndexOfSubscriberId);
            final int _tmpMeterId;
            _tmpMeterId = _cursor.getInt(_cursorIndexOfMeterId);
            final String _tmpMeterNumber;
            _tmpMeterNumber = _cursor.getString(_cursorIndexOfMeterNumber);
            final String _tmpMeterType;
            if (_cursor.isNull(_cursorIndexOfMeterType)) {
              _tmpMeterType = null;
            } else {
              _tmpMeterType = _cursor.getString(_cursorIndexOfMeterType);
            }
            final String _tmpLocation;
            if (_cursor.isNull(_cursorIndexOfLocation)) {
              _tmpLocation = null;
            } else {
              _tmpLocation = _cursor.getString(_cursorIndexOfLocation);
            }
            final boolean _tmpIsActive;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsActive);
            _tmpIsActive = _tmp != 0;
            final boolean _tmpIsPrimary;
            final int _tmp_1;
            _tmp_1 = _cursor.getInt(_cursorIndexOfIsPrimary);
            _tmpIsPrimary = _tmp_1 != 0;
            final String _tmpLinkedAt;
            if (_cursor.isNull(_cursorIndexOfLinkedAt)) {
              _tmpLinkedAt = null;
            } else {
              _tmpLinkedAt = _cursor.getString(_cursorIndexOfLinkedAt);
            }
            _item = new LocalSubscriberMeterEntity(_tmpSubscriberMeterId,_tmpSubscriberId,_tmpMeterId,_tmpMeterNumber,_tmpMeterType,_tmpLocation,_tmpIsActive,_tmpIsPrimary,_tmpLinkedAt);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}
