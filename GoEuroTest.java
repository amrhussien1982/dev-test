/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package goeurotest;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author amr
 */
import java.io.File;
import java.io.FileWriter;
import org.json.CDL;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class GoEuroTest {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        String jsonString = querieLocation(args[0]);
        saveSVCFile(jsonString);
    }

    public static String querieLocation(String location) {
        String output = null;
        try {
            URL url = new URL("http://api.goeuro.com/api/v2/position/suggest/en/" + location);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", "application/json");
            BufferedReader br = new BufferedReader(new InputStreamReader(
                    (conn.getInputStream())));

            output = br.readLine();
            //	System.out.println(output);

            conn.disconnect();

        } catch (MalformedURLException ex) {
            Logger.getLogger(GoEuroTest.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(GoEuroTest.class.getName()).log(Level.SEVERE, null, ex);
        }
        output = "{infile :" + output + "}";
        return output;
    }

    public static void saveSVCFile(String jsonString) {
        JSONObject output;
        try {
            output = new JSONObject(jsonString);
            JSONArray docs = output.getJSONArray("infile");

                 
            try {
                
                BufferedWriter bw = new BufferedWriter(new FileWriter(new File("D:\\LocationInfo.csv")));
                bw.write("_id,name,type,latitude,longitude");
                bw.newLine();
                for (int i = 0; i < docs.length(); i++) {
                    bw.write(docs.getJSONObject(i).getInt("_id") + "," + docs.getJSONObject(i).getString("name") + "," + docs.getJSONObject(i).getString("type") + "," + docs.getJSONObject(i).getJSONObject("geo_position").getDouble("latitude") + "," + docs.getJSONObject(i).getJSONObject("geo_position").getDouble("longitude"));
                    bw.newLine();
                }

                bw.close();
            } catch (IOException ex) {
                Logger.getLogger(GoEuroTest.class.getName()).log(Level.SEVERE, null, ex);
            }
            //    System.out.println(csv);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}
