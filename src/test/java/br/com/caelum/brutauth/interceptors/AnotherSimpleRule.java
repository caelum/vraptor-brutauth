package br.com.caelum.brutauth.interceptors;

import br.com.caelum.brutauth.auth.rules.SimpleBrutauthRule;

public class AnotherSimpleRule implements SimpleBrutauthRule {
	@Override
	public boolean isAllowed(long accessLevel) {
		return true;
	}

}
