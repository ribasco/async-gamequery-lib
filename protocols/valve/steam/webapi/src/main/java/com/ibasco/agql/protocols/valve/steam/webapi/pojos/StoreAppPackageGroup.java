/*
 * Copyright (c) 2022 Asynchronous Game Query Library
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.ibasco.agql.protocols.valve.steam.webapi.pojos;

import com.google.gson.annotations.SerializedName;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>StoreAppPackageGroup class.</p>
 *
 * @author Rafael Luis Ibasco
 */
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
    private List<StoreAppSubPackage> subPackages = new ArrayList<>();

    /**
     * <p>Getter for the field <code>name</code>.</p>
     *
     * @return a {@link java.lang.String} object
     */
    public String getName() {
        return name;
    }

    /**
     * <p>Setter for the field <code>name</code>.</p>
     *
     * @param name
     *         a {@link java.lang.String} object
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * <p>Getter for the field <code>title</code>.</p>
     *
     * @return a {@link java.lang.String} object
     */
    public String getTitle() {
        return title;
    }

    /**
     * <p>Setter for the field <code>title</code>.</p>
     *
     * @param title
     *         a {@link java.lang.String} object
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * <p>Getter for the field <code>description</code>.</p>
     *
     * @return a {@link java.lang.String} object
     */
    public String getDescription() {
        return description;
    }

    /**
     * <p>Setter for the field <code>description</code>.</p>
     *
     * @param description
     *         a {@link java.lang.String} object
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * <p>Getter for the field <code>selectionText</code>.</p>
     *
     * @return a {@link java.lang.String} object
     */
    public String getSelectionText() {
        return selectionText;
    }

    /**
     * <p>Setter for the field <code>selectionText</code>.</p>
     *
     * @param selectionText
     *         a {@link java.lang.String} object
     */
    public void setSelectionText(String selectionText) {
        this.selectionText = selectionText;
    }

    /**
     * <p>Getter for the field <code>saveText</code>.</p>
     *
     * @return a {@link java.lang.String} object
     */
    public String getSaveText() {
        return saveText;
    }

    /**
     * <p>Setter for the field <code>saveText</code>.</p>
     *
     * @param saveText
     *         a {@link java.lang.String} object
     */
    public void setSaveText(String saveText) {
        this.saveText = saveText;
    }

    /**
     * <p>Getter for the field <code>displayType</code>.</p>
     *
     * @return a int
     */
    public int getDisplayType() {
        return displayType;
    }

    /**
     * <p>Setter for the field <code>displayType</code>.</p>
     *
     * @param displayType
     *         a int
     */
    public void setDisplayType(int displayType) {
        this.displayType = displayType;
    }

    /**
     * <p>Getter for the field <code>recurringSubscription</code>.</p>
     *
     * @return a {@link java.lang.String} object
     */
    public String getRecurringSubscription() {
        return recurringSubscription;
    }

    /**
     * <p>Setter for the field <code>recurringSubscription</code>.</p>
     *
     * @param recurringSubscription
     *         a {@link java.lang.String} object
     */
    public void setRecurringSubscription(String recurringSubscription) {
        this.recurringSubscription = recurringSubscription;
    }

    /**
     * <p>Getter for the field <code>subPackages</code>.</p>
     *
     * @return a {@link java.util.List} object
     */
    public List<StoreAppSubPackage> getSubPackages() {
        return subPackages;
    }

    /**
     * <p>Setter for the field <code>subPackages</code>.</p>
     *
     * @param subPackages
     *         a {@link java.util.List} object
     */
    public void setSubPackages(List<StoreAppSubPackage> subPackages) {
        this.subPackages = subPackages;
    }
}
