package org.ribasco.agql.protocols.valve.steam.webapi.pojos;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class StoreSaleDetails {
    private int status;
    private int id;
    private String name;
    @SerializedName("available")
    private String availableDate;
    @SerializedName("items")
    private List<StoreSaleApp> appsOnSale;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAvailableDate() {
        return availableDate;
    }

    public void setAvailableDate(String availableDate) {
        this.availableDate = availableDate;
    }

    public List<StoreSaleApp> getAppsOnSale() {
        return appsOnSale;
    }

    public void setAppsOnSale(List<StoreSaleApp> appsOnSale) {
        this.appsOnSale = appsOnSale;
    }
}
