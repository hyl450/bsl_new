package com.bsl.pojo;

public class BslZjdUseInfo extends BslZjdUseInfoKey {
    private Float prodUseWeight;

    private Float prodUseBl;

    private String prodPlanId;

    public Float getProdUseWeight() {
        return prodUseWeight;
    }

    public void setProdUseWeight(Float prodUseWeight) {
        this.prodUseWeight = prodUseWeight;
    }

    public Float getProdUseBl() {
        return prodUseBl;
    }

    public void setProdUseBl(Float prodUseBl) {
        this.prodUseBl = prodUseBl;
    }

    public String getProdPlanId() {
        return prodPlanId;
    }

    public void setProdPlanId(String prodPlanId) {
        this.prodPlanId = prodPlanId == null ? null : prodPlanId.trim();
    }
}