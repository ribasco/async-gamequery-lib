package org.ribasco.agql.protocols.valve.steam.webapi.enums;

public enum VanityUrlType {
    DEFAULT(1),
    INDIVIDUAL_PROFILE(1),
    GROUP(2),
    OFFICIAL_GAME_GROUP(3);

    private int type;

    VanityUrlType(int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }
}
