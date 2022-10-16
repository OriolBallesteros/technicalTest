package com.akkodis.technical.infrastructure.mapper;

import com.akkodis.technical.domain.entity.PriceEntity;
import com.akkodis.technical.domain.vo.Brand;
import com.akkodis.technical.domain.vo.Currency;
import com.akkodis.technical.domain.vo.PriceData;
import org.springframework.stereotype.Component;

@Component
public class PriceMapper {

  public PriceData mapPriceEntityToData(final PriceEntity entity) {

    if (entity == null) {
      return null;
    }

    final PriceData data = new PriceData();
    if (entity.getProductId() != null) {
      data.setProductId(entity.getProductId());
    }
    if (entity.getBrandId() != null) {
      data.setBrand(Brand.getByValue(entity.getBrandId()));
    }
    if (entity.getPriceValue() != null) {
      data.setPriceValue(entity.getPriceValue());
    }
    if (entity.getPriceList() != null) {
      data.setPriceList(entity.getPriceList());
    }
    if (entity.getCurr() != null) {
      data.setCurrency(Currency.getByValue(entity.getCurr()));
    }
    if (entity.getStartDate() != null) {
      data.setStartDate(entity.getStartDate());
    }
    if (entity.getEndDate() != null) {
      data.setEndDate(entity.getEndDate());
    }

    return data;
  }

}
