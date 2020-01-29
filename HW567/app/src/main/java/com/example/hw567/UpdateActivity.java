package com.example.hw567;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.UserDictionary;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class UpdateActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);

        String a = "Enter the name of the entry to update," +
                "then enter the param to edit\n(name,category,date,amount,notes)\nmust be lower case and " +
                "spelled correctly. then enter the updated param:\n" + getDB();
        TextView textView = (TextView) findViewById(R.id.textView);
        textView.setText(a);

    }

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
    public void onClick(View view)
    {
        if(view.getId() == R.id.update)
        {
            EditText editText = (EditText) findViewById(R.id.entryText);
            EditText pickParam = (EditText) findViewById(R.id.pickParam);
            EditText editParam = (EditText) findViewById(R.id.editParam);
            if(editText.getText().toString().isEmpty())
            {
                Toast.makeText(UpdateActivity.this,"Enter the name to update", Toast.LENGTH_LONG).show();
            }
            else if(pickParam.getText().toString().isEmpty())
            {
                Toast.makeText(UpdateActivity.this,"Enter the param to update", Toast.LENGTH_LONG).show();
            }
            else if(editParam.getText().toString().isEmpty())
            {
                Toast.makeText(UpdateActivity.this,"Enter the content to update with", Toast.LENGTH_LONG).show();
            }
            else
            {
                ContentValues updateValues = new ContentValues();

                String selectionClause = DBProvider.NAME +  " LIKE ?";
                String[] selectionArgs = {editText.getText().toString()};
                int rowsupdated = 0;

                if(pickParam.getText().toString().equals(DBProvider.NAME))
                {
                    Toast.makeText(UpdateActivity.this,"You chose name", Toast.LENGTH_LONG).show();
                    updateValues.put(DBProvider.NAME,editParam.getText().toString());
                    rowsupdated = getContentResolver().update(DBProvider.CONTENT_URI, updateValues,selectionClause,selectionArgs);

                }
                else if(pickParam.getText().toString().equals(DBProvider.CATEGORY))
                {
                    Toast.makeText(UpdateActivity.this,"You chose category", Toast.LENGTH_LONG).show();
                    updateValues.put(DBProvider.CATEGORY,editParam.getText().toString());
                    rowsupdated = getContentResolver().update(DBProvider.CONTENT_URI, updateValues,selectionClause,selectionArgs);

                }
                else if(pickParam.getText().toString().equals(DBProvider.DATE))
                {
                    Toast.makeText(UpdateActivity.this,"You chose date", Toast.LENGTH_LONG).show();
                    updateValues.put(DBProvider.DATE,editParam.getText().toString());
                    rowsupdated = getContentResolver().update(DBProvider.CONTENT_URI, updateValues,selectionClause,selectionArgs);

                }
                else if(pickParam.getText().toString().equals(DBProvider.AMOUNT))
                {
                    Toast.makeText(UpdateActivity.this,"You chose amount", Toast.LENGTH_LONG).show();
                    updateValues.put(DBProvider.AMOUNT,editParam.getText().toString());
                    rowsupdated = getContentResolver().update(DBProvider.CONTENT_URI, updateValues,selectionClause,selectionArgs);

                }
                else if(pickParam.getText().toString().equals(DBProvider.NOTES))
                {
                    Toast.makeText(UpdateActivity.this,"You chose notes", Toast.LENGTH_LONG).show();
                    updateValues.put(DBProvider.NOTES,editParam.getText().toString());
                    rowsupdated = getContentResolver().update(DBProvider.CONTENT_URI, updateValues,selectionClause,selectionArgs);

                }

                String a = "Enter the name of the entry to update," +
                        "then enter the param to edit\n(name,category,date,amount,notes)\nmust be lower case and " +
                        "spelled correctly. then enter the updated param:\n" + getDB();
                TextView textView = (TextView) findViewById(R.id.textView);
                textView.setText(a);
            }

        }
    }
}
