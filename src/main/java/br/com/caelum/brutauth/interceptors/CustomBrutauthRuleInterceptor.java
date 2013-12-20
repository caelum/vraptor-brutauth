package br.com.caelum.brutauth.interceptors;

import javax.inject.Inject;

import br.com.caelum.brutauth.auth.annotations.CustomBrutauthRules;
import br.com.caelum.brutauth.verifier.CustomBrutauthRulesVerifier;
import br.com.caelum.vraptor.InterceptionException;
import br.com.caelum.vraptor.Intercepts;
import br.com.caelum.vraptor.controller.ControllerMethod;
import br.com.caelum.vraptor.core.InterceptorStack;
import br.com.caelum.vraptor.interceptor.Interceptor;

@Intercepts
public class CustomBrutauthRuleInterceptor implements Interceptor {

	private CustomBrutauthRulesVerifier verifier;
	
	/**
	 * @deprecated CDI eyes only
	 */
	public CustomBrutauthRuleInterceptor() {
	}
	
	@Inject 
	public CustomBrutauthRuleInterceptor(CustomBrutauthRulesVerifier verifier) {
		this.verifier = verifier;
	}

	@Override
	public void intercept(InterceptorStack stack, ControllerMethod method,
			Object controllerInstance) throws InterceptionException {

		BrutauthClassOrMethod controllerType = new BrutauthClassOrMethod(method.getController().getType());
		if(!verifier.rulesOfTypeAllows(controllerType)) return;
		
		BrutauthClassOrMethod methodtype = new BrutauthClassOrMethod(method.getMethod());
		if(!verifier.rulesOfTypeAllows(methodtype)) return;
		
		stack.next(method, controllerInstance);
	}
	
	@Override
	public boolean accepts(ControllerMethod method) {
		return method.containsAnnotation(CustomBrutauthRules.class) || method.getController().getType().isAnnotationPresent(CustomBrutauthRules.class);
	}
}