package br.com.caelum.brutauth.reflection.methodsearchers;

import java.lang.reflect.Method;

import javax.inject.Inject;

import br.com.caelum.brutauth.auth.rules.CustomBrutauthRule;
import br.com.caelum.brutauth.reflection.BrutauthMethod;

public class AdaptedMethodSearcher implements MethodSearcher {

	private DefaultMethodSearcher defaultMethodSearcher;

	/**
	 * @deprecated CDI eyes only
	 */
	public AdaptedMethodSearcher() {
		this(null);
	}
	
	@Inject
	public AdaptedMethodSearcher(DefaultMethodSearcher defaultMethodSearcher) {
		this.defaultMethodSearcher = defaultMethodSearcher;
	}
	
	@Override
	public BrutauthMethod search(CustomBrutauthRule ruleToSearch, Object... withArgs) {
		try {
			Method defaultMethod = defaultMethodSearcher.getMethod(ruleToSearch);
			Class<?>[] classes = defaultMethod.getParameterTypes();
			return new BrutauthMethod(getArgumentsThatMatchToTypes(classes, withArgs), defaultMethod, ruleToSearch);
		} catch (NoSuchMethodException e) {
			return null;
		}
	}

	private Object[] getArgumentsThatMatchToTypes(Class<?>[] types, Object[] args) throws NoSuchMethodException {
		Object[] argsToUse = new Object[types.length];
		for (int i = 0; i < types.length; i++) {
			for (Object arg : args) {
				if (arg != null && arg.getClass().isAssignableFrom(types[i])){
					argsToUse[i] = arg;
				}
			}
			if(argsToUse[i] == null) throw new IllegalArgumentException("Your resource method should recieve all the parameters that your rule needs: "+ types);
		}
		return argsToUse;
	}
}
