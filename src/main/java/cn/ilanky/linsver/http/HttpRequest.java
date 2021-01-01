package cn.ilanky.linsver.http;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author: linzihong
 * @Date: 2020/12/30 11:44
 */
public class HttpRequest {
    private Map<String,String> header;
    private Map<String,String> params;
    private String method;
    private String uri;
    private int bodyStart = 0;
    public HttpRequest(String http) {
        header = new HashMap<>();
        params = new HashMap<>();
        String[] lines = http.split(System.lineSeparator());
        String[] items = lines[0].split(" ");
        method = items[0];
        uri = items[1];
        parseHeader(lines);
        if (method.equals("GET")){
            parseGetParam(uri);
        }else if (method.equals("POST")){
            parsePostParam(lines);
        }
    }

    private void parseGetParam(String uri){
        int start = uri.indexOf("?");
        if (start == -1){
            return;
        }
        this.uri = uri.substring(0,start);
        String paramString = uri.substring(start+1);
        parseParam(paramString);
    }

    private void parsePostParam(String[] lines){
        for (int i = bodyStart + 1;i<lines.length;i++){
            String line = lines[i];
            parseParam(line);
        }
    }

    private void parseHeader(String[] lines){
        for (int i = 1; i < lines.length; i++) {
            String line = lines[i];
            if (line.equals("")){
                bodyStart = i;
                break;
            }
            String[] split = line.split(": ");
            header.put(split[0],split[1]);
        }
    }

    private void parseParam(String str){
        String[] groups = str.split("&");
        for (String s : groups) {
            String[] group = s.split("=");
            params.put(group[0],group[1]);
        }
    }

    public Map<String, String> getHeaders() {
        return header;
    }

    public Map<String, String> getParams() {
        return params;
    }

    public String getMethod() {
        return method;
    }

    public String getUri() {
        return uri;
    }

    public String getHeader(String key){
        return header.get(key);
    }

    public String getParam(String key){
        return params.get(key);
    }


}
