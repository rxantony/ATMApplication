package com.bank.atm.domain.application;

public interface MediaOutput extends AutoCloseable {
	MediaOutput writeln(String format, Object... args);
}
