package br.com.caelum.brutauth.interceptors;

import br.com.caelum.brutauth.auth.rules.CustomBrutauthRule;

public class MyCustomRule implements CustomBrutauthRule {
	public boolean isAllowed(String myString) {
		return MyController.MY_STRING.equals(myString);
	}
}
