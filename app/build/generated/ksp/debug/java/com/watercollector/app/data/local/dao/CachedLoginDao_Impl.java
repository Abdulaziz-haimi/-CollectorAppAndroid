package com.watercollector.app.data.local.dao;

import android.database.Cursor;
import android.os.CancellationSignal;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.CoroutinesRoom;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.watercollector.app.data.local.entities.CachedLoginEntity;
import java.lang.Class;
import java.lang.Exception;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import javax.annotation.processing.Generated;
import kotlin.Unit;
import kotlin.coroutines.Continuation;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class CachedLoginDao_Impl implements CachedLoginDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<CachedLoginEntity> __insertionAdapterOfCachedLoginEntity;

  public CachedLoginDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfCachedLoginEntity = new EntityInsertionAdapter<CachedLoginEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `cached_login` (`userName`,`passwordHash`,`baseUrl`,`token`,`collectorId`,`collectorName`,`fullName`,`deviceCode`,`cachedAt`) VALUES (?,?,?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final CachedLoginEntity entity) {
        statement.bindString(1, entity.getUserName());
        statement.bindString(2, entity.getPasswordHash());
        statement.bindString(3, entity.getBaseUrl());
        statement.bindString(4, entity.getToken());
        statement.bindLong(5, entity.getCollectorId());
        if (entity.getCollectorName() == null) {
          statement.bindNull(6);
        } else {
          statement.bindString(6, entity.getCollectorName());
        }
        if (entity.getFullName() == null) {
          statement.bindNull(7);
        } else {
          statement.bindString(7, entity.getFullName());
        }
        statement.bindString(8, entity.getDeviceCode());
        statement.bindLong(9, entity.getCachedAt());
      }
    };
  }

  @Override
  public Object upsert(final CachedLoginEntity entity,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfCachedLoginEntity.insert(entity);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object getByUserName(final String userName,
      final Continuation<? super CachedLoginEntity> $completion) {
    final String _sql = "SELECT * FROM cached_login WHERE userName = ? LIMIT 1";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindString(_argIndex, userName);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<CachedLoginEntity>() {
      @Override
      @Nullable
      public CachedLoginEntity call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfUserName = CursorUtil.getColumnIndexOrThrow(_cursor, "userName");
          final int _cursorIndexOfPasswordHash = CursorUtil.getColumnIndexOrThrow(_cursor, "passwordHash");
          final int _cursorIndexOfBaseUrl = CursorUtil.getColumnIndexOrThrow(_cursor, "baseUrl");
          final int _cursorIndexOfToken = CursorUtil.getColumnIndexOrThrow(_cursor, "token");
          final int _cursorIndexOfCollectorId = CursorUtil.getColumnIndexOrThrow(_cursor, "collectorId");
          final int _cursorIndexOfCollectorName = CursorUtil.getColumnIndexOrThrow(_cursor, "collectorName");
          final int _cursorIndexOfFullName = CursorUtil.getColumnIndexOrThrow(_cursor, "fullName");
          final int _cursorIndexOfDeviceCode = CursorUtil.getColumnIndexOrThrow(_cursor, "deviceCode");
          final int _cursorIndexOfCachedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "cachedAt");
          final CachedLoginEntity _result;
          if (_cursor.moveToFirst()) {
            final String _tmpUserName;
            _tmpUserName = _cursor.getString(_cursorIndexOfUserName);
            final String _tmpPasswordHash;
            _tmpPasswordHash = _cursor.getString(_cursorIndexOfPasswordHash);
            final String _tmpBaseUrl;
            _tmpBaseUrl = _cursor.getString(_cursorIndexOfBaseUrl);
            final String _tmpToken;
            _tmpToken = _cursor.getString(_cursorIndexOfToken);
            final int _tmpCollectorId;
            _tmpCollectorId = _cursor.getInt(_cursorIndexOfCollectorId);
            final String _tmpCollectorName;
            if (_cursor.isNull(_cursorIndexOfCollectorName)) {
              _tmpCollectorName = null;
            } else {
              _tmpCollectorName = _cursor.getString(_cursorIndexOfCollectorName);
            }
            final String _tmpFullName;
            if (_cursor.isNull(_cursorIndexOfFullName)) {
              _tmpFullName = null;
            } else {
              _tmpFullName = _cursor.getString(_cursorIndexOfFullName);
            }
            final String _tmpDeviceCode;
            _tmpDeviceCode = _cursor.getString(_cursorIndexOfDeviceCode);
            final long _tmpCachedAt;
            _tmpCachedAt = _cursor.getLong(_cursorIndexOfCachedAt);
            _result = new CachedLoginEntity(_tmpUserName,_tmpPasswordHash,_tmpBaseUrl,_tmpToken,_tmpCollectorId,_tmpCollectorName,_tmpFullName,_tmpDeviceCode,_tmpCachedAt);
          } else {
            _result = null;
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @Override
  public Object getLastLogin(final Continuation<? super CachedLoginEntity> $completion) {
    final String _sql = "SELECT * FROM cached_login ORDER BY cachedAt DESC LIMIT 1";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<CachedLoginEntity>() {
      @Override
      @Nullable
      public CachedLoginEntity call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfUserName = CursorUtil.getColumnIndexOrThrow(_cursor, "userName");
          final int _cursorIndexOfPasswordHash = CursorUtil.getColumnIndexOrThrow(_cursor, "passwordHash");
          final int _cursorIndexOfBaseUrl = CursorUtil.getColumnIndexOrThrow(_cursor, "baseUrl");
          final int _cursorIndexOfToken = CursorUtil.getColumnIndexOrThrow(_cursor, "token");
          final int _cursorIndexOfCollectorId = CursorUtil.getColumnIndexOrThrow(_cursor, "collectorId");
          final int _cursorIndexOfCollectorName = CursorUtil.getColumnIndexOrThrow(_cursor, "collectorName");
          final int _cursorIndexOfFullName = CursorUtil.getColumnIndexOrThrow(_cursor, "fullName");
          final int _cursorIndexOfDeviceCode = CursorUtil.getColumnIndexOrThrow(_cursor, "deviceCode");
          final int _cursorIndexOfCachedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "cachedAt");
          final CachedLoginEntity _result;
          if (_cursor.moveToFirst()) {
            final String _tmpUserName;
            _tmpUserName = _cursor.getString(_cursorIndexOfUserName);
            final String _tmpPasswordHash;
            _tmpPasswordHash = _cursor.getString(_cursorIndexOfPasswordHash);
            final String _tmpBaseUrl;
            _tmpBaseUrl = _cursor.getString(_cursorIndexOfBaseUrl);
            final String _tmpToken;
            _tmpToken = _cursor.getString(_cursorIndexOfToken);
            final int _tmpCollectorId;
            _tmpCollectorId = _cursor.getInt(_cursorIndexOfCollectorId);
            final String _tmpCollectorName;
            if (_cursor.isNull(_cursorIndexOfCollectorName)) {
              _tmpCollectorName = null;
            } else {
              _tmpCollectorName = _cursor.getString(_cursorIndexOfCollectorName);
            }
            final String _tmpFullName;
            if (_cursor.isNull(_cursorIndexOfFullName)) {
              _tmpFullName = null;
            } else {
              _tmpFullName = _cursor.getString(_cursorIndexOfFullName);
            }
            final String _tmpDeviceCode;
            _tmpDeviceCode = _cursor.getString(_cursorIndexOfDeviceCode);
            final long _tmpCachedAt;
            _tmpCachedAt = _cursor.getLong(_cursorIndexOfCachedAt);
            _result = new CachedLoginEntity(_tmpUserName,_tmpPasswordHash,_tmpBaseUrl,_tmpToken,_tmpCollectorId,_tmpCollectorName,_tmpFullName,_tmpDeviceCode,_tmpCachedAt);
          } else {
            _result = null;
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
