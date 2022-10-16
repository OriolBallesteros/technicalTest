package com.akkodis.technical.domain.dto;

import com.akkodis.technical.domain.vo.PriceData;
import java.util.List;

public class PriceDTO {

  private List<PriceData> priceDataList;

  public PriceDTO() { }

  public PriceDTO(List<PriceData> priceDataList) {
    this.priceDataList = priceDataList;
  }

  public List<PriceData> getPriceDataList() {
    return priceDataList;
  }

  public void setPriceDataList(final List<PriceData> priceDataList) {
    this.priceDataList = priceDataList;
  }
}
