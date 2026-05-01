package com.watercollector.app.data.local.dao;

import android.database.Cursor;
import android.os.CancellationSignal;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.CoroutinesRoom;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.SharedSQLiteStatement;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.watercollector.app.data.local.entities.LocalSubscriberEntity;
import java.lang.Class;
import java.lang.Double;
import java.lang.Exception;
import java.lang.Integer;
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
import kotlinx.coroutines.flow.Flow;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class SubscriberDao_Impl implements SubscriberDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<LocalSubscriberEntity> __insertionAdapterOfLocalSubscriberEntity;

  private final SharedSQLiteStatement __preparedStmtOfClearAll;

  public SubscriberDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfLocalSubscriberEntity = new EntityInsertionAdapter<LocalSubscriberEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `local_subscribers` (`subscriberId`,`subscriberName`,`phoneNumber`,`address`,`primaryMeterId`,`primaryMeterNumber`,`primaryMeterLocation`,`currentDue`,`currentCredit`,`currentBalance`,`lastInvoiceId`,`lastInvoiceNumber`,`lastInvoiceDate`,`lastInvoiceTotal`,`lastInvoiceRemaining`) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final LocalSubscriberEntity entity) {
        statement.bindLong(1, entity.getSubscriberId());
        statement.bindString(2, entity.getSubscriberName());
        if (entity.getPhoneNumber() == null) {
          statement.bindNull(3);
        } else {
          statement.bindString(3, entity.getPhoneNumber());
        }
        if (entity.getAddress() == null) {
          statement.bindNull(4);
        } else {
          statement.bindString(4, entity.getAddress());
        }
        if (entity.getPrimaryMeterId() == null) {
          statement.bindNull(5);
        } else {
          statement.bindLong(5, entity.getPrimaryMeterId());
        }
        if (entity.getPrimaryMeterNumber() == null) {
          statement.bindNull(6);
        } else {
          statement.bindString(6, entity.getPrimaryMeterNumber());
        }
        if (entity.getPrimaryMeterLocation() == null) {
          statement.bindNull(7);
        } else {
          statement.bindString(7, entity.getPrimaryMeterLocation());
        }
        statement.bindDouble(8, entity.getCurrentDue());
        statement.bindDouble(9, entity.getCurrentCredit());
        statement.bindDouble(10, entity.getCurrentBalance());
        if (entity.getLastInvoiceId() == null) {
          statement.bindNull(11);
        } else {
          statement.bindLong(11, entity.getLastInvoiceId());
        }
        if (entity.getLastInvoiceNumber() == null) {
          statement.bindNull(12);
        } else {
          statement.bindString(12, entity.getLastInvoiceNumber());
        }
        if (entity.getLastInvoiceDate() == null) {
          statement.bindNull(13);
        } else {
          statement.bindString(13, entity.getLastInvoiceDate());
        }
        if (entity.getLastInvoiceTotal() == null) {
          statement.bindNull(14);
        } else {
          statement.bindDouble(14, entity.getLastInvoiceTotal());
        }
        if (entity.getLastInvoiceRemaining() == null) {
          statement.bindNull(15);
        } else {
          statement.bindDouble(15, entity.getLastInvoiceRemaining());
        }
      }
    };
    this.__preparedStmtOfClearAll = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM local_subscribers";
        return _query;
      }
    };
  }

  @Override
  public Object insertAll(final List<LocalSubscriberEntity> items,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfLocalSubscriberEntity.insert(items);
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
  public Flow<List<LocalSubscriberEntity>> observeSubscribers() {
    final String _sql = "SELECT * FROM local_subscribers ORDER BY subscriberName";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"local_subscribers"}, new Callable<List<LocalSubscriberEntity>>() {
      @Override
      @NonNull
      public List<LocalSubscriberEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfSubscriberId = CursorUtil.getColumnIndexOrThrow(_cursor, "subscriberId");
          final int _cursorIndexOfSubscriberName = CursorUtil.getColumnIndexOrThrow(_cursor, "subscriberName");
          final int _cursorIndexOfPhoneNumber = CursorUtil.getColumnIndexOrThrow(_cursor, "phoneNumber");
          final int _cursorIndexOfAddress = CursorUtil.getColumnIndexOrThrow(_cursor, "address");
          final int _cursorIndexOfPrimaryMeterId = CursorUtil.getColumnIndexOrThrow(_cursor, "primaryMeterId");
          final int _cursorIndexOfPrimaryMeterNumber = CursorUtil.getColumnIndexOrThrow(_cursor, "primaryMeterNumber");
          final int _cursorIndexOfPrimaryMeterLocation = CursorUtil.getColumnIndexOrThrow(_cursor, "primaryMeterLocation");
          final int _cursorIndexOfCurrentDue = CursorUtil.getColumnIndexOrThrow(_cursor, "currentDue");
          final int _cursorIndexOfCurrentCredit = CursorUtil.getColumnIndexOrThrow(_cursor, "currentCredit");
          final int _cursorIndexOfCurrentBalance = CursorUtil.getColumnIndexOrThrow(_cursor, "currentBalance");
          final int _cursorIndexOfLastInvoiceId = CursorUtil.getColumnIndexOrThrow(_cursor, "lastInvoiceId");
          final int _cursorIndexOfLastInvoiceNumber = CursorUtil.getColumnIndexOrThrow(_cursor, "lastInvoiceNumber");
          final int _cursorIndexOfLastInvoiceDate = CursorUtil.getColumnIndexOrThrow(_cursor, "lastInvoiceDate");
          final int _cursorIndexOfLastInvoiceTotal = CursorUtil.getColumnIndexOrThrow(_cursor, "lastInvoiceTotal");
          final int _cursorIndexOfLastInvoiceRemaining = CursorUtil.getColumnIndexOrThrow(_cursor, "lastInvoiceRemaining");
          final List<LocalSubscriberEntity> _result = new ArrayList<LocalSubscriberEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final LocalSubscriberEntity _item;
            final int _tmpSubscriberId;
            _tmpSubscriberId = _cursor.getInt(_cursorIndexOfSubscriberId);
            final String _tmpSubscriberName;
            _tmpSubscriberName = _cursor.getString(_cursorIndexOfSubscriberName);
            final String _tmpPhoneNumber;
            if (_cursor.isNull(_cursorIndexOfPhoneNumber)) {
              _tmpPhoneNumber = null;
            } else {
              _tmpPhoneNumber = _cursor.getString(_cursorIndexOfPhoneNumber);
            }
            final String _tmpAddress;
            if (_cursor.isNull(_cursorIndexOfAddress)) {
              _tmpAddress = null;
            } else {
              _tmpAddress = _cursor.getString(_cursorIndexOfAddress);
            }
            final Integer _tmpPrimaryMeterId;
            if (_cursor.isNull(_cursorIndexOfPrimaryMeterId)) {
              _tmpPrimaryMeterId = null;
            } else {
              _tmpPrimaryMeterId = _cursor.getInt(_cursorIndexOfPrimaryMeterId);
            }
            final String _tmpPrimaryMeterNumber;
            if (_cursor.isNull(_cursorIndexOfPrimaryMeterNumber)) {
              _tmpPrimaryMeterNumber = null;
            } else {
              _tmpPrimaryMeterNumber = _cursor.getString(_cursorIndexOfPrimaryMeterNumber);
            }
            final String _tmpPrimaryMeterLocation;
            if (_cursor.isNull(_cursorIndexOfPrimaryMeterLocation)) {
              _tmpPrimaryMeterLocation = null;
            } else {
              _tmpPrimaryMeterLocation = _cursor.getString(_cursorIndexOfPrimaryMeterLocation);
            }
            final double _tmpCurrentDue;
            _tmpCurrentDue = _cursor.getDouble(_cursorIndexOfCurrentDue);
            final double _tmpCurrentCredit;
            _tmpCurrentCredit = _cursor.getDouble(_cursorIndexOfCurrentCredit);
            final double _tmpCurrentBalance;
            _tmpCurrentBalance = _cursor.getDouble(_cursorIndexOfCurrentBalance);
            final Integer _tmpLastInvoiceId;
            if (_cursor.isNull(_cursorIndexOfLastInvoiceId)) {
              _tmpLastInvoiceId = null;
            } else {
              _tmpLastInvoiceId = _cursor.getInt(_cursorIndexOfLastInvoiceId);
            }
            final String _tmpLastInvoiceNumber;
            if (_cursor.isNull(_cursorIndexOfLastInvoiceNumber)) {
              _tmpLastInvoiceNumber = null;
            } else {
              _tmpLastInvoiceNumber = _cursor.getString(_cursorIndexOfLastInvoiceNumber);
            }
            final String _tmpLastInvoiceDate;
            if (_cursor.isNull(_cursorIndexOfLastInvoiceDate)) {
              _tmpLastInvoiceDate = null;
            } else {
              _tmpLastInvoiceDate = _cursor.getString(_cursorIndexOfLastInvoiceDate);
            }
            final Double _tmpLastInvoiceTotal;
            if (_cursor.isNull(_cursorIndexOfLastInvoiceTotal)) {
              _tmpLastInvoiceTotal = null;
            } else {
              _tmpLastInvoiceTotal = _cursor.getDouble(_cursorIndexOfLastInvoiceTotal);
            }
            final Double _tmpLastInvoiceRemaining;
            if (_cursor.isNull(_cursorIndexOfLastInvoiceRemaining)) {
              _tmpLastInvoiceRemaining = null;
            } else {
              _tmpLastInvoiceRemaining = _cursor.getDouble(_cursorIndexOfLastInvoiceRemaining);
            }
            _item = new LocalSubscriberEntity(_tmpSubscriberId,_tmpSubscriberName,_tmpPhoneNumber,_tmpAddress,_tmpPrimaryMeterId,_tmpPrimaryMeterNumber,_tmpPrimaryMeterLocation,_tmpCurrentDue,_tmpCurrentCredit,_tmpCurrentBalance,_tmpLastInvoiceId,_tmpLastInvoiceNumber,_tmpLastInvoiceDate,_tmpLastInvoiceTotal,_tmpLastInvoiceRemaining);
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
  public Object getById(final int subscriberId,
      final Continuation<? super LocalSubscriberEntity> $completion) {
    final String _sql = "SELECT * FROM local_subscribers WHERE subscriberId = ? LIMIT 1";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, subscriberId);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<LocalSubscriberEntity>() {
      @Override
      @Nullable
      public LocalSubscriberEntity call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfSubscriberId = CursorUtil.getColumnIndexOrThrow(_cursor, "subscriberId");
          final int _cursorIndexOfSubscriberName = CursorUtil.getColumnIndexOrThrow(_cursor, "subscriberName");
          final int _cursorIndexOfPhoneNumber = CursorUtil.getColumnIndexOrThrow(_cursor, "phoneNumber");
          final int _cursorIndexOfAddress = CursorUtil.getColumnIndexOrThrow(_cursor, "address");
          final int _cursorIndexOfPrimaryMeterId = CursorUtil.getColumnIndexOrThrow(_cursor, "primaryMeterId");
          final int _cursorIndexOfPrimaryMeterNumber = CursorUtil.getColumnIndexOrThrow(_cursor, "primaryMeterNumber");
          final int _cursorIndexOfPrimaryMeterLocation = CursorUtil.getColumnIndexOrThrow(_cursor, "primaryMeterLocation");
          final int _cursorIndexOfCurrentDue = CursorUtil.getColumnIndexOrThrow(_cursor, "currentDue");
          final int _cursorIndexOfCurrentCredit = CursorUtil.getColumnIndexOrThrow(_cursor, "currentCredit");
          final int _cursorIndexOfCurrentBalance = CursorUtil.getColumnIndexOrThrow(_cursor, "currentBalance");
          final int _cursorIndexOfLastInvoiceId = CursorUtil.getColumnIndexOrThrow(_cursor, "lastInvoiceId");
          final int _cursorIndexOfLastInvoiceNumber = CursorUtil.getColumnIndexOrThrow(_cursor, "lastInvoiceNumber");
          final int _cursorIndexOfLastInvoiceDate = CursorUtil.getColumnIndexOrThrow(_cursor, "lastInvoiceDate");
          final int _cursorIndexOfLastInvoiceTotal = CursorUtil.getColumnIndexOrThrow(_cursor, "lastInvoiceTotal");
          final int _cursorIndexOfLastInvoiceRemaining = CursorUtil.getColumnIndexOrThrow(_cursor, "lastInvoiceRemaining");
          final LocalSubscriberEntity _result;
          if (_cursor.moveToFirst()) {
            final int _tmpSubscriberId;
            _tmpSubscriberId = _cursor.getInt(_cursorIndexOfSubscriberId);
            final String _tmpSubscriberName;
            _tmpSubscriberName = _cursor.getString(_cursorIndexOfSubscriberName);
            final String _tmpPhoneNumber;
            if (_cursor.isNull(_cursorIndexOfPhoneNumber)) {
              _tmpPhoneNumber = null;
            } else {
              _tmpPhoneNumber = _cursor.getString(_cursorIndexOfPhoneNumber);
            }
            final String _tmpAddress;
            if (_cursor.isNull(_cursorIndexOfAddress)) {
              _tmpAddress = null;
            } else {
              _tmpAddress = _cursor.getString(_cursorIndexOfAddress);
            }
            final Integer _tmpPrimaryMeterId;
            if (_cursor.isNull(_cursorIndexOfPrimaryMeterId)) {
              _tmpPrimaryMeterId = null;
            } else {
              _tmpPrimaryMeterId = _cursor.getInt(_cursorIndexOfPrimaryMeterId);
            }
            final String _tmpPrimaryMeterNumber;
            if (_cursor.isNull(_cursorIndexOfPrimaryMeterNumber)) {
              _tmpPrimaryMeterNumber = null;
            } else {
              _tmpPrimaryMeterNumber = _cursor.getString(_cursorIndexOfPrimaryMeterNumber);
            }
            final String _tmpPrimaryMeterLocation;
            if (_cursor.isNull(_cursorIndexOfPrimaryMeterLocation)) {
              _tmpPrimaryMeterLocation = null;
            } else {
              _tmpPrimaryMeterLocation = _cursor.getString(_cursorIndexOfPrimaryMeterLocation);
            }
            final double _tmpCurrentDue;
            _tmpCurrentDue = _cursor.getDouble(_cursorIndexOfCurrentDue);
            final double _tmpCurrentCredit;
            _tmpCurrentCredit = _cursor.getDouble(_cursorIndexOfCurrentCredit);
            final double _tmpCurrentBalance;
            _tmpCurrentBalance = _cursor.getDouble(_cursorIndexOfCurrentBalance);
            final Integer _tmpLastInvoiceId;
            if (_cursor.isNull(_cursorIndexOfLastInvoiceId)) {
              _tmpLastInvoiceId = null;
            } else {
              _tmpLastInvoiceId = _cursor.getInt(_cursorIndexOfLastInvoiceId);
            }
            final String _tmpLastInvoiceNumber;
            if (_cursor.isNull(_cursorIndexOfLastInvoiceNumber)) {
              _tmpLastInvoiceNumber = null;
            } else {
              _tmpLastInvoiceNumber = _cursor.getString(_cursorIndexOfLastInvoiceNumber);
            }
            final String _tmpLastInvoiceDate;
            if (_cursor.isNull(_cursorIndexOfLastInvoiceDate)) {
              _tmpLastInvoiceDate = null;
            } else {
              _tmpLastInvoiceDate = _cursor.getString(_cursorIndexOfLastInvoiceDate);
            }
            final Double _tmpLastInvoiceTotal;
            if (_cursor.isNull(_cursorIndexOfLastInvoiceTotal)) {
              _tmpLastInvoiceTotal = null;
            } else {
              _tmpLastInvoiceTotal = _cursor.getDouble(_cursorIndexOfLastInvoiceTotal);
            }
            final Double _tmpLastInvoiceRemaining;
            if (_cursor.isNull(_cursorIndexOfLastInvoiceRemaining)) {
              _tmpLastInvoiceRemaining = null;
            } else {
              _tmpLastInvoiceRemaining = _cursor.getDouble(_cursorIndexOfLastInvoiceRemaining);
            }
            _result = new LocalSubscriberEntity(_tmpSubscriberId,_tmpSubscriberName,_tmpPhoneNumber,_tmpAddress,_tmpPrimaryMeterId,_tmpPrimaryMeterNumber,_tmpPrimaryMeterLocation,_tmpCurrentDue,_tmpCurrentCredit,_tmpCurrentBalance,_tmpLastInvoiceId,_tmpLastInvoiceNumber,_tmpLastInvoiceDate,_tmpLastInvoiceTotal,_tmpLastInvoiceRemaining);
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
