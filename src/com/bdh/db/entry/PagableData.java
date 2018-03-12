/**
 * 
 */
package com.bdh.db.entry;

import java.util.ArrayList;
import java.util.List;

/**
 * @author mac
 *
 */
public class PagableData<T> {

    private List<T> dataList = new ArrayList<T>(0);
    
    private List<City> cityList = new ArrayList<City>(0);
    
    private List<Category> categoryList = new ArrayList<Category>(0);
    
    
    
    private Integer totalCount = 0;


    
    private Integer itemStart =0;


    
    private Integer pageItemCount =0;



	public List<Category> getCategoryList() {
        return categoryList;
    }


    
    public List<City> getCityList() {
        return cityList;
    }


    
    public List<T> getDataList() {
        return dataList;
    }



    public Integer getItemCount() {
        return pageItemCount;
    }
    
    public Integer getItemStart() {
        return itemStart;
    }
    public Integer getPageItemCount() {
        return pageItemCount;
    }

    
    public Integer getTotalCount() {
        return totalCount;
    }

    
    public void setCategoryList(List<Category> categoryList) {
        this.categoryList = categoryList;
    }

    
    public void setCityList(List<City> cityList) {
        this.cityList = cityList;
    }

    
    public void setDataList(List<T> dataList) {
        this.dataList = dataList;
    }


    
    public void setItemCount(Integer itemCount) {
        this.pageItemCount = itemCount;
    }


    
    public void setItemStart(Integer itemStart) {
        this.itemStart = itemStart;
    }


    
    public void setPageItemCount(Integer pageItemCount) {
        this.pageItemCount = pageItemCount;
    }


    
    public void setTotalCount(Integer totalCount) {
        this.totalCount = totalCount;
    }



	



	
    
}
