package com.prj.ShoppingList;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;

import com.prj.ShoppingList.data.ProductItem;

import java.util.UUID;

import io.realm.Realm;

public class AddItem extends AppCompatActivity {

    public static final String KEY_TODO = "KEY_TODO";
    private EditText itemName;
    private Spinner itemCategory;
    private EditText itemPrice;
    private EditText itemDescription;

    private CheckBox purchased;
    private ProductItem productItem = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_item);

        //toolbar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        itemName = (EditText) findViewById(R.id.item_name);
        itemCategory = (Spinner) findViewById(R.id.item_category);
        itemPrice = (EditText) findViewById(R.id.item_price);
        itemDescription = (EditText) findViewById(R.id.item_description);

        //category list adjusting
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.category_item_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        itemCategory.setAdapter(adapter);

        purchased = (CheckBox) findViewById(R.id.purchased);

        Button btnSave = (Button) findViewById(R.id.btnSave);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveItem();
            }
        });

//        if (productItem != null) {
//            itemName.setText(productItem.getItemName());
//            purchased.setChecked(productItem.isPurchased());
//        }
    }

    public Realm getRealm() {
        return ((MainApplication) getApplication()).getRealmTodo();
    }

    private void saveItem() {
        if ("".equals(itemName.getText().toString())) {
            itemName.setError("can not be empty");
        } else if ("".equals(itemPrice.getText().toString())) {
            itemPrice.setError("can not be empty");
        } else if ("".equals(itemDescription.getText().toString())) {
            itemDescription.setError("can not be empty");
        } else {

            getRealm().beginTransaction();
            ProductItem productItem = getRealm().createObject(ProductItem.class, UUID.randomUUID().toString());
            productItem.setItemName(itemName.getText().toString());
            productItem.setItemCategory(itemCategory.getSelectedItem().toString());
            double price = Double.parseDouble(itemPrice.getText().toString());
            productItem.setItemPrice(price);
            productItem.setItemDescription(itemDescription.getText().toString());
            productItem.setPurchased(purchased.isChecked());
            getRealm().commitTransaction();

            Intent intentResult = new Intent(AddItem.this, MainActivity.class);

            intentResult.putExtra("productItem", productItem);
            startActivity(intentResult);
            finish();
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

}
