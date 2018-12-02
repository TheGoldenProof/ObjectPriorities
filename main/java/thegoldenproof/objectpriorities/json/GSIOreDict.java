package thegoldenproof.objectpriorities.json;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nullable;

public class GSIOreDict implements GeneralSimpleItem {
	static final Map<String, GeneralSimpleItem> oreDictStacks = new HashMap<>();

	public final String od;

	private GSIOreDict(String od) {
		this.od = od;
	}

	@Nullable
	public static GeneralSimpleItem create(String oreDictionaryName) {
		return oreDictStacks.computeIfAbsent(oreDictionaryName, GSIOreDict::new);
	}

	@Override
	public int hashCode() {
		return od.hashCode();
	}

	@Override
	public boolean equals(Object o) {
		return o instanceof GSIOreDict && this.od.equals(((GSIOreDict) o).od);
	}

	@Override
	public String json() {
		return "OD|" + this.od;
	}

	@Override
	public String toString() {
		return "OD: " + od;
	}
}