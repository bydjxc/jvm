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
public class PrintConstructor {

    @OnMethod(
            clazz="com.yifeng.jvm.pojo.User",
            method="<init>",
            location=@Location(Kind.ENTRY)
    )
    public static void anyRead(@ProbeClassName String pcn, @ProbeMethodName String pmn, AnyType[] args) {
        BTraceUtils.printArray(args);
        BTraceUtils.println(pcn+","+pmn);
        BTraceUtils.println();
    }
}
