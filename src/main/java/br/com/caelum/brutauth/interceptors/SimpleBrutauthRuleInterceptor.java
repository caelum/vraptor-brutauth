package br.com.caelum.brutauth.interceptors;

import javax.inject.Inject;

import br.com.caelum.brutauth.auth.annotations.SimpleBrutauthRules;
import br.com.caelum.brutauth.verifier.SimpleBrutauthRulesVerifier;
import br.com.caelum.vraptor.InterceptionException;
import br.com.caelum.vraptor.Intercepts;
import br.com.caelum.vraptor.controller.ControllerMethod;
import br.com.caelum.vraptor.core.InterceptorStack;
import br.com.caelum.vraptor.interceptor.Interceptor;

@Intercepts
public class SimpleBrutauthRuleInterceptor implements Interceptor {
	private SimpleBrutauthRulesVerifier verifier;
	
	/**
	 * @deprecated CDI eyes only
	 */
	public SimpleBrutauthRuleInterceptor() {
		this(null);
	}
	
	@Inject 
	public SimpleBrutauthRuleInterceptor(SimpleBrutauthRulesVerifier verifier) {
		this.verifier = verifier;
	}
	
	@Override
	public void intercept(InterceptorStack stack, ControllerMethod method,
			Object controllerInstance) throws InterceptionException {
		
		BrutauthClassOrMethod controllerType = new BrutauthClassOrMethod(method.getController().getType());
		if(!verifier.rulesOfTypeAllows(controllerType)) return;
		
		BrutauthClassOrMethod methodType = new BrutauthClassOrMethod(method.getMethod());
		if(!verifier.rulesOfTypeAllows(methodType)) return;
		
		stack.next(method, controllerInstance);
		
	}

	@Override
	public boolean accepts(ControllerMethod method) {
		return method.containsAnnotation(SimpleBrutauthRules.class)  || method.getController().getType().isAnnotationPresent(SimpleBrutauthRules.class);
	}
	
	

}
