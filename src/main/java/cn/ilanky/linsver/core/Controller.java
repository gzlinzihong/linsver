package cn.ilanky.linsver.core;

import cn.ilanky.linsver.http.HttpRequest;
import cn.ilanky.linsver.http.HttpResponse;

/**
 * @author 嘿 林梓鸿
 */
public interface Controller {

    void init();

    void doGet(HttpRequest request, HttpResponse response);

    void doPost(HttpRequest request, HttpResponse response);

    void destroy();
}
