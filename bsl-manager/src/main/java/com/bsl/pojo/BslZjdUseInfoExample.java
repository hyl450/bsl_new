package com.bsl.pojo;

import java.util.ArrayList;
import java.util.List;

public class BslZjdUseInfoExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    public BslZjdUseInfoExample() {
        oredCriteria = new ArrayList<Criteria>();
    }

    public void setOrderByClause(String orderByClause) {
        this.orderByClause = orderByClause;
    }

    public String getOrderByClause() {
        return orderByClause;
    }

    public void setDistinct(boolean distinct) {
        this.distinct = distinct;
    }

    public boolean isDistinct() {
        return distinct;
    }

    public List<Criteria> getOredCriteria() {
        return oredCriteria;
    }

    public void or(Criteria criteria) {
        oredCriteria.add(criteria);
    }

    public Criteria or() {
        Criteria criteria = createCriteriaInternal();
        oredCriteria.add(criteria);
        return criteria;
    }

    public Criteria createCriteria() {
        Criteria criteria = createCriteriaInternal();
        if (oredCriteria.size() == 0) {
            oredCriteria.add(criteria);
        }
        return criteria;
    }

    protected Criteria createCriteriaInternal() {
        Criteria criteria = new Criteria();
        return criteria;
    }

    public void clear() {
        oredCriteria.clear();
        orderByClause = null;
        distinct = false;
    }

    protected abstract static class GeneratedCriteria {
        protected List<Criterion> criteria;

        protected GeneratedCriteria() {
            super();
            criteria = new ArrayList<Criterion>();
        }

        public boolean isValid() {
            return criteria.size() > 0;
        }

        public List<Criterion> getAllCriteria() {
            return criteria;
        }

        public List<Criterion> getCriteria() {
            return criteria;
        }

        protected void addCriterion(String condition) {
            if (condition == null) {
                throw new RuntimeException("Value for condition cannot be null");
            }
            criteria.add(new Criterion(condition));
        }

        protected void addCriterion(String condition, Object value, String property) {
            if (value == null) {
                throw new RuntimeException("Value for " + property + " cannot be null");
            }
            criteria.add(new Criterion(condition, value));
        }

        protected void addCriterion(String condition, Object value1, Object value2, String property) {
            if (value1 == null || value2 == null) {
                throw new RuntimeException("Between values for " + property + " cannot be null");
            }
            criteria.add(new Criterion(condition, value1, value2));
        }

        public Criteria andProdIdIsNull() {
            addCriterion("prod_id is null");
            return (Criteria) this;
        }

        public Criteria andProdIdIsNotNull() {
            addCriterion("prod_id is not null");
            return (Criteria) this;
        }

        public Criteria andProdIdEqualTo(String value) {
            addCriterion("prod_id =", value, "prodId");
            return (Criteria) this;
        }

        public Criteria andProdIdNotEqualTo(String value) {
            addCriterion("prod_id <>", value, "prodId");
            return (Criteria) this;
        }

        public Criteria andProdIdGreaterThan(String value) {
            addCriterion("prod_id >", value, "prodId");
            return (Criteria) this;
        }

        public Criteria andProdIdGreaterThanOrEqualTo(String value) {
            addCriterion("prod_id >=", value, "prodId");
            return (Criteria) this;
        }

        public Criteria andProdIdLessThan(String value) {
            addCriterion("prod_id <", value, "prodId");
            return (Criteria) this;
        }

        public Criteria andProdIdLessThanOrEqualTo(String value) {
            addCriterion("prod_id <=", value, "prodId");
            return (Criteria) this;
        }

        public Criteria andProdIdLike(String value) {
            addCriterion("prod_id like", value, "prodId");
            return (Criteria) this;
        }

        public Criteria andProdIdNotLike(String value) {
            addCriterion("prod_id not like", value, "prodId");
            return (Criteria) this;
        }

        public Criteria andProdIdIn(List<String> values) {
            addCriterion("prod_id in", values, "prodId");
            return (Criteria) this;
        }

        public Criteria andProdIdNotIn(List<String> values) {
            addCriterion("prod_id not in", values, "prodId");
            return (Criteria) this;
        }

        public Criteria andProdIdBetween(String value1, String value2) {
            addCriterion("prod_id between", value1, value2, "prodId");
            return (Criteria) this;
        }

        public Criteria andProdIdNotBetween(String value1, String value2) {
            addCriterion("prod_id not between", value1, value2, "prodId");
            return (Criteria) this;
        }

        public Criteria andProdZjdIdIsNull() {
            addCriterion("prod_zjd_id is null");
            return (Criteria) this;
        }

        public Criteria andProdZjdIdIsNotNull() {
            addCriterion("prod_zjd_id is not null");
            return (Criteria) this;
        }

        public Criteria andProdZjdIdEqualTo(String value) {
            addCriterion("prod_zjd_id =", value, "prodZjdId");
            return (Criteria) this;
        }

        public Criteria andProdZjdIdNotEqualTo(String value) {
            addCriterion("prod_zjd_id <>", value, "prodZjdId");
            return (Criteria) this;
        }

        public Criteria andProdZjdIdGreaterThan(String value) {
            addCriterion("prod_zjd_id >", value, "prodZjdId");
            return (Criteria) this;
        }

        public Criteria andProdZjdIdGreaterThanOrEqualTo(String value) {
            addCriterion("prod_zjd_id >=", value, "prodZjdId");
            return (Criteria) this;
        }

        public Criteria andProdZjdIdLessThan(String value) {
            addCriterion("prod_zjd_id <", value, "prodZjdId");
            return (Criteria) this;
        }

        public Criteria andProdZjdIdLessThanOrEqualTo(String value) {
            addCriterion("prod_zjd_id <=", value, "prodZjdId");
            return (Criteria) this;
        }

        public Criteria andProdZjdIdLike(String value) {
            addCriterion("prod_zjd_id like", value, "prodZjdId");
            return (Criteria) this;
        }

        public Criteria andProdZjdIdNotLike(String value) {
            addCriterion("prod_zjd_id not like", value, "prodZjdId");
            return (Criteria) this;
        }

        public Criteria andProdZjdIdIn(List<String> values) {
            addCriterion("prod_zjd_id in", values, "prodZjdId");
            return (Criteria) this;
        }

        public Criteria andProdZjdIdNotIn(List<String> values) {
            addCriterion("prod_zjd_id not in", values, "prodZjdId");
            return (Criteria) this;
        }

        public Criteria andProdZjdIdBetween(String value1, String value2) {
            addCriterion("prod_zjd_id between", value1, value2, "prodZjdId");
            return (Criteria) this;
        }

        public Criteria andProdZjdIdNotBetween(String value1, String value2) {
            addCriterion("prod_zjd_id not between", value1, value2, "prodZjdId");
            return (Criteria) this;
        }

        public Criteria andProdUseWeightIsNull() {
            addCriterion("prod_use_weight is null");
            return (Criteria) this;
        }

        public Criteria andProdUseWeightIsNotNull() {
            addCriterion("prod_use_weight is not null");
            return (Criteria) this;
        }

        public Criteria andProdUseWeightEqualTo(Float value) {
            addCriterion("prod_use_weight =", value, "prodUseWeight");
            return (Criteria) this;
        }

        public Criteria andProdUseWeightNotEqualTo(Float value) {
            addCriterion("prod_use_weight <>", value, "prodUseWeight");
            return (Criteria) this;
        }

        public Criteria andProdUseWeightGreaterThan(Float value) {
            addCriterion("prod_use_weight >", value, "prodUseWeight");
            return (Criteria) this;
        }

        public Criteria andProdUseWeightGreaterThanOrEqualTo(Float value) {
            addCriterion("prod_use_weight >=", value, "prodUseWeight");
            return (Criteria) this;
        }

        public Criteria andProdUseWeightLessThan(Float value) {
            addCriterion("prod_use_weight <", value, "prodUseWeight");
            return (Criteria) this;
        }

        public Criteria andProdUseWeightLessThanOrEqualTo(Float value) {
            addCriterion("prod_use_weight <=", value, "prodUseWeight");
            return (Criteria) this;
        }

        public Criteria andProdUseWeightIn(List<Float> values) {
            addCriterion("prod_use_weight in", values, "prodUseWeight");
            return (Criteria) this;
        }

        public Criteria andProdUseWeightNotIn(List<Float> values) {
            addCriterion("prod_use_weight not in", values, "prodUseWeight");
            return (Criteria) this;
        }

        public Criteria andProdUseWeightBetween(Float value1, Float value2) {
            addCriterion("prod_use_weight between", value1, value2, "prodUseWeight");
            return (Criteria) this;
        }

        public Criteria andProdUseWeightNotBetween(Float value1, Float value2) {
            addCriterion("prod_use_weight not between", value1, value2, "prodUseWeight");
            return (Criteria) this;
        }

        public Criteria andProdUseBlIsNull() {
            addCriterion("prod_use_bl is null");
            return (Criteria) this;
        }

        public Criteria andProdUseBlIsNotNull() {
            addCriterion("prod_use_bl is not null");
            return (Criteria) this;
        }

        public Criteria andProdUseBlEqualTo(Float value) {
            addCriterion("prod_use_bl =", value, "prodUseBl");
            return (Criteria) this;
        }

        public Criteria andProdUseBlNotEqualTo(Float value) {
            addCriterion("prod_use_bl <>", value, "prodUseBl");
            return (Criteria) this;
        }

        public Criteria andProdUseBlGreaterThan(Float value) {
            addCriterion("prod_use_bl >", value, "prodUseBl");
            return (Criteria) this;
        }

        public Criteria andProdUseBlGreaterThanOrEqualTo(Float value) {
            addCriterion("prod_use_bl >=", value, "prodUseBl");
            return (Criteria) this;
        }

        public Criteria andProdUseBlLessThan(Float value) {
            addCriterion("prod_use_bl <", value, "prodUseBl");
            return (Criteria) this;
        }

        public Criteria andProdUseBlLessThanOrEqualTo(Float value) {
            addCriterion("prod_use_bl <=", value, "prodUseBl");
            return (Criteria) this;
        }

        public Criteria andProdUseBlIn(List<Float> values) {
            addCriterion("prod_use_bl in", values, "prodUseBl");
            return (Criteria) this;
        }

        public Criteria andProdUseBlNotIn(List<Float> values) {
            addCriterion("prod_use_bl not in", values, "prodUseBl");
            return (Criteria) this;
        }

        public Criteria andProdUseBlBetween(Float value1, Float value2) {
            addCriterion("prod_use_bl between", value1, value2, "prodUseBl");
            return (Criteria) this;
        }

        public Criteria andProdUseBlNotBetween(Float value1, Float value2) {
            addCriterion("prod_use_bl not between", value1, value2, "prodUseBl");
            return (Criteria) this;
        }

        public Criteria andProdPlanIdIsNull() {
            addCriterion("prod_plan_id is null");
            return (Criteria) this;
        }

        public Criteria andProdPlanIdIsNotNull() {
            addCriterion("prod_plan_id is not null");
            return (Criteria) this;
        }

        public Criteria andProdPlanIdEqualTo(String value) {
            addCriterion("prod_plan_id =", value, "prodPlanId");
            return (Criteria) this;
        }

        public Criteria andProdPlanIdNotEqualTo(String value) {
            addCriterion("prod_plan_id <>", value, "prodPlanId");
            return (Criteria) this;
        }

        public Criteria andProdPlanIdGreaterThan(String value) {
            addCriterion("prod_plan_id >", value, "prodPlanId");
            return (Criteria) this;
        }

        public Criteria andProdPlanIdGreaterThanOrEqualTo(String value) {
            addCriterion("prod_plan_id >=", value, "prodPlanId");
            return (Criteria) this;
        }

        public Criteria andProdPlanIdLessThan(String value) {
            addCriterion("prod_plan_id <", value, "prodPlanId");
            return (Criteria) this;
        }

        public Criteria andProdPlanIdLessThanOrEqualTo(String value) {
            addCriterion("prod_plan_id <=", value, "prodPlanId");
            return (Criteria) this;
        }

        public Criteria andProdPlanIdLike(String value) {
            addCriterion("prod_plan_id like", value, "prodPlanId");
            return (Criteria) this;
        }

        public Criteria andProdPlanIdNotLike(String value) {
            addCriterion("prod_plan_id not like", value, "prodPlanId");
            return (Criteria) this;
        }

        public Criteria andProdPlanIdIn(List<String> values) {
            addCriterion("prod_plan_id in", values, "prodPlanId");
            return (Criteria) this;
        }

        public Criteria andProdPlanIdNotIn(List<String> values) {
            addCriterion("prod_plan_id not in", values, "prodPlanId");
            return (Criteria) this;
        }

        public Criteria andProdPlanIdBetween(String value1, String value2) {
            addCriterion("prod_plan_id between", value1, value2, "prodPlanId");
            return (Criteria) this;
        }

        public Criteria andProdPlanIdNotBetween(String value1, String value2) {
            addCriterion("prod_plan_id not between", value1, value2, "prodPlanId");
            return (Criteria) this;
        }
    }

    public static class Criteria extends GeneratedCriteria {

        protected Criteria() {
            super();
        }
    }

    public static class Criterion {
        private String condition;

        private Object value;

        private Object secondValue;

        private boolean noValue;

        private boolean singleValue;

        private boolean betweenValue;

        private boolean listValue;

        private String typeHandler;

        public String getCondition() {
            return condition;
        }

        public Object getValue() {
            return value;
        }

        public Object getSecondValue() {
            return secondValue;
        }

        public boolean isNoValue() {
            return noValue;
        }

        public boolean isSingleValue() {
            return singleValue;
        }

        public boolean isBetweenValue() {
            return betweenValue;
        }

        public boolean isListValue() {
            return listValue;
        }

        public String getTypeHandler() {
            return typeHandler;
        }

        protected Criterion(String condition) {
            super();
            this.condition = condition;
            this.typeHandler = null;
            this.noValue = true;
        }

        protected Criterion(String condition, Object value, String typeHandler) {
            super();
            this.condition = condition;
            this.value = value;
            this.typeHandler = typeHandler;
            if (value instanceof List<?>) {
                this.listValue = true;
            } else {
                this.singleValue = true;
            }
        }

        protected Criterion(String condition, Object value) {
            this(condition, value, null);
        }

        protected Criterion(String condition, Object value, Object secondValue, String typeHandler) {
            super();
            this.condition = condition;
            this.value = value;
            this.secondValue = secondValue;
            this.typeHandler = typeHandler;
            this.betweenValue = true;
        }

        protected Criterion(String condition, Object value, Object secondValue) {
            this(condition, value, secondValue, null);
        }
    }
}