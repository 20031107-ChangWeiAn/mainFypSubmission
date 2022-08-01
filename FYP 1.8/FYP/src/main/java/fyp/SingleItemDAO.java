package fyp;


import java.util.ArrayList;
import java.util.List;

public class SingleItemDAO {
    private int id;
    private String name;
    private String description;
    private double price;
    private int quantity;
    private String size;
    private String images;
    private Integer deleted;
    private List<ItemReview> itemsReview;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getImages() {
        return images;
    }

    public void setImages(String images) {
        this.images = images;
    }

    public Integer getDeleted() {
        return deleted;
    }

    public void setDeleted(Integer deleted) {
        this.deleted = deleted;
    }

    public List<ItemReview> getItemsReview() {
        return itemsReview;
    }

    public void setItemsReview(List<ItemReview> itemsReview) {
        this.itemsReview = itemsReview;
    }
}
