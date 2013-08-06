package br.com.caelum.brutauth.auth.handlers;

import static br.com.caelum.vraptor.view.Results.http;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.ioc.Component;

@Component
public class AccessNotAllowedHandler implements RuleHandler{
	private final Result result;

	public AccessNotAllowedHandler(Result result) {
		this.result = result;
	}
	
	@Override
	public void handle() {
		result.use(http()).sendError(403);
	}

}
