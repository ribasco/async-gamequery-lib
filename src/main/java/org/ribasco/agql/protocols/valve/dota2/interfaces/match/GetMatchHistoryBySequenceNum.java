package org.ribasco.agql.protocols.valve.dota2.interfaces.match;

import org.ribasco.agql.protocols.valve.dota2.Dota2ApiConstants;
import org.ribasco.agql.protocols.valve.dota2.interfaces.Dota2MatchRequest;

public class GetMatchHistoryBySequenceNum extends Dota2MatchRequest {
    public GetMatchHistoryBySequenceNum(int apiVersion, int startSeqNum, int matchesRequested) {
        super(Dota2ApiConstants.DOTA2_METHOD_GETMATCHHISTORYBYSEQNUM, apiVersion, null);
        urlParam("start_at_match_seq_num", startSeqNum);
        urlParam("matches_requested", matchesRequested);
    }
}
