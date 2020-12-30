package org.geekbang.time.commonmistakes.java8.CopyOnWriteArraySetDemo;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * @Author xialh
 * @Date 2020/12/29 7:05 PM
 */
public class CopyOnWriteArraySetDemo {



    //路由信息
     final class Router{
        private final String  ip;
        private final Integer port;
        private final String  iface;
        //构造函数
        public Router(String ip,
                      Integer port, String iface){
            this.ip = ip;
            this.port = port;
            this.iface = iface;
        }
        //重写equals方法
        @Override
        public boolean equals(Object obj){
            if (obj instanceof Router) {
                Router r = (Router)obj;
                return iface.equals(r.iface) &&
                        ip.equals(r.ip) &&
                        port.equals(r.port);
            }
            return false;
        }
//         int hashCode() {
//            //省略hashCode相关代码
//        }
    }
    //路由表信息
     class RouterTable {
        //Key:接口名
        //Value:路由集合
        ConcurrentHashMap<String, CopyOnWriteArraySet<Router>> rt = new ConcurrentHashMap<>();
        //根据接口名获取路由表
        public Set<Router> get(String iface){
            return rt.get(iface);
        }
        //删除路由
         void remove(Router router) {
            Set<Router> set=rt.get(router.iface);
            if (set != null) {
                set.remove(router);
            }
        }
        //增加路由
         void add(Router router) {
            Set<Router> set = rt.computeIfAbsent(router.iface, r ->new CopyOnWriteArraySet<>());
            set.add(router);
        }
    }


}

