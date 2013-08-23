package br.com.caelum.brutauth.auth.handlers;

import javax.inject.Inject;

import br.com.caelum.brutauth.auth.annotations.HandledBy;
import br.com.caelum.brutauth.auth.rules.BrutauthRule;
import br.com.caelum.vraptor4.controller.ControllerMethod;
import br.com.caelum.vraptor4.ioc.Container;

public class HandlerSearcher {

	private Container container;
	private ControllerMethod controllerMethod;

	@Deprecated // CDI eyes only
	public HandlerSearcher() {}

	@Inject
	public HandlerSearcher(Container container,
				ControllerMethod controllerMethod) {

		this.container = container;
		this.controllerMethod = controllerMethod;
	}

	public RuleHandler getHandler(BrutauthRule rule) {
		if(controllerMethodContainsSpecificHandler(controllerMethod)){
			HandledBy handledBy = controllerMethod.getMethod().getAnnotation(HandledBy.class);
			return container.instanceFor(handledBy.value());
		}

		if(ruleContainsSpecificHandler(rule)){
			HandledBy handledBy = rule.getClass().getAnnotation(HandledBy.class);
			return container.instanceFor(handledBy.value());
		}

		return container.instanceFor(AccessNotPermitedHandler.class);
	}

	private boolean controllerMethodContainsSpecificHandler(
			ControllerMethod controllerMethod) {
		return controllerMethod.getMethod().isAnnotationPresent(HandledBy.class);
	}

	private boolean ruleContainsSpecificHandler(BrutauthRule rule) {
		return rule.getClass().isAnnotationPresent(HandledBy.class);
	}

}
