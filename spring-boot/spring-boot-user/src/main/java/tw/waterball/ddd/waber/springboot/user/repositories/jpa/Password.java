package tw.waterball.ddd.waber.springboot.user.repositories.jpa;

import lombok.Getter;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

/**
 * @author Waterball (johnny850807@gmail.com)
 */
@Entity
@Getter
public class Password {
    @Id
    private int id;
    private String password;

    public Password(int userId, String password) {
        this.id = userId;
        this.password = password;
    }

    public Password() {

    }

    public void setPassword(String password) {
        this.password = password;
    }

}