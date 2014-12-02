package br.com.caelum.brutauth.auth.handlers;

import static br.com.caelum.vraptor.view.Results.http;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;

import br.com.caelum.vraptor.Result;

@Dependent
public class AccessNotAllowedHandler implements RuleHandler {

	private final Result result;

	@Inject
	public AccessNotAllowedHandler(Result result) {
		this.result = result;
	}
	
	@Override
	public void handle() {
		result.use(http()).sendError(403);
	}

}
