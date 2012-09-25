package greendroid.sql;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class FileSQLiteHelper extends SQLiteOpenHelper {
	public static final String TABLE_FILE = "FILE";
	public static final String COLUMN_FILE_NAME = "FileName";
	public static final String COLUMN_IDENFIFIER = "Identifier";
	public static final String COLUMN_SEQUENCE = "Sequence";
	
	private static final String DATABASE_NAME = "FILE.db";
	private static final int DATABASE_VERSION = 1;

	public FileSQLiteHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		String DATABASE_CREATE_TABLE = "create table " + TABLE_FILE + "(" + 
				COLUMN_FILE_NAME	+ " TEXT" + ", " +
				COLUMN_IDENFIFIER	+ " TEXT" + ", " +
				COLUMN_SEQUENCE		+ " INTEGER" + ");";
		db.execSQL(DATABASE_CREATE_TABLE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS" +  TABLE_FILE);
		onCreate(db);
	}
}