package database;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import javax.persistence.EntityManager;
import javax.persistence.criteria.*;
import java.util.List;

import static java.lang.String.format;

@Component
public class WrappedEntityManager {

  EntityManager entityManager;

  @Autowired
  public void setEntityManager(EntityManager entityManager) {
    this.entityManager = entityManager;
  }

  @Transactional(readOnly = true)
  public <T> List<T> findAllByFieldName(String fieldName, Object fieldValue, Class<T> clazz) {
    try {
      return getListOfResultsForFieldName(fieldName, fieldValue, clazz);
    } catch (Exception ex) {
      ex.printStackTrace();
      throw new RuntimeException(format("An exception occurred during database retrieval while looking up"
        + " Column: '%s' with value '%s' from table '%s'", fieldName, fieldValue, clazz.getSimpleName()), ex);
    }
  }

  @Transactional
  public int performNativeQuery(String query) {
    int affectedRows;
    try {
      affectedRows = entityManager.createNativeQuery(query).executeUpdate();
    } catch (Exception exception) {
      throw new RuntimeException("An exception occurred during deletion for query: " + query, exception);
    }
    return affectedRows;
  }

  private <T> List<T> getListOfResultsForFieldName(String fieldName, Object fieldValue, Class<T> clazz) {
    CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
    CriteriaQuery<T> criteriaQuery = criteriaBuilder.createQuery(clazz);
    Root<T> entityRoot = criteriaQuery.from(clazz);
    criteriaQuery
      .select(entityRoot)
      .where(criteriaBuilder.equal(entityRoot.get(fieldName), fieldValue));
    return entityManager.createQuery(criteriaQuery).getResultList();
  }

}
