package bjnech.testjson;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.lang.String;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // URL String
        String urlString = "http://api.openweathermap.org/data/2.5/weather?q=khulna,bd";
        // Create a GetJson object
        GetJson gj = new GetJson();
        // Call execute function
        gj.execute(urlString);
        try {
            // Call get function to get the JSON string
            String jsonString = gj.get();
            // Print the JSON string (Log cannot print the full JSON string if the string is too large)
            Log.v("json", jsonString);
        } catch (Exception e){
            e.printStackTrace();
        }
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

    // Http helper class
    private class HttpHandler {

        // Constructor
        public HttpHandler(){}

        // getJsonWithUrl get JSON String from urlString
        public String getJsonWithUrl(String urlString) {

            // Result JSON String
            String jsonString = null;

            try {
                // Initialization of URL object
                URL url = new URL(urlString);
                // URL opens new connection and casts the result to HttpURLConnection
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                // Check connection respond code
                if (connection.getResponseCode() == 200){
                    // Create a BufferedInputStream for incoming data
                    InputStream in = new BufferedInputStream(connection.getInputStream());
                    // Read InputStream
                    BufferedReader br = new BufferedReader(new InputStreamReader(in));
                    // Create a StringBuilder
                    StringBuilder stringbu = new StringBuilder();
                    // Each character
                    int c;
                    // Read each character form BufferedReader to StringBuilder
                    while (true){
                        c = br.read();
                        // if br.read() reaches the end, then break
                        if (c == -1){
                            break;
                        }
                        // Append every character in StringBuilder
                        stringbu.append((char)c);
                    }
                    // Put StringBuilder into jsonString
                    jsonString = stringbu.toString();
                    // End the URL connection
                    connection.disconnect();
                    // Log will not print full JSON string if the string is too large
                    //Log.v("json", jsonString.substring(jsonString.length()/2+12, jsonString.length()));
                }
                else {
                    Log.v("json", "The response code is " + connection.getResponseCode());
                }
            } catch (Exception e){
                e.printStackTrace();
            }
            // Return the JSON String
            return jsonString;
        }

    }

    // Get JSON class which calls the Http helper class
    private class GetJson extends AsyncTask<String, Void, String> {
        // String... means undetermined number of parameters
        protected String doInBackground(String... urlStrings){
            String urlString = urlStrings[0];
            // Create a HttpHandler object
            HttpHandler httpHandler = new HttpHandler();
            // Call getJsonWithUrl function
            String jsonString = httpHandler.getJsonWithUrl(urlString);
            return jsonString;
        }
    }
}
