package cn.ilanky.linsver.http;

import cn.ilanky.simplenetty.handler.ChannelHandlerContext;
import cn.ilanky.simplenetty.handler.ChannelOutboundHandlerAdapter;

import java.nio.ByteBuffer;

/**
 * @author 嘿 林梓鸿
 * @date 2020年 12月31日 13:11:20
 */
public class Test extends ChannelOutboundHandlerAdapter {

    @Override
    public Object channelWrite(ChannelHandlerContext var1, Object var2) throws Exception {
        ByteBuffer byteBuffer = (ByteBuffer) var2;
        System.out.println("write: " + new String(byteBuffer.array(),0,byteBuffer.position()));
        return var2;
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext var1, Throwable var2) throws Exception {
        var2.printStackTrace();
    }
}
