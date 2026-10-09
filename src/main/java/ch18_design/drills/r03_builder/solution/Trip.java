package ch18_design.drills.r03_builder.solution;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/** Un voyage immuable, construit par un builder qui valide tout au build(). */
public final class Trip {

    private final City from;
    private final City to;
    private final LocalDate date;
    private final int passengers;
    private final List<String> options;

    private Trip(Builder b) {
        this.from = b.from;
        this.to = b.to;
        this.date = b.date;
        this.passengers = b.passengers;
        this.options = List.copyOf(b.options);
    }

    public static Builder builder() {
        return new Builder();
    }

    public static Trip oneWay(String from, String to, LocalDate date) {
        return builder().from(from).to(to).date(date).build();
    }

    public Builder toBuilder() {
        Builder b = builder().from(from.code()).to(to.code()).date(date).passengers(passengers);
        b.options.addAll(options);
        return b;
    }

    public City from() {
        return from;
    }

    public City to() {
        return to;
    }

    public LocalDate date() {
        return date;
    }

    public int passengers() {
        return passengers;
    }

    public List<String> options() {
        return options;
    }

    public static final class Builder {
        private City from;
        private City to;
        private LocalDate date;
        private int passengers = 1;
        private final List<String> options = new ArrayList<>();

        private Builder() {
        }

        public Builder from(String city) {
            this.from = City.of(city);
            return this;
        }

        public Builder to(String city) {
            this.to = City.of(city);
            return this;
        }

        public Builder date(LocalDate date) {
            this.date = date;
            return this;
        }

        public Builder passengers(int passengers) {
            this.passengers = passengers;
            return this;
        }

        public Builder option(String option) {
            options.add(option);
            return this;
        }

        public Trip build() {
            check(from != null && to != null, "depart et arrivee obligatoires");
            check(!from.equals(to), "depart et arrivee identiques");
            check(date != null, "date obligatoire");
            check(passengers >= 1 && passengers <= 9, "passagers : de 1 a 9");
            return new Trip(this);
        }

        private static void check(boolean condition, String message) {
            if (!condition) {
                throw new IllegalStateException(message);
            }
        }
    }
}
