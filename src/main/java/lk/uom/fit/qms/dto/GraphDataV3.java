package lk.uom.fit.qms.dto;

import java.io.Serializable;

/**
 * @author Yasas Pansilu Jayasuriya
 * @version 1.0
 * @E-mail yasas.jayasuriya@axiatadigitallabs.com
 * @Telephone +94777332170
 * @project qms
 * @user Yasas_105071
 * @created on 5/2/2020
 * @Package lk.uom.fit.qms.dto
 * @company Axiata Digital Labs (pvt)Ltd.
 */

public class GraphDataV3 implements Serializable {

    private static final long serialVersionUID = 5697057704179614119L;

    private String key;
    private Long value1;
    private Long value2;
    private Long value3;
    private Long value4;

    public GraphDataV3(String key, Long value1, Long value2, Long value3, Long value4) {
        this.key = key;
        this.value1 = value1;
        this.value2 = value2;
        this.value3 = value3;
        this.value4 = value4;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Long getValue1() {
        return value1;
    }

    public void setValue1(Long value1) {
        this.value1 = value1;
    }

    public Long getValue2() {
        return value2;
    }

    public void setValue2(Long value2) {
        this.value2 = value2;
    }

    public Long getValue3() {
        return value3;
    }

    public void setValue3(Long value3) {
        this.value3 = value3;
    }

    public Long getValue4() {
        return value4;
    }

    public void setValue4(Long value4) {
        this.value4 = value4;
    }
}
