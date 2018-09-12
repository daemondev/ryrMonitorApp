package com.example.eidan.wsapp;

import java.util.Comparator;




/**
 * A collection of {@link Comparator}s for {@link Agent} objects.
 *
 * @author ISchwarz
 */
public final class AgentComparators {

    private AgentComparators() {
        //no instance
    }

    public static Comparator<Agent> getCarProducerComparator() {
        return new CarProducerComparator();
    }

    public static Comparator<Agent> getCarPowerComparator() {
        return new CarPowerComparator();
    }

    public static Comparator<Agent> getCarNameComparator() {
        return new CarNameComparator();
    }

    public static Comparator<Agent> getCarPriceComparator() {
        return new CarPriceComparator();
    }


    private static class CarProducerComparator implements Comparator<Agent> {

        @Override
        public int compare(final Agent car1, final Agent car2) {
            return car1.getExten().compareTo(car2.getExten());
        }
    }

    private static class CarPowerComparator implements Comparator<Agent> {

        @Override
        public int compare(final Agent car1, final Agent car2) {
            return car1.getCallerid() - car2.getCallerid();
        }
    }

    private static class CarNameComparator implements Comparator<Agent> {

        @Override
        public int compare(final Agent car1, final Agent car2) {
            return car1.getExten().compareTo(car2.getExten());
        }
    }

    private static class CarPriceComparator implements Comparator<Agent> {

        @Override
        public int compare(final Agent car1, final Agent car2) {
            if (car1.getCallerid() < car2.getCallerid()) return -1;
            if (car1.getCallerid() > car2.getCallerid()) return 1;
            return 0;
        }
    }

}
