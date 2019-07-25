package com.example.bses;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

public class background extends AsyncTask <String, Void, String> {

    /*public interface AsyncResponse {
        void processFinish(String output);
    }*/

    AlertDialog dialog;
    Context context;
    Activity a;
    //AsyncResponse delegate = null;

    public background(Context context,Activity activity)
    {
        this.context = context;
        this.a = activity;
        //this.delegate = delegate;
    }

    @Override
    protected void onPreExecute() {
        //super.onPreExecute();
        dialog = new AlertDialog.Builder(context).create();
        dialog.setMessage("Login Status");

    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        //dialog.setMessage(s);
        //dialog.show();
        //delegate.processFinish(s);
        if( s.equals("Connected. Enter valid id or passsword")){
            Toast.makeText(a, "Enter valid id or passsword", Toast.LENGTH_SHORT).show();
        }
        else if( s.equals("Connected. Login successfull")){
            Toast.makeText(a, "Login Successfull", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(a,second.class);
            a.startActivity(intent);
        }
        else{
            Toast.makeText(a, s, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected String doInBackground(String... voids){
        String result = "";
        String user = voids[0];
        String pass = voids[1];

        String connstr;
        //connstr = "http://127.0.0.1/login.php";
        connstr = "http://192.168.43.212/login.php";

        try {
            URL url = new URL(connstr);
            HttpURLConnection http = (HttpURLConnection) url.openConnection();
            http.setRequestMethod("GET");
            http.setDoInput(true);
            http.setDoOutput(true);

            OutputStream ops = http.getOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(ops, "UTF-8"));
            String data = URLEncoder.encode("user", "UTF-8")+"="+URLEncoder.encode(user, "UTF-8")+"&&"+ URLEncoder.encode("pass", "UTF-8")+"="+URLEncoder.encode(pass, "UTF-8");
            writer.write(data);
            writer.flush();
            writer.close();
            ops.close();

            InputStream ips = http.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(ips, "ISO-8859-1"));
            String line = "";
            while((line = reader.readLine()) != null){
                result += line;
            }
            reader.close();
            ips.close();
            http.disconnect();
            return result;


        }catch(MalformedURLException e){
            result = e.getMessage();
            //result = "abcdedfdf";
        }catch (IOException e){
            result = e.getMessage();
            //result = "not abcdfse";
        }

        return result;
    }
}
