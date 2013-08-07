package br.com.caelum.brutauth.auth.handlers;

import static br.com.caelum.vraptor4.view.Results.http;

import javax.inject.Inject;

import br.com.caelum.vraptor4.Result;

public class AccessNotPermitedHandler implements RuleHandler {

	@Inject private Result result;

	@Override
	public void handle() {
		result.use(http()).sendError(403);
	}

}
