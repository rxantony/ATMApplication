package com.bank.atm.domain.application;

public interface MediaOutput extends AutoCloseable {
    void writeln(String str);
    void writelnf(String format, Object... args);
}
