package blog.dao;

import blog.entity.Blog;
import org.omg.CORBA.TypeCodePackage.BadKind;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BlogRepository extends JpaRepository<Blog,Long> , JpaSpecificationExecutor<Blog> {

    Page<Blog> findAllByPublished(Boolean published,Pageable pageable);

    @Query("select b from Blog b where b.recommend = true ")
    List<Blog> listBolgTop(Pageable pageable);
}
