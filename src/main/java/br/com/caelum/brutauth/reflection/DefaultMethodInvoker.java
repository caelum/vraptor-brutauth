package br.com.caelum.brutauth.reflection;

import javax.inject.Inject;

import br.com.caelum.brutauth.auth.rules.CustomBrutauthRule;
import br.com.caelum.brutauth.reflection.methodsearchers.MethodSearchers;

public class DefaultMethodInvoker {

		@Inject private MethodSearchers searcher;

		public boolean invoke(CustomBrutauthRule toInvoke, Object[] args) {
			return searcher.search(toInvoke, args).invoke();
		}

}
