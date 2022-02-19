package com.bank.atm.domain;

import java.util.ArrayList;
import java.util.List;

import com.bank.atm.domain.application.MediaInput;

public class ListStringMediaInput implements MediaInput {
    int idx;
    public final List<String> strings = new ArrayList<>();
    @Override
    public String readLine() {
        return strings.get(idx++);
    }
    
}
