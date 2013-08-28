package br.com.caelum.brutauth.util;

import java.lang.reflect.Method;

import net.vidageek.mirror.dsl.Mirror;
import br.com.caelum.brutauth.interceptors.BrutauthClassOrMethod;
import br.com.caelum.vraptor.resource.DefaultResourceClass;
import br.com.caelum.vraptor.resource.DefaultResourceMethod;
import br.com.caelum.vraptor.resource.ResourceMethod;

public class TestUtils {

	public static ResourceMethod method(Class<?> clazz, String method) {
		return new DefaultResourceMethod(
				new DefaultResourceClass(clazz),
				getMethod(clazz, method));
	}

	public static BrutauthClassOrMethod brutauthMethod(Class<?> clazz, String method) {
		return new BrutauthClassOrMethod(getMethod(clazz, method));
	}
	
	private static Method getMethod(Class<?> clazz, String method) {
		return new Mirror().on(clazz).reflect().method(method).withAnyArgs();
	}
}
