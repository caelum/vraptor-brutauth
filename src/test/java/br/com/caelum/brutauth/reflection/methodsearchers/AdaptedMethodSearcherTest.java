package br.com.caelum.brutauth.reflection.methodsearchers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;

import br.com.caelum.brutauth.auth.rules.CustomBrutauthRule;
import br.com.caelum.brutauth.reflection.Argument;
import br.com.caelum.brutauth.reflection.BrutauthMethod;

public class AdaptedMethodSearcherTest {

	private DefaultMethodSearcher methodSearcher = new DefaultMethodSearcher();
	private AdaptedMethodSearcher adaptedMethodSearcher;
	private Argument dog;
	private Argument cat;
	
	@Before
	public void setUp(){
		ArgumentParameterMatcher argumentParameterMatcher = new ArgumentParameterMatcher(new MatchedParameters());
		adaptedMethodSearcher = new AdaptedMethodSearcher(methodSearcher, argumentParameterMatcher);
		dog = argument("dog", new Dog());
		cat = argument("cat", new Cat());
	}

	private Argument argument(String name, Object value) {
		return new Argument(name, value);
	}
	
	@Test
	public void should_match_default_method_with_correct_parameters() {
		BrutauthMethod method = adaptedMethodSearcher.search(new AnimalsRule(), cat, dog);
		
		assertMatchedMethod(method);
	}


	@Test
	public void should_match_default_method_with_one_correct_parameter() {
		BrutauthMethod method = adaptedMethodSearcher.search(new CatRule(), cat, dog);
		
		assertMatchedMethod(method);
	}
	
	@Test
	public void should_match_methods_without_parameters() {
		BrutauthMethod method = adaptedMethodSearcher.search(new NoneRule(), cat, cat);
		
		assertMatchedMethod(method);
	}
	
	@Test
	public void should_ignore_nulls() {
		BrutauthMethod method = adaptedMethodSearcher.search(new CatRule(), cat, null);
		
		assertMatchedMethod(method);
	}


	private void assertMatchedMethod(BrutauthMethod method) {
		assertNotNull(method);
		assertEquals("isAllowed", method.getMethod().getName());
	}
	
	
	
	private class AnimalsRule implements CustomBrutauthRule{
		@SuppressWarnings("unused")
		public boolean isAllowed(Cat cat, Dog dog){
			return false;
		}
	}
	
	private class CatRule implements CustomBrutauthRule{
		@SuppressWarnings("unused")
		public boolean isAllowed(Cat cat){
			return false;
		}
	}
	
	private class NoneRule implements CustomBrutauthRule{
		@SuppressWarnings("unused")
		public boolean isAllowed(){
			return false;
		}
	}
	


}
