package org.pancakelab.service;

import org.pancakelab.model.Order;
import org.pancakelab.model.pancakes.PancakeRecipe;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class PancakeService {
	private final List<Order> orders = new ArrayList<>();
	private final Set<UUID> completedOrders = new HashSet<>();
	private final Set<UUID> preparedOrders = new HashSet<>();

	public synchronized Order createOrder(int building, int room) {
		var order = new Order(building, room);
		orders.add(order);
		return order;
	}

	public List<String> viewOrder(UUID orderId) {
		final var order = getOrderById(orderId);
		if (order == null) {
			return Collections.emptyList();
		}
		return order.getPancakeRecipes().stream() //
				.map(PancakeRecipe::description) //
				.toList();
	}

	public void addPancake(UUID orderId, int count, String... ingredients) {
		final var ingredientAsList = List.of(ingredients);
		for (var i = 0; i < count; ++i) {
			addPancake(new PancakeRecipe(ingredientAsList), getOrderById(orderId));
		}
	}

	private void addPancake(PancakeRecipe pancake, Order order) {
		final var pancakeRecipes = order.getPancakeRecipes();
		pancakeRecipes.add(pancake);

		OrderLog.logAddPancake(order, pancake.description());
	}

	public synchronized void removePancakes(String description, UUID orderId, int count) {
		final var removedCount = new AtomicInteger(0);
		var order = getOrderById(orderId);
		final var pancakeRecipes = order.getPancakeRecipes();
		pancakeRecipes.removeIf(pancake -> {
			return pancake.description().equals(description) && removedCount.getAndIncrement() < count;
		});

		OrderLog.logRemovePancakes(order, description, removedCount.get());
	}

	public synchronized void cancelOrder(UUID orderId) {
		var order = getOrderById(orderId);
		OrderLog.logCancelOrder(order);

		orders.removeIf(o -> o.getId().equals(orderId));
		completedOrders.removeIf(u -> u.equals(orderId));
		preparedOrders.removeIf(u -> u.equals(orderId));

		OrderLog.logCancelOrder(order);
	}

	public synchronized void completeOrder(UUID orderId) {
		completedOrders.add(orderId);
	}

	public Set<UUID> listCompletedOrders() {
		return completedOrders;
	}

	public synchronized void prepareOrder(UUID orderId) {
		preparedOrders.add(orderId);
		completedOrders.removeIf(u -> u.equals(orderId));
	}

	public Set<UUID> listPreparedOrders() {
		return preparedOrders;
	}

	public synchronized Object[] deliverOrder(UUID orderId) {
		if (!preparedOrders.contains(orderId))
			return null;

		var order = getOrderById(orderId);
		var pancakesToDeliver = viewOrder(orderId);
		OrderLog.logDeliverOrder(order);

		orders.removeIf(o -> o.getId().equals(orderId));
		preparedOrders.removeIf(u -> u.equals(orderId));

		return new Object[] {order, pancakesToDeliver};
	}

	private Order getOrderById(final UUID orderId) {
		return orders.stream().filter(o -> o.getId().equals(orderId)).findFirst().orElse(null);
	}
}
