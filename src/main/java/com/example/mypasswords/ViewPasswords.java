package com.example.mypasswords;
/*Developed by Sheriff © */

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import static java.lang.String.format;

public class ViewPasswords extends Activity {
    private static long back_pressed;
    private SQLiteDatabase sql;
    private ListView passList;
    private Cursor passCursor;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_passwords);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        DatabasePasswords databasePasswords = new DatabasePasswords(this);
        databasePasswords.updateDataBase();
        sql = databasePasswords.getWritableDatabase();

        passList = findViewById(R.id.listpasswords);

        passList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView infoStringDown = view.findViewById(R.id.infoDOWN);
                TextView infoStringUp = view.findViewById(R.id.infoUP);
                String infoUp = infoStringUp.getText().toString();
                copyTextToClipboard(infoStringDown);
                Toast.makeText(getBaseContext(), "Пароль скопирован от\n" + infoUp, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void copyTextToClipboard(TextView txtView) {
        ClipboardManager clipboard = (android.content.ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = android.content.ClipData.newPlainText("text label", txtView.getText().toString());
        clipboard.setPrimaryClip(clip);
    }

    @Override
    public void onBackPressed() {
        if (back_pressed + 1000 > System.currentTimeMillis()) {
            super.onBackPressed();
        } else {
            Toast.makeText(getBaseContext(), R.string.quit,
                    Toast.LENGTH_SHORT).show();
            back_pressed = System.currentTimeMillis();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        passCursor = sql.rawQuery(format("select * from %s order by %s",
                DatabasePasswords.TABLE_ACCESS, DatabasePasswords.COLUMN_ID), null);
        String[] headers = new String[]{DatabasePasswords.COLUMN_SITE,
                DatabasePasswords.COLUMN_LOGIN, DatabasePasswords.COLUMN_PASSWORD};
        SimpleCursorAdapter scAdapter = new SimpleCursorAdapter(this, R.layout.list_item_passwords,
                passCursor, headers, new int[]{R.id.infoUP, R.id.infoMIDDLE, R.id.infoDOWN}, 0);
        scAdapter.notifyDataSetChanged();
        passList.setAdapter(scAdapter);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        sql.close();
        passCursor.close();
        finish();
    }
}