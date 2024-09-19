

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.StringReader;

public class A1 {
    public static void main(String[] args) {
        try {
            BufferedReader productList = new BufferedReader(
            		new FileReader(
            				"products.txt"
            				));
            BufferedReader productCart = new BufferedReader(
            		new FileReader(
            				"cart.txt"
            				));

            CostOfLiving costOfLiving = new CostOfLiving();

            //costOfLiving.loadProductHistory(productList);
            int cartId = costOfLiving.loadShoppingCart(productCart);

            float cost = costOfLiving.shoppingCartCost(cartId, 2024, 12);

            var inflation = costOfLiving.inflation(1997, 1, 2024, 12);

            var inversion = costOfLiving.priceInversion(2024, 12, 0);

            System.out.println("Shopping cart cost: " + cost);

            System.out.println("Inflation Map: " + inflation);

        } catch (IOException e) {
        	System.out.println("Error loading products, Please try again");
        }
    }
}
