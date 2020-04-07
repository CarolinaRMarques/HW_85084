package com.tqs_assignment.airquality.connection;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class HttpClient {

    public String get(URL url) throws Throwable {
        String inline = "";
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.connect();
        int responsecode = conn.getResponseCode();
        if (responsecode != 200) {
            error(responsecode);
        } else {
            Scanner sc = new Scanner(url.openStream());
            while (sc.hasNext()) {
                inline += sc.nextLine();
            }
            sc.close();
        }
        return inline;
    }

    public void error(int bar) throws Throwable {  // Noncompliant
        throw new RuntimeException("HttpResponseCode: " + bar);     // Noncompliant
    }
}
