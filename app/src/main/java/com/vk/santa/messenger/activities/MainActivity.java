package com.vk.santa.messenger.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.vk.santa.messenger.R;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private Button choose_file_btn;
    private Button vkAPI;
    private TextView file_path_tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        choose_file_btn = (Button) findViewById(R.id.choose_file_btn);
        vkAPI = (Button) findViewById(R.id.vk);
        file_path_tv = (TextView) findViewById(R.id.file_path_tv);

        choose_file_btn.setOnClickListener(this);
        vkAPI.setOnClickListener(this);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        Log.i("MAIN"," screen clicked");
        switch (v.getId()){
            case R.id.choose_file_btn:
                Log.i("MAIN","CHOOSE_FILE_BTN clicked");
                Intent intent = new Intent(this,FilePickerActivity.class);
                startActivityForResult(intent,1);
                break;
            case R.id.vk:
                Log.i("MAIN","VK_API_BTN clicked");
                Intent intent2 = new Intent(this, VKGetRequestActivity.class);
                startActivity(intent2);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(data == null){return;}
        String filePath = data.getStringExtra("filePath");
        file_path_tv.setText(filePath);
    }
}
