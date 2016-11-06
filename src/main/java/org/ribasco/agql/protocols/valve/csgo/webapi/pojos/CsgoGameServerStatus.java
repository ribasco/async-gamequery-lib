package org.ribasco.agql.protocols.valve.csgo.webapi.pojos;

import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.List;

public class CsgoGameServerStatus {
    @SerializedName("app")
    private CsgoAppStatus appStatus;
    @SerializedName("services")
    private CsgoAppServicesStatus appServicesStatus;
    @SerializedName("datacenters")
    private List<CsgoDatacenterStatus> datacenterStatus;
    @SerializedName("matchmaking")
    private CsgoMatchmakingStatus matchmakingStatus;

    public CsgoAppStatus getAppStatus() {
        return appStatus;
    }

    public void setAppStatus(CsgoAppStatus appStatus) {
        this.appStatus = appStatus;
    }

    public CsgoAppServicesStatus getAppServicesStatus() {
        return appServicesStatus;
    }

    public void setAppServicesStatus(CsgoAppServicesStatus appServicesStatus) {
        this.appServicesStatus = appServicesStatus;
    }

    public List<CsgoDatacenterStatus> getDatacenterStatus() {
        return datacenterStatus;
    }

    public void setDatacenterStatus(List<CsgoDatacenterStatus> datacenterStatus) {
        this.datacenterStatus = datacenterStatus;
    }

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.NO_CLASS_NAME_STYLE);
    }
}
