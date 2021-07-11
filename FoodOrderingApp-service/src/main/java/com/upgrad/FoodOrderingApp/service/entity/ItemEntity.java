package com.upgrad.FoodOrderingApp.service.entity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Objects;

@NamedNativeQueries({
        // Using native query as named queries do not support LIMIT in nested statements.
        @NamedNativeQuery(
                name = "topFivePopularItemsByRestaurant",
                query =
                        "select * from item where id in "
                                + "(select item_id from order_item where order_id in "
                                + "(select id from orders where restaurant_id = ? ) "
                                + "group by order_item.item_id "
                                + "order by (count(order_item.order_id)) "
                                + "desc LIMIT 5)",
                resultClass = ItemEntity.class)
})
@NamedQueries({
        @NamedQuery(name = "itemByUUID", query = "select i from ItemEntity i where i.uuid=:itemUUID"),
        @NamedQuery(
                name = "getAllItemsInCategoryInRestaurant",
                query =
                        "select i from ItemEntity i  where i.id in (select ri.itemId from RestaurantItemEntity ri "
                                + "inner join CategoryItemEntity ci on ci.itemId = ri.itemId "
                                + "where ri.restaurantId = (select r.id from RestaurantEntity r where "
                                + "r.uuid=:restaurantUuid) and ci.categoryId = "
                                + "(select c.id from CategoryEntity c where c.uuid=:categoryUuid ) )"
                                + "order by i.itemName asc")
})


@Entity
@Table(name="item")
public class ItemEntity implements Serializable {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotNull
    @Column(name="uuid")
    private String uuid;

    @Column(name = "item_name")
    private String itemName;

    @Column(name="price")
    private Integer price;

    @Column(name = "type")
    private String type;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ItemEntity that = (ItemEntity) o;
        return Objects.equals(id, that.id) && Objects.equals(uuid, that.uuid) && Objects.equals(itemName, that.itemName) && Objects.equals(price, that.price) && Objects.equals(type, that.type);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, uuid, itemName, price, type);
    }

    @Override
    public String toString() {
        return "ItemEntity{" +
                "id=" + id +
                ", uuid='" + uuid + '\'' +
                ", itemName='" + itemName + '\'' +
                ", price=" + price +
                ", type='" + type + '\'' +
                '}';
    }
}
