package base.property.lianjia;

import java.util.Date;

/**
 * Created by xinszhou on 03/04/2017.
 */
public class LianjiaCommunityInfo {

    // 小区名字
    String communityName;
    // 创建时间
    int yearBuilt;
    // 住房类型，板楼还是塔楼，不太重要
    String type;
    // 物业费
    String propertyFee;
    // 物业公司
    String propertyCompany;

    int buildingNum;
    int apartmentNum;
    int pricePerSquare;
    // 开发商，和物业公司可能不同
    String kfs;

    long infoDate;

    public long getInfoDate() {
        return infoDate;
    }

    public void setInfoDate(long infoDate) {
        this.infoDate = infoDate;
    }

    public int getYearBuilt() {
        return yearBuilt;
    }

    public void setYearBuilt(int yearBuilt) {
        this.yearBuilt = yearBuilt;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCommunityName() {
        return communityName;
    }

    public void setCommunityName(String communityName) {
        this.communityName = communityName;
    }


    public String getPropertyFee() {
        return propertyFee;
    }

    public void setPropertyFee(String propertyFee) {
        this.propertyFee = propertyFee;
    }

    public String getPropertyCompany() {
        return propertyCompany;
    }

    public void setPropertyCompany(String propertyCompany) {
        this.propertyCompany = propertyCompany;
    }

    public int getBuildingNum() {
        return buildingNum;
    }

    public void setBuildingNum(int buildingNum) {
        this.buildingNum = buildingNum;
    }

    public int getApartmentNum() {
        return apartmentNum;
    }

    public void setApartmentNum(int apartmentNum) {
        this.apartmentNum = apartmentNum;
    }

    public int getPricePerSquare() {
        return pricePerSquare;
    }

    public void setPricePerSquare(int pricePerSquare) {
        this.pricePerSquare = pricePerSquare;
    }

    public String getKfs() {
        return kfs;
    }

    public void setKfs(String kfs) {
        this.kfs = kfs;
    }
}
