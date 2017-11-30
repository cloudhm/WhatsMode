package com.whatsmode.shopify.block.me;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * Created by tom on 17-11-22.
 */

public class LineItem implements Serializable{

    private Variant variant;
    private int quantity;
    private String title;
    private CustomAttributes customAttributes;

    public LineItem() {
    }

    public LineItem(Variant variant, Integer quantity, String title, CustomAttributes customAttributes) {
        this.variant = variant;
        this.quantity = quantity;
        this.title = title;
        this.customAttributes = customAttributes;
    }

    protected LineItem(Parcel in) {
        title = in.readString();
    }

    public Variant getVariant() {
        return variant;
    }

    public void setVariant(Variant variant) {
        this.variant = variant;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public CustomAttributes getCustomAttributes() {
        return customAttributes;
    }

    public void setCustomAttributes(CustomAttributes customAttributes) {
        this.customAttributes = customAttributes;
    }

    public static class CustomAttributes implements Serializable{
        private String key;
        private String value;

        public CustomAttributes() {
        }

        public CustomAttributes(String key, String value) {
            this.key = key;
            this.value = value;
        }

        protected CustomAttributes(Parcel in) {
            key = in.readString();
            value = in.readString();
        }

        public String getKey() {
            return key;
        }

        public void setKey(String key) {
            this.key = key;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

    }

    public static class Variant implements Serializable{
        private boolean availableForSale;
        private String title;
        private String sku;
        private BigDecimal price;
        private BigDecimal compareAtPrice;
        private Image image;
        private List<SelectedOptions> selectedOptions;

        public List<SelectedOptions> getSelectedOptions() {
            return selectedOptions;
        }

        public void setSelectedOptions(List<SelectedOptions> selectedOptions) {
            this.selectedOptions = selectedOptions;
        }

        public Variant() {
        }

        public Variant(boolean availableForSale, String title, String sku, BigDecimal price,BigDecimal compareAtPrice, Image image, List<SelectedOptions> selectedOptions) {
            this.availableForSale = availableForSale;
            this.title = title;
            this.sku = sku;
            this.price = price;
            this.compareAtPrice = compareAtPrice;
            this.image = image;
            this.selectedOptions = selectedOptions;
        }

        public boolean isAvailableForSale() {
            return availableForSale;
        }

        public void setAvailableForSale(boolean availableForSale) {
            this.availableForSale = availableForSale;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getSku() {
            return sku;
        }

        public void setSku(String sku) {
            this.sku = sku;
        }

        public BigDecimal getPrice() {
            return price;
        }

        public void setPrice(BigDecimal price) {
            this.price = price;
        }

        public BigDecimal getCompareAtPrice() {
            return compareAtPrice;
        }

        public void setCompareAtPrice(BigDecimal compareAtPrice) {
            this.compareAtPrice = compareAtPrice;
        }

        public Image getImage() {
            return image;
        }

        public void setImage(Image image) {
            this.image = image;
        }

        public static class Image implements Serializable{
            private String src;

            public Image() {
            }

            public Image(String src) {
                this.src = src;
            }

            public String getSrc() {
                return src;
            }

            public void setSrc(String src) {
                this.src = src;
            }
        }

        public static class SelectedOptions implements Serializable{
            private String name;
            private String value;

            public SelectedOptions(String name, String value) {
                this.name = name;
                this.value = value;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getValue() {
                return value;
            }

            public void setValue(String value) {
                this.value = value;
            }
        }
    }
}
