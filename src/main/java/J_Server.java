import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

public class J_Server {
    private final int port;

    public J_Server(int port) {
        this.port = port;
    }

    public static void main(String[] args) {
        int p_num = 8029;
        J_Server server = new J_Server(p_num);
        server.loopRun();
    }

    private void loopRun() {
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup,workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        public void initChannel(SocketChannel ch){
                            ch.pipeline().addLast(new Decoder(),
                                    new Encoder(),
                                    new Handler());
                        }
                    });

            ChannelFuture f = b.bind(port).sync();
            System.out.println("Running at : "+ port);
            f.channel().closeFuture().sync();
        } catch(Exception e) {
            System.out.println(e.getMessage());
        }

        System.out.println("Turning off server ");
        workerGroup.shutdownGracefully();
        bossGroup.shutdownGracefully();

    }
}



