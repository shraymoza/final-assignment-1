
import java.util.*;

public class Utility {

    // this function is used to convert any given quantity into metrics
    static double convertSizeInMetrics(String size) {
        String[] sizeParts = size.split(" ");
        double quantity = Double.parseDouble(sizeParts[0]);
        String unit = sizeParts[1];

        return switch (unit) {
            case "kg", "km", "kl" -> quantity * 1000;
            case "g", "l", "m" -> quantity;
            case "ml", "mg", "mm" -> quantity / 1000;
            case "lb" -> quantity * 453.592;
            case "ft" -> quantity * 0.3048;
            default -> throw new IllegalArgumentException("Unknown unit: " + unit);
        };
    }

    // function used to calculate inflation
    public static Map<String, Float> calculateInflation(List<List<Product>> availableSizeLists){
        Map<String, Float> inflationMap = new HashMap<>();
        for(List<Product> sizeList: availableSizeLists){
            float initialValue= sizeList.get(0).cost;
            float finalValue= sizeList.get(1).cost;
           if(!Utility.isNullOrEmpty(Float.toString(finalValue))&& finalValue>initialValue){
               float inflation= ((finalValue-initialValue)/initialValue);
               inflationMap.put(sizeList.get(1).name + " " + sizeList.get(1).size, inflation);
           }else if(Utility.isNullOrEmpty(Float.toString(finalValue))){
               for(List<Product> sizeList2: availableSizeLists){
                   if(Utility.convertSizeInMetrics(sizeList2.get(1).size)<Utility.convertSizeInMetrics(sizeList.get(1).size)){
                       double finalValue2= sizeList2.get(1).cost/Utility.convertSizeInMetrics(sizeList2.get(1).size);
                       double initialValue2= sizeList.get(0).cost/Utility.convertSizeInMetrics(sizeList.get(0).size);
                       if(finalValue2>initialValue2){
                           float inflationForDiscontrinued = (float) ((finalValue2 - initialValue2) / initialValue2);
                           inflationMap.put(sizeList.get(1).name + " " + sizeList.get(1).size, inflationForDiscontrinued);
                       }
                   }
               }
           }
        }
        return inflationMap;
    }

    // finds the most economical product from the available quantities of a product
    public static Product getLowestCostPerUnitProduct(List<Product> mostRecentFilteredSizes) {
        if (Utility.isNullOrEmpty(Integer.toString(mostRecentFilteredSizes.size()))) {
            return null;
        }
        Product lowestCostProduct = mostRecentFilteredSizes.get(0);
        double lowestCostPerUnit = lowestCostProduct.cost / Utility.convertSizeInMetrics(lowestCostProduct.size);

        for (Product product : mostRecentFilteredSizes) {
            double size = Utility.convertSizeInMetrics(product.size);
            double costPerUnit = product.cost / size;

            if (costPerUnit < lowestCostPerUnit ||
                    (costPerUnit == lowestCostPerUnit && size < Utility.convertSizeInMetrics(lowestCostProduct.size))) {
                lowestCostProduct = product;
                lowestCostPerUnit = costPerUnit;
            }
        }

        return lowestCostProduct;
    }

    //checks if the recent entry of a quantity was discontinued based on their zero cost to filter them out
    public static List<Product> removeDiscontinuedProducts(List<List<Product>> availableSizeLists){
        List<Product> availableProducts = new ArrayList<>();
        for (List<Product> sizeList : availableSizeLists) {
            Product lastElement=sizeList.get(sizeList.size() - 1);
            if(!Utility.isNullOrEmpty(Float.toString(lastElement.cost))) {
                availableProducts.add(lastElement);
            }
        }
        return availableProducts;
    }

    // returns the oldest and most recent product from the inflation lists that can be used to calculate the inflation
    public static List<List<Product>> initialFinalProductList(List<List<Product>> availableSizeLists){
        List<List<Product>> finalAvailableSizeLists = new ArrayList<>();
        for (List<Product> sizeList : availableSizeLists) {
            List<Product> finalSizeLists= new ArrayList<>();
            if(sizeList.size()<2){
                return null;
            }
            Product startElement=sizeList.get(0);
            Product lastElement=sizeList.get(sizeList.size() - 1);
            finalSizeLists.add(startElement);
            finalSizeLists.add(lastElement);
            finalAvailableSizeLists.add(finalSizeLists);
        }
        return finalAvailableSizeLists;
    }

    // Function used to return the list of list of products where the lists are filtered up to a final dates
    public static List<List<Product>> getDateFilteredLists(List<List<Product>> availableSizeLists, String finalDates) {
        List<List<Product>> finalAvailableSizeLists = new ArrayList<>();
        for (List<Product> sizeList : availableSizeLists) {
            List<Product> finalSizeLists= new ArrayList<>();
            for(Product product: sizeList){
                String[] pDateParts = product.date.split("/");
                int productYear = Integer.parseInt(pDateParts[0]);
                int productMonth = Integer.parseInt(pDateParts[1]);
                int productDate = Integer.parseInt(pDateParts[2]);
                String[] fDateParts = finalDates.split("/");
                int finalYear = Integer.parseInt(fDateParts[0]);
                int finalMonth = Integer.parseInt(fDateParts[1]);
                int finalDate = Integer.parseInt(fDateParts[2]);
                if(productYear<finalYear||
                        (productYear==finalYear&&productMonth<finalMonth)||
                        (productYear==finalYear&&productMonth==finalMonth && productDate==finalDate)){
                    finalSizeLists.add(product);
                }
            }
            if(!finalSizeLists.isEmpty()){
                finalAvailableSizeLists.add(finalSizeLists);
            }
        }
        return finalAvailableSizeLists;
    }

