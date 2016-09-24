package org.salespointframework;

import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

/**
 * Base class for application integration tests.
 * 
 * @author Oliver Gierke
 */
// tag::testBase[]
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Salespoint.class, webEnvironment = WebEnvironment.NONE)
@Transactional
public abstract class AbstractIntegrationTests {}
// end::testBase[]
