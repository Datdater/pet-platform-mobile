package com.prm392.assignment.productsale.model.orders;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AttributeModel implements Serializable {
    private String size;
    private String flavor;
}
