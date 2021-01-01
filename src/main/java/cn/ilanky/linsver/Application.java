package cn.ilanky.linsver;

import cn.ilanky.linsver.core.Linsver;

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
