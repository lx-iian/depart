package com.james.depart.listener;

import com.james.depart.service.DepartmentService;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * @author james
 * @date 2021-01-07
 */
@Slf4j
@Order(1)
@Component
public class DepartmentInitializeCommandLineRunner implements CommandLineRunner {

    final DepartmentService departmentService;

    public DepartmentInitializeCommandLineRunner(DepartmentService departmentService) {
        this.departmentService = departmentService;
    }

    @Override
    public void run(String... args) throws IOException {
        log.info(departmentService.initialize());
    }

}
