package suncere.androidapp.basemodule;

import java.util.concurrent.atomic.AtomicInteger;

import suncere.androidapp.basemodule.BaseDAL.DatabaseHelper;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

class DatabaseManager {
	
	interface IInitializer
	{
		SQLiteOpenHelper GetHelper();
	}
	
	 private AtomicInteger mOpenCounter = new AtomicInteger();
	 
	   private static DatabaseManager instance;
	   private static SQLiteOpenHelper mDatabaseHelper;
	   private static IInitializer _initializer;
	   private SQLiteDatabase mDatabase;
	 
	    static synchronized void SetInitializer(IInitializer initializer)
	   {
		   _initializer=initializer;
	   }
	   
//	   public static synchronized void initializeInstance(SQLiteOpenHelper helper) {
//	       if (instance == null) {
//	           instance = new DatabaseManager();
//	           mDatabaseHelper = helper;
//	       }
//	   }
	   
	   
	 
	   public static synchronized DatabaseManager getInstance() {
		   if(instance==null)
		   {
			   instance=new DatabaseManager();
			   if(_initializer!=null)
			   {
				   mDatabaseHelper=_initializer.GetHelper();
			   }
		   }
	       if (instance == null) {
//	           throw new IllegalStateException(DatabaseManager.class.getSimpleName() +
//	                   " is not initialized, call initializeInstance(..) method first.");
	    	   throw new IllegalStateException("Initial Faile,  Check Has  IInitializer Is Null Or Throw Exception While Initial");
	       }
	 
	       return instance;
	   }
	 
	   public synchronized SQLiteDatabase openDatabase() {
	       if(mOpenCounter.incrementAndGet() == 1) {
	           // Opening new database
	           mDatabase = mDatabaseHelper.getWritableDatabase();
	       }
	       return mDatabase;
	   }
	 
	   public synchronized void closeDatabase() {
	       if(mOpenCounter.decrementAndGet() == 0) {
	           // Closing database
	           mDatabase.close();
	 
	       }
	   }
}
