package com.whatsmode.shopify.block.main.demo;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.innodroid.expandablerecycler.ExpandableRecyclerAdapter;
import com.whatsmode.shopify.R;
import com.whatsmode.shopify.WhatsApplication;

import java.util.ArrayList;
import java.util.List;

public class PeopleAdapter extends ExpandableRecyclerAdapter<PeopleAdapter.CategoryItem> {
    public static final int TYPE_PERSON = 1001;

    public PeopleAdapter(Context context) {
        super(context);

        setItems(getSampleItems());
    }

    public static class CategoryItem extends ExpandableRecyclerAdapter.ListItem {
        public String Text;

        public CategoryItem(String group) {
            super(TYPE_HEADER);

            Text = group;
        }

        public CategoryItem(String first, String last) {
            super(TYPE_PERSON);

            Text = first + " " + last;
        }
    }

    public class HeaderViewHolder extends ExpandableRecyclerAdapter.HeaderViewHolder {
        TextView name;

        public HeaderViewHolder(View view) {
            super(view, (ImageView) view.findViewById(R.id.item_arrow));

            name = (TextView) view.findViewById(R.id.item_header_name);
        }

        public void bind(int position) {
            super.bind(position);

            name.setText(visibleItems.get(position).Text);
        }
    }

    public class PersonViewHolder extends ExpandableRecyclerAdapter.ViewHolder {
        TextView name;

        public PersonViewHolder(View view) {
            super(view);

            name = (TextView) view.findViewById(R.id.item_name);
        }

        public void bind(int position) {
            name.setText(visibleItems.get(position).Text);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case TYPE_HEADER:
                return new HeaderViewHolder(inflate(R.layout.item_header, parent));
            case TYPE_PERSON:
            default:
                return new PersonViewHolder(inflate(R.layout.item_sub, parent));
        }
    }

    @Override
    public void onBindViewHolder(ExpandableRecyclerAdapter.ViewHolder holder, int position) {
        switch (getItemViewType(position)) {
            case TYPE_HEADER:
                ((HeaderViewHolder) holder).bind(position);
                break;
            case TYPE_PERSON:
            default:
                ((PersonViewHolder) holder).bind(position);
                break;
        }
    }

    private List<CategoryItem> getSampleItems() {
        List<CategoryItem> items = new ArrayList<>();
        String[] stringArray = WhatsApplication.getContext().getResources().getStringArray(R.array.category);
        String[] subCategoryFirst = WhatsApplication.getContext().getResources().getStringArray(R.array.subCategory_APPAREL);
        String[] subCategorySecond = WhatsApplication.getContext().getResources().getStringArray(R.array.subCategory_ACCESSORIES);
        String[] subCategoryThird = WhatsApplication.getContext().getResources().getStringArray(R.array.subCategory_BAGS);
        String[] subCategoryFourth = WhatsApplication.getContext().getResources().getStringArray(R.array.subCategory_SHOES);
        for (int i = 0; i < stringArray.length; i++) {
            items.add(new CategoryItem(stringArray[i]));
            String [] templateArray = null;
            switch (i) {
                case 0:
                    templateArray = subCategoryFirst;
                    break;
                case 1:
                    templateArray = subCategorySecond;
                    break;
                case 2:
                    templateArray = subCategoryThird;
                    break;
                case 3:
                    templateArray = subCategoryFourth;
                    break;
            }
            if (templateArray != null) {
                for (String s : templateArray) {
                    items.add(new CategoryItem(s,""));
                }
            }
        }
        return items;
    }
}
