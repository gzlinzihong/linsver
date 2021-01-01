package cn.ilanky.linsver.core;

import cn.ilanky.linsver.http.HttpDecoder;
import cn.ilanky.linsver.http.HttpParser;
import cn.ilanky.linsver.http.Test;
import cn.ilanky.simplelogger.Logger;
import cn.ilanky.simplelogger.LoggerFactory;
import cn.ilanky.simplenetty.channel.ChannelOption;
import cn.ilanky.simplenetty.channel.NioServerSocketChannel;
import cn.ilanky.simplenetty.concurrent.ChannelFuture;
import cn.ilanky.simplenetty.core.Bootstrap;
import cn.ilanky.simplenetty.core.ByteConstants;
import cn.ilanky.simplenetty.core.NioEventLoopGroup;
import cn.ilanky.simplenetty.handler.ChannelHandlerContext;
import cn.ilanky.simplenetty.handler.ChannelInitializer;

import java.util.HashMap;
import java.util.Map;

/**
 * @author 嘿 林梓鸿
 * @date 2020年 12月30日 20:43:24
 */
public class Linsver {
    private static final Logger LOG = LoggerFactory.getLogger(Linsver.class);
    private final int PORT;
    private int acceptThreadSize = 2;
    private int coreSize = 4;
    private int bufferSize = ByteConstants.MB;
    public static String basicDir;
    public static Map<String,Controller> controllerMap = new HashMap<>();

    public Linsver setAcceptThreadSize(int acceptThreadSize) {
        this.acceptThreadSize = acceptThreadSize;
        return this;
    }

    public Linsver setCoreSize(int coreSize) {
        this.coreSize = coreSize;
        return this;
    }

    public Linsver setBufferSize(int bufferSize) {
        this.bufferSize = bufferSize;
        return this;
    }

    public static void setBasicDir(String basicDir) {
        Linsver.basicDir = basicDir;
    }

    public Linsver(int port) {
        this.PORT = port;
        basicDir = null;
    }

    public Linsver(int PORT, String basicDir) {
        this.PORT = PORT;
        Linsver.basicDir = basicDir;
    }

    public void start(){
        NioEventLoopGroup boss = new NioEventLoopGroup(true);
        NioEventLoopGroup worker = new NioEventLoopGroup(false);
        Bootstrap bootstrap = new Bootstrap();
        ChannelFuture cf = bootstrap.group(boss, worker)
                .channel(NioServerSocketChannel.class)
                .option(ChannelOption.CORE_SIZE,acceptThreadSize)
                .childOption(ChannelOption.CORE_SIZE,coreSize)
                .childOption(ChannelOption.BUFFER_SIZE, bufferSize)
                .channel(NioServerSocketChannel.class)
                .handler(new ChannelInitializer() {
                    @Override
                    public void init(ChannelHandlerContext context) {
                        context.pipeline().addLast(new HttpDecoder());
                        context.pipeline().addLast(new HttpParser());
                        context.pipeline().addLast(new Test());
                    }
                })
                .bind(PORT);
        try {
            cf.addListener(future -> {
                if (future.isFail()){
                    LOG.error("the port has bee bounded");
                    worker.shutdown();
                    boss.shutdown();
                }
            },1);
            cf.sync();
            controllerMap.values().forEach(Controller::init);
            ChannelFuture closeFuture = cf.channel().closeFuture();
            closeFuture.addListener(future ->{
                controllerMap.values().forEach(Controller::destroy);
                worker.shutdown();
                boss.shutdown();
            },1);
            LOG.info("linsver is running at port: " + PORT);
            LOG.info("basicDir is " + (basicDir==null?"default":basicDir));
            LOG.info(String.format("acceptThreadSize is %s,coreSize is %s,bufferSize is %s",acceptThreadSize,coreSize,ByteConstants.toMbString(bufferSize)));
            closeFuture.sync();
        } catch (InterruptedException e) {
            LOG.error(e.getMessage());
        }
    }

    public void addController(String mapping,Controller controller){
        controllerMap.put(mapping,controller);
    }
}
