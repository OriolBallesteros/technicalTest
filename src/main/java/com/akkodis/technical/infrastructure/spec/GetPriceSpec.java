package com.akkodis.technical.infrastructure.spec;

import com.akkodis.technical.domain.dto.PriceDTO;
import com.akkodis.technical.domain.exception.NotFoundPriceException;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.http.ResponseEntity;

public interface GetPriceSpec {

  @ApiOperation(
      value = "Retrieve prices data for given params",
      nickname = "Get Price's data",
      notes = "Retrieve price's: product and brand identifiers, final price and its valid dates. "
          + "Get desired price from params: date to apply, product and brand identifiers.",
      response = ResponseEntity.class,
      tags = {"Price",})
  @ApiResponses(value = {
      @ApiResponse(code = 200, message = "OK", response = ResponseEntity.class),
      @ApiResponse(code = 404, message = "Not Found", response = NotFoundPriceException.class)
  })
  ResponseEntity<PriceDTO> getPriceDataForGivenBrandProductIdAndDateSpecs(
      @ApiParam(value = "Brand identifier.") final Long brand,
      @ApiParam(value = "Product identifier.") final Long productId,
      @ApiParam(value = "Date on which the query applies") final String date);

}
