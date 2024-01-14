### Spring JPA: Hibernate Mappings with JUnit Testing

<p>This project includes a demonstration of a Spring JPA project which utilizes Hibernate mappings, Spring JPA Repository with Service layer.  The mappings are then tested in JUnit with the help of an H2 in memory database.</p>

<p>There are some details related to the testing, the most impactful of which is the @DirtiesContext annotation on the test classes required to reset services after each test.</p>

<p>The simple database structure, with relationships, can be seen in the following Entity Diagram:</p>

<img src="db/ER-Diagram.png" />

<p>With results from the current JUnit tests:</p>

<img src="screenshots/test_results.png" />

<p>Future updates to this repository will expand both relationships and testing.</p>