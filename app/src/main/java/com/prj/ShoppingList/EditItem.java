package com.prj.ShoppingList;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;

import com.prj.ShoppingList.data.ProductItem;
import io.realm.Realm;

public class EditItem extends AppCompatActivity {
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
        setContentView(R.layout.edit_item);

        //toolbar

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        if (getIntent().hasExtra(MainActivity.KEY_TODO_ID)) {
            String itemID = getIntent().getStringExtra(MainActivity.KEY_TODO_ID);
            productItem = getRealm().where(ProductItem.class)
                    .equalTo("itemID", itemID)
                    .findFirst();
        }

        itemName = (EditText) findViewById(R.id.item_name);
        itemCategory = (Spinner) findViewById(R.id.item_category);
        itemPrice = (EditText) findViewById(R.id.item_price);
        itemDescription = (EditText) findViewById(R.id.item_description);


        // making the keyboard soft
        softKeyboard(itemDescription);
        softKeyboard(itemName);
        softKeyboard(itemPrice);

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
                saveTodo();
            }
        });

        if (productItem != null) {
            itemName.setText(productItem.getItemName());
            //itemCategory.set
            itemPrice.setText(String.valueOf(productItem.getItemPrice()));
            itemDescription.setText(productItem.getItemDescription());
            purchased.setChecked(productItem.isPurchased());
        }
    }

    public Realm getRealm() {
        return ((MainApplication) getApplication()).getRealmTodo();
    }

    private void saveTodo() {
        if ("".equals(itemName.getText().toString())) {
            itemName.setError("can not be empty");
        } else if ("".equals(itemPrice.getText().toString())) {
            itemPrice.setError("can not be empty");
        } else if ("".equals(itemDescription.getText().toString())) {
            itemDescription.setError("can not be empty");
        } else {
            Intent intentResult = new Intent();

            getRealm().beginTransaction();
            productItem.setItemName(itemName.getText().toString());
            productItem.setItemCategory(itemCategory.getSelectedItem().toString());
            double price = Double.parseDouble(itemPrice.getText().toString());
            productItem.setItemPrice(price);
            productItem.setItemDescription(itemDescription.getText().toString());
            productItem.setPurchased(purchased.isChecked());
            getRealm().commitTransaction();

            intentResult.putExtra(KEY_TODO, productItem.getItemID());
            setResult(RESULT_OK, intentResult);
            finish();
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private void softKeyboard(EditText editText) {
        editText.setInputType(InputType.TYPE_CLASS_TEXT);
        editText.requestFocus();
        InputMethodManager mgr = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        mgr.showSoftInput(editText, InputMethodManager.SHOW_FORCED);
    }
}
