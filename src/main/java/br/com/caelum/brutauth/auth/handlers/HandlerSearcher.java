package br.com.caelum.brutauth.auth.handlers;

import javax.inject.Inject;

import br.com.caelum.brutauth.auth.annotations.HandledBy;
import br.com.caelum.brutauth.auth.rules.BrutauthRule;
import br.com.caelum.vraptor.controller.BeanClass;
import br.com.caelum.vraptor.controller.ControllerMethod;
import br.com.caelum.vraptor.ioc.Container;
import br.com.caelum.vraptor.proxy.CDIProxies;

public class HandlerSearcher {

	private Container container;
	private ControllerMethod controllerMethod;

	/**
	 * @deprecated CDI eyes only.
	 */
	protected HandlerSearcher() {
		this(null, null);
	}

	@Inject
	public HandlerSearcher(Container container, ControllerMethod controllerMethod) {
		this.container = container;
		this.controllerMethod = controllerMethod;
	}

	public RuleHandler getHandler(BrutauthRule rule) {
		if(controllerMethodContainsSpecificHandler(controllerMethod)){
			HandledBy handledBy = controllerMethod.getMethod().getAnnotation(HandledBy.class);
			return container.instanceFor(handledBy.value());
		}

		BeanClass resource = controllerMethod.getController();
		if(resourceClassContainsSpecificHandler(resource)){
			HandledBy handledBy = resource.getType().getAnnotation(HandledBy.class);
			return container.instanceFor(handledBy.value());
		}

        Class<?> ruleClass = rule.getClass();
        ruleClass = CDIProxies.extractRawTypeIfPossible(ruleClass);

		if(ruleContainsSpecificHandler(ruleClass)){
            HandledBy handledBy = ruleClass.getAnnotation(HandledBy.class);
			return container.instanceFor(handledBy.value());
		}

		return container.instanceFor(AccessNotAllowedHandler.class);
	}

	private boolean controllerMethodContainsSpecificHandler(
			ControllerMethod controllerMethod) {
		return controllerMethod.getMethod().isAnnotationPresent(HandledBy.class);
	}

	private boolean resourceClassContainsSpecificHandler(
			BeanClass beanClass) {
		return beanClass.getType().isAnnotationPresent(HandledBy.class);
	}

	private boolean ruleContainsSpecificHandler(Class<?> ruleClass) {
        return ruleClass.isAnnotationPresent(HandledBy.class);
	}

}
