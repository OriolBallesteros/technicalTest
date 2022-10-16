package com.akkodis.technical.application.port.primary;

import com.akkodis.technical.domain.dto.PriceDTO;

public interface PricePrimaryPort {

  PriceDTO getPriceDataForGivenBrandProductIdAndDate(final Long brand, final Long productId,
      final String date);

}
