package br.com.caelum.brutauth.reflection;

import static java.util.Arrays.asList;

import java.lang.reflect.Method;

import javax.enterprise.inject.Vetoed;

import net.vidageek.mirror.dsl.ClassController;
import net.vidageek.mirror.dsl.Mirror;
import net.vidageek.mirror.list.dsl.Matcher;
import net.vidageek.mirror.list.dsl.MirrorList;
import br.com.caelum.brutauth.auth.rules.CustomBrutauthRule;

@Vetoed
public class BrutauthMethod {

	private final Method methodToInvoke;
	private Object[] argumentValues;
	private CustomBrutauthRule toInvoke;

	public BrutauthMethod(Argument[] arguments, Method defaultMethod, CustomBrutauthRule toInvoke) throws NoSuchMethodException, SecurityException {
		this.argumentValues = toValue(arguments);
		this.toInvoke = toInvoke;
		this.methodToInvoke = getMethodToInvoke(toInvoke.getClass(), defaultMethod.getName(), argumentValues);
	}

	private Object[] toValue(Argument[] arguments) {
		if(arguments == null) return null;
		Object[] values = new Object[arguments.length];
		for (int i = 0; i < arguments.length; i++) {
			Argument argument = arguments[i];
			values[i] = argument == null ? null : argument.getValue();
		}
		return values;
	}

	public boolean invoke() {
		return (boolean) new Mirror().on(toInvoke).invoke().method(methodToInvoke).withArgs(argumentValues);
	}
	
	public Method getMethod() {
		return methodToInvoke;
	}

	private Method getMethodToInvoke(Class<?> toInvoke, String methodName, final Object[] arguments) throws NoSuchMethodException, SecurityException {
		
		ClassController<?> clazz = new Mirror().on(toInvoke);
		if(arguments == null)
			return clazz.reflect().method(methodName).withoutArgs();

		MirrorList<Method> matching = clazz
				.reflectAll()
				.methods()
				.matching(havingSomeOfThese(methodName,arguments));
		
		if(matching.isEmpty()) throw new IllegalArgumentException("Didn't find any methods with name "+methodName+" and arguments "+asList(arguments));
		return matching.get(0);
	}

	private Matcher<Method> havingSomeOfThese(final String methodName, final Object[] arguments) {
		return new Matcher<Method>() {
			@Override
			public boolean accepts(Method element) {
				Class<?>[] parameterTypes = element.getParameterTypes();
				if(!element.getName().equals(methodName)) return false;
				if(parameterTypes.length != arguments.length) return false; 
				
				for (int i = 0; i < parameterTypes.length; i++) {
					if(arguments[i] == null) continue;
					if(!parameterTypes[i].isAssignableFrom(arguments[i].getClass())) return false;
				}
				return true;
			}
		};
	}
}
