package br.com.caelum.brutauth.interceptors;

import net.vidageek.mirror.dsl.Mirror;
import br.com.caelum.brutauth.auth.annotations.AccessLevel;
import br.com.caelum.brutauth.auth.annotations.CustomBrutauthRules;
import br.com.caelum.brutauth.auth.annotations.SimpleBrutauthRules;
import br.com.caelum.vraptor.resource.DefaultResourceClass;
import br.com.caelum.vraptor.resource.DefaultResourceMethod;
import br.com.caelum.vraptor.resource.ResourceMethod;

class MyController {
	public static final String MY_STRING = "Brutauth rulez";
	public static final String UNNACCEPTABLE_STRING = "Brutauth suckz";

	public static ResourceMethod method(String name) {
		return new DefaultResourceMethod(
				new DefaultResourceClass(MyController.class),
				new Mirror().on(MyController.class).reflect().method(name).withAnyArgs());
	}

	@SimpleBrutauthRules(MySimpleBiggerThanZeroRule.class)
	public void mySimpleRuleMethod() {}

	@SimpleBrutauthRules(MySimpleBiggerThanZeroRule.class)
	@AccessLevel(1234l)
	public void mySimpleRuleMethodWithAccessLevel() {}

	@SimpleBrutauthRules({ MySimpleBiggerThanZeroRule.class, AnotherSimpleRule.class })
	public void myManySimpleRulesMethod() {}

	@SimpleBrutauthRules({ MySimpleBiggerThanZeroRule.class, AnotherSimpleRule.class })
	@AccessLevel(1234l)
	public void myManySimpleRulesMethodWithAccessLevel() {}

	@CustomBrutauthRules(MyCustomRule.class)
	public void myCustomRuleMethod(String myString) {}

	@CustomBrutauthRules({MyCustomRule.class, AnotherCustomRule.class})
	public void myManyCustomRulesMethod(String myString) {}
}
