package br.com.caelum.brutauth.interceptors;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

public class BrutauthClassOrMethod {
	private final Annotation[] annotations;

	public BrutauthClassOrMethod(Method method) {
		annotations = method.getAnnotations();
	}
	
	public BrutauthClassOrMethod(Class<?> clazz) {
		annotations = clazz.getAnnotations();
	}

	public boolean containsAnnotation(Class<? extends Annotation> annotation) {
		for (Annotation currentAnnotation : annotations) {
			if(currentAnnotation.annotationType().equals(annotation)) return true;
		}
		return false;
	}

	@SuppressWarnings("unchecked")
	public <T> T getAnnotation(Class<T> annotation) {
		for (Annotation currentAnnotation : annotations) {
			if(currentAnnotation.annotationType().equals(annotation)) return (T) currentAnnotation;
		}
		throw new IllegalStateException("Cannot find annotation "+ annotation.getSimpleName()+ " at current class or method");
	}

}
