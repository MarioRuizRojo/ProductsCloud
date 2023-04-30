package com.springboot.webflux.productscurrency.model.dto.coingecko;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 
 * @author Mario Ruiz Rojo
 * CoinGecko Rate Object (POJO)
 * {name:'Euro',unit:'$',value:1.2,type:'fiat'}
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RateDTO {
	private String name;
	private String unit;
	private Double value;
	private String type;
}