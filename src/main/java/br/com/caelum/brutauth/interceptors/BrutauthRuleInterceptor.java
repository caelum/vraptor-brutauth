package br.com.caelum.brutauth.interceptors;

import javax.inject.Inject;

import br.com.caelum.brutauth.auth.annotations.Public;
import br.com.caelum.brutauth.verifier.BrutauthRulesVerifiers;
import br.com.caelum.vraptor.InterceptionException;
import br.com.caelum.vraptor.Intercepts;
import br.com.caelum.vraptor.controller.ControllerMethod;
import br.com.caelum.vraptor.core.InterceptorStack;
import br.com.caelum.vraptor.interceptor.Interceptor;

@Intercepts
public class BrutauthRuleInterceptor implements Interceptor {
	
	private BrutauthRulesVerifiers verifiers;

	/**
	 * @deprecated CDI eyes only
	 */
	protected BrutauthRuleInterceptor() {
	}
	
	@Inject 
	public BrutauthRuleInterceptor(BrutauthRulesVerifiers verifiers) {
		this.verifiers = verifiers;
	}

	@Override
	public void intercept(InterceptorStack stack, ControllerMethod method,
			Object controllerInstance) throws InterceptionException {
		
		BrutauthClassOrMethod controllerType = new BrutauthClassOrMethod(method.getController().getType());
		if(!verifiers.verify(controllerType)) return;
		
		BrutauthClassOrMethod methodtype = new BrutauthClassOrMethod(method.getMethod());
		if(!verifiers.verify(methodtype)) return;
		
		stack.next(method, controllerInstance);
	}
	
	@Override
	public boolean accepts(ControllerMethod method) {
		return !method.containsAnnotation(Public.class)
				&& !method.getController().getType().isAnnotationPresent(Public.class); 
	}
}