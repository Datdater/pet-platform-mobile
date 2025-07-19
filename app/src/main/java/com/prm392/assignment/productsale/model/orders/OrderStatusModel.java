package com.prm392.assignment.productsale.model.orders;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderStatusModel implements Serializable {
    private String orderStatus;
}
