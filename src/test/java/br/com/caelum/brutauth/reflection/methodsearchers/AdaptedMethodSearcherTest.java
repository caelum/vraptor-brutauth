package br.com.caelum.brutauth.reflection.methodsearchers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import br.com.caelum.brutauth.auth.rules.CustomBrutauthRule;
import br.com.caelum.brutauth.reflection.BrutauthMethod;

public class AdaptedMethodSearcherTest {

	private DefaultMethodSearcher methodSearcher = new DefaultMethodSearcher();
	private ArgumentMatcher matcher = new ArgumentMatcher();
	private AdaptedMethodSearcher adaptedMethodSearcher;
	
	@Before
	public void setUp(){
		
		adaptedMethodSearcher = new AdaptedMethodSearcher(methodSearcher, matcher);
	}
	
	@Test
	public void should_match_default_method_with_parameters_of_the_same_type() {
		BrutauthMethod method = adaptedMethodSearcher.search(new PugRule(), argument("pug", new Dog("pug")), argument("pastor", new Dog()));
		method.invoke();
		assertMatchedMethod(method);
	}
	
	@Test
	public void should_match_default_method_with_correct_parameters() {
		BrutauthMethod method = adaptedMethodSearcher.search(new AnimalsRule(), argument("cat", new Cat()), argument("dog", new Dog()));
		assertMatchedMethod(method);
	}

	@Test
	public void should_match_default_method_with_one_correct_parameter() {
		BrutauthMethod method = adaptedMethodSearcher.search(new CatRule(), argument("cat", new Cat()), argument("dog", new Dog()));
		assertMatchedMethod(method);
	}
	
	@Test
	public void should_match_default_method_with_two_() {
		BrutauthMethod method = adaptedMethodSearcher.search(new CatRule(), argument("cat", new Cat()), argument("dog", new Dog()));
		assertMatchedMethod(method);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void should_not_allow_null_parameters() {
		adaptedMethodSearcher.search(new AnimalsRule(), argument("cat", new Cat()));
	}
	
	@Test
	public void should_match_methods_without_parameters() {
		BrutauthMethod method = adaptedMethodSearcher.search(new NoneRule(), argument("cat", new Cat()), argument("dog", new Dog()));
		assertMatchedMethod(method);
	}
	
	@Test
	public void should_ignore_nulls() {
		BrutauthMethod method = adaptedMethodSearcher.search(new CatRule(), argument("cat", new Cat()), null);
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
	
	public class PugRule implements CustomBrutauthRule{
		@SuppressWarnings("unused")
		public boolean isAllowed(Dog pug){
			assertEquals("pug", pug.getRace());
			return false;
		}
	}
	
	private class NoneRule implements CustomBrutauthRule{
		@SuppressWarnings("unused")
		public boolean isAllowed(){
			return false;
		}
	}
	
	private class Cat{
	}
	
	private class Dog{
		private final String race;

		public Dog() {
			this(null);
		}

		public Dog(String race) {
			this.race = race;
		}
		
		public String getRace() {
			return race;
		}
	}

	private Argument argument(String variableName, Object argument) {
		return new Argument(variableName, argument);
	}

}
