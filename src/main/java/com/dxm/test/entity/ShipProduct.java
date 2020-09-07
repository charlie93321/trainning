package com.dxm.test.entity;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

/**
 *  "express_type": "YZDB",
 *     "product_doornorule": "",
 *     "product_id": "1961",
 *     "product_shortname": "北京航空大包",
 *     "product_tracknoapitype": ""
 */
@Data
public class ShipProduct {
    @SerializedName(value = "expressType",alternate = {"express_type"})
    private String expressType;
    @SerializedName(value = "productDoornorule",alternate = {"product_doornorule"})
    private String productDoornorule;
    @SerializedName(value = "productId",alternate = {"product_id"})
    private Long productId;
    @SerializedName(value = "productShortname",alternate = {"product_shortname"})
    private String productShortname;
    @SerializedName(value = "productTracknoapitype",alternate = {"product_tracknoapitype"})
    private String productTracknoapitype;
}
