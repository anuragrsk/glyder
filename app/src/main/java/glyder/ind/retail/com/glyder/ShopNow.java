package glyder.ind.retail.com.glyder;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.vision.barcode.Barcode;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

public class ShopNow extends AppCompatActivity implements View.OnClickListener{
    private TextView userInfo,tax,total;
    private Button shopNow;
    private DataBaseHelper dbHelper;
    private static final int RC_BARCODE_CAPTURE = 9001;
    private ArrayList<Product> products= new ArrayList<Product>();
    private ArrayAdapter adapter = null;
    private ListView list=null;
    private ArrayList<Product> taxAndTotal;
    private NumberFormat numberFormat= NumberFormat.getCurrencyInstance(Locale.US);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_now);
        list = (ListView)findViewById(R.id.listview);
        userInfo=(TextView)findViewById(R.id.userInfo);
        tax=(TextView)findViewById(R.id.tax);
        total=(TextView)findViewById(R.id.total);
        shopNow = (Button)findViewById(R.id.shopnow);
        Intent intent = getIntent();
        String name=intent.getStringExtra("UserName");
        userInfo.setText("Welcome "+name);
        shopNow.setOnClickListener(this);
        dbHelper=new DataBaseHelper(this);
        adapter = new ProductListViewAdapter(getApplicationContext(),products);
        list.setAdapter(adapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            int pos=0;
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                this.pos=position;
                ImageView image = (ImageView)view.findViewById(R.id.delete);
                if(products!=null&& !products.isEmpty()&& products.size()>0)
                    products.remove(pos);
                setTaxAndTotal(products);
                adapter.notifyDataSetChanged();

            }
        });



    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.shopnow) {
            // launch barcode activity.
            Intent intent = new Intent(this, FullscreenActivity.class);
            intent.putExtra(FullscreenActivity.AutoFocus, true);
            intent.putExtra(FullscreenActivity.UseFlash, false);

            startActivityForResult(intent, RC_BARCODE_CAPTURE);
        }
        if (v.getId() == R.id.listview) {
            // launch barcode activity.
           setTaxAndTotal(products);
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RC_BARCODE_CAPTURE) {
            if (resultCode == CommonStatusCodes.SUCCESS) {
                if (data != null) {
                    Barcode barcode = data.getParcelableExtra(FullscreenActivity.BarcodeObject);
                    if (dbHelper.isProductExist(barcode.displayValue)) {
                        Product p = dbHelper.getProduct(barcode.displayValue);
                        products.add(p);
                        setTaxAndTotal(products);
                        adapter.notifyDataSetChanged();
                    } else {
                        Product p = new Product();
                        p.setBarcode(barcode.displayValue);
                        dbHelper.createProduct((p));
                        p = dbHelper.getProduct(barcode.displayValue);
                        products.add(p);
                        setTaxAndTotal(products);
                        adapter.notifyDataSetChanged();
                    }

//                    statusMessage.setText(R.string.barcode_success);
//                    barcodeValue.setText(barcode.displayValue);
//                    Log.d(TAG, "Barcode read: " + barcode.displayValue);
                } else {
//                    statusMessage.setText(R.string.barcode_failure);
//                    Log.d(TAG, "No barcode captured, intent data is null");
                }
            } else {
//                statusMessage.setText(String.format(getString(R.string.barcode_error),
//                        CommonStatusCodes.getStatusCodeString(resultCode)));
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    public void setTaxAndTotal(ArrayList<Product> taxAndTotal) {
        if(taxAndTotal!=null && !taxAndTotal.isEmpty()&& taxAndTotal.size()>0){
            tax.setVisibility(View.VISIBLE);
            total.setVisibility(View.VISIBLE);
            int totalAmount =0;
            for (int i=0;i<taxAndTotal.size();i++){
                totalAmount=totalAmount+Integer.parseInt(taxAndTotal.get(i).getPrice());
            }
            double taxAmt=(totalAmount*9.25)/100;

            tax.setText("Tax: "+numberFormat.format(taxAmt));
            total.setText("Total: "+numberFormat.format(totalAmount));
            tax.setVisibility(View.VISIBLE);
            total.setVisibility(View.VISIBLE);

        }else{
            tax.setVisibility(View.GONE);
            total.setVisibility(View.GONE);
        }
    }
}
