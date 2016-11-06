
package org.ribasco.agql.protocols.valve.dota2.pojos;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.annotation.Generated;

@Generated("org.jsonschema2pojo")
public class Dota2TeamMemberDetails {

    @SerializedName("account_id")
    @Expose
    private int accountId;
    @SerializedName("time_joined")
    @Expose
    private int timeJoined;
    @SerializedName("admin")
    @Expose
    private boolean admin;
    @SerializedName("sub")
    @Expose
    private boolean sub;

    /**
     * @return The accountId
     */
    public int getAccountId() {
        return accountId;
    }

    /**
     * @param accountId The account_id
     */
    public void setAccountId(int accountId) {
        this.accountId = accountId;
    }

    /**
     * @return The timeJoined
     */
    public int getTimeJoined() {
        return timeJoined;
    }

    /**
     * @param timeJoined The time_joined
     */
    public void setTimeJoined(int timeJoined) {
        this.timeJoined = timeJoined;
    }

    /**
     * @return The admin
     */
    public boolean isAdmin() {
        return admin;
    }

    /**
     * @param admin The admin
     */
    public void setAdmin(boolean admin) {
        this.admin = admin;
    }

    /**
     * @return The sub
     */
    public boolean isSub() {
        return sub;
    }

    /**
     * @param sub The sub
     */
    public void setSub(boolean sub) {
        this.sub = sub;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.NO_CLASS_NAME_STYLE);
    }

}
