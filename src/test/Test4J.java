import com.mysql.base.util.SpringContextHolder;

import base.BaseJunit;
import lombok.extern.slf4j.Slf4j;

import org.junit.Test;

import java.util.Arrays;


@Slf4j
public class Test4J extends BaseJunit {

    @Test
    public void printBean() {
        String[] beanNameList = SpringContextHolder.getApplicationContext().getBeanDefinitionNames();
        Arrays.sort(beanNameList);
        for (String bean : beanNameList) {
            log.info(bean + " of Type :: " + SpringContextHolder.getApplicationContext().getBean(bean).getClass());
        }
    }

}
