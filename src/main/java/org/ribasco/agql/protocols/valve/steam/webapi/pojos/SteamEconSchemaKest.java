package org.ribasco.agql.protocols.valve.steam.webapi.pojos;

import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * <p>
 * "Kest" which stands for "Kill Eater Score Types". An object describing suffixes to use
 * after a kill eater value in an attribute display line.
 * </p>
 */
public class SteamEconSchemaKest {
    private int type;
    @SerializedName("type_name")
    private String name;
    @SerializedName("level_data")
    private String levelData;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLevelData() {
        return levelData;
    }

    public void setLevelData(String levelData) {
        this.levelData = levelData;
    }

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.NO_CLASS_NAME_STYLE);
    }
}
