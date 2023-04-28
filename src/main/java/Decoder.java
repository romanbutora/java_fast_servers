

import cz.cvut.fel.esw.server.proto.Request;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufInputStream;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;



public class Decoder extends ByteToMessageDecoder {
    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) throws Exception {
        long message =  byteBuf.getUnsignedInt(byteBuf.readerIndex());
        if (byteBuf.readableBytes() >= 4 + message){
            byteBuf.skipBytes(4);
            list.add(Request.parseFrom(new ByteBufInputStream(byteBuf)));
        }
    }
}
