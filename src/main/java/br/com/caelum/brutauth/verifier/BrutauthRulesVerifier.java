package br.com.caelum.brutauth.verifier;

import java.lang.annotation.Annotation;

import br.com.caelum.brutauth.interceptors.BrutauthClassOrMethod;

public interface BrutauthRulesVerifier {
	boolean canVerify(Class<? extends Annotation> annotation);
	boolean rulesOfTypeAllows(BrutauthClassOrMethod type);
}
