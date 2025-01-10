package org.pancakelab.model.pancakes;

import org.jspecify.annotations.NullMarked;

import java.util.List;

@NullMarked
public record PancakeRecipe(List<String> ingredients) {

	public String description() {
		return "Delicious pancake with %s!".formatted(String.join(", ", ingredients()));
	}
}
