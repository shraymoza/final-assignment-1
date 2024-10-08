
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

public class InversionChecker {
    private Map<String, List<Product>> productHistory;

    //load product history
    public InversionChecker(Map<String, List<Product>> productHistory) {
        this.productHistory = productHistory;
    }

    // function to find the Price Inversion at any point in time based on Year and Month and Tolerance
    public List<String> inversionChecker(int year, int month, int tolerance) {
        // checking if dates are valid
        if(year<=0||month>12||month<1||tolerance<=0){
            System.out.println("Invalid input values");
            return null;
        }
        List<String> inversions = new ArrayList<>();
        // create a string called final date based on year and month input
        String finalDate= year +"/"+ month +"/"+"1";
        //checking if product history or products for a specific item are null
        if (productHistory == null||productHistory.isEmpty()) {
            return null;
        }
        for (String productName : productHistory.keySet()) {
            List<Product> products = productHistory.get(productName);
            List<List<Product>> availableSizesLists,dateFilteredLists;
            List<Product> mostRecentFilteredSizes;

            // return separate lists of product sizes that are sorted based on dates
            availableSizesLists = Utility.getAvailableSizesLists(products);

            // filter the lists based on given month and year and  final day is set to 1 of given month
            dateFilteredLists= Utility.getDateFilteredLists(availableSizesLists, finalDate);

            // return a list of available product sizes
            mostRecentFilteredSizes = Utility.removeDiscontinuedProducts(dateFilteredLists);

            // Sort the filtered products by size using the Utility class
            mostRecentFilteredSizes.sort(Comparator.comparing(product -> Utility.convertSizeInMetrics(product.size)));
            //check if there are any products available to calculate inversion
            if(mostRecentFilteredSizes==null){
                return null;
            }else if(mostRecentFilteredSizes.size()<=1){
                return null;
            }
            // Check for price inversions using nested loops for each of the available quantities for a product
            for (int i = 0; i < mostRecentFilteredSizes.size(); i++) {
                Product smallerProduct = mostRecentFilteredSizes.get(i);
                double smallerCostPerUnit = smallerProduct.cost / Utility.convertSizeInMetrics(smallerProduct.size);

                for (int j = i + 1; j < mostRecentFilteredSizes.size(); j++) {
                    Product largerProduct = mostRecentFilteredSizes.get(j);
                    double largerCostPerUnit = largerProduct.cost / Utility.convertSizeInMetrics(largerProduct.size);

                    // Check if larger product cost is higher than smaller product cost per unit and calculate the difference
                    if (largerCostPerUnit > smallerCostPerUnit) {
                        double percentageDifference = ((largerCostPerUnit - smallerCostPerUnit) / largerCostPerUnit) * 100;

                        //check if tolerance is lower than the difference
                        if (percentageDifference > tolerance) {
                            inversions.add(productName + "\t" + largerProduct.size + "\t" + smallerProduct.size);
                        }
                    }
                }
            }
        }
        // returning the list of strings of inverted quantities
        return inversions;
    }

}
