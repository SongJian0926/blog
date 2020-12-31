package blog.dao;


import blog.entity.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TageRepository extends JpaRepository<Tag,Long> {
    Tag findByName(String name);
}
