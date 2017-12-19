package com.whatsmode.shopify.ui.helper;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.innodroid.expandablerecycler.ExpandableRecyclerAdapter;
import com.whatsmode.shopify.AppNavigator;
import com.whatsmode.shopify.R;
import com.whatsmode.shopify.WhatsApplication;
import com.whatsmode.shopify.actionlog.ActionLog;
import com.whatsmode.shopify.block.WebActivity;
import com.whatsmode.shopify.common.Constant;

import java.util.ArrayList;
import java.util.List;

public class CategoryAdapter extends ExpandableRecyclerAdapter<CategoryAdapter.CategoryItem> {
    private static final int TYPE_PERSON = 1001;
    private static final int TYPE_HEAD_LINK= 1002;


    public CategoryAdapter(Context context) {
        super(context);

        setItems(getSampleItems());
    }

    static class CategoryItem extends ExpandableRecyclerAdapter.ListItem {
        String Text;
        String tag;
        int index;

        CategoryItem(String group) {
            super(TYPE_HEADER);

            Text = group;
        }

        CategoryItem(int index,String first, String tag) {
            super(TYPE_PERSON);
            this.index = index;
            Text = first;
            this.tag = tag;
        }
        CategoryItem() {
            super(TYPE_HEAD_LINK);
        }
    }

    private class HeaderViewHolder extends ExpandableRecyclerAdapter.HeaderViewHolder {
        TextView name;
        LinearLayout menuLayout;
        HeaderViewHolder(View view) {
            super(view, (ImageView) view.findViewById(R.id.item_arrow));
            menuLayout = (LinearLayout) view.findViewById(R.id.menu_bar);
            name = (TextView) view.findViewById(R.id.item_header_name);
        }


        public void bind(int position) {
            super.bind(position);
            menuLayout.setVisibility(position == 0 ? View.VISIBLE:View.GONE);
            menuLayout.findViewById(R.id.new_arrive).setOnClickListener(v ->
                     AppNavigator.jumpToWebActivity(mContext, WhatsApplication.getContext()
                    .getResources().getStringArray(R.array.pop_icon_name)[0], WhatsApplication
                    .getContext().getResources().getStringArray(R.array.pop_icon_link)[0]));
            menuLayout.findViewById(R.id.discover).setOnClickListener(v ->
                    AppNavigator.jumpToWebActivity(mContext, WhatsApplication.getContext()
                    .getResources().getStringArray(R.array.pop_icon_name)[1], WhatsApplication
                    .getContext().getResources().getStringArray(R.array.pop_icon_link)[1]));
            menuLayout.findViewById(R.id.sale).setOnClickListener(v ->
                    AppNavigator.jumpToWebActivity(mContext, WhatsApplication.getContext()
                    .getResources().getStringArray(R.array.pop_icon_name)[2], WhatsApplication
                    .getContext().getResources().getStringArray(R.array.pop_icon_link)[2]));
            name.setText(visibleItems.get(position).Text);
        }
    }

    private class HeadLinkHolder extends ExpandableRecyclerAdapter.ViewHolder {
        ImageView icon;
        HeadLinkHolder(View view) {
            super(view);
            icon = (ImageView) view.findViewById(R.id.new_arrive);
        }


        public void bind(int position) {
            switch (position) {
                case 0:
                    icon.setBackgroundResource(R.drawable.left_bg_new);
                    break;
                case 1:
                    icon.setBackgroundResource(R.drawable.left_bg_discover);
                    break;
                case 2:
                    icon.setBackgroundResource(R.drawable.left_bg_sale);
                    break;
            }
            icon.setOnClickListener(v ->
                            AppNavigator.jumpToWebActivity(mContext,WhatsApplication.getContext()
                            .getResources().getStringArray(R.array.pop_icon_name)[position],WhatsApplication
                            .getContext().getResources().getStringArray(R.array.pop_icon_link)[position]));
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
                CategoryItem categoryItem = visibleItems.get(position);
                            AppNavigator.jumpToWebActivity(mContext, WebActivity.STATE_COLLECTIONS,
                            new StringBuilder(Constant.WEB_PREFIX)
                                    .append(categoryItem.tag).toString());

                switch (categoryItem.index){
                    case 0:
                        ActionLog.onEvent(Constant.Event.APPAREL);
                        break;
                    case 1:
                        ActionLog.onEvent(Constant.Event.ACCESSORIES);
                        break;
                    case 2:
                        ActionLog.onEvent(Constant.Event.BAGS);
                        break;
                    case 3:
                        ActionLog.onEvent(Constant.Event.SHOES);
                        break;
                }

            });
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case TYPE_HEADER:
                return new HeaderViewHolder(inflate(R.layout.item_header, parent));
            case TYPE_HEAD_LINK:
                return new HeadLinkHolder(inflate(R.layout.menu_header_item, parent));
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
            case TYPE_HEAD_LINK:
                ((HeadLinkHolder)holder).bind(position);
            case TYPE_PERSON:
            default:
                ((CategoryViewHolder) holder).bind(position);
                break;
        }
    }

    private List<CategoryItem> getSampleItems() {
        List<CategoryItem> items = new ArrayList<>();
        items.add(new CategoryItem());
        items.add(new CategoryItem());
        items.add(new CategoryItem());
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
                    items.add(new CategoryItem(i,templateArray[k],templateArrayLinks[k]));
                }
            }
        }
        return items;
    }
}
