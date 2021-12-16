package com.dkatalist.atm;

import com.dkatalist.atm.domain.application.MediaInput;
import com.dkatalist.atm.domain.application.MediaOutput;

public interface ATMMachineRunner {
    void runATMMachine(MediaInput inputReader, MediaOutput inputWriter);
}
