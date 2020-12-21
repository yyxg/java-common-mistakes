package org.geekbang.time.commonmistakes.clientdata.trustclientuserid;

import java.lang.annotation.*;

/**
 * @author xialihui
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.PARAMETER)
@Documented
public @interface LoginRequired {
    String sessionKey() default "currentUser";
}