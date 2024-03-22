package com.bsl.reportbean;

public class BslTopTwoZjdInfo {
    private String prodParentNo1;

    private String prodParentNo2;

    private float prodRelWeightParent1;
    
    private float prodRelWeightParent2;

	public String getProdParentNo1() {
		return prodParentNo1;
	}

	public void setProdParentNo1(String prodParentNo1) {
		this.prodParentNo1 = prodParentNo1;
	}

	public String getProdParentNo2() {
		return prodParentNo2;
	}

	public void setProdParentNo2(String prodParentNo2) {
		this.prodParentNo2 = prodParentNo2;
	}

	public float getProdRelWeightParent1() {
		return prodRelWeightParent1;
	}

	public void setProdRelWeightParent1(float prodRelWeightParent1) {
		this.prodRelWeightParent1 = prodRelWeightParent1;
	}

	public float getProdRelWeightParent2() {
		return prodRelWeightParent2;
	}

	public void setProdRelWeightParent2(float prodRelWeightParent2) {
		this.prodRelWeightParent2 = prodRelWeightParent2;
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof BslTopTwoZjdInfo){
			BslTopTwoZjdInfo btzNew = (BslTopTwoZjdInfo)obj;
			if(btzNew.prodParentNo1.equals(this.prodParentNo1) &&
				btzNew.prodParentNo2.equals(this.prodParentNo2) &&	
				(btzNew.prodRelWeightParent1 == this.prodRelWeightParent1) &&	
				(btzNew.prodRelWeightParent2 == this.prodRelWeightParent2)){
				return true;
			}else{
				return false;
			}
			
		}else{
			return false;
		}
	}

	
    
    
}