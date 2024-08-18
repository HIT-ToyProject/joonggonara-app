package com.hit.joonggonara.dto.request.board;

import com.hit.joonggonara.common.type.CategoryType;
import com.hit.joonggonara.common.type.SchoolType;
import com.hit.joonggonara.entity.Product;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import static com.hit.joonggonara.common.properties.ValidationMessageProperties.*;

public record ProductRequest(
        @NotBlank(message= TITLE_NOT_BLANK)
        String title,
        String category,
        @NotNull(message= PRICE_NOT_BLANK)
        Long price,
        @NotBlank(message= CONTENT_NOT_BLANK)
        String content,
        @NotBlank(message= TRADING_PLACE_NOT_BLANK)
        String tradingPlace,
        @NotBlank(message= PRODUCT_STATUS_NOT_BLANK)
        String productStatus,
        @NotBlank(message= SCHOOL_NOT_BLANK)
        String school
) {

    public static ProductRequest of(
            String title,
            String category,
            Long price,
            String content,
            String tradingPlace,
            String productStatus,
            String school
    ){
        return new ProductRequest(title, category, price, content, tradingPlace, productStatus, school);
    }

    public Product toEntity(){
        return Product.builder()
                .title(title())
                .categoryType(CategoryType.toEnum(category()))
                .price(price())
                .content(content())
                .tradingPlace(tradingPlace())
                .productStatus(productStatus())
                .schoolType(SchoolType.toEnum(school()))
                .build();
    }

}
