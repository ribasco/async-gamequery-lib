package com.ribasco.gamecrawler.protocols.valve.source;

import io.netty.buffer.ByteBuf;
import org.apache.commons.lang3.reflect.ConstructorUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;

import static com.ribasco.gamecrawler.protocols.valve.source.SourceConstants.HEADER_RESPONSE_MAP;

/**
 * Created by raffy on 9/5/2016.
 */
public class SourcePacketHelper {
    private static final Logger log = LoggerFactory.getLogger(SourcePacketHelper.class);

    public static boolean isValidResponsePacket(ByteBuf packet) {
        packet.markReaderIndex();
        byte packetHeader = packet.readByte();
        boolean exists = HEADER_RESPONSE_MAP.containsKey(packetHeader);
        packet.resetReaderIndex();
        return exists;
    }

    public static SourceResponsePacket getResponsePacket(ByteBuf packet) {
        return getResponsePacket(packet, null);
    }

    public static SourceResponsePacket getResponsePacket(ByteBuf packet, Boolean test) {
        byte packetHeader = packet.readByte();
        SourceResponsePacket handler = null;

        //Check
        if (HEADER_RESPONSE_MAP.containsKey(packetHeader)) {
            try {
                Class<? extends SourceResponsePacket> cResponsePacket = HEADER_RESPONSE_MAP.get(packetHeader);
                handler = ConstructorUtils.invokeConstructor(cResponsePacket, packet);
            } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException | InstantiationException e) {
                log.error(e.getMessage(), e);
                throw new RuntimeException(e);
            }
        }

        return handler;
    }

}
