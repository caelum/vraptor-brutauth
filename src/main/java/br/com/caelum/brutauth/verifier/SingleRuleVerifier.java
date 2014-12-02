package br.com.caelum.brutauth.verifier;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;

import br.com.caelum.brutauth.auth.handlers.HandlerSearcher;
import br.com.caelum.brutauth.auth.rules.BrutauthRule;
import br.com.caelum.brutauth.reflection.Argument;
import br.com.caelum.brutauth.reflection.MethodInvokers;

@Dependent
public class SingleRuleVerifier {

	private MethodInvokers invokers;
	private HandlerSearcher handlers;
	
	@Inject
	public SingleRuleVerifier(MethodInvokers invokers, HandlerSearcher handlers) {
		this.invokers = invokers;
		this.handlers = handlers;
	}

	public boolean verify(BrutauthRule brutauthRule, Argument[] arguments) {
		boolean allowed = invokers.invoke(brutauthRule,  arguments);
		if(!allowed) handlers.getHandler(brutauthRule).handle();
		return allowed;
	}

}
