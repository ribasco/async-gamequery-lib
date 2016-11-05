package org.ribasco.asyncgamequerylib.protocols.valve.dota2.adapters;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import org.ribasco.asyncgamequerylib.core.exceptions.AsyncGameLibUncheckedException;
import org.ribasco.asyncgamequerylib.protocols.valve.dota2.pojos.Dota2MatchTeamInfo;

import java.io.IOException;

public class Dota2TeamInfoAdapter extends TypeAdapter<Dota2MatchTeamInfo> {
    @Override
    public Dota2MatchTeamInfo read(JsonReader in) throws IOException {
        in.beginObject();
        Dota2MatchTeamInfo teamInfo = new Dota2MatchTeamInfo();
        while (in.hasNext()) {
            String name = in.nextName();
            if (name.startsWith("league_id_")) {
                teamInfo.getLeagueIds().add(in.nextInt());
            } else if (name.startsWith("player_")) {
                teamInfo.getPlayerAccountIds().add(in.nextLong());
            } else {
                switch (name) {
                    case "name":
                        teamInfo.setName(in.nextString());
                        break;
                    case "tag":
                        teamInfo.setTag(in.nextString());
                        break;
                    case "time_created":
                        teamInfo.setTimeCreated(in.nextLong());
                        break;
                    case "calibration_games_remaining":
                        teamInfo.setCalibrationGamesRemaining(in.nextInt());
                        break;
                    case "logo":
                        teamInfo.setLogo(in.nextLong());
                        break;
                    case "logo_sponsor":
                        teamInfo.setLogoSponsor(in.nextLong());
                        break;
                    case "country_code":
                        teamInfo.setCountryCode(in.nextString());
                        break;
                    case "url":
                        teamInfo.setUrl(in.nextString());
                        break;
                    case "games_played":
                        teamInfo.setGamesPlayed(in.nextInt());
                        break;
                    case "admin_account_id":
                        teamInfo.setAdminAccountId(in.nextLong());
                        break;
                }
            }
        }
        in.endObject();
        return teamInfo;
    }

    @Override
    public void write(JsonWriter out, Dota2MatchTeamInfo value) throws IOException {
        throw new AsyncGameLibUncheckedException("Not implemented");
    }
}
