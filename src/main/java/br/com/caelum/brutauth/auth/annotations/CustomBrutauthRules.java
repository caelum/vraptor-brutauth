package br.com.caelum.brutauth.auth.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import br.com.caelum.brutauth.auth.rules.CustomBrutauthRule;

@Retention(RetentionPolicy.RUNTIME)
public @interface CustomBrutauthRules {
	Class<? extends CustomBrutauthRule>[] value() ;
}
