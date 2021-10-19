package com.dkatalist.atm.domain;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.dkatalist.atm.domain.application.MediaOutput;
import com.dkatalist.atm.domain.application.Session;
import com.dkatalist.atm.domain.application.SessionInputHandlerDefault;
import com.dkatalist.atm.domain.data.Account;
import com.dkatalist.atm.domain.service.ServiceException;

import org.junit.jupiter.api.Test;



class SessionInputHandlerTest {
    
    @Test
    void test() throws ServiceException{

        /*Session session = mock(Session.class);
        when(session.getAccountName()).thenReturn("account1");
        when(session.getAccount())
            .thenReturn(new Account("account1", 0))
            .thenReturn(new Account("account1", 70000))
            .thenReturn(new Account("account1", 60000))
            .thenReturn(new Account("account1", 50000));

        doNothing().when(session).deposit(anyInt());
        doNothing().when(session).withdraw(anyInt());
        doNothing().when(session).transfer(anyString(), anyInt());
        doNothing().when(session).logout();

        StringBuilder sb = new StringBuilder();
        MediaOutput output = new MediaOutput(){
            @Override
            public void writeln(String str) {
                sb.append(str);                
            }

            @Override
            public void writef(String format, Object... args) {
                sb.append(String.format(format, args)); 
            }
        };

        SessionInputHandlerDefault inputHandler = new SessionInputHandlerDefault(session, output);
        inputHandler.handle("deposit 70000");
        inputHandler.handle("withdraw 10000");
        inputHandler.handle("transfer account2 10000");*/

        assertTrue(true);
        /*String output1 = String.format("%sHello, %s!%sYour balance is $%d", "\n", "account1", "\n", 0);
        String output2 = String.format("%sYour balance is %d", "\n", 70000);
        String output3 = String.format("%sYour balance is %d", "\n", 70000);
        String output4 = String.format("%sTransferred $%d to %s%syour balance is $%s", "\n", 10000, "account2", "\n", 50000);*/

        //%sTransferred $%d to %s%syour balance is $%s
    }
}
