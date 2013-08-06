package br.com.caelum.brutauth.interceptors;

import br.com.caelum.brutauth.auth.handlers.RuleHandler;

public class MySimpleRuleHandler implements RuleHandler {
	@Override
	public boolean handle(boolean isAllowed) {
		return true;
	}
}
