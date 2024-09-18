
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Utility {

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
    public static Map<String, Float> calculateInflation(List<List<Product>> availableSizeLists){
        Map<String, Float> inflationMap = new HashMap<>();
        for(List<Product> sizeList: availableSizeLists){
            float initialValue= sizeList.get(0).cost;
            float finalValue= sizeList.get(1).cost;
           if(!Utility.isNullOrEmpty(Float.toString(finalValue))&& finalValue>initialValue){
               float inflation= ((finalValue-initialValue)/initialValue);
               inflationMap.put(sizeList.get(1).name + " " + sizeList.get(1).size, inflation);
           }
        }
        return inflationMap;
    }

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

    public static List<List<Product>> initialFinalProductList(List<List<Product>> availableSizeLists){
        List<List<Product>> finalAvailableSizeLists = new ArrayList<>();
        for (List<Product> sizeList : availableSizeLists) {
            List<Product> finalSizeLists= new ArrayList<>();
            Product startElement=sizeList.get(0);
            Product lastElement=sizeList.get(sizeList.size() - 1);
            finalSizeLists.add(startElement);
            finalSizeLists.add(lastElement);
            finalAvailableSizeLists.add(finalSizeLists);
        }
        return finalAvailableSizeLists;
    }

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

    public static boolean isNullOrEmpty(String str) {
        return str == null ||str == "null" || str.isEmpty() || str.equals("0") || str.equals("0.0") || str.equals("-1");
    }

    public static boolean isBlank(String str) {
        return str == "";
    }

}
