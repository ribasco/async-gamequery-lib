package org.ribasco.asyncgamequerylib.protocols.valve.steam.webapi.pojos;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class StoreAppPackageGroup {
    private String name;
    private String title;
    private String description;
    @SerializedName("selection_text")
    private String selectionText;
    @SerializedName("save_text")
    private String saveText;
    @SerializedName("display_type")
    private int displayType;
    @SerializedName("is_recurring_subscription")
    private String recurringSubscription;
    @SerializedName("subs")
    private List<StoreAppSubPackage> subPackages;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSelectionText() {
        return selectionText;
    }

    public void setSelectionText(String selectionText) {
        this.selectionText = selectionText;
    }

    public String getSaveText() {
        return saveText;
    }

    public void setSaveText(String saveText) {
        this.saveText = saveText;
    }

    public int getDisplayType() {
        return displayType;
    }

    public void setDisplayType(int displayType) {
        this.displayType = displayType;
    }

    public String getRecurringSubscription() {
        return recurringSubscription;
    }

    public void setRecurringSubscription(String recurringSubscription) {
        this.recurringSubscription = recurringSubscription;
    }

    public List<StoreAppSubPackage> getSubPackages() {
        return subPackages;
    }

    public void setSubPackages(List<StoreAppSubPackage> subPackages) {
        this.subPackages = subPackages;
    }
}
