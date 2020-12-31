package blog.admin;

import blog.entity.Type;
import blog.service.TypeService;
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
public class TypeController {

    @Resource
    TypeService typeService;

    @GetMapping("/types")
    public ModelAndView list(@PageableDefault(size = 8,sort = {"id"}) Pageable pageable){
        ModelAndView modelAndView = new ModelAndView("admin/types");
        modelAndView.addObject("page",typeService.listType(pageable));
        return modelAndView;
    }
    /*
    * 进入编辑页面
    * */
    @GetMapping("/types/edit")
    public ModelAndView editPage(){
        ModelAndView modelAndView = new ModelAndView("admin/types-publish");
        modelAndView.addObject("type",new Type());
        return modelAndView;
    }
    /*
    * 点击点击编辑，进入编辑页面
    * */
    @GetMapping("/types/{id}/edit")
    public ModelAndView edit(@PathVariable(name = "id") Long id){
        ModelAndView modelAndView = new ModelAndView("admin/types-publish");
        modelAndView.addObject("type",typeService.getType(id));
        return modelAndView;
    }
    /*删除
    * */
    @GetMapping("/types/{id}/delete")
    public ModelAndView delete(@PathVariable(name = "id") Long id,RedirectAttributes attributes){
        typeService.deleteType(id);
        ModelAndView modelAndView =new ModelAndView("redirect:/admin/types");
        attributes.addFlashAttribute("message","恭喜，删除成功");
        return modelAndView;
    }

    /*
    * 添加博客类型
    * */
    @PostMapping("/types")
    public ModelAndView post(@Valid Type type, BindingResult result, RedirectAttributes attributes){
        if (typeService.findByName(type.getName())!=null) {
            result.rejectValue("name","nameError","很遗憾，不能重复添加博客类型，该类型已存在。");
        }
        if (result.hasErrors()) {
            ModelAndView modelAndView=new ModelAndView("admin/types-publish");
            return modelAndView;
        }
        Type saveType = typeService.saveType(type);
        if (saveType==null) {
            attributes.addFlashAttribute("message","很遗憾，新增失败");
        }else {
            attributes.addFlashAttribute("message","恭喜，新增成功");
        }
        ModelAndView modelAndView =new ModelAndView("redirect:/admin/types");
        return modelAndView;
    }
    /*
    * 修改博客
    * */
    @PostMapping("/types/{id}")
    public ModelAndView editPost(@Valid Type type, BindingResult result,
                                 @PathVariable Long id,
                                 RedirectAttributes attributes) throws InvocationTargetException, IllegalAccessException, NotFoundException {
        if (typeService.findByName(type.getName())!=null) {
            result.rejectValue("name","nameError","很遗憾，该类型已存在，无法跟新成功。");
        }
        if (result.hasErrors()) {
            ModelAndView modelAndView=new ModelAndView("admin/types-publish");
            return modelAndView;
        }
        Type updateType = typeService.updateType(id, type);
        if (updateType==null) {
            attributes.addFlashAttribute("message","很遗憾，更新失败");
        }else {
            attributes.addFlashAttribute("message","恭喜，更新成功");
        }
        ModelAndView modelAndView =new ModelAndView("redirect:/admin/types");
        return modelAndView;
    }
}
