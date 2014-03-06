package br.com.caelum.brutauth.reflection.methodsearchers;

import br.com.caelum.brutauth.reflection.Argument;
import br.com.caelum.brutauth.reflection.MatchLevel;

public class MatchedArgument {

	private Argument arg;
	private MatchLevel matchLevel;

	public MatchedArgument(Argument arg, MatchLevel matchLevel) {
		this.arg = arg;
		this.matchLevel = matchLevel;
	}

	public boolean isFull() {
		return MatchLevel.FULL.equals(matchLevel);
	}

	public Argument getArgument() {
		return arg;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((arg == null) ? 0 : arg.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		MatchedArgument other = (MatchedArgument) obj;
		if (arg == null) {
			if (other.arg != null)
				return false;
		} else if (!arg.equals(other.arg))
			return false;
		return true;
	}
	
	
}
