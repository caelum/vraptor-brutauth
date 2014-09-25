package br.com.caelum.brutauth.util;

import java.lang.reflect.Method;

import net.vidageek.mirror.dsl.Mirror;
import br.com.caelum.brutauth.auth.annotations.CustomBrutauthRules;
import br.com.caelum.brutauth.auth.annotations.SimpleBrutauthRules;
import br.com.caelum.brutauth.auth.rules.BrutauthRule;
import br.com.caelum.brutauth.interceptors.BrutauthClassOrMethod;
import br.com.caelum.brutauth.reflection.Argument;
import br.com.caelum.vraptor.controller.ControllerMethod;
import br.com.caelum.vraptor.controller.DefaultBeanClass;
import br.com.caelum.vraptor.controller.DefaultControllerMethod;

public class TestUtils {

	public static ControllerMethod method(Class<?> clazz, String method) {
		return new DefaultControllerMethod(
				new DefaultBeanClass(clazz),
				getMethod(clazz, method));
	}

	public static BrutauthClassOrMethod brutauthMethod(Class<?> clazz, String method) {
		return new BrutauthClassOrMethod(getMethod(clazz, method));
	}
	
	public static Class<? extends BrutauthRule>[] customRulesFor(BrutauthClassOrMethod brutauthMethod) {
		return brutauthMethod.getAnnotation(CustomBrutauthRules.class).value();
	}
	
	public static Class<? extends BrutauthRule>[] simpleRulesFor(BrutauthClassOrMethod brutauthMethod) {
		return brutauthMethod.getAnnotation(SimpleBrutauthRules.class).value();
	}

	private static Method getMethod(Class<?> clazz, String method) {
		return new Mirror().on(clazz).reflect().method(method).withAnyArgs();
	}

	public static Argument[] singleArgument(String name, Object value) {
		return new Argument[]{new Argument(name, value)};
	}
	
	public static Argument[] singleArgument(Object value) {
		return singleArgument("", value);
	}
	

}
