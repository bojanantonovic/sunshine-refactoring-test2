package org.pancakelab.service;

import org.pancakelab.model.Order;

public class OrderLog {
	private static final StringBuilder log = new StringBuilder();

	public static void logAddPancake(Order order, String description) {
		var pancakesInOrder = getNumberOfPancakesForOrder(order);

		log.append("Added pancake with description '%s' ".formatted(description)).append("to order %s containing %d pancakes, ".formatted(order.getId(), pancakesInOrder)).append("for building %d, room %d.".formatted(order.getBuilding(), order.getRoom()));
	}

	public static void logRemovePancakes(Order order, String description, int count) {
		var pancakesInOrder = getNumberOfPancakesForOrder(order);

		log.append("Removed %d pancake(s) with description '%s' ".formatted(count, description)).append("from order %s now containing %d pancakes, ".formatted(order.getId(), pancakesInOrder)).append("for building %d, room %d.".formatted(order.getBuilding(), order.getRoom()));
	}

	public static void logCancelOrder(Order order) {
		var pancakesInOrder = getNumberOfPancakesForOrder(order);
		log.append("Cancelled order %s with %d pancakes ".formatted(order.getId(), pancakesInOrder)).append("for building %d, room %d.".formatted(order.getBuilding(), order.getRoom()));
	}

	public static void logDeliverOrder(Order order) {
		var pancakesInOrder = getNumberOfPancakesForOrder(order);
		log.append("Order %s with %d pancakes ".formatted(order.getId(), pancakesInOrder)).append("for building %d, room %d out for delivery.".formatted(order.getBuilding(), order.getRoom()));
	}

	private static long getNumberOfPancakesForOrder(final Order order) {
		return order.getPancakeRecipes().size();
	}
}
