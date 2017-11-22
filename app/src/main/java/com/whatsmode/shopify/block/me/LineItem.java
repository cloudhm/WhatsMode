package com.whatsmode.shopify.block.me;

import java.math.BigDecimal;

/**
 * Created by tom on 17-11-22.
 */

public class LineItem {

    private Variant variant;
    private Integer quantity;
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

    public static class CustomAttributes{
        private String key;
        private String value;

        public CustomAttributes() {
        }

        public CustomAttributes(String key, String value) {
            this.key = key;
            this.value = value;
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

    public static class Variant{
        private boolean availableForSale;
        private String title;
        private String sku;
        private BigDecimal price;
        private Image image;

        public Variant() {
        }

        public Variant(boolean availableForSale, String title, String sku, BigDecimal price, Image image) {
            this.availableForSale = availableForSale;
            this.title = title;
            this.sku = sku;
            this.price = price;
            this.image = image;
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

        public Image getImage() {
            return image;
        }

        public void setImage(Image image) {
            this.image = image;
        }

        public static class Image{
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
    }
}
