package org.ribasco.agql.protocols.valve.dota2.webapi.enums;

public enum Dota2TeamType {
    RADIANT(0),
    DIRE(1),
    BROADCASTER(2),
    UNASSIGNED(4);

    private int typeCode;

    Dota2TeamType(int typeCode) {
        this.typeCode = typeCode;
    }

    public int getTypeCode() {
        return typeCode;
    }
}
