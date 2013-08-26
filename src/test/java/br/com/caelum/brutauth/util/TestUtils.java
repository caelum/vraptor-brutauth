package br.com.caelum.brutauth.util;

import net.vidageek.mirror.dsl.Mirror;
import br.com.caelum.vraptor.resource.DefaultResourceClass;
import br.com.caelum.vraptor.resource.DefaultResourceMethod;
import br.com.caelum.vraptor.resource.ResourceMethod;

public class TestUtils {

	public static ResourceMethod method(Class<?> clazz, String method) {
		return new DefaultResourceMethod(
				new DefaultResourceClass(clazz),
				new Mirror().on(clazz).reflect().method(method).withAnyArgs());
	}

}
