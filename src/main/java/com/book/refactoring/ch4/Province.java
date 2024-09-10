package com.book.refactoring.ch4;

import com.book.refactoring.ch4.dto.ProducerData;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

/**
 * 수요(demand)가 있고 판매 가격(price)이 있다.
 * 그리고 각각의 생산자(Producer)가 비용(cost)를 받고 생산품 수(production)만큼 만든다.
 *
 * 수요는 고정돼 있기 때문에 생산자들이 생산품을 수요보다 많이 만들어도 다 팔 수 없다.
 * 즉 수요 내에서만 계산된다.
 */
@Getter
@Setter
public class Province {
    @Setter(AccessLevel.NONE)
    private String name;
    @Setter(AccessLevel.NONE)
    private List<Producer> producers;
    private int demand;
    private int price;
    private int totalProduction;

    public Province(String name, List<ProducerData> producers, int demand, int price) {
        this.name = name;
        this.demand = demand;
        this.price = price;
        this.totalProduction = 0;
        this.producers = new LinkedList<>();
        producers.forEach(p -> addProducer(new Producer(this, p)));
    }

    public void addProducer(Producer arg) {
        producers.add(arg);
        totalProduction += arg.getProduction();
    }

    public int getShortFall() { // 생산 부족분
        return demand - totalProduction;
    }

    public int getProfit() {
        return getDemandValue() - getDemandCost();
    }

    public int getDemandValue() {
        return getSatisfiedDemand() * price;
    }

    public int getSatisfiedDemand() {
        return Math.min(demand, totalProduction);
    }

    public int getDemandCost() {
        int remainingDemand = this.demand;
        int result = 0;

        // stream으로 sort 처리가 있는데 자바에는 적절하지 않아서 그냥 for문 돌림
        List<Producer> sortedList = this.producers.stream()
                .sorted(Comparator.comparingInt(Producer::getCost)).toList(); // 최대 이익을 위해 싼 것 먼저.
        for (Producer p : sortedList) {
            final int contribution = Math.min(remainingDemand, p.getProduction());
            remainingDemand -= contribution;
            result += (contribution * p.getCost());
        }
        return result;
    }



}
