package test;

import com.bochkov.duty.jpa.DutyJpaConfig;
import com.bochkov.duty.wicket.service.converter.WicketJpaConverter;
import com.bochkov.duty.wicket.service.converter.CompositeConverter;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.apache.wicket.ConverterLocator;
import org.apache.wicket.util.convert.IConverter;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.core.EntityInformation;
import org.springframework.data.repository.support.Repositories;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@SpringBootTest(classes = DutyJpaConfig.class)
@RunWith(SpringRunner.class)
@Slf4j
public class ReadRepositories {
    @Autowired
    ApplicationContext context;

    @Test
    public void collectInfo() {
        ConverterLocator converterLocator = new ConverterLocator();
        List<String> jpaRepositoryNames = Lists.newArrayList(context.getBeanNamesForType(JpaRepository.class));
        Repositories repositories = new Repositories(context);
        repositories.forEach(clazz -> {
            log.info("Class = {}", clazz.getName());
            EntityInformation entityInformation = repositories.getEntityInformationFor(clazz);
            Class entityClass = entityInformation.getJavaType();
            Class idClass = entityInformation.getIdType();
            JpaRepository jpaRepository = (JpaRepository) repositories.getRepositoryFor(entityClass).get();
            log.info("idClass = {}", idClass.getName());
            IConverter idConverter = converterLocator.get(idClass);
            if (idConverter == null) {
                idConverter = new CompositeConverter(idClass);
                converterLocator.set(idClass, idConverter);
            }
            IConverter entityConverter = converterLocator.get(entityClass);
            if (entityConverter == null) {
                entityConverter = new WicketJpaConverter(entityClass, idConverter, jpaRepository);
                converterLocator.set(entityClass,entityConverter);
            }

        });

        log.info("{}", converterLocator);

    }
}
