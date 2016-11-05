
package org.ribasco.asyncgamequerylib.protocols.valve.dota2.webapi.pojos;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class Dota2TeamMemberDetails {

    @SerializedName("account_id")
    @Expose
    private long accountId;
    @SerializedName("time_joined")
    @Expose
    private long timeJoined;
    @SerializedName("admin")
    @Expose
    private boolean admin;
    @SerializedName("sub")
    @Expose
    private boolean sub;

    /**
     * @return The accountId
     */
    public long getAccountId() {
        return accountId;
    }

    /**
     * @param accountId The account_id
     */
    public void setAccountId(long accountId) {
        this.accountId = accountId;
    }

    /**
     * @return The timeJoined
     */
    public long getTimeJoined() {
        return timeJoined;
    }

    /**
     * @param timeJoined The time_joined
     */
    public void setTimeJoined(long timeJoined) {
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
