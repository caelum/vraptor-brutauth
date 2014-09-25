package br.com.caelum.brutauth.auth.annotations;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;

import javax.inject.Qualifier;

@Retention(RUNTIME)
@Qualifier
public @interface DefaultRule {

}
