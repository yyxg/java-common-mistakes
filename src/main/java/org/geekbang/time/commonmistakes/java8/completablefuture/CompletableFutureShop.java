package org.geekbang.time.commonmistakes.java8.completablefuture;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadLocalRandom;

import static java.util.stream.Collectors.toList;

/**
 * JAVA8 实现中CompletableFuture
 * @Author xialh
 * @Date 2020/12/24 7:58 AM
 */
public class CompletableFutureShop {

    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public CompletableFutureShop(){
    }
    public CompletableFutureShop(String name ){
        this.name = name;
    }


    /**
     * 方法一
     * 如果calculatePrice方法异常，调用方会发生阻塞
     * 即便getPriceAsync方法被捕获 或者calculatePrice被捕获
     * @param product
     * @return
     */
    public Future<Double> getPriceAsync1(String product){

        CompletableFuture<Double> futurePrice = new CompletableFuture<>();

        new Thread(()->{


            try {
                double price = calculatePrice(product);
                futurePrice.complete(price);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }).start();

        return futurePrice;

    }

    /**
     * 方法2
     * 在捕获异常里面调用 futurePrice.completeExceptionally(e)
     * 避免线程阻塞
     * @param product
     * @return
     */
    public Future<Double> getPriceAsync2(String product){

        CompletableFuture<Double> futurePrice = new CompletableFuture<>();

        new Thread(()->{

            try {
                double price = calculatePrice(product);
                futurePrice.complete(price);
            } catch (Exception e) {
                e.printStackTrace();
                // 抛出导致失败的异常
                futurePrice.completeExceptionally(e);
            }

        }).start();

        return futurePrice;

    }

    /**
     * supplyAsync方法接受一个Supplier 生产者作为参数
     * 返回一个CompletableFuture对象，该对象完成异步执行后，调用生产者方法的返回值
     * 方法作用同getPriceAsync2
     * @param product
     * @return
     */
    public Future<Double> getPriceAsync3(String product){

        return CompletableFuture.supplyAsync(()->calculatePrice(product));

    }

    private double calculatePrice(String product) {

        delay();
        if(true){

            throw new RuntimeException("product not available");
        }
        return ThreadLocalRandom.current().nextDouble();
    }

    private void delay() {
        try {
            Thread.sleep(1000L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    public static void main(String[] args) {

        getShopPrice();


//        getAllShopInfo();


    }

    List<CompletableFutureShop>  shops = Arrays.asList(new CompletableFutureShop("shop1"),
            new CompletableFutureShop("shop1"),
            new CompletableFutureShop("shop1"),
            new CompletableFutureShop("shop1"),
            new CompletableFutureShop("shop1")
    );



    public List<String> findPrice(String product){
       return shops.stream().map(s -> String.format("%s price  is %.2f", s.getName(),s.calculatePrice(product))).collect(toList());

    }
    public List<String> findPrice2(String product){
        //并行计算
        return shops.parallelStream().map(s->String.format("%s price is %.2f",s.getName(),s.calculatePrice(product))).collect(toList());
    }
    public List<String> findPrice3(String product){

        List<CompletableFuture<String>> completableFutures = shops.stream().map(s -> CompletableFuture.supplyAsync(() ->
                s.getName()+ s.calculatePrice(product))).collect(toList());
        // join 等待所有的异步操作结束
        return completableFutures.stream().map(CompletableFuture::join).collect(toList());


    }

  
    private static void getShopPrice() {
        CompletableFutureShop shop = new CompletableFutureShop();
        Future<Double> futurePrice = shop.getPriceAsync3("test1");

        //do other things

        try {
            Double aDouble = futurePrice.get();
            System.out.println("价格是"+aDouble);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }
}
