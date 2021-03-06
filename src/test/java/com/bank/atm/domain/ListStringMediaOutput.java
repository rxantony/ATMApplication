package com.bank.atm.domain;

import java.util.ArrayList;
import java.util.List;

import com.bank.atm.domain.application.MediaOutput;

public class ListStringMediaOutput implements MediaOutput{
    public final List<String> strings = new ArrayList<>();

    @Override
    public void writeln(String str) {
        strings.add(str);                
    }

    @Override
    public void writelnf(String format, Object... args) {
        strings.add(String.format(format, args)); 
    }

    @Override
    public void close() throws Exception {
    }
};