    // Function used to return the list of list of products where the lists are filtered based on the initial and final dates
    public static List<List<Product>> getDateFilteredInflationLists(List<List<Product>> availableSizeLists, String finalDates,String initialDate) {
        List<List<Product>> finalAvailableSizeLists = new ArrayList<>();
        for (List<Product> sizeList : availableSizeLists) {
            List<Product> finalSizeLists= new ArrayList<>();
            for(Product product: sizeList){
                String[] pDateParts = product.date.split("/");
                int productYear = Integer.parseInt(pDateParts[0]);
                int productMonth = Integer.parseInt(pDateParts[1]);
                int productDate = Integer.parseInt(pDateParts[2]);
                String[] fDateParts = finalDates.split("/");
                int finalYear = Integer.parseInt(fDateParts[0]);
                int finalMonth = Integer.parseInt(fDateParts[1]);
                int finalDate = Integer.parseInt(fDateParts[2]);
                String[] iDateParts = initialDate.split("/");
                int startYear = Integer.parseInt(iDateParts[0]);
                int startMonth = Integer.parseInt(iDateParts[1]);
                int startDate = Integer.parseInt(iDateParts[2]);
                if(productYear>=startYear){
                    if(productMonth>=startMonth){
                        if(productDate>=startDate){
                            if(productYear<finalYear||
                                    (productYear==finalYear&&productMonth<finalMonth)||
                                    (productYear==finalYear&&productMonth==finalMonth && productDate==finalDate)){
                                finalSizeLists.add(product);
                            }
                        }
                    }
                }
            }
            if(!finalSizeLists.isEmpty()){
                finalAvailableSizeLists.add(finalSizeLists);
            }
        }
        return finalAvailableSizeLists;
    }

    // function used to create list of list of products for a specific cart item Product name based on their sizes. this also sorts the lists based on their dates
    public static List<List<Product>> getAvailableSizesLists(List<Product> products) {
        Map<String, List<Product>> sizeMap = new HashMap<>();

        for (Product product : products) {
            sizeMap.computeIfAbsent(Double.toString(convertSizeInMetrics(product.size)), k -> new ArrayList<>()).add(product);
        }
        for (List<Product> productList : sizeMap.values()) {
            productList.sort(Comparator.comparing(p -> p.date));
        }
        return new ArrayList<>(sizeMap.values());
    }
    // function to check null , zero or empty values
    public static boolean isNullOrEmpty(String str) {
        return str == null ||str == "null" || str.isEmpty() || str.equals("0") || str.equals("0.0") || str.equals("-1");
    }
    // function to check blank values
    public static boolean isBlank(String str) {
        return str == "";
    }

    //method to check correctness of name string
    static boolean checkProductNameCorrectness(String name) {
        // check for blank spaces
        if (Utility.isNullOrEmpty(name.trim())) {
            return false;
        } else {
            return true;
        }
    }

    //method to check correctness of size string
    static boolean checkProductSizeCorrectness(String size) {
        String[] units = {"kg", "km", "kl", "g", "l", "m", "ml", "mg", "mm", "lb", "ft"};
        if (size.contains(" ")) {
            // Checking if product contains null or empty values.
            if (Utility.isNullOrEmpty(size.trim())) {
                System.out.println(" Blank or empty values for product parameters not allowed");
                return false;
            }
            // Handling zero and negative quantities.
            else if (Float.parseFloat(size.split(" ")[0]) <= 0) {
                System.out.println("Negative and Zero quantities not allowed.");
                return false;
            }
            // Handling incorrect units.
            else if (!Arrays.asList(units).contains(size.split(" ")[1])) {
                System.out.println("Invalid Units");
                return false;
            }else {
                return true;
            }
        } else {
            // handling missing separator
            System.out.println("Size missing space separator");
            return false;
        }
    }

    //method to check correctness of date string
    static boolean checkProductDateCorrectness(String date) {
        // check for blank spaces
        if (Utility.isNullOrEmpty(date.trim())) {
            return false;
        } else {
            return true;
        }
    }

    //method to check correctness of cost string
    static boolean checkProductCostCorrectness(String cost) {
        //return -1 if cost is less than 0
        if (Float.parseFloat(cost) < 0.00) {
            System.out.println("Negative quantities not allowed.");
            return false;
        }
        // check for blank spaces
        else if (Utility.isBlank(cost.trim())) {
            return false;
        } else {
            return true;
        }
    }

}
