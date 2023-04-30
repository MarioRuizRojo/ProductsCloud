package com.springboot.webflux.productscurrency.model.dto;

import java.util.Map;

import com.springboot.webflux.productscurrency.model.dto.coingecko.RateDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 
 * @author Mario Ruiz Rojo
 * CoinGecko response (POJO)
 * CoinGecko = {'rates': {'eur':{name:'Euro',unit:'$',value:1.2,type:'fiat'},{'usd':{name:'US Dollar',unit:'$',value:1.0,type:'fiat'}};
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CoinGeckoDTO {
	/**
	 * Set of JSON rate name and rate info {'eur':{name:'euro',unit:'$',value:1.2,type:'fiat'},...}
	 */
	private Map<String,RateDTO> rates;
}