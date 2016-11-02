package org.ribasco.asyncgamequerylib.protocols.valve.steam.webapi.pojos;

import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.List;

public class SteamEconSchemaItemSet {
    @SerializedName("item_set")
    private String itemSet;
    private String name;
    private List<String> items;
    private List<SteamEconSchemaItemAttribute> attributes;
    @SerializedName("store_bundle ")
    private String storeBundle;

    public String getItemSet() {
        return itemSet;
    }

    public void setItemSet(String itemSet) {
        this.itemSet = itemSet;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getItems() {
        return items;
    }

    public void setItems(List<String> items) {
        this.items = items;
    }

    public List<SteamEconSchemaItemAttribute> getAttributes() {
        return attributes;
    }

    public void setAttributes(List<SteamEconSchemaItemAttribute> attributes) {
        this.attributes = attributes;
    }

    public String getStoreBundle() {
        return storeBundle;
    }

    public void setStoreBundle(String storeBundle) {
        this.storeBundle = storeBundle;
    }

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.NO_CLASS_NAME_STYLE);
    }
}
