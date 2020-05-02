package com.example.toko;

public class ConfigURL {
    //ini buat constant yang di pake di db nantinya
    //ip wifi arenda =  192.168.0.108
    private final static String staticIP = "192.168.0.110";
    private final static String databaseName = "toko";
    public final static String Login = "http://" + staticIP + "/"+databaseName+"/login.php/";
    public final static String Register = "http://" + staticIP + "/"+databaseName+"/register.php/";
    public final static String SaveNewProduct = "http://" + staticIP + "/"+databaseName+"/saveNewProduct.php/";
    public final static String UpdateProduct = "http://" + staticIP + "/"+databaseName+"/updateProduct.php/";
    public final static String TakeProductCategories = "http://" + staticIP + "/"+databaseName+"/takeProductCategories.php/";
    public final static String TakeProductCategoriesDetail = "http://" + staticIP + "/"+databaseName+"/takeProductCategoriesDetail.php/";
    public final static String TakeProductAll = "http://" + staticIP + "/"+databaseName+"/takeProductAll.php/";
    public final static String TakePesananAll = "http://" + staticIP + "/"+databaseName+"/takePesananAll.php/";
    public final static String TakePesananAllCatering = "http://" + staticIP + "/"+databaseName+"/takePesananAllCatering.php/";

    public final static String SaveNewCombo = "http://" + staticIP + "/"+databaseName+"/saveNewCombo.php/";
    public final static String UpdateCombo = "http://" + staticIP + "/"+databaseName+"/updateCombo.php/";

    public final static String TakePriceBox = "http://" + staticIP + "/"+databaseName+"/takePriceBox.php/";
    public final static String SavePriceBox = "http://" + staticIP + "/"+databaseName+"/savePriceBox.php/";
    public final static String SaveNewOrder = "http://" + staticIP + "/"+databaseName+"/saveNewOrder.php/";
    public final static String SaveNewOrderCatering = "http://" + staticIP + "/"+databaseName+"/saveNewOrderCatering.php/";
    public final static String ValidateBalance = "http://" + staticIP + "/"+databaseName+"/validateBalance.php/";

    public final static String TakeHourDefault = "http://" + staticIP + "/"+databaseName+"/takeHourDefault.php/";
    public final static String SaveHourDefault = "http://" + staticIP + "/"+databaseName+"/saveHourDefault.php/";
    public final static String UpdateStatusPesanan = "http://" + staticIP + "/"+databaseName+"/updateStatusPesanan.php/";
    public final static String UpdateStatusPembayaranPesanan = "http://" + staticIP + "/"+databaseName+"/updateStatusPembayaranPesanan.php/";


}
