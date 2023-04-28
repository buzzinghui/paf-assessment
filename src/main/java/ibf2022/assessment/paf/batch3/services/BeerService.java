package ibf2022.assessment.paf.batch3.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import ibf2022.assessment.paf.batch3.models.*;
import ibf2022.assessment.paf.batch3.repositories.BeerRepository;
import ibf2022.assessment.paf.batch3.repositories.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BeerService {

	private final BeerRepository beerRepository;
	private final OrderRepository orderRepository;
	private final ObjectMapper objectMapper;
	public List<Style> getBeerStyles() {
		return beerRepository.getStyles();
	}

	public List<Beer> getBeers(int id) {
		return beerRepository.getBreweriesByBeer(id);
	}

	public Brewery getBeersFromBrewery(int id) {
		Optional<Brewery> brewery = beerRepository.getBeersFromBrewery(id);
		return brewery.orElseGet(Brewery::new);
	}

	// DO NOT CHANGE THE METHOD'S NAME OR THE RETURN TYPE OF THIS METHOD
	public String placeOrder(int breweryId, List<Integer> beerIds, List<Integer> beerQuantities,
							 Model model) throws JsonProcessingException {

		// Create new order form
		if (!beerIds.isEmpty() && !beerQuantities.isEmpty()) {
			Brewery brewery = getBeersFromBrewery(breweryId);
			OrderForm orderForm = new OrderForm();
			orderForm.setOrderId(generateOrderId());
			orderForm.setDate(LocalDateTime.now());
			orderForm.setBreweryId(breweryId);
			List<Order> orderList = new ArrayList<>();

			for (int i = 0; i < beerQuantities.size(); i++) {
				Integer quantity = beerQuantities.get(i);
				if (quantity != null  && quantity > 0) {
					int beerId = beerIds.get(i);
					Order orderItem = new Order();
					orderItem.setBeerId(beerId);
					orderItem.setQuantity(quantity);
					orderList.add(orderItem);
				}
			}
			orderForm.setOrders(orderList);
			String orderJson = objectMapper.writeValueAsString(orderForm);
			if (!orderList.isEmpty()) {
				orderRepository.insertOrder(orderJson);
				model.addAttribute("orderId", orderForm.getOrderId());
				return "view3";
			} else {
				return "view4";
			}
		}
		return "view4";
	}

	public static String generateOrderId() {
		UUID uuid = UUID.randomUUID();
		String randomId = uuid.toString().replace("-", "").substring(0, 8);
		return randomId;
	}

}
