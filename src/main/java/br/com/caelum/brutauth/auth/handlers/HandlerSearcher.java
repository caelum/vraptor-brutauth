package br.com.caelum.brutauth.auth.handlers;

import br.com.caelum.brutauth.auth.annotations.HandledBy;
import br.com.caelum.brutauth.auth.rules.BrutauthRule;
import br.com.caelum.vraptor.core.MethodInfo;
import br.com.caelum.vraptor.ioc.Component;
import br.com.caelum.vraptor.ioc.Container;
import br.com.caelum.vraptor.resource.ResourceMethod;

@Component
public class HandlerSearcher {
	private final Container container;
	private ResourceMethod resourceMethod;

	public HandlerSearcher(Container container, MethodInfo methodInfo) {
		this.container = container;
		resourceMethod = methodInfo.getResourceMethod();
	}
	
	public RuleHandler getHandler(BrutauthRule rule) {
		if(resourceMethodContainsSpecificHandler(resourceMethod)){
			HandledBy handledBy = resourceMethod.getMethod().getAnnotation(HandledBy.class);
			return container.instanceFor(handledBy.value());
		}
	
		if(ruleContainsSpecificHandler(rule)){
			HandledBy handledBy = rule.getClass().getAnnotation(HandledBy.class);
			return container.instanceFor(handledBy.value());		
		}
		
		return container.instanceFor(AccessNotAllowedHandler.class);
	}
	
	private boolean resourceMethodContainsSpecificHandler(
			ResourceMethod resourceMethod) {
		return resourceMethod.getMethod().isAnnotationPresent(HandledBy.class);
	}

	private boolean ruleContainsSpecificHandler(BrutauthRule rule) {
		return rule.getClass().isAnnotationPresent(HandledBy.class);
	}

}
