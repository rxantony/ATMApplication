package com.dkatalist.atm;

import com.dkatalist.atm.domain.application.MediaInput;
import com.dkatalist.atm.domain.application.MediaOutput;

public interface ATMMachineRunner {
    void run(MediaInput inputReader, MediaOutput inputWriter);
}
