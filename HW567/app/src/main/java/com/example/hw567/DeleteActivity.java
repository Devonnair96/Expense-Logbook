package com.example.hw567;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class DeleteActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete);

        //sets the text view in the xml file
        String a = getDB();
        TextView textView = (TextView) findViewById(R.id.textView);
        textView.setText(a);

    }

    //Function to show entries from database on the textview
    public String getDB()
    {
        String URL = "content://com.example.hw567DBtest.DBProvider";
        List<String> list = new ArrayList<>();
        Uri entries = Uri.parse(URL);

        Cursor c = managedQuery(entries, null, null, null, "_id");
        if (c.moveToFirst()) {
            do {
                if (!c.getString(c.getColumnIndex(DBProvider.NOTES)).isEmpty()) {
                    list.add(c.getString(c.getColumnIndex(DBProvider.NAME)) + ", "
                            + c.getString(c.getColumnIndex(DBProvider.CATEGORY)) + ", "
                            + c.getString(c.getColumnIndex(DBProvider.DATE)) + ", "
                            + c.getString(c.getColumnIndex(DBProvider.AMOUNT)) + ", "
                            + c.getString(c.getColumnIndex(DBProvider.NOTES)));
                } else {

                    list.add(c.getString(c.getColumnIndex(DBProvider.NAME)) + ", "
                            + c.getString(c.getColumnIndex(DBProvider.CATEGORY)) + ", "
                            + c.getString(c.getColumnIndex(DBProvider.DATE)) + ", "
                            + c.getString(c.getColumnIndex(DBProvider.AMOUNT)));
                }


            } while (c.moveToNext());

            String a = "";
            for (int i = 0; i < list.size(); i++) {
                a += i + ". " + list.get(i) + "\n";
            }
            return a;
        }
        return "something went wrong";

    }
    public void onClicked(View view)
    {
        if(view.getId() == R.id.deleteButton)
        {
            EditText editText = (EditText) findViewById(R.id.delText);
            if(editText.getText().toString().isEmpty())
            {
                Toast.makeText(DeleteActivity.this,"Enter the name of the item to delete", Toast.LENGTH_LONG).show();
            }
            String selectionClause = DBProvider.NAME + " LIKE ?";
            String[] selectionArgs = {editText.getText().toString()};
            int rowsDeleted = 0;
            rowsDeleted = getContentResolver().delete(DBProvider.CONTENT_URI, selectionClause, selectionArgs);

            String a = getDB();
            TextView textView = (TextView) findViewById(R.id.textView);
            textView.setText(a);
        }
    }
}

