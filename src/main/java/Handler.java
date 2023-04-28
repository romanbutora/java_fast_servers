
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.apache.commons.io.IOUtils;
import static io.netty.util.CharsetUtil.UTF_8;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.io.UncheckedIOException;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.zip.GZIPInputStream;
import static java.util.Objects.isNull;
import cz.cvut.fel.esw.server.proto.Response;

import cz.cvut.fel.esw.server.proto.Response.Builder;

import cz.cvut.fel.esw.server.proto.Request;
import java.util.concurrent.*;


public class Handler extends ChannelInboundHandlerAdapter {
    static ConcurrentHashMap<String,String> map = new ConcurrentHashMap<>();
    Set<String> diffWords = map.keySet("SET-ENTRY");

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        Request request = (Request) msg;

        if (request.hasGetCount()){
            Builder builder = Response.newBuilder();
            builder.setStatus(Response.Status.OK).setCounter(diffWords.size());
            ctx.writeAndFlush(builder.build());

            diffWords.clear();
        }

        else{
            String words = decompress(request.getPostWords().getData().toByteArray());

            StringTokenizer st = new StringTokenizer(words);
            while (st.hasMoreTokens()) {
                diffWords.add(st.nextToken());
            }

            Builder builder = Response.newBuilder();
            builder.setStatus(Response.Status.OK);
            ctx.writeAndFlush(builder.build());
        }
    }

    public static String decompress(final byte[] compressed) {
        if (isNull(compressed) || compressed.length == 0) {
            return null;
        }

        try (final GZIPInputStream gzipInput = new GZIPInputStream(new ByteArrayInputStream(compressed));
             final StringWriter stringWriter = new StringWriter()) {
            IOUtils.copy(gzipInput, stringWriter, UTF_8);
            return stringWriter.toString();
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
}
