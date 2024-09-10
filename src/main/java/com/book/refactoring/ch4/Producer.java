package com.book.refactoring.ch4;

import com.book.refactoring.ch4.dto.ProducerData;
import lombok.AccessLevel;
import lombok.Getter;

@Getter
public class Producer {
    @Getter(AccessLevel.NONE)
    private Province province;
    private String name;
    private int cost;
    private int production;

    public Producer(Province province, ProducerData data) {
        this.province = province;
        this.name = data.name();
        this.cost = data.cost();
        this.production = data.production();
    }

    public void setCost(int cost) {
        this.cost = cost;
    }

    public void setProduction(int amount) {
        final int newProduction = amount;
        province.setTotalProduction(province.getTotalProduction() + newProduction - production);
        production = newProduction;
    }
}
