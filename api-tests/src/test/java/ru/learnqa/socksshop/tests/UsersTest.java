package ru.learnqa.socksshop.tests;


import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.apache.commons.lang3.RandomStringUtils;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import ru.learnqa.socksshop.Cards;
import ru.learnqa.socksshop.UserPayload;
import static org.hamcrest.Matchers.emptyString;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.core.Is.is;


public class UsersTest {


	@BeforeClass
	public void setUp() {
		RestAssured.baseURI = ("http://217.28.229.104/");
	}

	@Test
    public void testCanBeRegistrationNewUser() {
		UserPayload user = new UserPayload();
				user.setEmail("test2@mail.gov");
				user.setUsername(RandomStringUtils.randomAlphanumeric(8));
				user.setPassword("test1234");
				RestAssured.given().contentType(ContentType.JSON).log().all()
				.body(user)
				.when()
				.post("register")
				.then().log().all()
				.assertThat()
				.statusCode(200)
				.body("id", is(not(emptyString())));

	}

	@Test
	public void testAddCard() {
		UserPayload user = new UserPayload();
		user.setEmail("test2@mail.gov");
		user.setUsername(RandomStringUtils.randomAlphanumeric(8));
		user.setPassword("test1234");

		RestAssured.given().contentType(ContentType.JSON).log().all()
				.body(user)
				.when()
				.post("register")
				.then().log().all()
				.assertThat()
				.statusCode(200)
				.body("id", is(not(emptyString())));

		Cards card = new Cards();
		card.setLongNum(RandomStringUtils.randomAlphanumeric(16));
		card.setExpires("01/22");
		card.setCcv(RandomStringUtils.randomAlphanumeric(3));
		card.setUserID("6036438fee11cb0001cb2df2");

		RestAssured.given().contentType(ContentType.JSON).log().all()
				.body(card)
				.when()
				.post("cards")
				.then().log().all()
				.assertThat()
				.statusCode(200)
				.body("id", is(not(emptyString())));

	}

	@Test
    public void testCanNotRegisterSameUserTwice(){
        UserPayload user = new UserPayload();
        user.setEmail("test2@mail.gov");
        user.setUsername(RandomStringUtils.randomAlphanumeric(8));
        user.setPassword("test1234");
        RestAssured.given().contentType(ContentType.JSON).log().all()
                .body(user)
                .when()
                .post("register")
                .then().log().all()
                .assertThat()
                .statusCode(200)
                .body("id", is(not(emptyString())));

        RestAssured.given().contentType(ContentType.JSON).log().all()
                .body(user)
                .when()
                .post("register")
                .then().log().all()
                .assertThat()
                .statusCode(500);
   }


}
