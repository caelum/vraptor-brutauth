package br.com.caelum.brutauth.reflection.methodsearchers;

import java.lang.reflect.Method;

import javax.inject.Inject;

import br.com.caelum.brutauth.auth.rules.CustomBrutauthRule;
import br.com.caelum.brutauth.reflection.Arguments;
import br.com.caelum.brutauth.reflection.BrutauthMethod;

public class VarArgsMethodSearcher implements MethodSearcher {

	private DefaultMethodSearcher defaultMethodSearcher;

	@Deprecated // CDI eyes only
	public VarArgsMethodSearcher() {}

	@Inject
	public VarArgsMethodSearcher(DefaultMethodSearcher defaultMethodSearcher) {
		this.defaultMethodSearcher = defaultMethodSearcher;
	}

	@Override
	public BrutauthMethod search(CustomBrutauthRule ruleToSearch, Argument... withArgs) {
		Object[] argsToUse = new Arguments(withArgs).toValuesOnly();
		try {
			Method defaultMethod = defaultMethodSearcher.getMethod(ruleToSearch);
			return new BrutauthMethod(fakeVarArgs(argsToUse), defaultMethod, ruleToSearch);
		} catch (NoSuchMethodException e) {
			return null;
		}
	}

	private Object[] fakeVarArgs(Object[] args) {
		return new Object[]{args};
	}
}
