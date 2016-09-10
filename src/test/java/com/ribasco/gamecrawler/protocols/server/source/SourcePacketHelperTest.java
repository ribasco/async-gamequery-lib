/***************************************************************************************************
 * MIT License
 *
 * Copyright (c) 2016 Rafael Ibasco
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 **************************************************************************************************/

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