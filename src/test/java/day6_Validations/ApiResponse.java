package day6_Validations;

import java.util.List;

public class ApiResponse {
    public String status;
    public Data data;
    public Meta meta;
}

class Data {
    public UserDetails userDetails;
    public List<Order> recentOrders;
}

class UserDetails {
    public int id;
    public String name;
    public String email;
    public List<PhoneNumber> phoneNumbers;
    public Address address;
    public Preferences preferences;
}

class PhoneNumber {
    public String type;
    public String number;
}

class Address {
    public String street;
    public String city;
    public String state;
    public String postalCode;
    public Geo geo;
}

class Geo {
    public double latitude;
    public double longitude;
}

class Preferences {
    public boolean notifications;
    public String theme;
    public List<String> languages;
}

class Order {
    public int orderId;
    public String orderDate;
    public List<Item> items;
    public double totalAmount;
}

class Item {
    public int itemId;
    public String name;
    public int quantity;
    public double price;
}

class Meta {
    public String requestId;
    public String timestamp;
    public int responseTimeMs;
}
