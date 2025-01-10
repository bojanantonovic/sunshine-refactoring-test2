package org.pancakelab.model.pancakes;

import java.util.List;

public class PancakeRecipe {
	private final List<String> ingredients;

	public PancakeRecipe(List<String> ingredients) {
		this.ingredients = ingredients;
	}

	public List<String> ingredients() {
		return ingredients;
	}

	public String description() {
		return "Delicious pancake with %s!".formatted(String.join(", ", ingredients()));
	}
}
