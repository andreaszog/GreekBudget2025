import java.util.List;

public interface BudgetRepository {

    void save(Budget budget);

    List<Budget> findByCountry(String countryCode);

    Budget findByCountryAndYear(String countryCode, int year);

    List<Budget> findAll();
}
