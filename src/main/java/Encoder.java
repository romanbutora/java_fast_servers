import io.netty.buffer.ByteBuf;
import io.netty.handler.codec.MessageToByteEncoder;
import java.nio.ByteBuffer;
import io.netty.channel.ChannelHandlerContext;
import cz.cvut.fel.esw.server.proto.Response;

public class Encoder extends MessageToByteEncoder<Response> {
    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, Response response, ByteBuf byteBuf)  {
        int size = response.getSerializedSize();
        byte[] size_buff = ByteBuffer.allocate(4).putInt(size).array();
        byte[] response_buff = response.toByteArray();
        byteBuf.writeBytes(size_buff);
        byteBuf.writeBytes(response_buff);
    }

}
