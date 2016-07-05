package com.cometproject.server.protocol.codec;

import com.cometproject.api.networking.messages.IMessageComposer;
import com.cometproject.server.protocol.messages.Composer;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

public class MessageEncoder extends MessageToByteEncoder<IMessageComposer> {
    @Override
    protected void encode(ChannelHandlerContext ctx, IMessageComposer msg, ByteBuf out) throws Exception {
        final Composer composer = ((Composer) msg.writeMessage(out));

        if (!composer.hasLength()) {
            composer.content().setInt(0, composer.content().writerIndex() - 4);
        }
    }
}
