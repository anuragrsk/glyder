package glyder.ind.retail.com.glyder;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

/**
 * Created by anuraag on 7/4/16.
 */

public class ProductListViewAdapter extends ArrayAdapter<Product> {
    ArrayList<Product> products=null;
    private NumberFormat numberFormat= NumberFormat.getCurrencyInstance(Locale.US);
    public ProductListViewAdapter(Context context, ArrayList<Product> users) {
        super(context, 0, users);
        this.products=users;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        Product user = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.glyde_list_view, parent, false);
        }
        // Lookup view for data population
        TextView tvName = (TextView) convertView.findViewById(R.id.product);
        TextView tvHome = (TextView) convertView.findViewById(R.id.price);

        // Populate the data into the template view using the data object
        tvName.setText(user.getName());
        tvHome.setText(numberFormat.format(Integer.parseInt(user.getPrice())));
        // Return the completed view to render on screen
        return convertView;
    }

}
