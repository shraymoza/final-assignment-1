import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class LoadCartItems {
    private final Map<Integer, List<CartItem>> shoppingCarts;
    private int cartCounter = 0;
    //load product history
    public LoadCartItems(Map<Integer, List<CartItem>> shoppingCarts) {
        this.shoppingCarts = shoppingCarts;
    }

    public int loadCartItems(BufferedReader cartStream) {
        try {
            //checking for null cartStream
            if (Utility.isNullOrEmpty(String.valueOf(cartStream))) {
                System.out.println("Null shopping cart stream");
                return -1;
            }
            List<CartItem> cartItems = new ArrayList<>();
            String line;
            // Reading string buffer line by line
            StringBuilder sb = new StringBuilder();
            while ((line = cartStream.readLine()) != null) {
                sb.append(line).append("\n");
            }
            // Remove empty lines at the start and end
            String data = sb.toString().trim();

            // Process the data
            BufferedReader reader = new BufferedReader(new StringReader(data));
            while ((line = reader.readLine()) != null) {
                // Skip empty lines
                if (line.trim().isEmpty()) {
                    continue;
                }
                String[] parts = line.split("\t");
                // Handling incomplete cart details
                if (parts.length < 2) {
                    System.out.println("Incomplete Cart details");
                    return -1;
                }
                // Handling Excessive Cart parameters
                else if (parts.length > 2) {
                    System.out.println("Too many fields");
                    return -1;
                }
                // Handling cart parameters when they are 2 in total
                else {
                    String name = parts[0];
                    String size = parts[1];
                    if (Utility.checkProductNameCorrectness(name) &&
                            Utility.checkProductSizeCorrectness(size)) {
                        CartItem cartItem = new CartItem(name, size);
                        //checking if cart item already exists in added items
                        for(CartItem item:cartItems){
                            if(Utility.convertSizeInMetrics(item.size)==Utility.convertSizeInMetrics(cartItem.size)&& item.name.equals(cartItem.name)){
                                    System.out.println("Duplicate items");
                                    return -1;
                            }else if(item.size.equals(cartItem.size) && item.name.equals(cartItem.name)){
                                System.out.println("Duplicate items");
                                return -1;
                            }
                        }
                        cartItems.add(cartItem);
                        System.out.println("Purchased product: " + name + " of size " + size);
                    }else {
                        return -1;
                    }
                }
            }
            shoppingCarts.put(++cartCounter, cartItems);
            return cartCounter;
        } catch (IOException e) {
            System.out.println("Error loading cart, Kindly check the cart values.");
            return -1;
        }
    }
}
