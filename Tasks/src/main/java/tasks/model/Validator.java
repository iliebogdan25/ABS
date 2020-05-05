package tasks.model;

import java.util.Date;

import static tasks.services.DateService.addMonths;
import static tasks.services.DateService.addYears;

public class Validator {
    public void validateCommonArguments(final String title, final Date start, final Date end) {
        if (start.before(addMonths(-1))) {
            throw new IllegalArgumentException("The start date must be no more than one month earlier than the current date.");
        }
        if (end.before(addMonths(-1))) {
            throw new IllegalArgumentException("The end date must be no more than one month earlier than the current date.");
        }
        if (end.after(addYears(2))) {
            throw new IllegalArgumentException("The end date must be no more than 2 years later than the start date.");
        }
        if (start.after(addYears(2))) {
            throw new IllegalArgumentException("The start date must be no more than 2 years later than the start date.");
        }
        if (end.before(start)) {
            throw new IllegalArgumentException("Start must be before End.");
        }
        if (title == null || (title.trim().isEmpty())) {
            throw new IllegalArgumentException("Title must not be empty");
        }
    }
}
