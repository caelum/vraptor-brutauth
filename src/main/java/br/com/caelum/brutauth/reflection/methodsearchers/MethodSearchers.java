package br.com.caelum.brutauth.reflection.methodsearchers;

import java.util.List;

import javax.inject.Inject;

import br.com.caelum.brutauth.auth.rules.CustomBrutauthRule;
import br.com.caelum.brutauth.reflection.BrutauthMethod;

public class MethodSearchers {

	@Inject private List<MethodSearcher> searchers;

	public BrutauthMethod search(CustomBrutauthRule ruleToSearch, Argument...withArgs){
		for (MethodSearcher searcher : searchers) {
			BrutauthMethod brutauthMethod = searcher.search(ruleToSearch, withArgs);
			if(brutauthMethod != null) return brutauthMethod;
		}
		throw new IllegalStateException("Não achei nenhum metodo para invocar na rule "+ruleToSearch.getClass().getSimpleName());
	}
}
