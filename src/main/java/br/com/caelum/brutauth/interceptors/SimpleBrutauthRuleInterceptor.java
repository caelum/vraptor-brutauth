package br.com.caelum.brutauth.interceptors;

import java.util.ArrayList;
import java.util.Arrays;

import br.com.caelum.brutauth.auth.annotations.AccessLevel;
import br.com.caelum.brutauth.auth.annotations.SimpleBrutauthRules;
import br.com.caelum.brutauth.auth.handlers.HandlerSearcher;
import br.com.caelum.brutauth.auth.handlers.RuleHandler;
import br.com.caelum.brutauth.auth.rules.SimpleBrutauthRule;
import br.com.caelum.vraptor.InterceptionException;
import br.com.caelum.vraptor.Intercepts;
import br.com.caelum.vraptor.core.InterceptorStack;
import br.com.caelum.vraptor.interceptor.Interceptor;
import br.com.caelum.vraptor.ioc.Container;
import br.com.caelum.vraptor.resource.ResourceMethod;

@Intercepts
public class SimpleBrutauthRuleInterceptor implements Interceptor {
	
	private Container container;
	private final HandlerSearcher handlers;

	public SimpleBrutauthRuleInterceptor(Container container, HandlerSearcher handlers) {
		this.container = container;
		this.handlers = handlers;
	}

	@Override
	public void intercept(InterceptorStack stack, ResourceMethod method,
			Object resourceInstance) throws InterceptionException {
		
		ArrayList<Class<? extends SimpleBrutauthRule>> rules = new ArrayList<>();
		
		Class<?> controllerType = method.getResource().getType();
		if (containsAnnotation(controllerType)) {
			SimpleBrutauthRules annotation = controllerType.getAnnotation(SimpleBrutauthRules.class);
			rules.addAll(Arrays.asList(annotation.value()));
		}
		
		if (method.containsAnnotation(SimpleBrutauthRules.class)) {
			SimpleBrutauthRules annotation = method.getMethod().getAnnotation(SimpleBrutauthRules.class);
			rules.addAll(Arrays.asList(annotation.value()));
		}
		
		long permissionData = 0l;
		Arrays.asList(method.getMethod().getAnnotations());
		if (method.containsAnnotation(AccessLevel.class)) {
			permissionData = method.getMethod().getAnnotation(AccessLevel.class).value();
		}
		for (Class<? extends SimpleBrutauthRule> permission : rules) {
			SimpleBrutauthRule rule = container.instanceFor(permission);
			RuleHandler handler = handlers.getHandler(rule);
			if(!rule.isAllowed(permissionData)){
				handler.handle();
				return;
			}
		}
		stack.next(method, resourceInstance);
	}

	private boolean containsAnnotation(Class<?> type) {
		return type.isAnnotationPresent(SimpleBrutauthRules.class);
	}
	
	@Override
	public boolean accepts(ResourceMethod method) {
		return method.containsAnnotation(SimpleBrutauthRules.class);
	}

}
