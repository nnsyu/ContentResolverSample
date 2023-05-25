package com.ewoo.contentresolversample;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.ContentResolver;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.util.Log;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "CalendarExample";
    private static final int PERMISSION_REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 달력 권한 체크
        if (checkSelfPermission(Manifest.permission.READ_CALENDAR)
                != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.READ_CALENDAR},
                    PERMISSION_REQUEST_CODE);
        } else {
            // 이벤트를 가져와보자
            getEvents();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == PERMISSION_REQUEST_CODE) {
            // 권한체크
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // 이벤트를 가져와보자
                getEvents();
            } else {
                // 권한거부
                Log.d(TAG, "Calendar permission denied");
            }
        }
    }

    /**
     * 달력 가져오는 함수
     */
    private void getCalendars() {
        String[] projection = new String[]{
                CalendarContract.Calendars.ACCOUNT_NAME,
                CalendarContract.Calendars.NAME,
        };

        String selection = "((" + CalendarContract.Events.ACCOUNT_TYPE + " = ?) AND ("
                + CalendarContract.Events.ACCOUNT_NAME + " = ?))";

        // 계정변경 필요!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
        String[] selectionArgs = new String[]{
                "com.google",
                "nnnsyu@gmail.com"
        };

        // 이걸 CalendarContract.Events.CONTENT_URI로 바꾸면 이벤트 단위로 가져옴
        Uri uri = CalendarContract.Calendars.CONTENT_URI;
        ContentResolver contentResolver = getContentResolver();
        Cursor cursor = contentResolver.query(uri, projection, selection, selectionArgs, null);


        if (cursor != null) {
            // 쿼리를 통해 원하는 정보 가져오기..?
            while (cursor.moveToNext()) {
                String account = cursor.getString(cursor.getColumnIndex(CalendarContract.Calendars.ACCOUNT_NAME));
                String name = cursor.getString(cursor.getColumnIndex(CalendarContract.Calendars.NAME));

                Log.d(TAG, "Account: " + account + ", Name: " + name);
            }
            cursor.close();
        }
    }

    /**
     * 이벤트 가져오기
     */
    private void getEvents() {
        String[] projection = new String[]{
                CalendarContract.Events.TITLE,
                CalendarContract.Events.DESCRIPTION,
                CalendarContract.Events.RDATE,
                CalendarContract.Events.RRULE,
        };

        String selection = "((" + CalendarContract.Events.ACCOUNT_TYPE + " = ?) AND ("
                + CalendarContract.Events.ACCOUNT_NAME + " = ?))";

        // 계정변경 필요!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
        String[] selectionArgs = new String[]{
                "com.google",
                "nnnsyu@gmail.com"
        };

        Uri uri = CalendarContract.Events.CONTENT_URI;
        ContentResolver contentResolver = getContentResolver();
        Cursor cursor = contentResolver.query(uri, projection, selection, selectionArgs, null);


        if (cursor != null) {
            while (cursor.moveToNext()) {
                String title = cursor.getString(cursor.getColumnIndex(CalendarContract.Events.TITLE));
                String desc = cursor.getString(cursor.getColumnIndex(CalendarContract.Events.DESCRIPTION));
                String rDate = cursor.getString(cursor.getColumnIndex(CalendarContract.Events.RDATE));
                String rRule = cursor.getString(cursor.getColumnIndex(CalendarContract.Events.RRULE));

                Log.d(TAG, "Title: " + title + ", Description: " + desc + ", RDate: " + rDate + ", RRule: " + rRule);
            }
            cursor.close();
        }
    }
}