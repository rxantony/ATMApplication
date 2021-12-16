package com.dkatalist.atm.domain.application;

public interface MediaOutput {
    void writeln(String str);
    void writelnf(String format, Object... args);
}
