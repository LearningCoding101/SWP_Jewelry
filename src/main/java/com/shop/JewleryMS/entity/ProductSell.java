
package com.shop.JewleryMS.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class ProductSell {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column( name = "PK_productID")
    Integer PK_productID;
    @Column( name = "FK_categoryID")
    Integer categoryID;
    @Column( name = "FK_promotionID")
    Integer promotionID;
    @Column( name = "pName")
    String name;
    @Column( name = "cost")
    Float cost;
    @Column( name = "pStatus")
    Boolean status;
    @Column( name = "pDescription")
    String description;
    @Column( name = "metalType")
    String metalType;
    @Column( name = "gemstoneType")
    String gemstoneType;
    @Column( name = "productCode")
    String productCode;
    @Column( name = "manufacturer")
    String manufacturer;
    @Column( name = "productCost")
    Integer productCost;
    @Column( name = "image")
    String image;
    @Column( name = "chi")
    Integer chi;
    @Column( name = "carat")
    Float carat;

    public ProductSell() {

    }

    public Integer getPK_productID() {
        return PK_productID;
    }

    public void setPK_productID(Integer PK_productID) {
        this.PK_productID = PK_productID;
    }

    public Integer getCategoryID() {
        return categoryID;
    }

    public void setCategoryID(Integer categoryID) {
        this.categoryID = categoryID;
    }

    public Integer getPromotionID() {
        return promotionID;
    }

    public void setPromotionID(Integer promotionID) {
        this.promotionID = promotionID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Float getCost() {
        return cost;
    }

    public void setCost(Float cost) {
        this.cost = cost;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getMetalType() {
        return metalType;
    }

    public void setMetalType(String metalType) {
        this.metalType = metalType;
    }

    public String getGemstoneType() {
        return gemstoneType;
    }

    public void setGemstoneType(String gemstoneType) {
        this.gemstoneType = gemstoneType;
    }

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public Integer getProductCost() {
        return productCost;
    }

    public void setProductCost(Integer productCost) {
        this.productCost = productCost;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Integer getChi() {
        return chi;
    }

    public void setChi(Integer chi) {
        this.chi = chi;
    }

    public Float getCarat() {
        return carat;
    }

    public void setCarat(Float carat) {
        this.carat = carat;
    }

    public ProductSell(Integer PK_productID, Integer categoryID, Integer promotionID, String name, Float cost, Boolean status, String description, String metalType, String gemstoneType, String productCode, String manufacturer, Integer productCost, String image, Integer chi, Float carat) {
        this.PK_productID = PK_productID;
        this.categoryID = categoryID;
        this.promotionID = promotionID;
        this.name = name;
        this.cost = cost;
        this.status = status;
        this.description = description;
        this.metalType = metalType;
        this.gemstoneType = gemstoneType;
        this.productCode = productCode;
        this.manufacturer = manufacturer;
        this.productCost = productCost;
        this.image = image;
        this.chi = chi;
        this.carat = carat;
    }
}
