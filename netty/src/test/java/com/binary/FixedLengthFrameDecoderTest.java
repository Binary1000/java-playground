package com.binary;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.embedded.EmbeddedChannel;
import io.netty.handler.codec.FixedLengthFrameDecoder;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * @author Binary on 2020/8/24
 */
public class FixedLengthFrameDecoderTest {

    @Test
    public void test() {
        ByteBuf buffer = Unpooled.buffer();
        for (int i = 0; i < 9; i++) {
            buffer.writeByte(i);
        }

        ByteBuf input = buffer.duplicate();
        EmbeddedChannel channel = new EmbeddedChannel(new FixedLengthFrameDecoder(3));

        assertTrue(channel.writeInbound(input));
        assertTrue(channel.finish());
        ByteBuf read = channel.readInbound();
        assertEquals(buffer.readSlice(3), read);
        read.release();

        read = channel.readInbound();
        assertEquals(buffer.readSlice(3), read);
        read.release();

        read = channel.readInbound();
        assertEquals(buffer.readSlice(3), read);
        read.release();

        assertNull(channel.readInbound());

    }

}
