package com.ribasco.gamecrawler.protocols.valve.source;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.modules.junit4.PowerMockRunner;

/**
 * Created by raffy on 9/6/2016.
 */
@RunWith(PowerMockRunner.class)
public class SourceMasterFilterTest {

    private SourceMasterFilter filter;

    @Before
    public void setUp() throws Exception {
        filter = new SourceMasterFilter();
    }

    @After
    public void tearDown() throws Exception {

    }

    @Test(expected = RuntimeException.class)
    public void allServersWithException() throws Exception {
        filter.appId(550).allServers().gamedata("sv_cheats");
    }

    @Test
    public void isSpecProxy() throws Exception {

    }

    @Test
    public void isFull() throws Exception {

    }

    @Test
    public void isEmpty() throws Exception {

    }

    @Test
    public void isPasswordProtected() throws Exception {

    }

    @Test
    public void isLinuxServer() throws Exception {

    }

    @Test
    public void mapName() throws Exception {

    }

    @Test
    public void gamedir() throws Exception {

    }

    @Test
    public void secured() throws Exception {

    }

    @Test
    public void dedicated() throws Exception {

    }

    @Test
    public void and() throws Exception {

    }

    @Test
    public void nor() throws Exception {

    }

    @Test
    public void napp() throws Exception {

    }

    @Test
    public void hasNoPlayers() throws Exception {

    }

    @Test
    public void gametypes() throws Exception {

    }

    @Test
    public void gamedata() throws Exception {

    }

    @Test
    public void gamedataOr() throws Exception {

    }

    @Test
    public void withHostName() throws Exception {

    }

    @Test
    public void hasVersion() throws Exception {

    }

    @Test
    public void onlyOneServerPerUniqueIp() throws Exception {

    }

    @Test
    public void hasServerIp() throws Exception {

    }

    @Test
    public void isWhitelisted() throws Exception {

    }

    @Test
    public void appId() throws Exception {

    }

}