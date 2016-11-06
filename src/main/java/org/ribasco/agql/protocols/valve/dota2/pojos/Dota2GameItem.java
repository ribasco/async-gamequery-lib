package org.ribasco.agql.protocols.valve.dota2.pojos;

import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

//TODO: Use type adapters for this class to convert the interger values to boolean types
public class Dota2GameItem {
    private int id;
    private String name;
    private int cost;
    @SerializedName("secret_shop")
    private int secretShop;
    @SerializedName("side_shop")
    private int sideShop;
    private int recipe;

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

    public int getCost() {
        return cost;
    }

    public void setCost(int cost) {
        this.cost = cost;
    }

    public int getSecretShop() {
        return secretShop;
    }

    public void setSecretShop(int secretShop) {
        this.secretShop = secretShop;
    }

    public int getRecipe() {
        return recipe;
    }

    public void setRecipe(int recipe) {
        this.recipe = recipe;
    }

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.NO_CLASS_NAME_STYLE);
    }
}
