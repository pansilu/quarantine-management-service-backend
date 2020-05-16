package lk.uom.fit.qms.model;

import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import java.io.Serializable;
import java.time.LocalDateTime;

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
@MappedSuperclass
public abstract class AbstractEntity implements Serializable {

    private static final long serialVersionUID = 574198917980529331L;

    @CreationTimestamp
    @Column(name = "created_date", updatable = false, nullable = false, columnDefinition = "DATETIME(0)")
    @ColumnDefault("CURRENT_TIMESTAMP")
    private LocalDateTime createdDate;

    @UpdateTimestamp
    @Column(name = "last_modified_date", nullable = false, columnDefinition = "DATETIME(0)")
    @ColumnDefault("CURRENT_TIMESTAMP")
    private LocalDateTime lastModifiedDate;

    @ColumnDefault("false")
    private boolean isDeleted;

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public LocalDateTime getLastModifiedDate() {
        return lastModifiedDate;
    }

    public void setLastModifiedDate(LocalDateTime lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
    }
}
