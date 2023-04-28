package ibf2022.assessment.paf.batch3.repositories;

import ibf2022.assessment.paf.batch3.models.Beer;
import ibf2022.assessment.paf.batch3.models.Brewery;
import ibf2022.assessment.paf.batch3.models.Style;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class BeerRepository {

	private final JdbcTemplate jdbcTemplate;

	// DO NOT CHANGE THE SIGNATURE OF THIS METHOD
	public List<Style> getStyles() {
		String sql = "SELECT sty.id AS styleId, sty.style_name AS name, count(beer.name) AS beerCount " +
				"FROM styles sty " +
				"LEFT JOIN beers beer ON beer.style_id = sty.id " +
				"GROUP BY(sty.id) " +
				"ORDER BY beerCount DESC, name ASC";
		return jdbcTemplate.query(sql, (rs, rowNum) -> {
			Style style = new Style();
			style.setStyleId(rs.getInt("styleId"));
			style.setName(rs.getString("name"));
			style.setBeerCount(rs.getInt("beerCount"));
			return style;
		});
	}
		
	// DO NOT CHANGE THE METHOD'S NAME OR THE RETURN TYPE OF THIS METHOD
	public List<Beer> getBreweriesByBeer(int styleId) {
		String sql = "SELECT beer.id AS beerId, beer.name AS beerName, beer.descript AS beerDescription, " +
				"beer.brewery_id AS breweryId, brew.name AS breweryName " +
				"FROM beers beer " +
				"INNER JOIN breweries brew ON brew.id = beer.brewery_id " +
				"WHERE beer.style_id = " + styleId + " " +
				"ORDER BY beerName ASC";
		return jdbcTemplate.query(sql, (rs, rowNum) -> {
			Beer beer = new Beer();
			beer.setBeerId(rs.getInt("beerId"));
			beer.setBeerName(rs.getString("beerName"));
			beer.setBeerDescription(rs.getString("beerDescription"));
			beer.setBreweryId(rs.getInt("breweryId"));
			beer.setBreweryName(rs.getString("breweryName"));
			return beer;
		});
	}

	// DO NOT CHANGE THE METHOD'S NAME OR THE RETURN TYPE OF THIS METHOD
	public Optional<Brewery> getBeersFromBrewery(Integer breweryId) {
		String brewerySql = "SELECT id AS breweryId, name, address1, address2, " +
				"city, phone, website, descript AS description " +
				"FROM breweries " +
				"WHERE id = " + breweryId;

		List<Brewery> breweryRes = jdbcTemplate.query(brewerySql, (rs, rowNum) -> {
			Brewery brewery = new Brewery();
			brewery.setBreweryId(rs.getInt("breweryId"));
			brewery.setName(rs.getString("name"));
			brewery.setAddress1(rs.getString("address1"));
			brewery.setAddress2(rs.getString("address2"));
			brewery.setCity(rs.getString("city"));
			brewery.setPhone(rs.getString("phone"));
			brewery.setWebsite(rs.getString("website"));
			brewery.setDescription(rs.getString("description"));
			return brewery;
		});

		if (!breweryRes.isEmpty()) {
			Brewery brewery = breweryRes.get(0);
			String sql = "SELECT beer.id AS beerId, beer.name AS beerName, beer.descript AS beerDescription, " +
					"beer.brewery_id AS breweryId, brew.name AS breweryName " +
					"FROM beers beer " +
					"INNER JOIN breweries brew ON brew.id = beer.brewery_id " +
					"WHERE beer.brewery_id = " + breweryId + " " +
					"ORDER BY beerName ASC";
			List<Beer> beers = jdbcTemplate.query(sql, (rs, rowNum) -> {
				Beer beer = new Beer();
				beer.setBeerId(rs.getInt("beerId"));
				beer.setBeerName(rs.getString("beerName"));
				beer.setBeerDescription(rs.getString("beerDescription"));
				beer.setBreweryId(rs.getInt("breweryId"));
				beer.setBreweryName(rs.getString("breweryName"));
				return beer;
			});
			brewery.setBeers(beers);
			return Optional.of(brewery);
		}
		return Optional.empty();
	}
}
