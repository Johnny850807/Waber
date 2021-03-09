package tw.waterball.ddd.waber.springboot.user.repositories.jpa;

import org.springframework.data.repository.CrudRepository;

/**
 * @author Waterball (johnny850807@gmail.com)
 */
public interface JpaPasswordPort extends CrudRepository<Password, Integer> {
}
