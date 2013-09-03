package br.com.caelum.brutauth.reflection;

import br.com.caelum.brutauth.auth.rules.CustomBrutauthRule;
import br.com.caelum.brutauth.reflection.methodsearchers.Argument;

public interface MethodInvoker {

	boolean invoke(CustomBrutauthRule toInvoke, Argument[] args);

}