package cn.ilanky.linsver.http;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author: linzihong
 * @Date: 2020/12/30 17:14
 */
public class HttpResponse {
    private Map<String,String> header;
    private ByteBuffer body;
    private static final String SERVER = "linsver";
    private int status;

    public HttpResponse() {
        header = new HashMap<>();
        body = ByteBuffer.allocate(1024 * 500);
        status = 200;
        putDefaultHeader();
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public void write(String msg){
        body.put(msg.getBytes());
    }

    public void write(byte[] bytes){
        body.put(bytes);
    }

    private void putDefaultHeader() {
        header.put("date", Instant.now().toString());
        header.put("server",SERVER);
        header.put("Content-Type","text/html; charset=utf-8");
        header.put("cache-control","no-cache, no-store, max-age=0, must-revalidate");
//        header.put("Content-Encoding","gzip");
        header.put("x-content-type-options","nosniff");
        header.put("x-frame-options","DENY");
        header.put("x-xss-protection","1; mode=block");
    }

    public void setHeader(String key,String value){
        header.put(key,value);
    }


    public ByteBuffer getBytes(){
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024 * 1000);
        StringBuilder s = new StringBuilder();
        s.append("HTTP/1.1 " + status + " OK" + System.lineSeparator());
        header.keySet().forEach( item -> {
            s.append(item + ": " + header.get(item) + System.lineSeparator());
        });
        s.append("Content-Length: " + body.position() + System.lineSeparator());
        s.append(System.lineSeparator());
        byteBuffer.put(s.toString().getBytes());
        for (int i = 0;i<body.position();i++){
            byteBuffer.put(body.get(i));
        }
        byteBuffer.flip();
        return byteBuffer;
    }
}
