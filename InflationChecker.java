import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InflationChecker {
    private Map<String, List<Product>> productHistory;

    //load product history
    public InflationChecker(Map<String, List<Product>> productHistory) {
        this.productHistory = productHistory;
    }

    // function to find the Price Inversion at any point in time based on Year and Month and Tolerance
    public Map<String, Float> inflationChecker(int startYear, int startMonth, int endYear, int endMonth) {
        // checking if dates are valid
        if (startYear <= 0 || endYear <= 0 || endMonth > 12 || endMonth < 1 ||
                startMonth > 12 || startMonth < 1 || startYear > endYear ||
                (startYear == endYear && startMonth >= endMonth)) {
            System.out.println("Invalid dates");
            return null;
        }
        //checking if product history or products for a specific item are null
        if (productHistory == null || productHistory.isEmpty()) {
            return null;
        }
        Map<String, Float> inflationMap = new HashMap<>();
        // create a string called initial date based on year and month input
        String initialDate = startYear + "/" + startMonth + "/" + "1";
        // create a string called final date based on year and month input
        String finalDate = endYear + "/" + endMonth + "/" + "1";
        for (String productName : productHistory.keySet()) {
            List<Product> products = productHistory.get(productName);
            List<List<Product>> availableSizesLists, dateFilteredLists, mostRecentFilteredSizes;
            //List<Product> mostRecentFilteredSizes;

            // return separate lists of product sizes that are sorted based on dates
            availableSizesLists = Utility.getAvailableSizesLists(products);

            // filter the lists based on given startYear, startMonth, endMonth and endYear and  final day is set to 1 of given month
            dateFilteredLists = Utility.getDateFilteredInflationLists(availableSizesLists, finalDate, initialDate);

            // return a list of available product sizes on start date and end date
            mostRecentFilteredSizes = Utility.initialFinalProductList(dateFilteredLists);

            //check if there are any products available to calculate inversion
            if (mostRecentFilteredSizes == null) {
                continue;
            } else if (mostRecentFilteredSizes.size() <= 1) {
                continue;
            }
            //calculate inflation per quantity of product
            inflationMap.putAll(Utility.calculateInflation(mostRecentFilteredSizes));
        }

        // returning the list of strings of inverted quantities
        return inflationMap;
    }

}
