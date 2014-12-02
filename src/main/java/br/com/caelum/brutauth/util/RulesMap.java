package br.com.caelum.brutauth.util;

import java.util.HashMap;

import javax.enterprise.context.Dependent;

import br.com.caelum.brutauth.auth.rules.BrutauthRule;

@Dependent
public class RulesMap extends HashMap<Class<? extends BrutauthRule>, BrutauthRule> {
	
	private static final long serialVersionUID = 1L;

	@Override
	public BrutauthRule get(Object key) {
		BrutauthRule rule = super.get(key);
		if (rule == null) {
			throw new IllegalArgumentException();
		}
		return rule;
	}
	
}
