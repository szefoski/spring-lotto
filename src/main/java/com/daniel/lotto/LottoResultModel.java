package com.daniel.lotto;

import java.util.List;

import lombok.Data;

@Data
public class LottoResultModel {

    private final List<Integer> numbers;
    private final String date;
    private final Integer gameNo;
}