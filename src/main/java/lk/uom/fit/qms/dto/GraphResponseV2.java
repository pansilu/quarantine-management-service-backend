package lk.uom.fit.qms.dto;

import java.io.Serializable;
import java.util.List;

/**
 * @author Yasas Pansilu Jayasuriya
 * @version 1.0
 * @E-mail yasas.jayasuriya@axiatadigitallabs.com
 * @Telephone +94777332170
 * @project qms
 * @user Yasas_105071
 * @created on 4/13/2020
 * @Package lk.uom.fit.qms.dto
 * @company Axiata Digital Labs (pvt)Ltd.
 */

public class GraphResponseV2 implements Serializable {

    private static final long serialVersionUID = -5542532040847187479L;

    private List<GraphDataV2> data;

    public List<GraphDataV2> getData() {
        return data;
    }

    public void setData(List<GraphDataV2> data) {
        this.data = data;
    }
}
