package blog.service;


import blog.entity.Type;
import javassist.NotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

public interface TypeService {

    Type saveType(Type type);

    Type getType(Long id);

    Page<Type> listType(Pageable pageable);

    Type updateType(Long id,Type type) throws InvocationTargetException, IllegalAccessException, NotFoundException;

    void deleteType(Long id);

    Type findByName(String name);

    List<Type> listType();

    List<Type> listPageType(Integer size);
}
