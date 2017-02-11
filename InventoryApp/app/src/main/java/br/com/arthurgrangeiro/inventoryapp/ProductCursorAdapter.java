package br.com.arthurgrangeiro.inventoryapp;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;

import br.com.arthurgrangeiro.inventoryapp.data.ProductsContract.ProductEntry;

/**
 * Created by Arthur on 25/01/2017.
 */

public class ProductCursorAdapter extends CursorAdapter {

    public ProductCursorAdapter(Context context, Cursor c) {
        super(context, c, 0 /* flags */);
    }


    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.list_item, parent, false);
    }

    @Override
    public void bindView(View view, Context context, final Cursor cursor) {

        String sProductName = cursor.getString(cursor.getColumnIndexOrThrow(ProductEntry.COLUMN_PRODUCT_NAME));
        final String sQuantity = cursor.getString(cursor.getColumnIndexOrThrow(ProductEntry.COLUMN_PRODUCT_QUANTITY));
        double dbPrice = cursor.getDouble(cursor.getColumnIndexOrThrow(ProductEntry.COLUMN_PRODUCT_PRICE));
        DecimalFormat numberFormat = new DecimalFormat("#.00");
        String sPrice = numberFormat.format(dbPrice);

        ViewHolder holder = new ViewHolder(view);

        final int mId = cursor.getInt(cursor.getColumnIndexOrThrow(ProductEntry._ID));

        holder.productName.setText(sProductName);
        holder.quantity.setText(sQuantity);
        holder.price.setText(sPrice);

        holder.mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int afterSale = Integer.parseInt(sQuantity) - 1;
                if (afterSale > 0 || afterSale == 0) {
                    ContentValues values = new ContentValues();
                    values.put(ProductEntry.COLUMN_PRODUCT_QUANTITY, afterSale);

                    Uri currentURI = ContentUris.withAppendedId(ProductEntry.CONTENT_URI, mId);
                    v.getContext().getContentResolver().update(currentURI, values, null, null);
                } else {
                    Toast.makeText(v.getContext(), v.getResources().getString(R.string.bigger_than_zero), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private static class ViewHolder {
        TextView productName;
        TextView quantity;
        TextView price;
        Button mButton;

        public ViewHolder(View view){
            productName = (TextView) view.findViewById(R.id.product_name);
            quantity = (TextView) view.findViewById(R.id.quantity);
            price = (TextView) view.findViewById(R.id.text_price);
            mButton = (Button) view.findViewById(R.id.button_sale);
        }
    }
}
