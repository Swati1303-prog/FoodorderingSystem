package model;

public class Menu {
    private int id;
    private String name;
    private double price;
    private int typeId;
    private String typeName;
    private String image;
    private String ingredients;
    private String status;
     private int calories;


    public Menu() {}

   
    public int getCalories() { return calories; }
    public void setCalories(int calories) { this.calories = calories; }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String v) { this.name = v; }

    public double getPrice() { return price; }
    public void setPrice(double v) { this.price = v; }

    public int getTypeId() { return typeId; }
    public void setTypeId(int v) { this.typeId = v; }

    public String getTypeName() { return typeName; }
    public void setTypeName(String v) { this.typeName = v; }

    public String getImage() { return image; }
    public void setImage(String v) { this.image = v; }

    public String getIngredients() { return ingredients; }
    public void setIngredients(String v) { this.ingredients = v; }

    public String getStatus() { return status; }
    public void setStatus(String v) { this.status = v; }
}