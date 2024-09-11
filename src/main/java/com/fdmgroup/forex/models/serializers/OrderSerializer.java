package com.fdmgroup.forex.models.serializers;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fdmgroup.forex.models.Order;
import com.fdmgroup.forex.models.dto.OrderResponseDTO;

import java.io.IOException;

public class OrderSerializer extends JsonSerializer<Order> {

	@Override
	public void serialize(Order order, JsonGenerator jsonGenerator, SerializerProvider serializerProvider)
			throws IOException {
		OrderResponseDTO dto = new OrderResponseDTO(order, order.getPortfolio().getUser().getId());
		jsonGenerator.writeObject(dto);
	}

}
