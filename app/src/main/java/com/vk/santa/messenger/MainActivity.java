package com.vk.santa.messenger;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private Button choose_file_btn;
    private TextView link_tv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.vk.santa.Messenger.R.layout.activity_main);

        choose_file_btn = (Button) findViewById(com.vk.santa.Messenger.R.id.choose_file_btn);
        link_tv = (TextView) findViewById(com.vk.santa.Messenger.R.id.fileLink);

        choose_file_btn.setOnClickListener(this);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(com.vk.santa.Messenger.R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == com.vk.santa.Messenger.R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        Log.i("MAIN"," screen clicked");
        switch (v.getId()){
            case com.vk.santa.Messenger.R.id.choose_file_btn:
                Log.i("MAIN","CHOOSE_FILE_BTN clicked");
                Intent intent = new Intent(this,FilePickerActivity.class);
                startActivityForResult(intent,1);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(data == null){return;}
        String filePath = data.getStringExtra("filePath");
        link_tv.setText(filePath);
    }
}
