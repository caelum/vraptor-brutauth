package br.com.caelum.brutauth.reflection.methodsearchers;

import javax.enterprise.inject.Any;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;

import br.com.caelum.brutauth.auth.rules.CustomBrutauthRule;
import br.com.caelum.brutauth.reflection.BrutauthMethod;

public class MethodSearchers {

	@Inject @Any private Instance<MethodSearcher> searchers;

	public BrutauthMethod search(CustomBrutauthRule ruleToSearch, Object...withArgs){
		for (MethodSearcher searcher : searchers) {
			BrutauthMethod brutauthMethod = searcher.search(ruleToSearch, withArgs);
			if(brutauthMethod != null) return brutauthMethod;
		}
		throw new IllegalStateException("Não achei nenhum metodo para invocar na rule "+ruleToSearch.getClass().getSimpleName());
	}
}
