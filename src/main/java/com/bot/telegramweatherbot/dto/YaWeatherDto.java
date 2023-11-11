package com.bot.telegramweatherbot.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Objects;

@Setter
@Getter
public class YaWeatherDto {
    private long now;
    private String now_dt;
    private Info info;
    private Fact fact;
    private Forecast forecast;

    public static class Info {
        public String url;
        public double lat;
        public double lon;
    }

    public static class Fact {
        public long obs_time;
        public int temp;
        public int feels_like;
        public String icon;
        public String condition;
        public int wind_speed;
        public String wind_dir;
        public int pressure_mm;
        public int pressure_pa;
        public int humidity;
        public String daytime;
        public boolean polar;
        public String season;
        public double wind_gust;
    }

    public static class Forecast {
        public String date;
        public long date_ts;
        public int week;
        public String sunrise;
        public String sunset;
        public int moon_code;
        public String moon_text;
        public Part[] parts;
    }

    public static class Part {
        public String part_name;
        public int temp_min;
        public int temp_avg;
        public int temp_max;
        public double wind_speed;
        public double wind_gust;
        public String wind_dir;
        public int pressure_mm;
        public int pressure_pa;
        public int humidity;
        public int prec_mm;
        public int prec_prob;
        public int prec_period;
        public String icon;
        public String condition;
        public int feels_like;
        public String daytime;
        public boolean polar;
    }

    @Override
    public String toString() {
        return "YaWeatherDto{" +
                "now=" + now +
                ", now_dt='" + now_dt + '\'' +
                ", info=" + info +
                ", fact=" + fact +
                ", forecast=" + forecast +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        YaWeatherDto that = (YaWeatherDto) o;
        return now == that.now && Objects.equals(now_dt, that.now_dt) && Objects.equals(info, that.info) && Objects.equals(fact, that.fact) && Objects.equals(forecast, that.forecast);
    }

    @Override
    public int hashCode() {
        return Objects.hash(now, now_dt, info, fact, forecast);
    }
}
