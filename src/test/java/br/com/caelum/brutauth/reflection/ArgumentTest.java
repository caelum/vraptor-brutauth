package br.com.caelum.brutauth.reflection;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import br.com.caelum.brutauth.reflection.methodsearchers.Cat;
import br.com.caelum.brutauth.reflection.methodsearchers.Dog;

public class ArgumentTest {

	@Test
	public void should_be_a_half_match_since_types_are_equals() {
		Parameter param = new Parameter("tom", Dog.class);
		Argument billy = new Argument("billy", new Dog());
		assertEquals(MatchLevel.HALF, billy.matches(param));
	}
	
	@Test
	public void should_not_be_a_half_match_since_types_are_not_equals() {
		Parameter param = new Parameter("tom", Dog.class);
		Argument tom = new Argument("tom", new Cat());
		assertEquals(MatchLevel.ZERO, tom.matches(param));
	}
	
	@Test
	public void should_be_a_zero_match() {
		Parameter param = new Parameter("tom", Dog.class);
		Argument billy = new Argument("billy", new Cat());
		assertEquals(MatchLevel.ZERO, billy.matches(param));
	}
	
	@Test
	public void should_be_a_full_match() {
		Parameter param = new Parameter("billy", Dog.class);
		Argument billy = new Argument("billy", new Dog());
		assertEquals(MatchLevel.FULL, billy.matches(param));
	}

}
