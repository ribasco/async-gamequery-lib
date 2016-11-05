package org.ribasco.asyncgamequerylib.protocols.valve.dota2.webapi.interfaces.match;

import org.ribasco.asyncgamequerylib.protocols.valve.dota2.webapi.Dota2ApiConstants;
import org.ribasco.asyncgamequerylib.protocols.valve.dota2.webapi.requests.Dota2MatchRequest;

public class GetMatchHistoryBySequenceNum extends Dota2MatchRequest {
    public GetMatchHistoryBySequenceNum(int apiVersion, int startSeqNum, int matchesRequested) {
        super(Dota2ApiConstants.DOTA2_METHOD_GETMATCHHISTORYBYSEQNUM, apiVersion, null);
        urlParam("start_at_match_seq_num", startSeqNum);
        urlParam("matches_requested", matchesRequested);
    }
}
