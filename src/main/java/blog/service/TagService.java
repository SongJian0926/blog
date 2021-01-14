package blog.service;


import blog.entity.Tag;
import javassist.NotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

public interface TagService {

    Tag saveTage(Tag tag);

    Tag getTage(Long id);

    Page<Tag> listTage(Pageable pageable);

    Tag updateTag(Long id,Tag tag) throws InvocationTargetException, IllegalAccessException, NotFoundException;

    void deleteTag(Long id);

    Tag findByName(String name);

    List<Tag> listTage();

    List<Tag> listTage(String tagIds);

    List<Tag> listPage(Integer size);
}
