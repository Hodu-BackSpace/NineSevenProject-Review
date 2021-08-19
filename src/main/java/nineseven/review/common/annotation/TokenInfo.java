package nineseven.review.common.annotation;

import org.springframework.web.bind.annotation.RequestMapping;

import java.lang.annotation.*;

@Target(value = ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
public @interface TokenInfo {
}
