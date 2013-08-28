package br.com.caelum.brutauth.auth.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface AccessLevel {

	long value();

}
