package br.com.caelum.brutauth.reflection;

import br.com.caelum.brutauth.auth.rules.BrutauthRule;


public interface MethodInvoker {
	boolean invoke(BrutauthRule toInvoke, Argument[] args);
	boolean canInvoke(Class<? extends BrutauthRule> brutauthRule);
}