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
 * @created on 4/11/2020
 * @Package lk.uom.fit.qms.dto
 * @company Axiata Digital Labs (pvt)Ltd.
 */

public class GraphResponse implements Serializable {

    private static final long serialVersionUID = -582299377941725080L;

    private List<GraphData> data;

    public List<GraphData> getData() {
        return data;
    }

    public void setData(List<GraphData> data) {
        this.data = data;
    }
}
