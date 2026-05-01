package com.watercollector.app.data.local.dao;

import android.database.Cursor;
import android.os.CancellationSignal;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.collection.LongSparseArray;
import androidx.room.CoroutinesRoom;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomDatabaseKt;
import androidx.room.RoomSQLiteQuery;
import androidx.room.SharedSQLiteStatement;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.room.util.RelationUtil;
import androidx.room.util.StringUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.watercollector.app.data.local.entities.LocalReceiptDraftEntity;
import com.watercollector.app.data.local.entities.LocalReceiptDraftLineEntity;
import com.watercollector.app.data.local.entities.ReceiptDraftWithLines;
import java.lang.Class;
import java.lang.Exception;
import java.lang.Integer;
import java.lang.Long;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.lang.StringBuilder;
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
public final class ReceiptDraftDao_Impl implements ReceiptDraftDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<LocalReceiptDraftEntity> __insertionAdapterOfLocalReceiptDraftEntity;

  private final EntityInsertionAdapter<LocalReceiptDraftLineEntity> __insertionAdapterOfLocalReceiptDraftLineEntity;

  private final SharedSQLiteStatement __preparedStmtOfUpdateSyncState;

  public ReceiptDraftDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfLocalReceiptDraftEntity = new EntityInsertionAdapter<LocalReceiptDraftEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `local_receipt_drafts` (`localReceiptId`,`localPaymentGuid`,`localReceiptNo`,`subscriberId`,`collectorId`,`paymentDate`,`totalReceived`,`paymentMethod`,`notes`,`syncStatus`,`syncBatchRef`,`serverImportId`,`serverStatus`,`rejectedReason`,`createdAt`,`updatedAt`,`sentAt`) VALUES (nullif(?, 0),?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final LocalReceiptDraftEntity entity) {
        statement.bindLong(1, entity.getLocalReceiptId());
        statement.bindString(2, entity.getLocalPaymentGuid());
        statement.bindString(3, entity.getLocalReceiptNo());
        statement.bindLong(4, entity.getSubscriberId());
        statement.bindLong(5, entity.getCollectorId());
        statement.bindString(6, entity.getPaymentDate());
        statement.bindDouble(7, entity.getTotalReceived());
        statement.bindString(8, entity.getPaymentMethod());
        if (entity.getNotes() == null) {
          statement.bindNull(9);
        } else {
          statement.bindString(9, entity.getNotes());
        }
        statement.bindString(10, entity.getSyncStatus());
        if (entity.getSyncBatchRef() == null) {
          statement.bindNull(11);
        } else {
          statement.bindString(11, entity.getSyncBatchRef());
        }
        if (entity.getServerImportId() == null) {
          statement.bindNull(12);
        } else {
          statement.bindLong(12, entity.getServerImportId());
        }
        if (entity.getServerStatus() == null) {
          statement.bindNull(13);
        } else {
          statement.bindString(13, entity.getServerStatus());
        }
        if (entity.getRejectedReason() == null) {
          statement.bindNull(14);
        } else {
          statement.bindString(14, entity.getRejectedReason());
        }
        statement.bindString(15, entity.getCreatedAt());
        if (entity.getUpdatedAt() == null) {
          statement.bindNull(16);
        } else {
          statement.bindString(16, entity.getUpdatedAt());
        }
        if (entity.getSentAt() == null) {
          statement.bindNull(17);
        } else {
          statement.bindString(17, entity.getSentAt());
        }
      }
    };
    this.__insertionAdapterOfLocalReceiptDraftLineEntity = new EntityInsertionAdapter<LocalReceiptDraftLineEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `local_receipt_draft_lines` (`localReceiptLineId`,`localReceiptId`,`invoiceId`,`appliedAmount`,`applicationType`,`notes`) VALUES (nullif(?, 0),?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final LocalReceiptDraftLineEntity entity) {
        statement.bindLong(1, entity.getLocalReceiptLineId());
        statement.bindLong(2, entity.getLocalReceiptId());
        if (entity.getInvoiceId() == null) {
          statement.bindNull(3);
        } else {
          statement.bindLong(3, entity.getInvoiceId());
        }
        statement.bindDouble(4, entity.getAppliedAmount());
        statement.bindString(5, entity.getApplicationType());
        if (entity.getNotes() == null) {
          statement.bindNull(6);
        } else {
          statement.bindString(6, entity.getNotes());
        }
      }
    };
    this.__preparedStmtOfUpdateSyncState = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "\n"
                + "        UPDATE local_receipt_drafts\n"
                + "        SET syncStatus = ?,\n"
                + "            syncBatchRef = ?,\n"
                + "            serverImportId = ?,\n"
                + "            serverStatus = ?,\n"
                + "            rejectedReason = ?,\n"
                + "            updatedAt = ?,\n"
                + "            sentAt = ?\n"
                + "        WHERE localReceiptId = ?\n"
                + "        ";
        return _query;
      }
    };
  }

  @Override
  public Object insert(final LocalReceiptDraftEntity item,
      final Continuation<? super Long> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Long>() {
      @Override
      @NonNull
      public Long call() throws Exception {
        __db.beginTransaction();
        try {
          final Long _result = __insertionAdapterOfLocalReceiptDraftEntity.insertAndReturnId(item);
          __db.setTransactionSuccessful();
          return _result;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object insertLines(final List<LocalReceiptDraftLineEntity> items,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfLocalReceiptDraftLineEntity.insert(items);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object insertDraftWithLines(final LocalReceiptDraftEntity draft,
      final List<LocalReceiptDraftLineEntity> lines, final Continuation<? super Long> $completion) {
    return RoomDatabaseKt.withTransaction(__db, (__cont) -> ReceiptDraftDao.DefaultImpls.insertDraftWithLines(ReceiptDraftDao_Impl.this, draft, lines, __cont), $completion);
  }

  @Override
  public Object updateSyncState(final long localReceiptId, final String syncStatus,
      final String syncBatchRef, final Integer serverImportId, final String serverStatus,
      final String rejectedReason, final String updatedAt, final String sentAt,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfUpdateSyncState.acquire();
        int _argIndex = 1;
        _stmt.bindString(_argIndex, syncStatus);
        _argIndex = 2;
        if (syncBatchRef == null) {
          _stmt.bindNull(_argIndex);
        } else {
          _stmt.bindString(_argIndex, syncBatchRef);
        }
        _argIndex = 3;
        if (serverImportId == null) {
          _stmt.bindNull(_argIndex);
        } else {
          _stmt.bindLong(_argIndex, serverImportId);
        }
        _argIndex = 4;
        if (serverStatus == null) {
          _stmt.bindNull(_argIndex);
        } else {
          _stmt.bindString(_argIndex, serverStatus);
        }
        _argIndex = 5;
        if (rejectedReason == null) {
          _stmt.bindNull(_argIndex);
        } else {
          _stmt.bindString(_argIndex, rejectedReason);
        }
        _argIndex = 6;
        if (updatedAt == null) {
          _stmt.bindNull(_argIndex);
        } else {
          _stmt.bindString(_argIndex, updatedAt);
        }
        _argIndex = 7;
        if (sentAt == null) {
          _stmt.bindNull(_argIndex);
        } else {
          _stmt.bindString(_argIndex, sentAt);
        }
        _argIndex = 8;
        _stmt.bindLong(_argIndex, localReceiptId);
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
          __preparedStmtOfUpdateSyncState.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Flow<List<LocalReceiptDraftEntity>> observeAll() {
    final String _sql = "SELECT * FROM local_receipt_drafts ORDER BY localReceiptId DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"local_receipt_drafts"}, new Callable<List<LocalReceiptDraftEntity>>() {
      @Override
      @NonNull
      public List<LocalReceiptDraftEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfLocalReceiptId = CursorUtil.getColumnIndexOrThrow(_cursor, "localReceiptId");
          final int _cursorIndexOfLocalPaymentGuid = CursorUtil.getColumnIndexOrThrow(_cursor, "localPaymentGuid");
          final int _cursorIndexOfLocalReceiptNo = CursorUtil.getColumnIndexOrThrow(_cursor, "localReceiptNo");
          final int _cursorIndexOfSubscriberId = CursorUtil.getColumnIndexOrThrow(_cursor, "subscriberId");
          final int _cursorIndexOfCollectorId = CursorUtil.getColumnIndexOrThrow(_cursor, "collectorId");
          final int _cursorIndexOfPaymentDate = CursorUtil.getColumnIndexOrThrow(_cursor, "paymentDate");
          final int _cursorIndexOfTotalReceived = CursorUtil.getColumnIndexOrThrow(_cursor, "totalReceived");
          final int _cursorIndexOfPaymentMethod = CursorUtil.getColumnIndexOrThrow(_cursor, "paymentMethod");
          final int _cursorIndexOfNotes = CursorUtil.getColumnIndexOrThrow(_cursor, "notes");
          final int _cursorIndexOfSyncStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "syncStatus");
          final int _cursorIndexOfSyncBatchRef = CursorUtil.getColumnIndexOrThrow(_cursor, "syncBatchRef");
          final int _cursorIndexOfServerImportId = CursorUtil.getColumnIndexOrThrow(_cursor, "serverImportId");
          final int _cursorIndexOfServerStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "serverStatus");
          final int _cursorIndexOfRejectedReason = CursorUtil.getColumnIndexOrThrow(_cursor, "rejectedReason");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
          final int _cursorIndexOfUpdatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "updatedAt");
          final int _cursorIndexOfSentAt = CursorUtil.getColumnIndexOrThrow(_cursor, "sentAt");
          final List<LocalReceiptDraftEntity> _result = new ArrayList<LocalReceiptDraftEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final LocalReceiptDraftEntity _item;
            final long _tmpLocalReceiptId;
            _tmpLocalReceiptId = _cursor.getLong(_cursorIndexOfLocalReceiptId);
            final String _tmpLocalPaymentGuid;
            _tmpLocalPaymentGuid = _cursor.getString(_cursorIndexOfLocalPaymentGuid);
            final String _tmpLocalReceiptNo;
            _tmpLocalReceiptNo = _cursor.getString(_cursorIndexOfLocalReceiptNo);
            final int _tmpSubscriberId;
            _tmpSubscriberId = _cursor.getInt(_cursorIndexOfSubscriberId);
            final int _tmpCollectorId;
            _tmpCollectorId = _cursor.getInt(_cursorIndexOfCollectorId);
            final String _tmpPaymentDate;
            _tmpPaymentDate = _cursor.getString(_cursorIndexOfPaymentDate);
            final double _tmpTotalReceived;
            _tmpTotalReceived = _cursor.getDouble(_cursorIndexOfTotalReceived);
            final String _tmpPaymentMethod;
            _tmpPaymentMethod = _cursor.getString(_cursorIndexOfPaymentMethod);
            final String _tmpNotes;
            if (_cursor.isNull(_cursorIndexOfNotes)) {
              _tmpNotes = null;
            } else {
              _tmpNotes = _cursor.getString(_cursorIndexOfNotes);
            }
            final String _tmpSyncStatus;
            _tmpSyncStatus = _cursor.getString(_cursorIndexOfSyncStatus);
            final String _tmpSyncBatchRef;
            if (_cursor.isNull(_cursorIndexOfSyncBatchRef)) {
              _tmpSyncBatchRef = null;
            } else {
              _tmpSyncBatchRef = _cursor.getString(_cursorIndexOfSyncBatchRef);
            }
            final Integer _tmpServerImportId;
            if (_cursor.isNull(_cursorIndexOfServerImportId)) {
              _tmpServerImportId = null;
            } else {
              _tmpServerImportId = _cursor.getInt(_cursorIndexOfServerImportId);
            }
            final String _tmpServerStatus;
            if (_cursor.isNull(_cursorIndexOfServerStatus)) {
              _tmpServerStatus = null;
            } else {
              _tmpServerStatus = _cursor.getString(_cursorIndexOfServerStatus);
            }
            final String _tmpRejectedReason;
            if (_cursor.isNull(_cursorIndexOfRejectedReason)) {
              _tmpRejectedReason = null;
            } else {
              _tmpRejectedReason = _cursor.getString(_cursorIndexOfRejectedReason);
            }
            final String _tmpCreatedAt;
            _tmpCreatedAt = _cursor.getString(_cursorIndexOfCreatedAt);
            final String _tmpUpdatedAt;
            if (_cursor.isNull(_cursorIndexOfUpdatedAt)) {
              _tmpUpdatedAt = null;
            } else {
              _tmpUpdatedAt = _cursor.getString(_cursorIndexOfUpdatedAt);
            }
            final String _tmpSentAt;
            if (_cursor.isNull(_cursorIndexOfSentAt)) {
              _tmpSentAt = null;
            } else {
              _tmpSentAt = _cursor.getString(_cursorIndexOfSentAt);
            }
            _item = new LocalReceiptDraftEntity(_tmpLocalReceiptId,_tmpLocalPaymentGuid,_tmpLocalReceiptNo,_tmpSubscriberId,_tmpCollectorId,_tmpPaymentDate,_tmpTotalReceived,_tmpPaymentMethod,_tmpNotes,_tmpSyncStatus,_tmpSyncBatchRef,_tmpServerImportId,_tmpServerStatus,_tmpRejectedReason,_tmpCreatedAt,_tmpUpdatedAt,_tmpSentAt);
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
  public Flow<List<LocalReceiptDraftEntity>> observePending() {
    final String _sql = "SELECT * FROM local_receipt_drafts WHERE syncStatus = 'Pending' ORDER BY localReceiptId DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"local_receipt_drafts"}, new Callable<List<LocalReceiptDraftEntity>>() {
      @Override
      @NonNull
      public List<LocalReceiptDraftEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfLocalReceiptId = CursorUtil.getColumnIndexOrThrow(_cursor, "localReceiptId");
          final int _cursorIndexOfLocalPaymentGuid = CursorUtil.getColumnIndexOrThrow(_cursor, "localPaymentGuid");
          final int _cursorIndexOfLocalReceiptNo = CursorUtil.getColumnIndexOrThrow(_cursor, "localReceiptNo");
          final int _cursorIndexOfSubscriberId = CursorUtil.getColumnIndexOrThrow(_cursor, "subscriberId");
          final int _cursorIndexOfCollectorId = CursorUtil.getColumnIndexOrThrow(_cursor, "collectorId");
          final int _cursorIndexOfPaymentDate = CursorUtil.getColumnIndexOrThrow(_cursor, "paymentDate");
          final int _cursorIndexOfTotalReceived = CursorUtil.getColumnIndexOrThrow(_cursor, "totalReceived");
          final int _cursorIndexOfPaymentMethod = CursorUtil.getColumnIndexOrThrow(_cursor, "paymentMethod");
          final int _cursorIndexOfNotes = CursorUtil.getColumnIndexOrThrow(_cursor, "notes");
          final int _cursorIndexOfSyncStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "syncStatus");
          final int _cursorIndexOfSyncBatchRef = CursorUtil.getColumnIndexOrThrow(_cursor, "syncBatchRef");
          final int _cursorIndexOfServerImportId = CursorUtil.getColumnIndexOrThrow(_cursor, "serverImportId");
          final int _cursorIndexOfServerStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "serverStatus");
          final int _cursorIndexOfRejectedReason = CursorUtil.getColumnIndexOrThrow(_cursor, "rejectedReason");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
          final int _cursorIndexOfUpdatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "updatedAt");
          final int _cursorIndexOfSentAt = CursorUtil.getColumnIndexOrThrow(_cursor, "sentAt");
          final List<LocalReceiptDraftEntity> _result = new ArrayList<LocalReceiptDraftEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final LocalReceiptDraftEntity _item;
            final long _tmpLocalReceiptId;
            _tmpLocalReceiptId = _cursor.getLong(_cursorIndexOfLocalReceiptId);
            final String _tmpLocalPaymentGuid;
            _tmpLocalPaymentGuid = _cursor.getString(_cursorIndexOfLocalPaymentGuid);
            final String _tmpLocalReceiptNo;
            _tmpLocalReceiptNo = _cursor.getString(_cursorIndexOfLocalReceiptNo);
            final int _tmpSubscriberId;
            _tmpSubscriberId = _cursor.getInt(_cursorIndexOfSubscriberId);
            final int _tmpCollectorId;
            _tmpCollectorId = _cursor.getInt(_cursorIndexOfCollectorId);
            final String _tmpPaymentDate;
            _tmpPaymentDate = _cursor.getString(_cursorIndexOfPaymentDate);
            final double _tmpTotalReceived;
            _tmpTotalReceived = _cursor.getDouble(_cursorIndexOfTotalReceived);
            final String _tmpPaymentMethod;
            _tmpPaymentMethod = _cursor.getString(_cursorIndexOfPaymentMethod);
            final String _tmpNotes;
            if (_cursor.isNull(_cursorIndexOfNotes)) {
              _tmpNotes = null;
            } else {
              _tmpNotes = _cursor.getString(_cursorIndexOfNotes);
            }
            final String _tmpSyncStatus;
            _tmpSyncStatus = _cursor.getString(_cursorIndexOfSyncStatus);
            final String _tmpSyncBatchRef;
            if (_cursor.isNull(_cursorIndexOfSyncBatchRef)) {
              _tmpSyncBatchRef = null;
            } else {
              _tmpSyncBatchRef = _cursor.getString(_cursorIndexOfSyncBatchRef);
            }
            final Integer _tmpServerImportId;
            if (_cursor.isNull(_cursorIndexOfServerImportId)) {
              _tmpServerImportId = null;
            } else {
              _tmpServerImportId = _cursor.getInt(_cursorIndexOfServerImportId);
            }
            final String _tmpServerStatus;
            if (_cursor.isNull(_cursorIndexOfServerStatus)) {
              _tmpServerStatus = null;
            } else {
              _tmpServerStatus = _cursor.getString(_cursorIndexOfServerStatus);
            }
            final String _tmpRejectedReason;
            if (_cursor.isNull(_cursorIndexOfRejectedReason)) {
              _tmpRejectedReason = null;
            } else {
              _tmpRejectedReason = _cursor.getString(_cursorIndexOfRejectedReason);
            }
            final String _tmpCreatedAt;
            _tmpCreatedAt = _cursor.getString(_cursorIndexOfCreatedAt);
            final String _tmpUpdatedAt;
            if (_cursor.isNull(_cursorIndexOfUpdatedAt)) {
              _tmpUpdatedAt = null;
            } else {
              _tmpUpdatedAt = _cursor.getString(_cursorIndexOfUpdatedAt);
            }
            final String _tmpSentAt;
            if (_cursor.isNull(_cursorIndexOfSentAt)) {
              _tmpSentAt = null;
            } else {
              _tmpSentAt = _cursor.getString(_cursorIndexOfSentAt);
            }
            _item = new LocalReceiptDraftEntity(_tmpLocalReceiptId,_tmpLocalPaymentGuid,_tmpLocalReceiptNo,_tmpSubscriberId,_tmpCollectorId,_tmpPaymentDate,_tmpTotalReceived,_tmpPaymentMethod,_tmpNotes,_tmpSyncStatus,_tmpSyncBatchRef,_tmpServerImportId,_tmpServerStatus,_tmpRejectedReason,_tmpCreatedAt,_tmpUpdatedAt,_tmpSentAt);
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
  public Object getPendingWithLines(
      final Continuation<? super List<ReceiptDraftWithLines>> $completion) {
    final String _sql = "SELECT * FROM local_receipt_drafts WHERE syncStatus = 'Pending' ORDER BY localReceiptId";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, true, _cancellationSignal, new Callable<List<ReceiptDraftWithLines>>() {
      @Override
      @NonNull
      public List<ReceiptDraftWithLines> call() throws Exception {
        __db.beginTransaction();
        try {
          final Cursor _cursor = DBUtil.query(__db, _statement, true, null);
          try {
            final int _cursorIndexOfLocalReceiptId = CursorUtil.getColumnIndexOrThrow(_cursor, "localReceiptId");
            final int _cursorIndexOfLocalPaymentGuid = CursorUtil.getColumnIndexOrThrow(_cursor, "localPaymentGuid");
            final int _cursorIndexOfLocalReceiptNo = CursorUtil.getColumnIndexOrThrow(_cursor, "localReceiptNo");
            final int _cursorIndexOfSubscriberId = CursorUtil.getColumnIndexOrThrow(_cursor, "subscriberId");
            final int _cursorIndexOfCollectorId = CursorUtil.getColumnIndexOrThrow(_cursor, "collectorId");
            final int _cursorIndexOfPaymentDate = CursorUtil.getColumnIndexOrThrow(_cursor, "paymentDate");
            final int _cursorIndexOfTotalReceived = CursorUtil.getColumnIndexOrThrow(_cursor, "totalReceived");
            final int _cursorIndexOfPaymentMethod = CursorUtil.getColumnIndexOrThrow(_cursor, "paymentMethod");
            final int _cursorIndexOfNotes = CursorUtil.getColumnIndexOrThrow(_cursor, "notes");
            final int _cursorIndexOfSyncStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "syncStatus");
            final int _cursorIndexOfSyncBatchRef = CursorUtil.getColumnIndexOrThrow(_cursor, "syncBatchRef");
            final int _cursorIndexOfServerImportId = CursorUtil.getColumnIndexOrThrow(_cursor, "serverImportId");
            final int _cursorIndexOfServerStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "serverStatus");
            final int _cursorIndexOfRejectedReason = CursorUtil.getColumnIndexOrThrow(_cursor, "rejectedReason");
            final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
            final int _cursorIndexOfUpdatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "updatedAt");
            final int _cursorIndexOfSentAt = CursorUtil.getColumnIndexOrThrow(_cursor, "sentAt");
            final LongSparseArray<ArrayList<LocalReceiptDraftLineEntity>> _collectionLines = new LongSparseArray<ArrayList<LocalReceiptDraftLineEntity>>();
            while (_cursor.moveToNext()) {
              final long _tmpKey;
              _tmpKey = _cursor.getLong(_cursorIndexOfLocalReceiptId);
              if (!_collectionLines.containsKey(_tmpKey)) {
                _collectionLines.put(_tmpKey, new ArrayList<LocalReceiptDraftLineEntity>());
              }
            }
            _cursor.moveToPosition(-1);
            __fetchRelationshiplocalReceiptDraftLinesAscomWatercollectorAppDataLocalEntitiesLocalReceiptDraftLineEntity(_collectionLines);
            final List<ReceiptDraftWithLines> _result = new ArrayList<ReceiptDraftWithLines>(_cursor.getCount());
            while (_cursor.moveToNext()) {
              final ReceiptDraftWithLines _item;
              final LocalReceiptDraftEntity _tmpDraft;
              final long _tmpLocalReceiptId;
              _tmpLocalReceiptId = _cursor.getLong(_cursorIndexOfLocalReceiptId);
              final String _tmpLocalPaymentGuid;
              _tmpLocalPaymentGuid = _cursor.getString(_cursorIndexOfLocalPaymentGuid);
              final String _tmpLocalReceiptNo;
              _tmpLocalReceiptNo = _cursor.getString(_cursorIndexOfLocalReceiptNo);
              final int _tmpSubscriberId;
              _tmpSubscriberId = _cursor.getInt(_cursorIndexOfSubscriberId);
              final int _tmpCollectorId;
              _tmpCollectorId = _cursor.getInt(_cursorIndexOfCollectorId);
              final String _tmpPaymentDate;
              _tmpPaymentDate = _cursor.getString(_cursorIndexOfPaymentDate);
              final double _tmpTotalReceived;
              _tmpTotalReceived = _cursor.getDouble(_cursorIndexOfTotalReceived);
              final String _tmpPaymentMethod;
              _tmpPaymentMethod = _cursor.getString(_cursorIndexOfPaymentMethod);
              final String _tmpNotes;
              if (_cursor.isNull(_cursorIndexOfNotes)) {
                _tmpNotes = null;
              } else {
                _tmpNotes = _cursor.getString(_cursorIndexOfNotes);
              }
              final String _tmpSyncStatus;
              _tmpSyncStatus = _cursor.getString(_cursorIndexOfSyncStatus);
              final String _tmpSyncBatchRef;
              if (_cursor.isNull(_cursorIndexOfSyncBatchRef)) {
                _tmpSyncBatchRef = null;
              } else {
                _tmpSyncBatchRef = _cursor.getString(_cursorIndexOfSyncBatchRef);
              }
              final Integer _tmpServerImportId;
              if (_cursor.isNull(_cursorIndexOfServerImportId)) {
                _tmpServerImportId = null;
              } else {
                _tmpServerImportId = _cursor.getInt(_cursorIndexOfServerImportId);
              }
              final String _tmpServerStatus;
              if (_cursor.isNull(_cursorIndexOfServerStatus)) {
                _tmpServerStatus = null;
              } else {
                _tmpServerStatus = _cursor.getString(_cursorIndexOfServerStatus);
              }
              final String _tmpRejectedReason;
              if (_cursor.isNull(_cursorIndexOfRejectedReason)) {
                _tmpRejectedReason = null;
              } else {
                _tmpRejectedReason = _cursor.getString(_cursorIndexOfRejectedReason);
              }
              final String _tmpCreatedAt;
              _tmpCreatedAt = _cursor.getString(_cursorIndexOfCreatedAt);
              final String _tmpUpdatedAt;
              if (_cursor.isNull(_cursorIndexOfUpdatedAt)) {
                _tmpUpdatedAt = null;
              } else {
                _tmpUpdatedAt = _cursor.getString(_cursorIndexOfUpdatedAt);
              }
              final String _tmpSentAt;
              if (_cursor.isNull(_cursorIndexOfSentAt)) {
                _tmpSentAt = null;
              } else {
                _tmpSentAt = _cursor.getString(_cursorIndexOfSentAt);
              }
              _tmpDraft = new LocalReceiptDraftEntity(_tmpLocalReceiptId,_tmpLocalPaymentGuid,_tmpLocalReceiptNo,_tmpSubscriberId,_tmpCollectorId,_tmpPaymentDate,_tmpTotalReceived,_tmpPaymentMethod,_tmpNotes,_tmpSyncStatus,_tmpSyncBatchRef,_tmpServerImportId,_tmpServerStatus,_tmpRejectedReason,_tmpCreatedAt,_tmpUpdatedAt,_tmpSentAt);
              final ArrayList<LocalReceiptDraftLineEntity> _tmpLinesCollection;
              final long _tmpKey_1;
              _tmpKey_1 = _cursor.getLong(_cursorIndexOfLocalReceiptId);
              _tmpLinesCollection = _collectionLines.get(_tmpKey_1);
              _item = new ReceiptDraftWithLines(_tmpDraft,_tmpLinesCollection);
              _result.add(_item);
            }
            __db.setTransactionSuccessful();
            return _result;
          } finally {
            _cursor.close();
            _statement.release();
          }
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object findLocalReceiptIdByGuid(final String guid,
      final Continuation<? super Long> $completion) {
    final String _sql = "SELECT localReceiptId FROM local_receipt_drafts WHERE localPaymentGuid = ? LIMIT 1";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindString(_argIndex, guid);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<Long>() {
      @Override
      @Nullable
      public Long call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final Long _result;
          if (_cursor.moveToFirst()) {
            if (_cursor.isNull(0)) {
              _result = null;
            } else {
              _result = _cursor.getLong(0);
            }
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

  private void __fetchRelationshiplocalReceiptDraftLinesAscomWatercollectorAppDataLocalEntitiesLocalReceiptDraftLineEntity(
      @NonNull final LongSparseArray<ArrayList<LocalReceiptDraftLineEntity>> _map) {
    if (_map.isEmpty()) {
      return;
    }
    if (_map.size() > RoomDatabase.MAX_BIND_PARAMETER_CNT) {
      RelationUtil.recursiveFetchLongSparseArray(_map, true, (map) -> {
        __fetchRelationshiplocalReceiptDraftLinesAscomWatercollectorAppDataLocalEntitiesLocalReceiptDraftLineEntity(map);
        return Unit.INSTANCE;
      });
      return;
    }
    final StringBuilder _stringBuilder = StringUtil.newStringBuilder();
    _stringBuilder.append("SELECT `localReceiptLineId`,`localReceiptId`,`invoiceId`,`appliedAmount`,`applicationType`,`notes` FROM `local_receipt_draft_lines` WHERE `localReceiptId` IN (");
    final int _inputSize = _map.size();
    StringUtil.appendPlaceholders(_stringBuilder, _inputSize);
    _stringBuilder.append(")");
    final String _sql = _stringBuilder.toString();
    final int _argCount = 0 + _inputSize;
    final RoomSQLiteQuery _stmt = RoomSQLiteQuery.acquire(_sql, _argCount);
    int _argIndex = 1;
    for (int i = 0; i < _map.size(); i++) {
      final long _item = _map.keyAt(i);
      _stmt.bindLong(_argIndex, _item);
      _argIndex++;
    }
    final Cursor _cursor = DBUtil.query(__db, _stmt, false, null);
    try {
      final int _itemKeyIndex = CursorUtil.getColumnIndex(_cursor, "localReceiptId");
      if (_itemKeyIndex == -1) {
        return;
      }
      final int _cursorIndexOfLocalReceiptLineId = 0;
      final int _cursorIndexOfLocalReceiptId = 1;
      final int _cursorIndexOfInvoiceId = 2;
      final int _cursorIndexOfAppliedAmount = 3;
      final int _cursorIndexOfApplicationType = 4;
      final int _cursorIndexOfNotes = 5;
      while (_cursor.moveToNext()) {
        final long _tmpKey;
        _tmpKey = _cursor.getLong(_itemKeyIndex);
        final ArrayList<LocalReceiptDraftLineEntity> _tmpRelation = _map.get(_tmpKey);
        if (_tmpRelation != null) {
          final LocalReceiptDraftLineEntity _item_1;
          final long _tmpLocalReceiptLineId;
          _tmpLocalReceiptLineId = _cursor.getLong(_cursorIndexOfLocalReceiptLineId);
          final long _tmpLocalReceiptId;
          _tmpLocalReceiptId = _cursor.getLong(_cursorIndexOfLocalReceiptId);
          final Integer _tmpInvoiceId;
          if (_cursor.isNull(_cursorIndexOfInvoiceId)) {
            _tmpInvoiceId = null;
          } else {
            _tmpInvoiceId = _cursor.getInt(_cursorIndexOfInvoiceId);
          }
          final double _tmpAppliedAmount;
          _tmpAppliedAmount = _cursor.getDouble(_cursorIndexOfAppliedAmount);
          final String _tmpApplicationType;
          _tmpApplicationType = _cursor.getString(_cursorIndexOfApplicationType);
          final String _tmpNotes;
          if (_cursor.isNull(_cursorIndexOfNotes)) {
            _tmpNotes = null;
          } else {
            _tmpNotes = _cursor.getString(_cursorIndexOfNotes);
          }
          _item_1 = new LocalReceiptDraftLineEntity(_tmpLocalReceiptLineId,_tmpLocalReceiptId,_tmpInvoiceId,_tmpAppliedAmount,_tmpApplicationType,_tmpNotes);
          _tmpRelation.add(_item_1);
        }
      }
    } finally {
      _cursor.close();
    }
  }
}
