package com.example.gianni.project_405;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class ProductScreen extends AppCompatActivity implements View.OnClickListener {

    private String LISTENER_PRODUCTTYPE_URL = Constants.SERVER_URL+"listener_producttype.php";

    private TextView productName;
    private TextView productType;
    private TextView stockLevel;
    private Button closeButton;
    private Button addStockButton;
    private Button subtractStockButton;

    private int typeId;

    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_product);
        productName = (TextView) findViewById(R.id.txt_ProductName);
        productType = (TextView) findViewById(R.id.txt_ProductType);
        stockLevel = (TextView) findViewById(R.id.txt_ProductStock);
        closeButton = (Button) findViewById(R.id.btn_close_product);
        closeButton.setOnClickListener(this);//makes programe listen to the click
        addStockButton = (Button) findViewById(R.id.btn_add_stock);
        addStockButton.setOnClickListener(this);//makes programe listen to the click
        subtractStockButton = (Button) findViewById(R.id.btn_stubtract_stock);
        subtractStockButton.setOnClickListener(this);//makes programe listen to the click

        Bundle extras = getIntent().getExtras();
        productName.setText(extras.getString("name"));
        productType.setText(extras.getString("type"));
        typeId = extras.getInt("typeId");
        int stockLvl = extras.getInt("stock");
        int minStock =  extras.getInt("reorderlevel");
        showStockLevel(stockLvl, minStock);
    }

    private void showStockLevel(int stockLvl, int minStock) {
        String txtStockLevel;
        if (stockLvl == 0) {
            txtStockLevel = "No stock.";
        } else {
            txtStockLevel = "In stock: "+stockLvl;
        }
        if (minStock != 0) {
            txtStockLevel += "  Min: "+minStock;
        }
        stockLevel.setText(txtStockLevel);
    }

    @Override
    public void onClick(View view) {
        if (view == closeButton) {
            this.finish();
        } else if (view == addStockButton) {
            new HttpAsyncChangeStockTask().execute(LISTENER_PRODUCTTYPE_URL, "addstock"); //de functie voor de http json comunicatie wordt opgestart
        } else if (view == subtractStockButton) {
            new HttpAsyncChangeStockTask().execute(LISTENER_PRODUCTTYPE_URL, "substock"); //de functie voor de http json comunicatie wordt opgestart
        }

    }

    final public class HttpAsyncChangeStockTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            return POST_changestock(params[0], params[1]);
        }
        @Override
        protected void onPostExecute(String result) {
            if (result != null) {
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    int error = jsonObject.getInt("error");
                    if (error == 0) {
                        int stock = jsonObject.getInt("stock");
                        int reorderlevel = jsonObject.getInt("reorderlevel");
                        showStockLevel(stock, reorderlevel);
                    } else {
                        String message = jsonObject.getString("message");
                        Toast.makeText(getBaseContext(), message, Toast.LENGTH_LONG).show();
                    }
                } catch (Exception e) {
                    Log.d("JSON decode", e.getLocalizedMessage());
                }
            }
        }
    }


    private String POST_changestock(String url, String action) {
        InputStream inputStream = null;
        String result = null;
        try {

            // 1. create HttpClient
            HttpClient httpclient = new DefaultHttpClient();

            // 2. make POST request to the given URL
            HttpPost httpPost = new HttpPost(url);

            // 3. configure post parameters
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair(action, ""+ typeId));
            httpPost.setEntity(new UrlEncodedFormEntity(params));

            // 4. Execute POST request to the given URL
            HttpResponse httpResponse = httpclient.execute(httpPost);
            Log.d("url", String.valueOf(httpPost.getURI()));

            // 5. receive response as inputStream
            inputStream = httpResponse.getEntity().getContent();

            // 6. convert inputstream to json and extract information
            if(inputStream != null) {
                result = Constants.convertInputStreamToString(inputStream);
            } else {
                Toast.makeText(getBaseContext(), "Did not work!", Toast.LENGTH_LONG).show();
                result = null;
            }

        } catch (Exception e) {
            Log.d("InputStream", e.getLocalizedMessage());
        }

        // 11. return result
        // Toast.makeText(getBaseContext(), result, Toast.LENGTH_LONG).show();

        return result;

    }

}
