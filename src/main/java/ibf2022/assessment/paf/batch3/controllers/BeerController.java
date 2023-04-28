package ibf2022.assessment.paf.batch3.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import ibf2022.assessment.paf.batch3.services.BeerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@Controller("BeerController")
public class BeerController {

	private final BeerService beerService;

	@GetMapping("/beer/style")
	public String getBeerStyles(Model model) {
		model.addAttribute("styles", beerService.getBeerStyles());
		return "view0";
	}
	
	@GetMapping("/beer/style/{id}")
	public String getBeerByStyle(@PathVariable int id, @RequestParam String styleName, Model model) {
		model.addAttribute("beers", beerService.getBeers(id));
		model.addAttribute("styleName", styleName);
		model.addAttribute("styleId", id);
		return "view1";
	}


	@GetMapping("/beer/brewery/{id}")
	public String getBeersFromBrewery(@PathVariable int id, @RequestParam int styleId, @RequestParam String styleName,
									  Model model) {
		model.addAttribute("brewery", beerService.getBeersFromBrewery(id));
		model.addAttribute("styleName", styleName);
		model.addAttribute("styleId", styleId);
		return "view2";
	}

	
	@PostMapping(value = "/brewery/{breweryId}/order", produces = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public String placeOrder(@PathVariable("breweryId") int breweryId,
							 @RequestParam(value = "beerIds") List<Integer> beerIds,
							 @RequestParam(value = "beerQuantities") List<Integer> beerQuantities,
							 Model model) throws JsonProcessingException {
		return beerService.placeOrder(breweryId, beerIds, beerQuantities, model);
	}

}
