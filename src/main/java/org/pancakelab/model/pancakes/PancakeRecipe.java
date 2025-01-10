package org.pancakelab.model.pancakes;

import java.util.List;

public record PancakeRecipe(List<String> ingredients) {

	public String description() {
		return "Delicious pancake with %s!".formatted(String.join(", ", ingredients()));
	}
}
