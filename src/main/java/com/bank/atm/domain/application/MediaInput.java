package com.bank.atm.domain.application;

public interface MediaInput extends AutoCloseable {
    String readLine();
}
