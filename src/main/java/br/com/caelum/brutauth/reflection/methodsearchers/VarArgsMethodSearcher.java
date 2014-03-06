package br.com.caelum.brutauth.reflection.methodsearchers;

import javax.inject.Inject;

import br.com.caelum.brutauth.auth.rules.CustomBrutauthRule;
import br.com.caelum.brutauth.reflection.Argument;
import br.com.caelum.brutauth.reflection.BrutauthMethod;
import br.com.caelum.brutauth.reflection.NamedParametersMethod;

public class VarArgsMethodSearcher implements MethodSearcher {

	private DefaultMethodSearcher defaultMethodSearcher;

	@Deprecated // CDI eyes only
	public VarArgsMethodSearcher() {}

	@Inject
	public VarArgsMethodSearcher(DefaultMethodSearcher defaultMethodSearcher) {
		this.defaultMethodSearcher = defaultMethodSearcher;
	}

	@Override
	public BrutauthMethod search(CustomBrutauthRule ruleToSearch,
			Argument... withArgs) {
		try {
			NamedParametersMethod defaultMethod = defaultMethodSearcher.getMethod(ruleToSearch);
			return new BrutauthMethod(fakeVarArgs(withArgs), defaultMethod.getMethod(), ruleToSearch);
		} catch (NoSuchMethodException e) {
			return null;
		}
	}

	private Object[] fakeVarArgs(Object[] args) {
		if(args == null) return fakeVarArgs(new Object[]{args});
		return new Object[]{args};
	}
}
