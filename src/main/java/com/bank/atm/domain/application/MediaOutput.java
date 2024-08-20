package com.bank.atm.domain.application;

public interface MediaOutput extends AutoCloseable {
	MediaOutput writeln(String str);

	MediaOutput writelnf(String format, Object... args);
}
