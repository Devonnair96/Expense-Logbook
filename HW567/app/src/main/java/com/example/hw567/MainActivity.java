package com.example.hw567;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

/*This is an application to track expenses with a content provider handling the database.
 *Also it plays music in the background. */
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Switch music = (Switch) findViewById(R.id.switch1);

        //sets a switch button to turn music on as a background service.
        music.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b)
                {
                    startServ();
                }
                else if(b == false)
                {
                    stopServ();
                }
            }
        });
    }
    public void onClick(View view)
    {
        Intent in;
        if(view.getId() == R.id.entryButton)
        {
            in = new Intent(this, EntryActivity.class);
            startActivity(in);
        }
        else if(view.getId() == R.id.deleteButton)
        {
            in = new Intent(this, DeleteActivity.class);
            startActivity(in);
        }
        else if(view.getId() == R.id.updateButton)
        {
            in = new Intent(this, UpdateActivity.class);
            startActivity(in);
        }
    }
    public void startServ()
    {
        startService(new Intent(this, SongService.class));
    }
    public void stopServ()
    {
        stopService(new Intent(this, SongService.class));
    }
}

