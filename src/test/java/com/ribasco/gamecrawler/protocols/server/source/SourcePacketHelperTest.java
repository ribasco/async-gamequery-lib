package com.ribasco.gamecrawler.protocols.server.source;

import com.ribasco.gamecrawler.protocols.GameRequestPacket;
import com.ribasco.gamecrawler.protocols.Response;
import com.ribasco.gamecrawler.protocols.valve.server.SourcePacketHelper;
import com.ribasco.gamecrawler.protocols.valve.server.packets.requests.SourceInfoRequestPacket;
import com.ribasco.gamecrawler.protocols.valve.server.packets.response.SourceInfoResponsePacket;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.junit.Assert.*;


/**
 * Created by raffy on 9/5/2016.
 */
@RunWith(PowerMockRunner.class)
@PowerMockIgnore("javax.management.*")
public class SourcePacketHelperTest {
    @Before
    public void setUp() throws Exception {

    }

    @Test
    public void getRequestClass() throws Exception {
        SourcePacketHelper helper = new SourcePacketHelper();
        Class<? extends GameRequestPacket> requestClass = helper.getRequestClass(SourceInfoResponsePacket.class);
        assertNotNull(requestClass);
        assertEquals(requestClass, SourceInfoRequestPacket.class);
    }

    @Test
    public void getRequestClassOnNonExistentResponse() throws Exception {
        SourcePacketHelper helper = new SourcePacketHelper();
        Class<? extends GameRequestPacket> requestClass = helper.getRequestClass(Response.class);
        assertNull(requestClass);
    }

    @Test
    public void getResponseClass() throws Exception {

    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void createFromPacket() throws Exception {

    }

    @Test
    public void isValidResponsePacket() throws Exception {

    }

    @Test
    public void getResponsePacket() throws Exception {

    }

    @Test
    public void getResponsePacket1() throws Exception {

    }

}