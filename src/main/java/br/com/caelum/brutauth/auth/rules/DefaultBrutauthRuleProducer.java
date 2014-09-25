package br.com.caelum.brutauth.auth.rules;

import javax.enterprise.event.Observes;
import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.AnnotatedType;
import javax.enterprise.inject.spi.Extension;
import javax.enterprise.inject.spi.ProcessAnnotatedType;
import javax.inject.Inject;

import br.com.caelum.brutauth.auth.annotations.DefaultRule;
import br.com.caelum.vraptor.ioc.Container;

public class DefaultBrutauthRuleProducer implements Extension{

	@Inject private Container container;
	@Inject private BrutauthDefaultRule brutauthDefaultRule;
	private Class<? extends AnnotatedType> usersDefaultRule;
	
	public <T> void processAnnotatedType(@Observes ProcessAnnotatedType<T> type) {
		AnnotatedType<T> componentType = type.getAnnotatedType();
		boolean isAnnotated = componentType.isAnnotationPresent(DefaultRule.class);
		if(isAnnotated) usersDefaultRule = componentType.getClass();
	}
	
	@Produces @DefaultRule
	public BrutauthRule getInstance(){
		if(usersDefaultRule != null) return (BrutauthRule) container.instanceFor(usersDefaultRule);
		return brutauthDefaultRule;
	}
}
