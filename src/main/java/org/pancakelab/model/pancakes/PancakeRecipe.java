package org.pancakelab.model.pancakes;

import java.util.List;
import java.util.UUID;

public class PancakeRecipe {
	private final List<String> ingredients;

	public PancakeRecipe(List<String> ingredients) {
		this.ingredients = ingredients;
	}

	private UUID orderId;

	public UUID getOrderId() {
		return orderId;
	}

	public void setOrderId(UUID orderId) {
		this.orderId = orderId;
	}

	public List<String> ingredients() {
		return ingredients;
	}

	public String description() {
		return "Delicious pancake with %s!".formatted(String.join(", ", ingredients()));
	}
}
