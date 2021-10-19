package com.dkatalist.atm.domain.application;

public interface MediaOutput {
    void writeln(String str);
    void writef(String format, Object... args);
}
