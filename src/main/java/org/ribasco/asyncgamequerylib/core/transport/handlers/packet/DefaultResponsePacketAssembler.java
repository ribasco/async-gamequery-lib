/***************************************************************************************************
 * MIT License
 *
 * Copyright (c) 2016 Rafael Luis Ibasco
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

package org.ribasco.asyncgamequerylib.core.transport.handlers.packet;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.socket.DatagramPacket;
import io.netty.util.ReferenceCountUtil;
import org.ribasco.asyncgamequerylib.core.PacketAssembler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

//TODO: Please re-evaluate if this is still needed
public class DefaultResponsePacketAssembler<M> extends ChannelInboundHandlerAdapter {
    private static final Logger log = LoggerFactory.getLogger(DefaultResponsePacketAssembler.class);

    private PacketAssembler assembler;

    public DefaultResponsePacketAssembler(PacketAssembler assembler) {
        this.assembler = assembler;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        try {
            //Make sure we are only receiving an instance of DatagramPacket
            if (!(msg instanceof DatagramPacket)) {
                return;
            }
            //Retrived the packet instance
            final DatagramPacket packet = (DatagramPacket) msg;
            final ByteBuf data = ((DatagramPacket) msg).content();

            assembler.assemble(ctx, data);
        } catch (Exception e) {
            log.error("Error while processing packet for {}", ((DatagramPacket) msg).sender());
            throw e;
        } finally {
            ReferenceCountUtil.release(msg);
        }
    }
}
