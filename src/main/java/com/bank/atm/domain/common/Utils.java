package com.bank.atm.domain.common;

import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class Utils {
    private Utils(){}
    
    public static <T> Stream<T> streamFrom(Iterable<T> it){
        return streamFrom(it, false);
    }

    public static <T> Stream<T> streamFrom(Iterable<T> it, boolean parallel){
        return StreamSupport.stream(it.spliterator(), parallel);
    }
}
