package com.vk.santa.messenger.activities;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.TextView;

import com.vk.santa.messenger.R;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutionException;

public class VKGetRequestActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vkget_request);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        APIParcer p = new APIParcer();
        Handler handler = new Handler(p);

        TextView tv = (TextView) findViewById(R.id.json_tv);

        String response = handler.getStr("https://api.vk.com/method/likes.isLiked?user_id=--111096931&type=video$item_id=456239561");
        tv.setText(response);

    }



    class Handler{

        APIParcer p = new APIParcer();
        String string;
        public Handler( APIParcer p) {
            this.p=p;
        }

        public String getStr(String url) {
            if (hasConnection(VKGetRequestActivity.this)) {
                p.execute(url);

                try {
                    string = p.get();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
            }
            return string;
        }

            private boolean hasConnection (Context context){
                Runtime runtime = Runtime.getRuntime();
                try {

                    Process ipProcess = runtime.exec("/system/bin/ping -c 1 8.8.8.8");
                    int exitValue = ipProcess.waitFor();
                    return (exitValue == 0);

                } catch (IOException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                return false;

            }
            }



    public class APIParcer extends AsyncTask<String, Void, String> {


        @Override
        protected String doInBackground(String... urls) {
            HttpURLConnection urlConnection = null;
            BufferedReader br = null;
            String string = "";

            try {
                java.net.URL url = new URL(urls[0]);

                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setDoInput(true);
                urlConnection.setDoOutput(false);
                urlConnection.setRequestProperty("Content-Type", "application/json");
                urlConnection.setRequestProperty("Accept", "application/json");
                InputStream inputStream = urlConnection.getInputStream();
                int statusCode = urlConnection.getResponseCode();

                if (statusCode == 200) {
                    inputStream = new BufferedInputStream(urlConnection.getInputStream());
                    string = convertInputStreamToString(inputStream);
                 }
            } catch (IOException e) {
                Log.e(this.getClass().getName(), e.toString());
            } finally {

                try {
                    if (br != null) br.close();
                    if (urlConnection != null) urlConnection.disconnect();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return string;

        }

        private String convertInputStreamToString(InputStream inputStream) throws IOException {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            String line = "";
            String result = "";
            while ((line = bufferedReader.readLine()) != null) {
                result += line;
            }

            /* Close Stream */
            if (null != inputStream) {
                inputStream.close();
            }
            return result;
        }
    }


}
