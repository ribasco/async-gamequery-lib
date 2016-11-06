package org.ribasco.agql.protocols.valve.dota2.enums;

public enum Dota2IconType {
    NORMAL(0),
    LARGE(1),
    INGAME(2);
    private int type;

    Dota2IconType(int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }
}

