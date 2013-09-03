package br.com.caelum.brutauth.reflection.methodsearchers;

import br.com.caelum.brutauth.auth.rules.CustomBrutauthRule;
import br.com.caelum.brutauth.reflection.BrutauthMethod;

public interface MethodSearcher {
	BrutauthMethod search(CustomBrutauthRule ruleToSearch, Argument...withArgs);
}
