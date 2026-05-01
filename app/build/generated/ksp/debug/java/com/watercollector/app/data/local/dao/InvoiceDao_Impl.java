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
import com.watercollector.app.data.local.entities.LocalOpenInvoiceEntity;
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
public final class InvoiceDao_Impl implements InvoiceDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<LocalOpenInvoiceEntity> __insertionAdapterOfLocalOpenInvoiceEntity;

  private final SharedSQLiteStatement __preparedStmtOfClearAll;

  public InvoiceDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfLocalOpenInvoiceEntity = new EntityInsertionAdapter<LocalOpenInvoiceEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `local_open_invoices` (`invoiceId`,`subscriberId`,`meterId`,`invoiceNumber`,`invoiceDate`,`consumption`,`unitPrice`,`serviceFees`,`arrears`,`totalAmount`,`grandTotal`,`paidTotal`,`remaining`,`status`,`notes`) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final LocalOpenInvoiceEntity entity) {
        statement.bindLong(1, entity.getInvoiceId());
        statement.bindLong(2, entity.getSubscriberId());
        if (entity.getMeterId() == null) {
          statement.bindNull(3);
        } else {
          statement.bindLong(3, entity.getMeterId());
        }
        if (entity.getInvoiceNumber() == null) {
          statement.bindNull(4);
        } else {
          statement.bindString(4, entity.getInvoiceNumber());
        }
        statement.bindString(5, entity.getInvoiceDate());
        statement.bindDouble(6, entity.getConsumption());
        statement.bindDouble(7, entity.getUnitPrice());
        statement.bindDouble(8, entity.getServiceFees());
        statement.bindDouble(9, entity.getArrears());
        statement.bindDouble(10, entity.getTotalAmount());
        statement.bindDouble(11, entity.getGrandTotal());
        statement.bindDouble(12, entity.getPaidTotal());
        statement.bindDouble(13, entity.getRemaining());
        if (entity.getStatus() == null) {
          statement.bindNull(14);
        } else {
          statement.bindString(14, entity.getStatus());
        }
        if (entity.getNotes() == null) {
          statement.bindNull(15);
        } else {
          statement.bindString(15, entity.getNotes());
        }
      }
    };
    this.__preparedStmtOfClearAll = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM local_open_invoices";
        return _query;
      }
    };
  }

  @Override
  public Object insertAll(final List<LocalOpenInvoiceEntity> items,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfLocalOpenInvoiceEntity.insert(items);
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
      final Continuation<? super List<LocalOpenInvoiceEntity>> $completion) {
    final String _sql = "SELECT * FROM local_open_invoices WHERE subscriberId = ? ORDER BY invoiceDate";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, subscriberId);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<LocalOpenInvoiceEntity>>() {
      @Override
      @NonNull
      public List<LocalOpenInvoiceEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfInvoiceId = CursorUtil.getColumnIndexOrThrow(_cursor, "invoiceId");
          final int _cursorIndexOfSubscriberId = CursorUtil.getColumnIndexOrThrow(_cursor, "subscriberId");
          final int _cursorIndexOfMeterId = CursorUtil.getColumnIndexOrThrow(_cursor, "meterId");
          final int _cursorIndexOfInvoiceNumber = CursorUtil.getColumnIndexOrThrow(_cursor, "invoiceNumber");
          final int _cursorIndexOfInvoiceDate = CursorUtil.getColumnIndexOrThrow(_cursor, "invoiceDate");
          final int _cursorIndexOfConsumption = CursorUtil.getColumnIndexOrThrow(_cursor, "consumption");
          final int _cursorIndexOfUnitPrice = CursorUtil.getColumnIndexOrThrow(_cursor, "unitPrice");
          final int _cursorIndexOfServiceFees = CursorUtil.getColumnIndexOrThrow(_cursor, "serviceFees");
          final int _cursorIndexOfArrears = CursorUtil.getColumnIndexOrThrow(_cursor, "arrears");
          final int _cursorIndexOfTotalAmount = CursorUtil.getColumnIndexOrThrow(_cursor, "totalAmount");
          final int _cursorIndexOfGrandTotal = CursorUtil.getColumnIndexOrThrow(_cursor, "grandTotal");
          final int _cursorIndexOfPaidTotal = CursorUtil.getColumnIndexOrThrow(_cursor, "paidTotal");
          final int _cursorIndexOfRemaining = CursorUtil.getColumnIndexOrThrow(_cursor, "remaining");
          final int _cursorIndexOfStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "status");
          final int _cursorIndexOfNotes = CursorUtil.getColumnIndexOrThrow(_cursor, "notes");
          final List<LocalOpenInvoiceEntity> _result = new ArrayList<LocalOpenInvoiceEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final LocalOpenInvoiceEntity _item;
            final int _tmpInvoiceId;
            _tmpInvoiceId = _cursor.getInt(_cursorIndexOfInvoiceId);
            final int _tmpSubscriberId;
            _tmpSubscriberId = _cursor.getInt(_cursorIndexOfSubscriberId);
            final Integer _tmpMeterId;
            if (_cursor.isNull(_cursorIndexOfMeterId)) {
              _tmpMeterId = null;
            } else {
              _tmpMeterId = _cursor.getInt(_cursorIndexOfMeterId);
            }
            final String _tmpInvoiceNumber;
            if (_cursor.isNull(_cursorIndexOfInvoiceNumber)) {
              _tmpInvoiceNumber = null;
            } else {
              _tmpInvoiceNumber = _cursor.getString(_cursorIndexOfInvoiceNumber);
            }
            final String _tmpInvoiceDate;
            _tmpInvoiceDate = _cursor.getString(_cursorIndexOfInvoiceDate);
            final double _tmpConsumption;
            _tmpConsumption = _cursor.getDouble(_cursorIndexOfConsumption);
            final double _tmpUnitPrice;
            _tmpUnitPrice = _cursor.getDouble(_cursorIndexOfUnitPrice);
            final double _tmpServiceFees;
            _tmpServiceFees = _cursor.getDouble(_cursorIndexOfServiceFees);
            final double _tmpArrears;
            _tmpArrears = _cursor.getDouble(_cursorIndexOfArrears);
            final double _tmpTotalAmount;
            _tmpTotalAmount = _cursor.getDouble(_cursorIndexOfTotalAmount);
            final double _tmpGrandTotal;
            _tmpGrandTotal = _cursor.getDouble(_cursorIndexOfGrandTotal);
            final double _tmpPaidTotal;
            _tmpPaidTotal = _cursor.getDouble(_cursorIndexOfPaidTotal);
            final double _tmpRemaining;
            _tmpRemaining = _cursor.getDouble(_cursorIndexOfRemaining);
            final String _tmpStatus;
            if (_cursor.isNull(_cursorIndexOfStatus)) {
              _tmpStatus = null;
            } else {
              _tmpStatus = _cursor.getString(_cursorIndexOfStatus);
            }
            final String _tmpNotes;
            if (_cursor.isNull(_cursorIndexOfNotes)) {
              _tmpNotes = null;
            } else {
              _tmpNotes = _cursor.getString(_cursorIndexOfNotes);
            }
            _item = new LocalOpenInvoiceEntity(_tmpInvoiceId,_tmpSubscriberId,_tmpMeterId,_tmpInvoiceNumber,_tmpInvoiceDate,_tmpConsumption,_tmpUnitPrice,_tmpServiceFees,_tmpArrears,_tmpTotalAmount,_tmpGrandTotal,_tmpPaidTotal,_tmpRemaining,_tmpStatus,_tmpNotes);
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
