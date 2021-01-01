package cn.ilanky.linsver.http;

import cn.ilanky.simplenetty.handler.ChannelHandlerContext;
import cn.ilanky.simplenetty.handler.ChannelInboundHandlerAdapter;

import java.nio.ByteBuffer;

/**
 * @Author: linzihong
 * @Date: 2020/12/30 11:39
 */
public class HttpDecoder extends ChannelInboundHandlerAdapter {

    @Override
    public Object channelRead(ChannelHandlerContext var1, Object var2) throws Exception {
        ByteBuffer buffer = (ByteBuffer)var2;
        return new String(buffer.array(), 0, buffer.position());
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext var1, Throwable var2){
        var2.printStackTrace();
    }
}
