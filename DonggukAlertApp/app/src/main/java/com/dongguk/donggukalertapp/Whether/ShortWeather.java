package com.dongguk.donggukalertapp.Whether;

public class ShortWeather {

    private String hour;  // 시간
    private String temp;  // 기온
    private String wfKor; // 상태
    private String pop; // 강수확률

    public String getHour() {
        return hour;
    }

    public void setHour(String hour) {
        this.hour = hour;
    }

    public String getTemp() {
        return temp;
    }

    public void setTemp(String temp) {
        this.temp = temp;
    }

    public String getWfKor() {
        return wfKor;
    }

    public void setWfKor(String wfKor) {
        this.wfKor = wfKor;
    }

    public String getPop() {
        return pop;
    }

    public void setPop(String pop) {
        this.pop = pop;
    }

}


