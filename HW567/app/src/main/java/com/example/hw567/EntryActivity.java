package com.example.hw567;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class EntryActivity extends AppCompatActivity implements  AdapterView.OnItemSelectedListener{

    String catClicked="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entry);

        String URL = "content://com.example.hw567DBtest.DBProvider";
        Uri entries = Uri.parse(URL);
        List<String> list = new ArrayList<>();
        Spinner spinner = (Spinner) findViewById(R.id.getCat);

        Cursor c = managedQuery(entries, null, null, null, "name");
        //pulls categories from database, parses them and sets them to the spinner
        if(c.getCount()==0)
        {
            Toast.makeText(EntryActivity.this,"No preloaded categories",Toast.LENGTH_LONG).show();
        }
        else
        {
            if (c.moveToFirst())
            {
                do{
                    list.add(c.getString(c.getColumnIndex( DBProvider.CATEGORY)) );
                }while(c.moveToNext());

                List<String> cats = sortList(list);
                ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, cats);
                dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner.setAdapter(dataAdapter);
                spinner.setOnItemSelectedListener(this);
            }
        }
    }
    //sort list removes duplicate categories.
    public List<String> sortList(List<String> l)
    {
        Set<String> set = new HashSet<>(l);
        l.clear();
        l.addAll(set);
        return l;
    }
    public void onClick(View view)
    {
        ContentValues values = new ContentValues();
        EditText getName = (EditText) findViewById(R.id.getName);
        EditText getAltCat = (EditText) findViewById(R.id.getAltCat);
        EditText getDate = (EditText) findViewById(R.id.getDate);
        EditText getAmt = (EditText) findViewById(R.id.getAmt);
        EditText getNotes = (EditText) findViewById(R.id.getNotes);

        if(getName.getText().toString().isEmpty())
        {
            Toast.makeText(EntryActivity.this,"Error: missing Name", Toast.LENGTH_LONG).show();
        }
        else if(getDate.getText().toString().isEmpty())
        {
            Toast.makeText(EntryActivity.this,"Error: missing Date", Toast.LENGTH_LONG).show();
        }
        else if(getAmt.getText().toString().isEmpty())
        {
            Toast.makeText(EntryActivity.this,"Error: missing Amount", Toast.LENGTH_LONG).show();
        }
        else if(catClicked.isEmpty() && getAltCat.getText().toString().isEmpty())
        {
            Toast.makeText(EntryActivity.this,"Error: missing Category", Toast.LENGTH_LONG).show();
        }
        else{

            values.put(DBProvider.NAME, (getName.getText().toString()));
            values.put(DBProvider.DATE,(getDate.getText().toString()));
            values.put(DBProvider.AMOUNT,(getAmt.getText().toString()));
            if(!getNotes.getText().toString().isEmpty())
            {
                values.put(DBProvider.NOTES,getNotes.getText().toString());
            }
            else
            {
                values.put(DBProvider.NOTES,"");
            }
            if(!getAltCat.getText().toString().isEmpty())
            {
                values.put(DBProvider.CATEGORY,getAltCat.getText().toString());
            }
            else if(!catClicked.isEmpty())
            {
                values.put(DBProvider.CATEGORY,catClicked); //if spinner is not empty and alt cat is empty
            }
            Uri uri = getContentResolver().insert(DBProvider.CONTENT_URI, values);
            Toast.makeText(getBaseContext(), uri.toString(), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        catClicked = (String) adapterView.getItemAtPosition(i);
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
