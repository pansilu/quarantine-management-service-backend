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
 * @created on 5/2/2020
 * @Package lk.uom.fit.qms.dto
 * @company Axiata Digital Labs (pvt)Ltd.
 */

public class GraphResponseV3 implements Serializable {

    private static final long serialVersionUID = 8682129797461964306L;

    private List<GraphDataV3> data;

    public List<GraphDataV3> getData() {
        return data;
    }

    public void setData(List<GraphDataV3> data) {
        this.data = data;
    }
}
