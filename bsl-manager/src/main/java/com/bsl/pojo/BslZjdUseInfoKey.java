package com.bsl.pojo;

public class BslZjdUseInfoKey {
    private String prodId;

    private String prodZjdId;

    public String getProdId() {
        return prodId;
    }

    public void setProdId(String prodId) {
        this.prodId = prodId == null ? null : prodId.trim();
    }

    public String getProdZjdId() {
        return prodZjdId;
    }

    public void setProdZjdId(String prodZjdId) {
        this.prodZjdId = prodZjdId == null ? null : prodZjdId.trim();
    }
}