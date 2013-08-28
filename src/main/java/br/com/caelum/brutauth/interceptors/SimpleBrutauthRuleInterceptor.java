package br.com.caelum.brutauth.interceptors;

import br.com.caelum.brutauth.auth.annotations.SimpleBrutauthRules;
import br.com.caelum.brutauth.verifier.SimpleBrutauthRulesVerifier;
import br.com.caelum.vraptor.InterceptionException;
import br.com.caelum.vraptor.Intercepts;
import br.com.caelum.vraptor.core.InterceptorStack;
import br.com.caelum.vraptor.interceptor.Interceptor;
import br.com.caelum.vraptor.resource.ResourceMethod;

@Intercepts
public class SimpleBrutauthRuleInterceptor implements Interceptor {
	
	private final SimpleBrutauthRulesVerifier verifier;

	public SimpleBrutauthRuleInterceptor(SimpleBrutauthRulesVerifier verifier) {
		this.verifier = verifier;
	}

	@Override
	public void intercept(InterceptorStack stack, ResourceMethod method,
			Object resourceInstance) throws InterceptionException {
		
		BrutauthClassOrMethod controllerType = new BrutauthClassOrMethod(method.getResource().getType());
		if(!verifier.rulesOfTypeAllows(controllerType)) return;
		
		BrutauthClassOrMethod methodType = new BrutauthClassOrMethod(method.getMethod());
		if(!verifier.rulesOfTypeAllows(methodType)) return;
		
		stack.next(method, resourceInstance);
	}

	@Override
	public boolean accepts(ResourceMethod method) {
		return method.containsAnnotation(SimpleBrutauthRules.class)  || method.getResource().getType().isAnnotationPresent(SimpleBrutauthRules.class);
	}

}
