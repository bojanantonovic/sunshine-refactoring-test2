package org.pancakelab.service;

import org.pancakelab.model.Order;
import org.pancakelab.model.pancakes.PancakeRecipe;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class PancakeService {
	private List<Order> orders = new ArrayList<>();
	private final Set<UUID> completedOrders = new HashSet<>();
	private final Set<UUID> preparedOrders = new HashSet<>();

	public Order createOrder(int building, int room) {
		Order order = new Order(building, room);
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
		for (int i = 0; i < count; ++i) {
			addPancake(new PancakeRecipe(ingredientAsList), getOrderById(orderId));
		}
	}

	private void addPancake(PancakeRecipe pancake, Order order) {
		pancake.setOrderId(order.getId());
		final var pancakeRecipes = order.getPancakeRecipes();
		pancakeRecipes.add(pancake);

		OrderLog.logAddPancake(order, pancake.description());
	}

	public void removePancakes(String description, UUID orderId, int count) {
		final AtomicInteger removedCount = new AtomicInteger(0);
		Order order = getOrderById(orderId);
		final var pancakeRecipes = order.getPancakeRecipes();
		pancakeRecipes.removeIf(pancake -> {
			return pancake.description().equals(description) && removedCount.getAndIncrement() < count;
		});

		OrderLog.logRemovePancakes(order, description, removedCount.get());
	}

	public void cancelOrder(UUID orderId) {
		Order order = getOrderById(orderId);
		final var pancakeRecipes = order.getPancakeRecipes();
		OrderLog.logCancelOrder(order);

		orders.removeIf(o -> o.getId().equals(orderId));
		completedOrders.removeIf(u -> u.equals(orderId));
		preparedOrders.removeIf(u -> u.equals(orderId));

		OrderLog.logCancelOrder(order);
	}

	public void completeOrder(UUID orderId) {
		completedOrders.add(orderId);
	}

	public Set<UUID> listCompletedOrders() {
		return completedOrders;
	}

	public void prepareOrder(UUID orderId) {
		preparedOrders.add(orderId);
		completedOrders.removeIf(u -> u.equals(orderId));
	}

	public Set<UUID> listPreparedOrders() {
		return preparedOrders;
	}

	public Object[] deliverOrder(UUID orderId) {
		if (!preparedOrders.contains(orderId))
			return null;

		Order order = getOrderById(orderId);
		final var pancakeRecipes = order.getPancakeRecipes();
		List<String> pancakesToDeliver = viewOrder(orderId);
		OrderLog.logDeliverOrder(order);

		orders.removeIf(o -> o.getId().equals(orderId));
		preparedOrders.removeIf(u -> u.equals(orderId));

		return new Object[] {order, pancakesToDeliver};
	}

	private Order getOrderById(final UUID orderId) {
		return orders.stream().filter(o -> o.getId().equals(orderId)).findFirst().orElse(null);
	}
}
