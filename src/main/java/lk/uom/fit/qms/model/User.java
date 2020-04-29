package lk.uom.fit.qms.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lk.uom.fit.qms.util.Constant;
import lk.uom.fit.qms.util.enums.Gender;
import org.hibernate.annotations.ColumnDefault;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.*;

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
@Inheritance(strategy = InheritanceType.JOINED)
public class User extends AbstractEntity implements UserDetails {

    private static final long serialVersionUID = 4452635975986896979L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "varchar(150)", nullable = false)
    private String name;
    private String username;
    private String password;
    @Column(columnDefinition = "varchar(10)", unique = true)
    private String mobile;
    @Column(columnDefinition = "varchar(10)")
    private String phone;
    @Column(columnDefinition = "varchar(12)", unique = true)
    private String nic;
    @Column(columnDefinition = "varchar(20)", unique = true)
    private String passportNo;
    @Column(columnDefinition = "SMALLINT(6)")
    private Integer age;

    @ManyToOne
    @JoinColumn(name = "addedBy")
    private User addedBy;

    @JsonManagedReference
    @OneToMany(mappedBy = "user", fetch = FetchType.EAGER, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private List<UserRole> userRoles = new ArrayList<>();

    @JsonBackReference
    @ManyToOne
    private Address address;

    @Enumerated(EnumType.STRING)
    @Column(length = 10)
    private Gender gender;


    @ColumnDefault("true")
    private boolean isAccountNonExpired = true;

    @ColumnDefault("true")
    private boolean isAccountNonLocked = true;

    @ColumnDefault("true")
    private boolean isCredentialsNonExpired = true;

    @ColumnDefault("true")
    private boolean isEnabled = true;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Set<GrantedAuthority> authorities = new HashSet<>();
        if (userRoles == null) {
            return authorities;
        }
        userRoles.forEach(r -> {
            authorities.add(new SimpleGrantedAuthority(r.getRole().getName().name()));
            if(r.isCreateUser()) {
                authorities.add(new SimpleGrantedAuthority(Constant.USER_CREATE_PERMISSION));
            }
        });
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return isAccountNonExpired;
    }

    @Override
    public boolean isAccountNonLocked() {
        return isAccountNonLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return isCredentialsNonExpired;
    }

    @Override
    public boolean isEnabled() {
        return isEnabled;
    }

    public Long getId() {
        return id;
    }

    public String getNic() {
        return nic;
    }

    public String getPassportNo() {
        return passportNo;
    }

    public Integer getAge() {
        return age;
    }

    public List<UserRole> getUserRoles() {
        return userRoles;
    }

    public void setUserRoles(List<UserRole> userRoles) {
        this.userRoles = userRoles;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public void setNic(String nic) {
        this.nic = nic;
    }

    public void setPassportNo(String passportNo) {
        this.passportNo = passportNo;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public void setAccountNonExpired(boolean accountNonExpired) {
        isAccountNonExpired = accountNonExpired;
    }

    public void setAccountNonLocked(boolean accountNonLocked) {
        isAccountNonLocked = accountNonLocked;
    }

    public void setCredentialsNonExpired(boolean credentialsNonExpired) {
        isCredentialsNonExpired = credentialsNonExpired;
    }

    public void setEnabled(boolean enabled) {
        isEnabled = enabled;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public User getAddedBy() {
        return addedBy;
    }

    public void setAddedBy(User addedBy) {
        this.addedBy = addedBy;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }
}
