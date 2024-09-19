import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

public class A1 {
    public static void main(String[] args) {
        try {
            // Create a Scanner object
            Scanner scanner = new Scanner(System.in);
            System.out.print("Enter product history file name: ");
            // Read product history file name
            String productHistory = scanner.next();
            System.out.print("Enter cart items file name: ");
            // Read cart file name
            String cartItems = scanner.next();
            // read the product history file
            BufferedReader productList = new BufferedReader(
                    new FileReader(
                            productHistory
                    ));
            //read the cart items file
            BufferedReader productCart = new BufferedReader(
                    new FileReader(
                            cartItems
                    ));
            //create an object of CostOfLiving class
            CostOfLiving costOfLiving = new CostOfLiving();

            //Load products in cart. Returns count of loaded products.
            costOfLiving.loadProductHistory(productList);
            //Load cart items. Returns cart number.
            int cartId = costOfLiving.loadShoppingCart(productCart);

        } catch (IOException e) {
            System.out.println("Error loading products, Please try again");
        }
    }
}
