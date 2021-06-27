package com.tyron.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @Description: 订单实体类
 * @Author: tyron
 * @Date: Created in 2021/6/27
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Order {
    private Integer id;
    private String buyerName;
    private String buyerTel;
    private String address;
    private Date createDate;
}
