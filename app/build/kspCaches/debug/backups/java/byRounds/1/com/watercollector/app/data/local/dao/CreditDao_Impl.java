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
import com.watercollector.app.data.local.entities.LocalSubscriberCreditEntity;
import java.lang.Class;
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

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class CreditDao_Impl implements CreditDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<LocalSubscriberCreditEntity> __insertionAdapterOfLocalSubscriberCreditEntity;

  private final SharedSQLiteStatement __preparedStmtOfClearAll;

  public CreditDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfLocalSubscriberCreditEntity = new EntityInsertionAdapter<LocalSubscriberCreditEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `local_subscriber_credits` (`creditId`,`subscriberId`,`paymentId`,`receiptId`,`meterId`,`creditDate`,`amountTotal`,`amountRemaining`,`notes`) VALUES (?,?,?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final LocalSubscriberCreditEntity entity) {
        statement.bindLong(1, entity.getCreditId());
        statement.bindLong(2, entity.getSubscriberId());
        if (entity.getPaymentId() == null) {
          statement.bindNull(3);
        } else {
          statement.bindLong(3, entity.getPaymentId());
        }
        if (entity.getReceiptId() == null) {
          statement.bindNull(4);
        } else {
          statement.bindLong(4, entity.getReceiptId());
        }
        if (entity.getMeterId() == null) {
          statement.bindNull(5);
        } else {
          statement.bindLong(5, entity.getMeterId());
        }
        statement.bindString(6, entity.getCreditDate());
        statement.bindDouble(7, entity.getAmountTotal());
        statement.bindDouble(8, entity.getAmountRemaining());
        if (entity.getNotes() == null) {
          statement.bindNull(9);
        } else {
          statement.bindString(9, entity.getNotes());
        }
      }
    };
    this.__preparedStmtOfClearAll = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM local_subscriber_credits";
        return _query;
      }
    };
  }

  @Override
  public Object insertAll(final List<LocalSubscriberCreditEntity> items,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfLocalSubscriberCreditEntity.insert(items);
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
      final Continuation<? super List<LocalSubscriberCreditEntity>> $completion) {
    final String _sql = "SELECT * FROM local_subscriber_credits WHERE subscriberId = ? ORDER BY creditDate DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, subscriberId);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<LocalSubscriberCreditEntity>>() {
      @Override
      @NonNull
      public List<LocalSubscriberCreditEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfCreditId = CursorUtil.getColumnIndexOrThrow(_cursor, "creditId");
          final int _cursorIndexOfSubscriberId = CursorUtil.getColumnIndexOrThrow(_cursor, "subscriberId");
          final int _cursorIndexOfPaymentId = CursorUtil.getColumnIndexOrThrow(_cursor, "paymentId");
          final int _cursorIndexOfReceiptId = CursorUtil.getColumnIndexOrThrow(_cursor, "receiptId");
          final int _cursorIndexOfMeterId = CursorUtil.getColumnIndexOrThrow(_cursor, "meterId");
          final int _cursorIndexOfCreditDate = CursorUtil.getColumnIndexOrThrow(_cursor, "creditDate");
          final int _cursorIndexOfAmountTotal = CursorUtil.getColumnIndexOrThrow(_cursor, "amountTotal");
          final int _cursorIndexOfAmountRemaining = CursorUtil.getColumnIndexOrThrow(_cursor, "amountRemaining");
          final int _cursorIndexOfNotes = CursorUtil.getColumnIndexOrThrow(_cursor, "notes");
          final List<LocalSubscriberCreditEntity> _result = new ArrayList<LocalSubscriberCreditEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final LocalSubscriberCreditEntity _item;
            final int _tmpCreditId;
            _tmpCreditId = _cursor.getInt(_cursorIndexOfCreditId);
            final int _tmpSubscriberId;
            _tmpSubscriberId = _cursor.getInt(_cursorIndexOfSubscriberId);
            final Integer _tmpPaymentId;
            if (_cursor.isNull(_cursorIndexOfPaymentId)) {
              _tmpPaymentId = null;
            } else {
              _tmpPaymentId = _cursor.getInt(_cursorIndexOfPaymentId);
            }
            final Integer _tmpReceiptId;
            if (_cursor.isNull(_cursorIndexOfReceiptId)) {
              _tmpReceiptId = null;
            } else {
              _tmpReceiptId = _cursor.getInt(_cursorIndexOfReceiptId);
            }
            final Integer _tmpMeterId;
            if (_cursor.isNull(_cursorIndexOfMeterId)) {
              _tmpMeterId = null;
            } else {
              _tmpMeterId = _cursor.getInt(_cursorIndexOfMeterId);
            }
            final String _tmpCreditDate;
            _tmpCreditDate = _cursor.getString(_cursorIndexOfCreditDate);
            final double _tmpAmountTotal;
            _tmpAmountTotal = _cursor.getDouble(_cursorIndexOfAmountTotal);
            final double _tmpAmountRemaining;
            _tmpAmountRemaining = _cursor.getDouble(_cursorIndexOfAmountRemaining);
            final String _tmpNotes;
            if (_cursor.isNull(_cursorIndexOfNotes)) {
              _tmpNotes = null;
            } else {
              _tmpNotes = _cursor.getString(_cursorIndexOfNotes);
            }
            _item = new LocalSubscriberCreditEntity(_tmpCreditId,_tmpSubscriberId,_tmpPaymentId,_tmpReceiptId,_tmpMeterId,_tmpCreditDate,_tmpAmountTotal,_tmpAmountRemaining,_tmpNotes);
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
