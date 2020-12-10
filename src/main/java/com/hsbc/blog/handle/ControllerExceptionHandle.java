package com.hsbc.blog.handle;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

/*会拦截到所有的 配置有Controller的注解类*/
@RestControllerAdvice
public class ControllerExceptionHandle {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    /*加了注解，才能说明该方法处理异常生效*/
    @ExceptionHandler(Exception.class)
    public ModelAndView exceptionHandler(HttpServletRequest request, Exception e) throws Exception {
        /*这里可以通过requesth对象回去到当前出错的url*/
        logger.error("Requst URL : {}，Exception : {}", request.getRequestURL(),e);
        //NotFoundException
        if (AnnotationUtils.findAnnotation(e.getClass(), ResponseStatus.class) != null) {
            throw e;
        }
        /*ModelAndView 这个对象 是一个视图解析对象。*/
        ModelAndView mv = new ModelAndView();
        mv.addObject("url",request.getRequestURL());
        mv.addObject("exception", e);
        mv.setViewName("error/error");
        return mv;
    }
}
