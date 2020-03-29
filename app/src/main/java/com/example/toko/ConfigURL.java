package com.example.toko;

public class ConfigURL {
    //ini buat constant yang di pake di db nantinya
    //ip wifi arenda =  192.168.0.108
    private final static String staticIP = "192.168.43.173";
    private final static String databaseName = "toko";
    public final static String Login = "http://" + staticIP + "/"+databaseName+"/login.php/";
    public final static String Register = "http://" + staticIP + "/"+databaseName+"/register.php/";
    public final static String SaveNewProduct = "http://" + staticIP + "/"+databaseName+"/saveNewProduct.php/";
    public final static String UpdateProduct = "http://" + staticIP + "/"+databaseName+"/updateProduct.php/";
    public final static String TakeProductCategories = "http://" + staticIP + "/"+databaseName+"/takeProductCategories.php/";
    public final static String TakeProductCategoriesDetail = "http://" + staticIP + "/"+databaseName+"/takeProductCategoriesDetail.php/";
}
