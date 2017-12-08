package com.whatsmode.shopify.ui.helper;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.innodroid.expandablerecycler.ExpandableRecyclerAdapter;
import com.whatsmode.shopify.AppNavigator;
import com.whatsmode.shopify.R;
import com.whatsmode.shopify.WhatsApplication;
import com.whatsmode.shopify.block.WebActivity;
import com.whatsmode.shopify.common.Constant;

import java.util.ArrayList;
import java.util.List;

public class CategoryAdapter extends ExpandableRecyclerAdapter<CategoryAdapter.CategoryItem> {
    private static final int TYPE_PERSON = 1001;
    

    public CategoryAdapter(Context context) {
        super(context);

        setItems(getSampleItems());
    }

    static class CategoryItem extends ExpandableRecyclerAdapter.ListItem {
        String Text;
        String tag;

        CategoryItem(String group) {
            super(TYPE_HEADER);

            Text = group;
        }

        CategoryItem(String first, String tag) {
            super(TYPE_PERSON);

            Text = first;
            this.tag = tag;
        }
    }

    private class HeaderViewHolder extends ExpandableRecyclerAdapter.HeaderViewHolder {
        TextView name;
        HeaderViewHolder(View view) {
            super(view, (ImageView) view.findViewById(R.id.item_arrow));

            name = (TextView) view.findViewById(R.id.item_header_name);
        }


        public void bind(int position) {
            super.bind(position);

            name.setText(visibleItems.get(position).Text);
        }
    }

    private class CategoryViewHolder extends ExpandableRecyclerAdapter.ViewHolder {
        TextView name;
        View view;

        CategoryViewHolder(View view) {
            super(view);

            name = (TextView) view.findViewById(R.id.item_name);
            this.view = view;
        }

        void bind(int position) {
            name.setText(visibleItems.get(position).Text);
            view.setOnClickListener(v ->{
                            AppNavigator.jumpToWebActivity(mContext, WebActivity.STATE_COLLECTIONS,
                            new StringBuilder(Constant.WEB_PREFIX)
                                    .append(visibleItems.get(position).tag).toString());
            });
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case TYPE_HEADER:
                return new HeaderViewHolder(inflate(R.layout.item_header, parent));
            case TYPE_PERSON:
            default:
                return new CategoryViewHolder(inflate(R.layout.item_sub, parent));
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
                ((CategoryViewHolder) holder).bind(position);
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

        String[] subCategoryFirstLinks = WhatsApplication.getContext().getResources().getStringArray(R.array.subCategory_APPAREL_LINK);
        String[] subCategorySecondLinks = WhatsApplication.getContext().getResources().getStringArray(R.array.subCategory_ACCESSORIES_LINK);
        String[] subCategoryThirdLinks = WhatsApplication.getContext().getResources().getStringArray(R.array.subCategory_BAGS_LINK);
        String[] subCategoryFourthLinks = WhatsApplication.getContext().getResources().getStringArray(R.array.subCategory_SHOES_LINK);
        for (int i = 0; i < stringArray.length; i++) {
            items.add(new CategoryItem(stringArray[i]));
            String [] templateArray = null;
            String[] templateArrayLinks = null;
            switch (i) {
                case 0:
                    templateArray = subCategoryFirst;
                    templateArrayLinks = subCategoryFirstLinks;
                    break;
                case 1:
                    templateArray = subCategorySecond;
                    templateArrayLinks = subCategorySecondLinks;
                    break;
                case 2:
                    templateArray = subCategoryThird;
                    templateArrayLinks = subCategoryThirdLinks;
                    break;
                case 3:
                    templateArray = subCategoryFourth;
                    templateArrayLinks = subCategoryFourthLinks;
                    break;
            }
            if (templateArray != null) {
                for (int k = 0; k < templateArray.length; k++) {
                    items.add(new CategoryItem(templateArray[k],templateArrayLinks[k]));
                }
            }
        }
        return items;
    }
}
