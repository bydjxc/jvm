package com.yifeng.jvm.btrace;

import com.sun.btrace.AnyType;
import com.sun.btrace.BTraceUtils;
import com.sun.btrace.annotations.BTrace;
import com.sun.btrace.annotations.Kind;
import com.sun.btrace.annotations.Location;
import com.sun.btrace.annotations.OnMethod;
import com.sun.btrace.annotations.ProbeClassName;
import com.sun.btrace.annotations.ProbeMethodName;

/**
 * @author kevin
 * @version v1.0
 * @description
 * @date 2019-11-07 16:28
 **/
@BTrace
public class PrintArgSimple {

    @OnMethod(
            clazz="com.yifeng.jvm.controller.BTraceController",
            method="arg1",
            location=@Location(Kind.ENTRY)
    )
    public static void anyRead(@ProbeClassName String pcn, @ProbeMethodName String pmn, AnyType[] args) {
        BTraceUtils.printArray(args);
        BTraceUtils.println(pcn+","+pmn);
        BTraceUtils.println();
    }
}
