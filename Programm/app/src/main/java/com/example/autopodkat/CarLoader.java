package com.example.autopodkat;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.LinkedList;
import java.util.List;

/*public class CarLoader extends AsyncTask<Void, Void, Void>
{

    @Override
    protected Void doInBackground(Void... voids)
    {
        String connString = "http://192.168.0.102:80/getCars.php";
        try
        {

            URL url = new URL(connString);
            HttpURLConnection urlConn = (HttpURLConnection) url.openConnection();
            urlConn.setDoInput(true);
            urlConn.setDoOutput(true);

            InputStream stream = urlConn.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(stream, "ISO-8859-1"));
            List<Car> carList = new LinkedList<Car>();
            String line = "";
            Car car;
            Log.e("func","1");
            while ((line = reader.readLine()) != null)
            {
                Log.e("res", line);
            }
            Log.e("result","nu blyat rabotai suka");
            reader.close();
            urlConn.disconnect();
            stream.close();
        }
        catch (MalformedURLException e)
        {
            Log.e("123",e.getMessage());
        } catch (IOException e) {
            Log.e("123",e.getMessage());
        }
        return null;
    }
}
 */
public class CarLoader {

}
