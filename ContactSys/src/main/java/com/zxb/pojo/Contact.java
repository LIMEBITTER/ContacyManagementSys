package com.zxb.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Contact {
    private Integer id;
    private String name;
    private String sex;
    private Integer age;
    private String address;
    private String telephone;

    private Integer affiliate;

}
