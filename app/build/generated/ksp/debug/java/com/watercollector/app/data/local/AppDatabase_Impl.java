package com.watercollector.app.data.local;

import androidx.annotation.NonNull;
import androidx.room.DatabaseConfiguration;
import androidx.room.InvalidationTracker;
import androidx.room.RoomDatabase;
import androidx.room.RoomOpenHelper;
import androidx.room.migration.AutoMigrationSpec;
import androidx.room.migration.Migration;
import androidx.room.util.DBUtil;
import androidx.room.util.TableInfo;
import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.sqlite.db.SupportSQLiteOpenHelper;
import com.watercollector.app.data.local.dao.CreditDao;
import com.watercollector.app.data.local.dao.CreditDao_Impl;
import com.watercollector.app.data.local.dao.InvoiceDao;
import com.watercollector.app.data.local.dao.InvoiceDao_Impl;
import com.watercollector.app.data.local.dao.MeterDao;
import com.watercollector.app.data.local.dao.MeterDao_Impl;
import com.watercollector.app.data.local.dao.ReceiptDraftDao;
import com.watercollector.app.data.local.dao.ReceiptDraftDao_Impl;
import com.watercollector.app.data.local.dao.SubscriberDao;
import com.watercollector.app.data.local.dao.SubscriberDao_Impl;
import java.lang.Class;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.annotation.processing.Generated;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class AppDatabase_Impl extends AppDatabase {
  private volatile SubscriberDao _subscriberDao;

  private volatile InvoiceDao _invoiceDao;

  private volatile MeterDao _meterDao;

  private volatile CreditDao _creditDao;

  private volatile ReceiptDraftDao _receiptDraftDao;

  @Override
  @NonNull
  protected SupportSQLiteOpenHelper createOpenHelper(@NonNull final DatabaseConfiguration config) {
    final SupportSQLiteOpenHelper.Callback _openCallback = new RoomOpenHelper(config, new RoomOpenHelper.Delegate(1) {
      @Override
      public void createAllTables(@NonNull final SupportSQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS `local_subscribers` (`subscriberId` INTEGER NOT NULL, `subscriberName` TEXT NOT NULL, `phoneNumber` TEXT, `address` TEXT, `primaryMeterId` INTEGER, `primaryMeterNumber` TEXT, `primaryMeterLocation` TEXT, `currentDue` REAL NOT NULL, `currentCredit` REAL NOT NULL, `currentBalance` REAL NOT NULL, `lastInvoiceId` INTEGER, `lastInvoiceNumber` TEXT, `lastInvoiceDate` TEXT, `lastInvoiceTotal` REAL, `lastInvoiceRemaining` REAL, PRIMARY KEY(`subscriberId`))");
        db.execSQL("CREATE TABLE IF NOT EXISTS `local_subscriber_meters` (`subscriberMeterId` INTEGER NOT NULL, `subscriberId` INTEGER NOT NULL, `meterId` INTEGER NOT NULL, `meterNumber` TEXT NOT NULL, `meterType` TEXT, `location` TEXT, `isActive` INTEGER NOT NULL, `isPrimary` INTEGER NOT NULL, `linkedAt` TEXT, PRIMARY KEY(`subscriberMeterId`))");
        db.execSQL("CREATE TABLE IF NOT EXISTS `local_open_invoices` (`invoiceId` INTEGER NOT NULL, `subscriberId` INTEGER NOT NULL, `meterId` INTEGER, `invoiceNumber` TEXT, `invoiceDate` TEXT NOT NULL, `consumption` REAL NOT NULL, `unitPrice` REAL NOT NULL, `serviceFees` REAL NOT NULL, `arrears` REAL NOT NULL, `totalAmount` REAL NOT NULL, `grandTotal` REAL NOT NULL, `paidTotal` REAL NOT NULL, `remaining` REAL NOT NULL, `status` TEXT, `notes` TEXT, PRIMARY KEY(`invoiceId`))");
        db.execSQL("CREATE TABLE IF NOT EXISTS `local_subscriber_credits` (`creditId` INTEGER NOT NULL, `subscriberId` INTEGER NOT NULL, `paymentId` INTEGER, `receiptId` INTEGER, `meterId` INTEGER, `creditDate` TEXT NOT NULL, `amountTotal` REAL NOT NULL, `amountRemaining` REAL NOT NULL, `notes` TEXT, PRIMARY KEY(`creditId`))");
        db.execSQL("CREATE TABLE IF NOT EXISTS `local_receipt_drafts` (`localReceiptId` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `localPaymentGuid` TEXT NOT NULL, `localReceiptNo` TEXT NOT NULL, `subscriberId` INTEGER NOT NULL, `collectorId` INTEGER NOT NULL, `paymentDate` TEXT NOT NULL, `totalReceived` REAL NOT NULL, `paymentMethod` TEXT NOT NULL, `notes` TEXT, `syncStatus` TEXT NOT NULL, `syncBatchRef` TEXT, `serverImportId` INTEGER, `serverStatus` TEXT, `rejectedReason` TEXT, `createdAt` TEXT NOT NULL, `updatedAt` TEXT, `sentAt` TEXT)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `local_receipt_draft_lines` (`localReceiptLineId` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `localReceiptId` INTEGER NOT NULL, `invoiceId` INTEGER, `appliedAmount` REAL NOT NULL, `applicationType` TEXT NOT NULL, `notes` TEXT)");
        db.execSQL("CREATE TABLE IF NOT EXISTS room_master_table (id INTEGER PRIMARY KEY,identity_hash TEXT)");
        db.execSQL("INSERT OR REPLACE INTO room_master_table (id,identity_hash) VALUES(42, '4722e836546f7a348b0ccd20720dc8ea')");
      }

      @Override
      public void dropAllTables(@NonNull final SupportSQLiteDatabase db) {
        db.execSQL("DROP TABLE IF EXISTS `local_subscribers`");
        db.execSQL("DROP TABLE IF EXISTS `local_subscriber_meters`");
        db.execSQL("DROP TABLE IF EXISTS `local_open_invoices`");
        db.execSQL("DROP TABLE IF EXISTS `local_subscriber_credits`");
        db.execSQL("DROP TABLE IF EXISTS `local_receipt_drafts`");
        db.execSQL("DROP TABLE IF EXISTS `local_receipt_draft_lines`");
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onDestructiveMigration(db);
          }
        }
      }

      @Override
      public void onCreate(@NonNull final SupportSQLiteDatabase db) {
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onCreate(db);
          }
        }
      }

      @Override
      public void onOpen(@NonNull final SupportSQLiteDatabase db) {
        mDatabase = db;
        internalInitInvalidationTracker(db);
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onOpen(db);
          }
        }
      }

      @Override
      public void onPreMigrate(@NonNull final SupportSQLiteDatabase db) {
        DBUtil.dropFtsSyncTriggers(db);
      }

      @Override
      public void onPostMigrate(@NonNull final SupportSQLiteDatabase db) {
      }

      @Override
      @NonNull
      public RoomOpenHelper.ValidationResult onValidateSchema(
          @NonNull final SupportSQLiteDatabase db) {
        final HashMap<String, TableInfo.Column> _columnsLocalSubscribers = new HashMap<String, TableInfo.Column>(15);
        _columnsLocalSubscribers.put("subscriberId", new TableInfo.Column("subscriberId", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsLocalSubscribers.put("subscriberName", new TableInfo.Column("subscriberName", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsLocalSubscribers.put("phoneNumber", new TableInfo.Column("phoneNumber", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsLocalSubscribers.put("address", new TableInfo.Column("address", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsLocalSubscribers.put("primaryMeterId", new TableInfo.Column("primaryMeterId", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsLocalSubscribers.put("primaryMeterNumber", new TableInfo.Column("primaryMeterNumber", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsLocalSubscribers.put("primaryMeterLocation", new TableInfo.Column("primaryMeterLocation", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsLocalSubscribers.put("currentDue", new TableInfo.Column("currentDue", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsLocalSubscribers.put("currentCredit", new TableInfo.Column("currentCredit", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsLocalSubscribers.put("currentBalance", new TableInfo.Column("currentBalance", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsLocalSubscribers.put("lastInvoiceId", new TableInfo.Column("lastInvoiceId", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsLocalSubscribers.put("lastInvoiceNumber", new TableInfo.Column("lastInvoiceNumber", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsLocalSubscribers.put("lastInvoiceDate", new TableInfo.Column("lastInvoiceDate", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsLocalSubscribers.put("lastInvoiceTotal", new TableInfo.Column("lastInvoiceTotal", "REAL", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsLocalSubscribers.put("lastInvoiceRemaining", new TableInfo.Column("lastInvoiceRemaining", "REAL", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysLocalSubscribers = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesLocalSubscribers = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoLocalSubscribers = new TableInfo("local_subscribers", _columnsLocalSubscribers, _foreignKeysLocalSubscribers, _indicesLocalSubscribers);
        final TableInfo _existingLocalSubscribers = TableInfo.read(db, "local_subscribers");
        if (!_infoLocalSubscribers.equals(_existingLocalSubscribers)) {
          return new RoomOpenHelper.ValidationResult(false, "local_subscribers(com.watercollector.app.data.local.entities.LocalSubscriberEntity).\n"
                  + " Expected:\n" + _infoLocalSubscribers + "\n"
                  + " Found:\n" + _existingLocalSubscribers);
        }
        final HashMap<String, TableInfo.Column> _columnsLocalSubscriberMeters = new HashMap<String, TableInfo.Column>(9);
        _columnsLocalSubscriberMeters.put("subscriberMeterId", new TableInfo.Column("subscriberMeterId", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsLocalSubscriberMeters.put("subscriberId", new TableInfo.Column("subscriberId", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsLocalSubscriberMeters.put("meterId", new TableInfo.Column("meterId", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsLocalSubscriberMeters.put("meterNumber", new TableInfo.Column("meterNumber", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsLocalSubscriberMeters.put("meterType", new TableInfo.Column("meterType", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsLocalSubscriberMeters.put("location", new TableInfo.Column("location", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsLocalSubscriberMeters.put("isActive", new TableInfo.Column("isActive", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsLocalSubscriberMeters.put("isPrimary", new TableInfo.Column("isPrimary", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsLocalSubscriberMeters.put("linkedAt", new TableInfo.Column("linkedAt", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysLocalSubscriberMeters = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesLocalSubscriberMeters = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoLocalSubscriberMeters = new TableInfo("local_subscriber_meters", _columnsLocalSubscriberMeters, _foreignKeysLocalSubscriberMeters, _indicesLocalSubscriberMeters);
        final TableInfo _existingLocalSubscriberMeters = TableInfo.read(db, "local_subscriber_meters");
        if (!_infoLocalSubscriberMeters.equals(_existingLocalSubscriberMeters)) {
          return new RoomOpenHelper.ValidationResult(false, "local_subscriber_meters(com.watercollector.app.data.local.entities.LocalSubscriberMeterEntity).\n"
                  + " Expected:\n" + _infoLocalSubscriberMeters + "\n"
                  + " Found:\n" + _existingLocalSubscriberMeters);
        }
        final HashMap<String, TableInfo.Column> _columnsLocalOpenInvoices = new HashMap<String, TableInfo.Column>(15);
        _columnsLocalOpenInvoices.put("invoiceId", new TableInfo.Column("invoiceId", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsLocalOpenInvoices.put("subscriberId", new TableInfo.Column("subscriberId", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsLocalOpenInvoices.put("meterId", new TableInfo.Column("meterId", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsLocalOpenInvoices.put("invoiceNumber", new TableInfo.Column("invoiceNumber", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsLocalOpenInvoices.put("invoiceDate", new TableInfo.Column("invoiceDate", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsLocalOpenInvoices.put("consumption", new TableInfo.Column("consumption", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsLocalOpenInvoices.put("unitPrice", new TableInfo.Column("unitPrice", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsLocalOpenInvoices.put("serviceFees", new TableInfo.Column("serviceFees", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsLocalOpenInvoices.put("arrears", new TableInfo.Column("arrears", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsLocalOpenInvoices.put("totalAmount", new TableInfo.Column("totalAmount", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsLocalOpenInvoices.put("grandTotal", new TableInfo.Column("grandTotal", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsLocalOpenInvoices.put("paidTotal", new TableInfo.Column("paidTotal", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsLocalOpenInvoices.put("remaining", new TableInfo.Column("remaining", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsLocalOpenInvoices.put("status", new TableInfo.Column("status", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsLocalOpenInvoices.put("notes", new TableInfo.Column("notes", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysLocalOpenInvoices = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesLocalOpenInvoices = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoLocalOpenInvoices = new TableInfo("local_open_invoices", _columnsLocalOpenInvoices, _foreignKeysLocalOpenInvoices, _indicesLocalOpenInvoices);
        final TableInfo _existingLocalOpenInvoices = TableInfo.read(db, "local_open_invoices");
        if (!_infoLocalOpenInvoices.equals(_existingLocalOpenInvoices)) {
          return new RoomOpenHelper.ValidationResult(false, "local_open_invoices(com.watercollector.app.data.local.entities.LocalOpenInvoiceEntity).\n"
                  + " Expected:\n" + _infoLocalOpenInvoices + "\n"
                  + " Found:\n" + _existingLocalOpenInvoices);
        }
        final HashMap<String, TableInfo.Column> _columnsLocalSubscriberCredits = new HashMap<String, TableInfo.Column>(9);
        _columnsLocalSubscriberCredits.put("creditId", new TableInfo.Column("creditId", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsLocalSubscriberCredits.put("subscriberId", new TableInfo.Column("subscriberId", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsLocalSubscriberCredits.put("paymentId", new TableInfo.Column("paymentId", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsLocalSubscriberCredits.put("receiptId", new TableInfo.Column("receiptId", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsLocalSubscriberCredits.put("meterId", new TableInfo.Column("meterId", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsLocalSubscriberCredits.put("creditDate", new TableInfo.Column("creditDate", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsLocalSubscriberCredits.put("amountTotal", new TableInfo.Column("amountTotal", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsLocalSubscriberCredits.put("amountRemaining", new TableInfo.Column("amountRemaining", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsLocalSubscriberCredits.put("notes", new TableInfo.Column("notes", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysLocalSubscriberCredits = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesLocalSubscriberCredits = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoLocalSubscriberCredits = new TableInfo("local_subscriber_credits", _columnsLocalSubscriberCredits, _foreignKeysLocalSubscriberCredits, _indicesLocalSubscriberCredits);
        final TableInfo _existingLocalSubscriberCredits = TableInfo.read(db, "local_subscriber_credits");
        if (!_infoLocalSubscriberCredits.equals(_existingLocalSubscriberCredits)) {
          return new RoomOpenHelper.ValidationResult(false, "local_subscriber_credits(com.watercollector.app.data.local.entities.LocalSubscriberCreditEntity).\n"
                  + " Expected:\n" + _infoLocalSubscriberCredits + "\n"
                  + " Found:\n" + _existingLocalSubscriberCredits);
        }
        final HashMap<String, TableInfo.Column> _columnsLocalReceiptDrafts = new HashMap<String, TableInfo.Column>(17);
        _columnsLocalReceiptDrafts.put("localReceiptId", new TableInfo.Column("localReceiptId", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsLocalReceiptDrafts.put("localPaymentGuid", new TableInfo.Column("localPaymentGuid", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsLocalReceiptDrafts.put("localReceiptNo", new TableInfo.Column("localReceiptNo", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsLocalReceiptDrafts.put("subscriberId", new TableInfo.Column("subscriberId", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsLocalReceiptDrafts.put("collectorId", new TableInfo.Column("collectorId", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsLocalReceiptDrafts.put("paymentDate", new TableInfo.Column("paymentDate", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsLocalReceiptDrafts.put("totalReceived", new TableInfo.Column("totalReceived", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsLocalReceiptDrafts.put("paymentMethod", new TableInfo.Column("paymentMethod", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsLocalReceiptDrafts.put("notes", new TableInfo.Column("notes", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsLocalReceiptDrafts.put("syncStatus", new TableInfo.Column("syncStatus", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsLocalReceiptDrafts.put("syncBatchRef", new TableInfo.Column("syncBatchRef", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsLocalReceiptDrafts.put("serverImportId", new TableInfo.Column("serverImportId", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsLocalReceiptDrafts.put("serverStatus", new TableInfo.Column("serverStatus", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsLocalReceiptDrafts.put("rejectedReason", new TableInfo.Column("rejectedReason", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsLocalReceiptDrafts.put("createdAt", new TableInfo.Column("createdAt", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsLocalReceiptDrafts.put("updatedAt", new TableInfo.Column("updatedAt", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsLocalReceiptDrafts.put("sentAt", new TableInfo.Column("sentAt", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysLocalReceiptDrafts = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesLocalReceiptDrafts = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoLocalReceiptDrafts = new TableInfo("local_receipt_drafts", _columnsLocalReceiptDrafts, _foreignKeysLocalReceiptDrafts, _indicesLocalReceiptDrafts);
        final TableInfo _existingLocalReceiptDrafts = TableInfo.read(db, "local_receipt_drafts");
        if (!_infoLocalReceiptDrafts.equals(_existingLocalReceiptDrafts)) {
          return new RoomOpenHelper.ValidationResult(false, "local_receipt_drafts(com.watercollector.app.data.local.entities.LocalReceiptDraftEntity).\n"
                  + " Expected:\n" + _infoLocalReceiptDrafts + "\n"
                  + " Found:\n" + _existingLocalReceiptDrafts);
        }
        final HashMap<String, TableInfo.Column> _columnsLocalReceiptDraftLines = new HashMap<String, TableInfo.Column>(6);
        _columnsLocalReceiptDraftLines.put("localReceiptLineId", new TableInfo.Column("localReceiptLineId", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsLocalReceiptDraftLines.put("localReceiptId", new TableInfo.Column("localReceiptId", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsLocalReceiptDraftLines.put("invoiceId", new TableInfo.Column("invoiceId", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsLocalReceiptDraftLines.put("appliedAmount", new TableInfo.Column("appliedAmount", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsLocalReceiptDraftLines.put("applicationType", new TableInfo.Column("applicationType", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsLocalReceiptDraftLines.put("notes", new TableInfo.Column("notes", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysLocalReceiptDraftLines = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesLocalReceiptDraftLines = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoLocalReceiptDraftLines = new TableInfo("local_receipt_draft_lines", _columnsLocalReceiptDraftLines, _foreignKeysLocalReceiptDraftLines, _indicesLocalReceiptDraftLines);
        final TableInfo _existingLocalReceiptDraftLines = TableInfo.read(db, "local_receipt_draft_lines");
        if (!_infoLocalReceiptDraftLines.equals(_existingLocalReceiptDraftLines)) {
          return new RoomOpenHelper.ValidationResult(false, "local_receipt_draft_lines(com.watercollector.app.data.local.entities.LocalReceiptDraftLineEntity).\n"
                  + " Expected:\n" + _infoLocalReceiptDraftLines + "\n"
                  + " Found:\n" + _existingLocalReceiptDraftLines);
        }
        return new RoomOpenHelper.ValidationResult(true, null);
      }
    }, "4722e836546f7a348b0ccd20720dc8ea", "cd1cd8b95840aa0c6cafc9454fba6014");
    final SupportSQLiteOpenHelper.Configuration _sqliteConfig = SupportSQLiteOpenHelper.Configuration.builder(config.context).name(config.name).callback(_openCallback).build();
    final SupportSQLiteOpenHelper _helper = config.sqliteOpenHelperFactory.create(_sqliteConfig);
    return _helper;
  }

  @Override
  @NonNull
  protected InvalidationTracker createInvalidationTracker() {
    final HashMap<String, String> _shadowTablesMap = new HashMap<String, String>(0);
    final HashMap<String, Set<String>> _viewTables = new HashMap<String, Set<String>>(0);
    return new InvalidationTracker(this, _shadowTablesMap, _viewTables, "local_subscribers","local_subscriber_meters","local_open_invoices","local_subscriber_credits","local_receipt_drafts","local_receipt_draft_lines");
  }

  @Override
  public void clearAllTables() {
    super.assertNotMainThread();
    final SupportSQLiteDatabase _db = super.getOpenHelper().getWritableDatabase();
    try {
      super.beginTransaction();
      _db.execSQL("DELETE FROM `local_subscribers`");
      _db.execSQL("DELETE FROM `local_subscriber_meters`");
      _db.execSQL("DELETE FROM `local_open_invoices`");
      _db.execSQL("DELETE FROM `local_subscriber_credits`");
      _db.execSQL("DELETE FROM `local_receipt_drafts`");
      _db.execSQL("DELETE FROM `local_receipt_draft_lines`");
      super.setTransactionSuccessful();
    } finally {
      super.endTransaction();
      _db.query("PRAGMA wal_checkpoint(FULL)").close();
      if (!_db.inTransaction()) {
        _db.execSQL("VACUUM");
      }
    }
  }

  @Override
  @NonNull
  protected Map<Class<?>, List<Class<?>>> getRequiredTypeConverters() {
    final HashMap<Class<?>, List<Class<?>>> _typeConvertersMap = new HashMap<Class<?>, List<Class<?>>>();
    _typeConvertersMap.put(SubscriberDao.class, SubscriberDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(InvoiceDao.class, InvoiceDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(MeterDao.class, MeterDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(CreditDao.class, CreditDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(ReceiptDraftDao.class, ReceiptDraftDao_Impl.getRequiredConverters());
    return _typeConvertersMap;
  }

  @Override
  @NonNull
  public Set<Class<? extends AutoMigrationSpec>> getRequiredAutoMigrationSpecs() {
    final HashSet<Class<? extends AutoMigrationSpec>> _autoMigrationSpecsSet = new HashSet<Class<? extends AutoMigrationSpec>>();
    return _autoMigrationSpecsSet;
  }

  @Override
  @NonNull
  public List<Migration> getAutoMigrations(
      @NonNull final Map<Class<? extends AutoMigrationSpec>, AutoMigrationSpec> autoMigrationSpecs) {
    final List<Migration> _autoMigrations = new ArrayList<Migration>();
    return _autoMigrations;
  }

  @Override
  public SubscriberDao subscriberDao() {
    if (_subscriberDao != null) {
      return _subscriberDao;
    } else {
      synchronized(this) {
        if(_subscriberDao == null) {
          _subscriberDao = new SubscriberDao_Impl(this);
        }
        return _subscriberDao;
      }
    }
  }

  @Override
  public InvoiceDao invoiceDao() {
    if (_invoiceDao != null) {
      return _invoiceDao;
    } else {
      synchronized(this) {
        if(_invoiceDao == null) {
          _invoiceDao = new InvoiceDao_Impl(this);
        }
        return _invoiceDao;
      }
    }
  }

  @Override
  public MeterDao meterDao() {
    if (_meterDao != null) {
      return _meterDao;
    } else {
      synchronized(this) {
        if(_meterDao == null) {
          _meterDao = new MeterDao_Impl(this);
        }
        return _meterDao;
      }
    }
  }

  @Override
  public CreditDao creditDao() {
    if (_creditDao != null) {
      return _creditDao;
    } else {
      synchronized(this) {
        if(_creditDao == null) {
          _creditDao = new CreditDao_Impl(this);
        }
        return _creditDao;
      }
    }
  }

  @Override
  public ReceiptDraftDao receiptDraftDao() {
    if (_receiptDraftDao != null) {
      return _receiptDraftDao;
    } else {
      synchronized(this) {
        if(_receiptDraftDao == null) {
          _receiptDraftDao = new ReceiptDraftDao_Impl(this);
        }
        return _receiptDraftDao;
      }
    }
  }
}
