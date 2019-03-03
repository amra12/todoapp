package com.prj.ShoppingList.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.prj.ShoppingList.MainActivity;
import com.prj.ShoppingList.R;
import com.prj.ShoppingList.data.ProductItem;
import com.prj.ShoppingList.touch.TodoTouchHelperAdapter;
import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.Sort;


public class ItemsAdapter
        extends RecyclerView.Adapter<ItemsAdapter.ViewHolder>
        implements TodoTouchHelperAdapter {

    private List<ProductItem> ItemsList;

    private Context context;

    private Realm realmItem;


    public ItemsAdapter(Context context, Realm realmItem) {
        this.context = context;
        this.realmItem = realmItem;

        RealmResults<ProductItem> ItemResult = realmItem.where(ProductItem.class).findAll().sort("itemName", Sort.ASCENDING);

        ItemsList = new ArrayList<ProductItem>();

        for (int i = 0; i < ItemResult.size(); i++) {
            ItemsList.add(ItemResult.get(i));

        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rowView = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.show_item, parent, false);

        return new ViewHolder(rowView);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.itemName.setText(ItemsList.get(position).getItemName());
        holder.purchased.setChecked(ItemsList.get(position).isPurchased());

        String price = String.valueOf(ItemsList.get(position).getItemPrice());
        holder.priceLabel.setText(price);

        String category = ItemsList.get(position).getItemCategory();
        holder.categoryLabel.setText(category);

        switch (category) {
            case "Vegetables":
                holder.imageView.setImageDrawable(context.getDrawable(R.drawable.vegi));
                break;

            case "Fruit":
                holder.imageView.setImageDrawable(context.getDrawable(R.drawable.fruit));
                break;
            case "Dairy Products":
                holder.imageView.setImageDrawable(context.getDrawable(R.drawable.dairy));
                break;
            case "Women Clothes":
                holder.imageView.setImageDrawable(context.getDrawable(R.drawable.women));
                break;
            case "Men Clothes":
                holder.imageView.setImageDrawable(context.getDrawable(R.drawable.men));
                break;

            default:
                break;
        }
//        Drawable myDrawable = getResources().getDrawable(R.drawable.imageView1);
//holder.imageView.setImageDrawable();

        holder.purchased.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                realmItem.beginTransaction();
                ItemsList.get(holder.getAdapterPosition()).setPurchased(holder.purchased.isChecked());
                realmItem.commitTransaction();
            }
        });

//        holder.itemView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                ((MainActivity) context).openEditActivity(holder.getAdapterPosition(),
//                        ItemsList.get(holder.getAdapterPosition()).getItemID()
//                );
//            }
//        });

        holder.editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity) context).openEditActivity(holder.getAdapterPosition(),
                        ItemsList.get(holder.getAdapterPosition()).getItemID()
                );
            }
        });

    }

    @Override
    public int getItemCount() {
        return ItemsList.size();
    }


    public void addItem(ProductItem productItem) {

        ItemsList.add(0, productItem);

        notifyItemInserted(0);
    }

    public void updateItem(String itemID, int positionToEdit) {
        ProductItem productItem = realmItem.where(ProductItem.class)
                .equalTo("itemID", itemID)
                .findFirst();

        ItemsList.set(positionToEdit, productItem);

        notifyItemChanged(positionToEdit);
    }


    @Override
    public void onItemDismiss(int position) {
        realmItem.beginTransaction();
        ItemsList.get(position).deleteFromRealm();
        realmItem.commitTransaction();


        ItemsList.remove(position);

        // refreshes the whole list
        //notifyDataSetChanged();
        // refreshes just the relevant part that has been deleted
        notifyItemRemoved(position);
    }

    @Override
    public void onItemMove(int fromPosition, int toPosition) {
        /*ItemsList.add(toPosition, ItemsList.get(fromPosition));
        ItemsList.remove(fromPosition);*/

        if (fromPosition < toPosition) {
            for (int i = fromPosition; i < toPosition; i++) {
                Collections.swap(ItemsList, i, i + 1);
            }
        } else {
            for (int i = fromPosition; i > toPosition; i--) {
                Collections.swap(ItemsList, i, i - 1);
            }
        }


        notifyItemMoved(fromPosition, toPosition);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private CheckBox purchased;
        private TextView itemName;
        private ImageView imageView;
        private EditText categoryLabel;
        private EditText priceLabel;
        private Button editButton;

        public ViewHolder(View itemView) {
            super(itemView);

            purchased = (CheckBox) itemView.findViewById(R.id.purchased);
            itemName = (TextView) itemView.findViewById(R.id.item_name);
            imageView = (ImageView) itemView.findViewById(R.id.imageView);
            categoryLabel = (EditText) itemView.findViewById(R.id.category_label);
            priceLabel = (EditText) itemView.findViewById(R.id.price_label);
            editButton = (Button) itemView.findViewById(R.id.edit_button);

        }
    }
    public void clearAllItems(){
        realmItem.beginTransaction();
        realmItem.deleteAll();
        ItemsList.clear();
        notifyDataSetChanged();
        realmItem.commitTransaction();
    }

}
