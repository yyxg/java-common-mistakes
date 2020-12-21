package org.geekbang.time.commonmistakes.exception.threadpoolandexception;

/**
 * @Author xialh
 * @Date 2020/10/15 8:37 AM
 */

    public class Test {
        public static int num=1;
        public static void main(String[] args){
            int result;
            result = num();
            System.out.println(result);//结果不受finally影响，输出4
            System.out.println(num);//5
        }
        private static int num() {
            try{
                int b=4/0;
                return  num+2;
            }catch(Exception e){
                return num+3;
            }finally {
                ++num;
            }
        }
    }
