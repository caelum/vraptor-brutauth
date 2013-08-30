package br.com.caelum.brutauth.util;

import java.lang.reflect.Method;

import net.vidageek.mirror.dsl.Mirror;
import br.com.caelum.brutauth.interceptors.BrutauthClassOrMethod;
import br.com.caelum.vraptor4.controller.ControllerMethod;
import br.com.caelum.vraptor4.controller.DefaultBeanClass;
import br.com.caelum.vraptor4.controller.DefaultControllerMethod;

public class TestUtils {

	public static ControllerMethod method(Class<?> clazz, String method) {
		return new DefaultControllerMethod(
				new DefaultBeanClass(clazz),
				getMethod(clazz, method));
	}

	public static BrutauthClassOrMethod brutauthMethod(Class<?> clazz, String method) {
		return new BrutauthClassOrMethod(getMethod(clazz, method));
	}
	
	private static Method getMethod(Class<?> clazz, String method) {
		return new Mirror().on(clazz).reflect().method(method).withAnyArgs();
	}
}
