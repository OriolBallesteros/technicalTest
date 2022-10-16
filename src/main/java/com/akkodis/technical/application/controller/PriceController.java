package com.akkodis.technical.application.controller;

import com.akkodis.technical.application.port.primary.PricePrimaryPort;
import com.akkodis.technical.domain.dto.PriceDTO;
import com.akkodis.technical.domain.service.PriceService;
import com.akkodis.technical.domain.vo.PriceData;
import com.akkodis.technical.infrastructure.spec.GetPriceSpec;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/prices")
public class PriceController implements PricePrimaryPort, GetPriceSpec {

  private final PriceService priceService;

  @Autowired
  public PriceController(final PriceService priceService) {
    this.priceService = priceService;
  }

  @Override
  @GetMapping(value = "/price/{brand}/{productId}", produces = MediaType.APPLICATION_JSON_VALUE)
  public PriceDTO getPriceDataForGivenBrandProductIdAndDate(@PathVariable final Long brand,
      @PathVariable final Long productId,
      @RequestBody final String date) {
    final List<PriceData> priceDataList =
        priceService.getPriceDataForGivenBrandProductIdAndDate(brand, productId, date);

    final PriceDTO dto = new PriceDTO();
    dto.setPriceDataList(priceDataList);
    return dto;
  }

  @Override
  public ResponseEntity<PriceDTO> getPriceDataForGivenBrandProductIdAndDateSpecs(
      final Long brand, final Long productId, final String date) {
    return ResponseEntity.of(Optional.of(getPriceDataForGivenBrandProductIdAndDate(brand, productId, date)));
  }
}
