package br.com.caelum.brutauth.verifier;

import br.com.caelum.brutauth.interceptors.BrutauthClassOrMethod;

public interface BrutauthRulesVerifier {
	boolean rulesOfTypeAllows(BrutauthClassOrMethod type);
}
