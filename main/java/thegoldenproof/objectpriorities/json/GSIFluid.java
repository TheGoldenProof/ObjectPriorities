package thegoldenproof.objectpriorities.json;

import javax.annotation.Nonnull;

import net.minecraftforge.fluids.Fluid;

public class GSIFluid implements GeneralSimpleItem {
	public final String name;

	private GSIFluid(Fluid f) {
		this.name = f.getName();
	}

	@Nonnull
	public static GeneralSimpleItem create(Fluid fluid) {
		//TODO cache The fluid normalizedSimpleStacks?
		return new GSIFluid(fluid);
	}

	@Override
	public boolean equals(Object o) {
		return o instanceof GSIFluid && name.equals(((GSIFluid) o).name);
	}

	@Override
	public String json() {
		return "FLUID|" + this.name;
	}

	@Override
	public int hashCode() {
		return this.name.hashCode();
	}

	@Override
	public String toString() {
		return "Fluid: " + this.name;
	}
}