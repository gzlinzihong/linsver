# linsver

基于自己写的JavaNIO框架简单封装的一个http server

目前支持

1. 静态资源获取
2. 动态编程

但整体实现还不够优雅,持续更新ing

(项目引用的日志类是自己写的,还未开源)

[simpleNetty](https://github.com/ilanky/simpleNetty)

## 流程

接到一个请求,首先找是否有对该uri进行绑定

有则找对应的controller处理

无则当静态资源处理

## demo

**启动**

```java
/**
 * @author 嘿 林梓鸿
 * @date 2020年 12月30日 20:56:29
 */
public class Application {
    public static void main(String[] args) {
        Linsver linsver = new Linsver(7676);
        linsver.addController("/login.do",new TestController());
        linsver.start();
    }
}
```

**核心方法**

```java
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
```

**编程**

```java
/**
 * @author 嘿 林梓鸿
 * @date 2020年 12月31日 09:59:30
 */
public class TestController implements Controller {
    private static final Logger LOG = LoggerFactory.getLogger(TestController.class);
    @Override
    public void init() {
        LOG.info("init..........");
    }

    @Override
    public void doGet(HttpRequest request, HttpResponse response) {
        String username = request.getParam("username");
        String password = request.getParam("password");
        if (username.equals("test") && password.equals("test")){
            response.write("登陆成功");
        }else {
            response.write("登陆失败");
        }
    }

    @Override
    public void doPost(HttpRequest request, HttpResponse response) {
        doGet(request,response);
    }

    @Override
    public void destroy() {

    }
}
```