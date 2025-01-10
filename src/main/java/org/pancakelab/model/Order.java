package org.pancakelab.model;

import org.pancakelab.model.pancakes.PancakeRecipe;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class Order {
	private final UUID id;
	private final int building;
	private final int room;
	// the next field are supportive for the PancakeService class => more data for an easier implementation
	private final List<PancakeRecipe> pancakeRecipes = new ArrayList<>();

	public Order(int building, int room) {
		this.id = UUID.randomUUID();
		this.building = building;
		this.room = room;
	}

	public UUID getId() {
		return id;
	}

	public int getBuilding() {
		return building;
	}

	public int getRoom() {
		return room;
	}

	public List<PancakeRecipe> getPancakeRecipes() {
		return pancakeRecipes;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		Order order = (Order) o;
		return Objects.equals(id, order.id);
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(id);
	}
}
