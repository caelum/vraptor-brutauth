package br.com.caelum.brutauth.auth.handlers;

import static br.com.caelum.vraptor.view.Results.http;

import javax.inject.Inject;

import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.view.Results;

public class AccessNotAllowedHandler implements RuleHandler {

	private final Result result;

	/**
	 * @deprecated CDI eyes only
	*/
	public AccessNotAllowedHandler() {
		this(null);
	}
	
	@Inject
	public AccessNotAllowedHandler(Result result) {
		this.result = result;
	}
	
	@Override
	public void handle() {
		result.use(http()).sendError(403);
	}

}
