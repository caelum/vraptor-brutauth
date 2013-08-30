package br.com.caelum.brutauth.auth.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import br.com.caelum.brutauth.auth.rules.SimpleBrutauthRule;

@Retention(RetentionPolicy.RUNTIME)
public @interface SimpleBrutauthRules {

	Class<? extends SimpleBrutauthRule>[] value();
	long accessLevel() default 0l;

}
