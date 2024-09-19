
import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class LoadProductHistory {
    private Map<String, List<Product>> productHistory;

    //load product history
    public LoadProductHistory(Map<String, List<Product>> productHistory) {
        this.productHistory = productHistory;
    }


    public int loadProducts(BufferedReader productStream) {
        productHistory.clear();
        try {
            // Check to handle if product history is null
            if (Utility.isNullOrEmpty(String.valueOf(productStream))) {
                System.out.println("Null product history stream");
                return -1;
            }
            // Reading string buffer line by line
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = productStream.readLine()) != null) {
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
                // Handling Incomplete Product parameters
                if (parts.length < 4) {
                    System.out.println("Incomplete Product details");
                    return -1;
                }
                // Handling Excessive Product parameters
                else if (parts.length > 4) {
                    System.out.println("Too many fields");
                    return -1;
                }
                // Handling Product parameters when they are 4 in total
                else {
                    String date = (parts[0]);
                    String name = Utility.checkProductNameCorrectness(parts[1])?parts[1].toLowerCase():parts[1];
                    String size = parts[2];
                    float cost = Float.parseFloat(parts[3]);

                    // add product to product history in case of correct product parameter values
                    if (Utility.checkProductCostCorrectness(String.valueOf(cost)) &&
                            Utility.checkProductDateCorrectness(date) &&
                            Utility.checkProductNameCorrectness(name) &&
                            Utility.checkProductSizeCorrectness(size)) {
                        //creating product object in lower case
                        Product product = new Product(date, name.toLowerCase(), size.toLowerCase(), cost);
                        productHistory.computeIfAbsent(name, k -> new ArrayList<>()).add(product);
                        System.out.println("Loaded product: " + product.name + ", " + product.size + ", " + product.cost + " on " + product.date);
                    }
                    //Handling in case of incorrect parameters
                    else {
                        return -1;
                    }
                }
            }

            // Checking if product history is empty
            if (productHistory.isEmpty()) {
                System.out.println("Empty product history stream");
                return 0;
            }
            // returning Size in case of non empty inventory
            else {
                System.out.println("Inventory size: " + productHistory.values().stream().mapToInt(List::size).sum());
                return productHistory.values().stream().mapToInt(List::size).sum();
            }
        } catch (IOException | NumberFormatException e) {
            System.out.println("Error loading products, kindly check the product values.");
            return -1;
        }
    }
}
