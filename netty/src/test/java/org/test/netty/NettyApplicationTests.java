package org.test.netty;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class NettyApplicationTests {

    @Test
    public void testEchoServer() throws Exception {
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            Bootstrap b = new Bootstrap();
            b.group(group)
                    .channel(NioSocketChannel.class)
                    .handler(new ChannelInitializer<Channel>() {
                        @Override
                        protected void initChannel(Channel ch) throws Exception {
                            ChannelPipeline p = ch.pipeline();
                            p.addLast(new StringDecoder());
                            p.addLast(new StringEncoder());
                            p.addLast(new EchoServerHandler());
                        }
                    });

            // Start the client.
            Channel ch = b.connect("localhost", 8888).sync().channel();

            // Send the message to server
            String message = "Hello, Netty!";
            ch.writeAndFlush(message).sync();

            // Wait for the server to echo back the message
            String echoedMessage = EchoServerHandler.getEchoedMessage();

            assertEquals(message, echoedMessage);
        } finally {
            group.shutdownGracefully();
        }
    }

}
