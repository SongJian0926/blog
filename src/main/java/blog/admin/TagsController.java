package blog.admin;

import blog.entity.Tag;
import blog.service.TagService;
import javassist.NotFoundException;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.lang.reflect.InvocationTargetException;

@RestController
@RequestMapping("/admin")
public class TagsController {

    @Resource
    TagService tagService;

    @GetMapping("/tags")
    public ModelAndView list(@PageableDefault(size = 8,sort = {"id"}) Pageable pageable){
        ModelAndView modelAndView = new ModelAndView("admin/tags");
        modelAndView.addObject("page",tagService.listTage(pageable));
        return modelAndView;
    }
    /*
    * 进入编辑页面
    * */
    @GetMapping("/tags/edit")
    public ModelAndView editPage(){
        ModelAndView modelAndView = new ModelAndView("admin/tags-publish");
        modelAndView.addObject("tag",new Tag());
        return modelAndView;
    }
    /*
    * 点击点击编辑，进入编辑页面
    * */
    @GetMapping("/tags/{id}/edit")
    public ModelAndView edit(@PathVariable(name = "id") Long id){
        ModelAndView modelAndView = new ModelAndView("admin/tags-publish");
        modelAndView.addObject("tag",tagService.getTage(id));
        return modelAndView;
    }
    /*删除
    * */
    @GetMapping("/tags/{id}/delete")
    public ModelAndView delete(@PathVariable(name = "id") Long id,RedirectAttributes attributes){
        tagService.deleteTag(id);
        ModelAndView modelAndView =new ModelAndView("redirect:/admin/tags");
        attributes.addFlashAttribute("message","恭喜，删除成功");
        return modelAndView;
    }

    /*
    * 添加博客类型
    * */
    @PostMapping("/tags")
    public ModelAndView post(@Valid Tag tag, BindingResult result, RedirectAttributes attributes){
        if (tagService.findByName(tag.getName())!=null) {
            result.rejectValue("name","nameError","很遗憾，不能重复添加博客标签，该类型已存在。");
        }
        if (result.hasErrors()) {
            ModelAndView modelAndView=new ModelAndView("admin/tags-publish");
            return modelAndView;
        }
        Tag tagType = tagService.saveTage(tag);
        if (tagType==null) {
            attributes.addFlashAttribute("message","很遗憾，新增失败");
        }else {
            attributes.addFlashAttribute("message","恭喜，新增成功");
        }
        ModelAndView modelAndView =new ModelAndView("redirect:/admin/tags");
        return modelAndView;
    }
    /*
    * 修改博客
    * */
    @PostMapping("/tags/{id}")
    public ModelAndView editPost(@Valid Tag tag, BindingResult result,
                                 @PathVariable Long id,
                                 RedirectAttributes attributes) throws InvocationTargetException, IllegalAccessException, NotFoundException {
        if (tagService.findByName(tag.getName())!=null) {
            result.rejectValue("name","nameError","很遗憾，该类型已存在，无法跟新成功。");
        }
        if (result.hasErrors()) {
            ModelAndView modelAndView=new ModelAndView("admin/tags-publish");
            return modelAndView;
        }
        Tag updateTag = tagService.updateTag(id, tag);
        if (updateTag==null) {
            attributes.addFlashAttribute("message","很遗憾，更新失败");
        }else {
            attributes.addFlashAttribute("message","恭喜，更新成功");
        }
        ModelAndView modelAndView =new ModelAndView("redirect:/admin/tags");
        return modelAndView;
    }
}
