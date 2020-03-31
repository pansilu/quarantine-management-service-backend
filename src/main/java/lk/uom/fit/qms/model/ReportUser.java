package lk.uom.fit.qms.model;

import org.hibernate.annotations.Where;

import javax.persistence.Entity;

/**
 * @author Yasas Pansilu Jayasuriya
 * @version 1.0
 * @E-mail jayasuriyay@gmail.com
 * @Telephone +94777332170
 * @project qms
 * @user Yasas_105071
 * @created on 3/31/2020
 * @Package lk.uom.fit.qms.model.
 */
@Entity
@Where(clause = "is_deleted = 0")
public class ReportUser extends User{

    private static final long serialVersionUID = -6862191390484786975L;


}
