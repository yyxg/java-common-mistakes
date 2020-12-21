package org.geekbang.time.commonmistakes.exception.predefinedexception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequestMapping("predefinedexception")
public class PredefinedExceptionController {

    @GetMapping("wrong")
    public static void wrong() {
        try {
            createOrderWrong();
        } catch (Exception ex) {
            log.error("createOrder got error", ex);
        }
        try {
            cancelOrderWrong();
        } catch (Exception ex) {
            log.error("cancelOrder got error", ex);
        }
    }

    public static void main(String[] args) {
        wrong();
    }
    @GetMapping("right")
    public void right() {

        try {
            createOrderRight();
        } catch (Exception ex) {
            log.error("createOrder got error", ex);
        }
        try {
            cancelOrderRight();
        } catch (Exception ex) {
            log.error("cancelOrder got error", ex);
        }
    }

    private static void  createOrderWrong() {
        throw Exceptions.ORDEREXISTS;
    }

    private static void cancelOrderWrong() {
        throw Exceptions.ORDEREXISTS;
    }

    private void createOrderRight() {
        throw Exceptions.orderExists();
    }

    private void cancelOrderRight() {
        throw Exceptions.orderExists();
    }
}
