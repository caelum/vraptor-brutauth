package br.com.caelum.brutauth.reflection.methodsearchers;

import br.com.caelum.brutauth.auth.rules.CustomBrutauthRule;
import br.com.caelum.brutauth.reflection.Argument;
import br.com.caelum.brutauth.reflection.BrutauthMethod;
import br.com.caelum.vraptor.http.ValuedParameter;

public interface MethodSearcher {
	BrutauthMethod search(CustomBrutauthRule ruleToSearch, Argument... arguments);
}
