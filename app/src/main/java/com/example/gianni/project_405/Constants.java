package com.example.gianni.project_405;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class Constants {

    public static final String SERVER_URL = "https://farys.000webhostapp.com/";

    public static String convertInputStreamToString(InputStream inputStream) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        String line = "";
        String result = "";
        while ((line = bufferedReader.readLine()) != null){
            result += line;
        }
        inputStream.close();
        return result;

    }


}
