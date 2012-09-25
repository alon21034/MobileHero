package greendroid.sql;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class FileManager {
	private SQLiteDatabase database;
	private FileSQLiteHelper dbHelper;
	private String[] columns = { 
			FileSQLiteHelper.COLUMN_FILE_NAME, 
			FileSQLiteHelper.COLUMN_IDENFIFIER,
			FileSQLiteHelper.COLUMN_SEQUENCE };

	public class FileInfo {
		private String identifier;
		private int sequence;
		public FileInfo(String identifier, int sequence) {
			this.setIdentifier(identifier);
			this.setSequence(sequence);
		}
		public String getIdentifier() {
			return identifier;
		}
		public void setIdentifier(String identifier) {
			this.identifier = identifier;
		}
		public int getSequence() {
			return sequence;
		}
		public void setSequence(int sequence) {
			this.sequence = sequence;
		}
	}
	
	public FileManager(Context context) {
		dbHelper = new FileSQLiteHelper(context);
	}

	public void open() throws SQLException {
		database = dbHelper.getWritableDatabase();
	}

	public void close() {
		dbHelper.close();
	}
	
	public long addFile(String fileName, String identifier, int sequence) {
		ContentValues values = new ContentValues();
		values.put(FileSQLiteHelper.COLUMN_FILE_NAME, fileName);
		values.put(FileSQLiteHelper.COLUMN_IDENFIFIER, identifier);
		values.put(FileSQLiteHelper.COLUMN_SEQUENCE, sequence);
				
		long insertId = -1; 
		insertId = database.insert(FileSQLiteHelper.TABLE_FILE, null, values);
		return insertId;
	}

	public void deleteFile(String fileName) {
		String[] parms = { fileName };
		database.delete(FileSQLiteHelper.TABLE_FILE, FileSQLiteHelper.COLUMN_FILE_NAME + "=?", parms);
	}
	
	public FileInfo getFileInfo(String fileName) {
		FileInfo fileinInfo = null;
		
		Cursor cursor = database.query(true, FileSQLiteHelper.TABLE_FILE, columns, 
				FileSQLiteHelper.COLUMN_FILE_NAME + "=?", 
				new String[] { fileName }, null, null, null, null);
		cursor.moveToFirst();
		if (!cursor.isAfterLast()) {
			String identifier = cursorToIdentifier(cursor);
			int sequence = cursorToSequence(cursor);
			fileinInfo = new FileInfo(identifier, sequence);
		}
		cursor.close();
		
		return fileinInfo;
	}
	
	public List<String> getAllFiles() {
		List<String> fileNames = new ArrayList<String>();
				
		Cursor cursor = database.query(FileSQLiteHelper.TABLE_FILE, columns, null, null, null, null, null);
		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			String filename = cursorToFileName(cursor);
			fileNames.add(filename);
			cursor.moveToNext();
		}
		cursor.close();
		return fileNames;
	}
		
	private String cursorToFileName(Cursor cursor) {
		return cursor.getString(0);
	}
	
	private String cursorToIdentifier(Cursor cursor) {
		return cursor.getString(1);
	}
	
	private int cursorToSequence(Cursor cursor) {
		return cursor.getInt(2);
	}
}