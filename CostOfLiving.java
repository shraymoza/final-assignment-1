import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class CostOfLiving {
    private final Map<String, List<Product>> productHistory = new HashMap<>();
    private final Map<Integer, List<CartItem>> shoppingCarts = new HashMap<>();

    // function to load the product history into inventory
    public int loadProductHistory(BufferedReader productStream) {
        int productInventoryCount = 0;
        LoadProductHistory loadProductHistory = new LoadProductHistory(productHistory);
        //find the total products loaded using the product stream
        productInventoryCount = loadProductHistory.loadProducts(productStream);
        // return the total count of loaded products
        return productInventoryCount;
    }

    // function to load the cart items into shopping cart map
    public int loadShoppingCart(BufferedReader cartStream) {
        int cart_number = -1;
        //create a map of LoadCartItems
        LoadCartItems loadCart = new LoadCartItems(shoppingCarts);
        // load cart items in cart map based on cart stream
        cart_number = loadCart.loadCartItems(cartStream);
        //return the cart number for a successfully loaded cart
        return cart_number;
    }


    // function to find the shopping cart cost at any point in time based on cartNumber,year and month
    public float shoppingCartCost(int cartNumber, int year, int month) {
        // fetch the cart items based on cart number
        List<CartItem> cartItems = shoppingCarts.get(cartNumber);
        // return -1 in call of empty cart
        if (cartItems == null) {
            return -1;
        }
        float totalCost = 0;
        float minCost;
        // for each cart item we try to find the cheapest possible value person can purchase using cartCostCalculator method
        for (CartItem cartItem : cartItems) {
            CartCostChecker checker = new CartCostChecker(productHistory, cartItem);
            // Using the cartCostCalculator function to find the min possible the cost of each item in cart as per our requirements
            minCost = checker.cartCostCalculator(year, month);
            //if mincost is equal to -1 in any case return -1 and end loop
            if(minCost==-1){
                return -1;
            }
            // keep incrementing the total cost based on min-cost of products
            else {
                totalCost += minCost;
            }
        }
        //return total cost of shopping cart
        return totalCost;
    }


    // function to find the Price Inversion at any point in time based on Year and Month and Tolerance
    public List<String> priceInversion(int year, int month, int tolerance) {
        List<String> inversions;
        System.out.print("Inversion : \n");
        // create an object of inversion Checker class that we load with product history
        InversionChecker checker = new InversionChecker(productHistory);
        // inversionChecker method returns a list of strings containing inverted prices
        inversions = checker.inversionChecker(year, month, tolerance);
        //return inversion list
        return inversions;
    }

    public Map<String, Float> inflation(int startYear, int startMonth, int endYear, int endMonth) {
        Map<String, Float> inflationMap;
        // create a object of inflationChecker class setting the product history
        InflationChecker checkInflation = new InflationChecker(productHistory);
        // inflationChecker method returns a list of strings containing inflated prices
        inflationMap = checkInflation.inflationChecker(startYear, startMonth, endYear, endMonth);
        // return inflation map
        return inflationMap;
    }
}

