package br.com.caelum.brutauth.reflection.methodsearchers;

import java.lang.reflect.Method;

import br.com.caelum.brutauth.auth.rules.CustomBrutauthRule;
import br.com.caelum.brutauth.reflection.BrutauthValidation;

public class DefaultMethodSearcher {

	public Method getMethod(CustomBrutauthRule toInvoke) throws NoSuchMethodException {
		Method[] methods = toInvoke.getClass().getMethods();
		for (Method method : methods) {
			if(isDefaultMethod(method)) return method; 
		}
		throw new NoSuchMethodException("Your rule should have a method with name 'isAllowed' or annotated with '@" + BrutauthValidation.class.getSimpleName() + "' and return boolean.  Rule: "+ toInvoke.getClass());
	}

	private boolean isDefaultMethod(Method method) {
		boolean returnsBoolean = method.getReturnType().isAssignableFrom(boolean.class);
		return returnsBoolean && (method.getName().equals("isAllowed") || method.isAnnotationPresent(BrutauthValidation.class));
	}

}
