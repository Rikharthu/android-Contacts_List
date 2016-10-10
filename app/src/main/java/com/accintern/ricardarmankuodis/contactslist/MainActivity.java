package com.accintern.ricardarmankuodis.contactslist;

import android.Manifest;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.provider.ContactsContract.Contacts;
import android.provider.ContactsContract.Data;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

// TODO - make listview and refactor
public class MainActivity extends AppCompatActivity {

    private final Uri DATA_URI = Data.CONTENT_URI;
    private static final int PERMISSIONS_REQUEST_READ_CONTACTS = 100;
    private TextView mTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTextView= (TextView) findViewById(R.id.text);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.READ_CONTACTS}, PERMISSIONS_REQUEST_READ_CONTACTS);
            //After this point you wait for callback in onRequestPermissionsResult(int, String[], int[]) overriden method
        } else {
            // Android version is lesser than 6.0 or the permission is already granted.
            String[] columns = {
                    Data._ID, // primary key
                    Contacts.DISPLAY_NAME, // person's name
                    Data.DATA1, // phone number
                    Data.DATA2 // phone type (mobile, home, work, etc.)
            };
            String where =
                    "(" + Data.MIMETYPE + "='"+ Phone.CONTENT_ITEM_TYPE + "' AND "
                            + Contacts.STARRED + "='1' )";
            String orderBy = Contacts.TIMES_CONTACTED + " DESC";
            //                Cursor cursor = getContentResolver().query(
//                        DATA_URI, columns, where, null, orderBy);
            // all columns
//            Cursor cursor = getContentResolver().query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);
            Cursor cursor = getContentResolver().query(DATA_URI, null, null, null, null);

            while(cursor.moveToNext()){
                mTextView.append(cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME))+"\n");
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == PERMISSIONS_REQUEST_READ_CONTACTS) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission is granted
                String[] columns = {
                        Data._ID, // primary key
                        Contacts.DISPLAY_NAME, // person's name
                        Data.DATA1, // phone number
                        Data.DATA2 // phone type (mobile, home, work, etc.)
                };
                String where =
                        "(" + Data.MIMETYPE + "='"+ Phone.CONTENT_ITEM_TYPE + "' AND "
                                + Contacts.STARRED + "='1' )";
                String orderBy = Contacts.TIMES_CONTACTED + " DESC";
//                Cursor cursor = getContentResolver().query(
//                        DATA_URI, columns, where, null, orderBy);
                // all columns
               Cursor cursor = getContentResolver().query(DATA_URI, null, null, null, null);

                while(cursor.moveToNext()){
                    mTextView.append(cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME))+"\n");
                }
            } else {
                Toast.makeText(this, "Until you grant the permission, we canot display the names", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
