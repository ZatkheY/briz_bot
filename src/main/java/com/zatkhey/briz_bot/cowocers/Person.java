package com.zatkhey.briz_bot.cowocers;


import java.util.HashMap;
import java.util.Map;


public class Person {
    private String name;
    private Map<String, Integer> mapMoney = new HashMap<>();
    private Map<String, Integer> mapOtkat = new HashMap<>();
    private long chatId;


    public String getName() {
        return name;
    }

    public void setMap(Map<String, Integer> map) {
        this.mapMoney = map;
    }

    public Map<String, Integer> getMap() {
        return mapMoney;
    }

    public long getChatId() {
        return chatId;
    }

    public void setOtkat(String date, int type) {
        mapOtkat.put(date, type);
    }

    public int printAllMoney() {
        return setAllMoney(mapMoney);
    }

    public int printAllOtkat() {
        return setAllMoney(mapOtkat);
    }

    public int printMoneyByDate(String date) {
        return setMoneyByDate(date, mapMoney);
    }

    public int printOtkatByDate(String date) {
        return setMoneyByDate(date, mapOtkat);
    }

    private int setMoneyByDate(String date, Map<String, Integer> map) {
        Integer count = 0;
        for (Map.Entry entry : map.entrySet()) {
            String subEntry = entry.getKey().toString().substring(0, 4);
            if (subEntry.equals(date.substring(0, 4))) {
                count += (Integer) entry.getValue();
            }
        }
        return count;
    }

    private int setAllMoney(Map<String, Integer> map) {
        int count = 0;
        for (Integer integer : map.values()) {
            count += integer;
        }
        return count;
    }

    public void clear() {
        mapOtkat.clear();
        mapMoney.clear();
    }
}

