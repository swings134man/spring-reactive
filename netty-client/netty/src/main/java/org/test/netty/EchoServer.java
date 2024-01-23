package org.test.netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.util.concurrent.DefaultThreadFactory;
import lombok.extern.slf4j.Slf4j;

/************
 * @info : Netty - Echo Server
 * @name : EchoServer
 * @date : 1/23/24 9:44 PM
 * @author : SeokJun Kang(swings134@gmail.com)
 * @version : 1.0.0
 * @Description :
 ************/
@Slf4j
public class EchoServer {
    public static void main(String[] args) throws Exception {
        // Server socker Listen
        EventLoopGroup bossGroup = new NioEventLoopGroup(1, new DefaultThreadFactory("boss")); // Threads = 1
        EventLoopGroup workerGroup = new NioEventLoopGroup(1, new DefaultThreadFactory("worker")); // Threads = Default

        try{
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class) // NIO Selector
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ChannelPipeline p = ch.pipeline();
                            p.addLast(new StringDecoder());
                            p.addLast(new StringEncoder());
                            p.addLast(new EchoServerHandler()); // 접속된 클라이언트로부터 수신된 데이터를 처리할 핸들러 정의
                        }
                    }); // Channel Handler

            ChannelFuture f = b.bind(8888).sync();

            f.channel().closeFuture().sync();
        }catch (Exception e) {
            log.error("Exception", e);
        }finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }

    }
}
