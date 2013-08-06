package br.com.caelum.brutauth.interceptors;

import br.com.caelum.brutauth.auth.rules.CustomBrutauthRule;

public class AnotherCustomRule implements CustomBrutauthRule {
	public boolean isAllowed(String myString) {
		return !myString.contains("Brutauth");
	}
}
