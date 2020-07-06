package ru.javawebinar.topjava.service;

import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles({"hsqldb", "jpa"})
public class JpaMealServiceTest extends AbstractMealServiceTest {
}
