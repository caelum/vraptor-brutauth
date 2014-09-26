package br.com.caelum.brutauth.auth.rules;

import java.util.Iterator;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Instance;
import javax.enterprise.inject.spi.CDI;
import javax.enterprise.util.AnnotationLiteral;

import br.com.caelum.brutauth.auth.annotations.DefaultRule;

@ApplicationScoped
public class DefaultBrutauthRuleProducer{

	public BrutauthRule getInstance(){
		Instance<Object> select = CDI.current().select(new AnnotationLiteral<DefaultRule>() {});
		Iterator<Object> iterator = select.iterator();
		return iterator.hasNext() ? (BrutauthRule) iterator.next() : null;
	}
}
