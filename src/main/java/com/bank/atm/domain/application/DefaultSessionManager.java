package com.bank.atm.domain.application;

import com.bank.atm.domain.common.Guard;
import com.bank.atm.domain.common.handler.HandlerExtensions;
import com.bank.atm.domain.common.handler.RequestHandlerManager;
import com.bank.atm.domain.service.account.command.createaccount.CreateAccountCommand;
import com.bank.atm.domain.service.account.query.getoptaccount.GetOptAccountQuery;

import lombok.experimental.ExtensionMethod;

@ExtensionMethod(HandlerExtensions.class)
public class DefaultSessionManager implements SessionManager {
	private Session activeSession;
	private final RequestHandlerManager manager;
	private final AbstractInputHandler inputHandler;
	private final SessionFactory sessionFactory;

	public DefaultSessionManager(RequestHandlerManager manager, SessionFactory sessionFactory,
			AbstractInputHandlerFactory<SessionManager> inputhandlerFactory) {

		Guard.validateArgNotNull(manager, "manager");
		Guard.validateArgNotNull(sessionFactory, "sessionFactory");
		Guard.validateArgNotNull(inputhandlerFactory, "inputhandlerFactory");

		this.manager = manager;
		this.sessionFactory = sessionFactory;
		this.inputHandler = inputhandlerFactory.create(this);
	}

	private void whenSessionLoggedOut(String accountName) {
		activeSession = null;
	}

	@Override
	public Session getSession() {
		return activeSession;
	}

	@Override
	public boolean hasActiveSession() {
		return activeSession != null;
	}

	@Override
	public AbstractInputHandler getInputHandler() {
		return inputHandler;
	}

	@Override
	public Session login(String userName) {
		var acc = manager.execute(new GetOptAccountQuery(userName))
				.orElseGet(() -> manager.execute(new CreateAccountCommand(userName, 0)));
		activeSession = sessionFactory.create(acc.getName(), this::whenSessionLoggedOut);
		return activeSession;
	}

	@Override
	public void close() throws Exception {
		if (activeSession != null)
			activeSession.close();
	}
}
