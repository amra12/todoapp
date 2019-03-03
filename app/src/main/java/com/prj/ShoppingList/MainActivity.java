package com.prj.ShoppingList;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Toast;

import com.prj.ShoppingList.adapter.ItemsAdapter;
import com.prj.ShoppingList.data.ProductItem;
import com.prj.ShoppingList.touch.TodoItemTouchHelperCallback;

public class MainActivity extends AppCompatActivity {

    public static final String KEY_TODO_ID = "KEY_TODO_ID";
    public static final int REQUEST_CODE_EDIT = 101;

    private ItemsAdapter itemsAdapter;
    private RecyclerView recyclerItems;

    private int positionToEdit = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Animation myAnimation = AnimationUtils.loadAnimation(this, R.anim.list_animation);

        View myView = findViewById(R.id.include);

        myView.startAnimation(myAnimation);

        ((MainApplication) getApplication()).openRealm();

        setupUI();
    }

    private void setupUI() {
        setUpToolBar();
        setUpAddItemUI();
        setupRecyclerView();
    }

    private void setupRecyclerView() {
        recyclerItems = (RecyclerView) findViewById(R.id.recyclerTodo);
        recyclerItems.setHasFixedSize(true);

        final LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerItems.setLayoutManager(layoutManager);

        itemsAdapter = new ItemsAdapter(this, ((MainApplication) getApplication()).getRealmTodo());
        recyclerItems.setAdapter(itemsAdapter);

        // adding touch support
        ItemTouchHelper.Callback callback = new TodoItemTouchHelperCallback(itemsAdapter);
        ItemTouchHelper touchHelper = new ItemTouchHelper(callback);
        touchHelper.attachToRecyclerView(recyclerItems);

    }

    private void setUpAddItemUI() {
    }

    private void setUpToolBar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    public void openEditActivity(int index, String ItemID) {
        positionToEdit = index;

        Intent startEdit = new Intent(this, EditItem.class);

        startEdit.putExtra(KEY_TODO_ID, ItemID);

        startActivityForResult(startEdit, REQUEST_CODE_EDIT);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (resultCode) {
            case RESULT_OK:
                if (requestCode == REQUEST_CODE_EDIT) {
                    String itemID = data.getStringExtra(
                            EditItem.KEY_TODO);

                    itemsAdapter.updateItem(itemID, positionToEdit);
                }
                break;
            case RESULT_CANCELED:
                Toast.makeText(MainActivity.this, "Cancelled", Toast.LENGTH_SHORT).show();
                break;
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        ((MainApplication) getApplication()).closeRealm();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_add) {

            Intent intent = new Intent(this, AddItem.class);

            startActivity(intent);


            return true;

        }
        if (id == R.id.action_deleteAll) {

            deleteAll();

            return true;

        }
        return super.onOptionsItemSelected(item);

    }


    @Override
    protected void onResume() {
        super.onResume();
        if (getIntent().getSerializableExtra("barcaItem") != null) {
            ProductItem newItem = (ProductItem) getIntent().getSerializableExtra("barcaItem");
            itemsAdapter.addItem(newItem);
            recyclerItems.scrollToPosition(0);
        }
    }

    public void deleteItem(int index, String itemID) {
        itemsAdapter.onItemDismiss(index);

    }

    public void deleteAll() {
        itemsAdapter.clearAllItems();
    }

}
