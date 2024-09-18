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
    private int cartCounter = 0;


    public int loadProductHistory(BufferedReader productStream) {
            int productInventoryCount=0;
            LoadProductHistory loadProductHistory= new LoadProductHistory(productHistory);
            productInventoryCount = loadProductHistory.loadProducts(productStream);
            return productInventoryCount;
    }


    public int loadShoppingCart(BufferedReader cartStream) {
        try {
            List<CartItem> cartItems = new ArrayList<>();
            String line;
            while ((line = cartStream.readLine()) != null) {
                String[] parts = line.split("\t");
                String name = parts[0];
                String size = parts[1];

                CartItem cartItem = new CartItem(name, size);
                cartItems.add(cartItem);
                System.out.println("Purchased product: " + name + " of size " + size);
            }
            shoppingCarts.put(++cartCounter, cartItems);
            return cartCounter;
        } catch (IOException e) {
            System.out.println("Error loading cart, Kindly check the cart values.");
            return -1;
        }
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
        // for each cart item we try to find the cheapest possible value we can buy using cartCostCalculator method
        for (CartItem cartItem : cartItems) {
            CartCostChecker checker = new CartCostChecker(productHistory, cartItem);
            minCost = checker.cartCostCalculator(year,month);
            totalCost+=minCost;
        }
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
        return inversions;
    }

    public Map<String, Float> inflation(int startYear, int startMonth, int endYear, int endMonth) {
        Map<String, Float> inflationMap;
        // create a object of inflationChecker class setting the product history
        InflationChecker checkInflation = new InflationChecker(productHistory);
         // inflationChecker method returns a list of strings containing inflated prices
        inflationMap = checkInflation.inflationChecker( startYear, startMonth, endYear, endMonth );
        return inflationMap;
    }
}

