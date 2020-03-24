package video.rental.demo.service;

import video.rental.demo.model.Customer;
import video.rental.demo.model.Rental;
import video.rental.demo.model.video.PriceCode;
import video.rental.demo.repository.Repository;

import java.util.List;

public class CustomerService {
    private Repository repository;

    public CustomerService(Repository repository) {
        this.repository = repository;
    }

    public static String getReport(Customer customer) {
        String result = "Customer Report for " + customer.getName() + "\n";

        List<Rental> rentals = customer.getRentals();

        double totalCharge = 0;
        int totalPoint = 0;

        for (Rental each : rentals) {
            double eachCharge = 0;
            int eachPoint = 0;
            int daysRented = 0;

            daysRented = each.getDaysRented();

            switch (each.getVideo().getPriceCode()) {
                case REGULAR:
                    eachCharge += 2;
                    if (daysRented > 2)
                        eachCharge += (daysRented - 2) * 1.5;
                    break;
                case NEW_RELEASE:
                    eachCharge = daysRented * 3;
                    break;
                case CHILDREN:
                    eachCharge += 1.5;
                    if (daysRented > 3)
                        eachCharge += (daysRented - 3) * 1.5;
                    break;
            }

            eachPoint++;
            if ((each.getVideo().getPriceCode() == PriceCode.NEW_RELEASE))
                eachPoint++;

            if (daysRented > each.getDaysRentedLimit())
                eachPoint -= Math.min(eachPoint, each.getVideo().getLateReturnPointPenalty());

            result += "\t" + each.getVideo().getTitle() + "\tDays rented: " + daysRented + "\tCharge: " + eachCharge
                    + "\tPoint: " + eachPoint + "\n";

            totalCharge += eachCharge;
            totalPoint += eachPoint;
        }

        result += "Total charge: " + totalCharge + "\tTotal Point:" + totalPoint + "\n";

        if (totalPoint >= 10) {
            System.out.println("Congrat! You earned one free coupon");
        }
        if (totalPoint >= 30) {
            System.out.println("Congrat! You earned two free coupon");
        }
        return result;
    }
}