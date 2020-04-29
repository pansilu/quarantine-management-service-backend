package lk.uom.fit.qms.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lk.uom.fit.qms.util.enums.Gender;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

/**
 * @author Yasas Pansilu Jayasuriya
 * @version 1.0
 * @E-mail jayasuriyay@gmail.com
 * @Telephone +94777332170
 * @project qms
 * @user Yasas_105071
 * @created on 4/1/2020
 * @Package lk.uom.fit.qms.dto.
 */
public class UserRequestDto {

    private Long id;
    @NotEmpty(message = "User name need to be entered")
    @Size(max = 150, message = "Name should need to be characters less than 150")
    private String name;
    @Pattern(regexp = "^0[0-9]{9}$", message = "Invalid mobile number pattern")
    private String mobile;
    @Pattern(regexp = "^0[0-9]{9}$", message = "Invalid land line number pattern")
    private String phone;
    @Pattern(regexp = "(^[0-9]{9}[vVxX]$)|(^[0-9]{12}$)", message = "Invalid nic number pattern")
    private String nic;
    @Size(max = 20, message = "Passport Number should need to be characters less than 20")
    private String passportNo;
    private Integer age;


    private Gender gender;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getNic() {
        return nic;
    }

    public void setNic(String nic) {
        this.nic = nic;
    }

    public String getPassportNo() {
        return passportNo;
    }

    public void setPassportNo(String passportNo) {
        this.passportNo = passportNo;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }
}
