package cn.ilanky.linsver.http;

import cn.ilanky.linsver.core.Controller;
import cn.ilanky.linsver.core.Linsver;
import cn.ilanky.linsver.core.MimeProperties;
import cn.ilanky.simplenetty.handler.ChannelHandlerContext;
import cn.ilanky.simplenetty.handler.ChannelInboundHandlerAdapter;

import java.io.*;
import java.net.URL;
import java.nio.ByteBuffer;
import java.util.Map;

/**
 * @Author: linzihong
 * @Date: 2020/12/30 11:41
 */
public class HttpParser extends ChannelInboundHandlerAdapter {
    @Override
    public Object channelRead(ChannelHandlerContext var1, Object var2) throws Exception {
        String http = (String) var2;
        HttpRequest request = new HttpRequest(http);
        HttpResponse response = new HttpResponse();
        doService(request,response);
        var1.writeAndFlush(response.getBytes());
        return http;
    }

    private void doService(HttpRequest request,HttpResponse response){
        String uri = request.getUri();
        String method = request.getMethod();
        Map<String, Controller> controllerMap = Linsver.controllerMap;
        if (controllerMap.containsKey(uri)){
            Controller controller = controllerMap.get(uri);
            if (method.equals("GET")){
                controller.doGet(request,response);
            }else {
                controller.doPost(request,response);
            }
        }else {
            if (Linsver.basicDir == null){
                URL resource = Linsver.class.getClassLoader().getResource(uri.substring(1));
                if (resource == null){
                    response.setStatus(404);
                    response.write("NOT FOUND");
                }else {
                    String extension = uri.substring(uri.lastIndexOf(".") + 1);
                    response.setHeader("Content-Type", MimeProperties.get(extension));
                    File file = new File(resource.getFile());
                    try {
                        FileInputStream fis = new FileInputStream(file);
                        ByteArrayOutputStream bos = new ByteArrayOutputStream();
                        byte[] b = new byte[fis.available()];
                        int len = -1;
                        while((len = fis.read(b)) != -1) {
                            bos.write(b, 0, len);
                        }
                        response.write(b);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}
