package com.yifeng.jvm.btrace;

import com.sun.btrace.AnyType;
import com.sun.btrace.BTraceUtils;
import com.sun.btrace.annotations.*;

/**
 * @author kevin
 * @version v1.0
 * @description
 * @date 2019-11-07 16:28
 **/
@BTrace
public class PrintSame {

    @OnMethod(
            clazz="com.yifeng.jvm.controller.BTraceController",
            method="same"
    )
    public static void anyRead(@ProbeClassName String pcn, @ProbeMethodName String pmn, String name, Integer id) {
        BTraceUtils.println(pcn+","+pmn+","+name+","+id);
        BTraceUtils.println();
    }
}
