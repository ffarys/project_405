package com.example.gianni.project_405;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class ProductScreen extends AppCompatActivity implements View.OnClickListener {

    private TextView productName;
    private TextView productType;
    private TextView stockLevel;
    private Button closeButton;

    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_product);
        productName = (TextView) findViewById(R.id.txt_ProductName);
        productType = (TextView) findViewById(R.id.txt_ProductType);
        stockLevel = (TextView) findViewById(R.id.txt_ProductStock);
        closeButton = (Button) findViewById(R.id.btn_close_product);
        closeButton.setOnClickListener(this);//makes programe listen to the click

        Bundle extras = getIntent().getExtras();
        productName.setText(extras.getString("name"));
        productType.setText(extras.getString("type"));
        String txtStockLevel = extras.getString("stock");
        if ("0".equals(txtStockLevel)) {
            txtStockLevel = "Alles is op.";
        } else {
            txtStockLevel = "In stock: "+txtStockLevel;
        }
        String txtMinStock =  extras.getString("reorderlevel");
        if (! "0".equals(txtMinStock)) {
            txtStockLevel += "  Min: "+txtMinStock;
        }
        stockLevel.setText(txtStockLevel);
    }

    @Override
    public void onClick(View view) {
        this.finish();
    }

}
