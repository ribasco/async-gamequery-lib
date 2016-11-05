package org.ribasco.asyncgamequerylib.protocols.valve.dota2.interfaces;

import org.ribasco.asyncgamequerylib.protocols.valve.dota2.Dota2WebApiInterface;
import org.ribasco.asyncgamequerylib.protocols.valve.steam.webapi.SteamWebApiClient;

public class Dota2Ticket extends Dota2WebApiInterface {
    /**
     * {@inheritDoc}
     *
     * @param client
     */
    public Dota2Ticket(SteamWebApiClient client) {
        super(client);
    }
}
