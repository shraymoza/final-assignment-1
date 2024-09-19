
import java.util.List;
import java.util.Map;

public class CartCostChecker {

    private Map<String, List<Product>> productHistory;
    private CartItem cartItem;

    //load product history
    public CartCostChecker(Map<String, List<Product>> productHistory,CartItem cartItem) {
        this.productHistory = productHistory;
        this.cartItem = cartItem;
    }

    public float cartCostCalculator(int year, int month){
        // checking if dates are valid
        if(year<=0||month>12||month<1){
            System.out.println("Invalid dates");
            return -1;
        }
        // create a string called final date based on year and month input
        String finalDate= year +"/"+ month +"/"+"1";
        float minCost=-2;
        //Based on the item in cart we fetch all the entries available for this product
        List<Product> products = productHistory.get(cartItem.name);

        //checking if product history or products for a specific item are null
        if (productHistory == null) {
            return -1;
        }
        if (products == null) {
            return -1;
        }
        List<List<Product>> availableSizesLists,dateFilteredLists;
        List<Product> mostRecentFilteredSizes;
        // return separate lists of product sizes that are sorted based on dates
        availableSizesLists = Utility.getAvailableSizesLists(products);
        // filter the lists based on given month and year and  final day is set to 1 of given month
        dateFilteredLists= Utility.getDateFilteredLists(availableSizesLists, finalDate);
        // return a list of available product sizes
        mostRecentFilteredSizes = Utility.removeDiscontinuedProducts(dateFilteredLists);
        //checking if most recent product list is not null
        if(mostRecentFilteredSizes.isEmpty()){
            return -1;
        }
        //return the product that has lowest per unit cost among the currently available quantities
        Product lowestCostProduct = Utility.getLowestCostPerUnitProduct(mostRecentFilteredSizes);
        double lowestCostProductSize = Utility.convertSizeInMetrics(lowestCostProduct.size);
        double cartItemSize = Utility.convertSizeInMetrics(cartItem.size);
        if(lowestCostProductSize>=cartItemSize){
            minCost=lowestCostProduct.cost;
        }else if(lowestCostProductSize<cartItemSize){
            double factor=cartItemSize/lowestCostProductSize;
            float roundedFactor= (float) Math.ceil(factor);
            minCost=roundedFactor*lowestCostProduct.cost;
        }
        return minCost;
    }
}
