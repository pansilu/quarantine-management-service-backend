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

public class GraphArrayResponse implements Serializable {

    private static final long serialVersionUID = -4529066348993826328L;

    private List<String> keys;
    private List<GraphDataArray> dataSet;

    public GraphArrayResponse(List<String> keys, List<GraphDataArray> dataSet) {
        this.keys = keys;
        this.dataSet = dataSet;
    }

    public List<String> getKeys() {
        return keys;
    }

    public void setKeys(List<String> keys) {
        this.keys = keys;
    }

    public List<GraphDataArray> getDataSet() {
        return dataSet;
    }

    public void setDataSet(List<GraphDataArray> dataSet) {
        this.dataSet = dataSet;
    }
}
