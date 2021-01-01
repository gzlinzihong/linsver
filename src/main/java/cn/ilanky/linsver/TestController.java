package cn.ilanky.linsver;

import cn.ilanky.linsver.core.Controller;
import cn.ilanky.linsver.http.HttpRequest;
import cn.ilanky.linsver.http.HttpResponse;
import cn.ilanky.simplelogger.Logger;
import cn.ilanky.simplelogger.LoggerFactory;

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
