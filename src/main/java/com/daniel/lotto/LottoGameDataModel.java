package com.daniel.lotto;

import java.util.List;

import lombok.Data;

@Data
public class LottoGameDataModel {

    private final List<Integer> numbers;
    private final String date;
    private final Integer gameNo;
}